package com.ensias.facture.services.Imp;

import com.ensias.facture.dto.CreatePaiementDto;
import com.ensias.facture.dto.PaiementDto;
import com.ensias.facture.exception.NotFoundException;
import com.ensias.facture.mappers.PaiementMapper;
import com.ensias.facture.models.Facture;
import com.ensias.facture.models.Paiement;
import com.ensias.facture.models.StatutPaiement;
import com.ensias.facture.repositories.FactureRepository;
import com.ensias.facture.repositories.PaiementRepository;
import com.ensias.facture.services.PaiementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class PaiementServiceImpl implements PaiementService {

    private final PaiementRepository paiementRepository;
    private final FactureRepository factureRepository;
    private final PaiementMapper paiementMapper;



    @Override
    public PaiementDto createPaiement(CreatePaiementDto dto) {
        if (dto.montant() <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0");
        }


        Facture facture = factureRepository.findById(dto.factureId())
                .orElseThrow(() -> new NotFoundException("Facture introuvable avec id : " + dto.factureId()));

        Paiement paiement = paiementMapper.toEntity(dto, facture);
        Paiement saved = paiementRepository.save(paiement);
        double totalPaye = paiementRepository.findByFactureId(facture.getId())
                .stream()
                .mapToDouble(Paiement::getMontant)
                .sum();

        // 🔄 Mettre à jour le statut de la facture
        if (totalPaye >= facture.getTotalTTC()) {
            facture.setStatutPaiement(StatutPaiement.PAYEE);
        } else {
            facture.setStatutPaiement(StatutPaiement.PARTIELLE);
        }

        factureRepository.save(facture);

        return paiementMapper.toDto(saved);
    }



    @Override
    public List<PaiementDto> getPaiementsByFacture(Long factureId) {
        return paiementRepository.findByFactureId(factureId)
                .stream()
                .map(paiementMapper::toDto)
                .toList();
    }

    @Override
    public PaiementDto getPaiement(Long id) {
        return paiementRepository.findById(id)
                .map(paiementMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Paiement introuvable avec id : " + id));
    }

    @Override
    public void deletePaiement(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Paiement introuvable avec id : " + id));
        paiementRepository.delete(paiement);

        // 🔄 Recalculer le statut de la facture après suppression du paiement
        Facture facture = paiement.getFacture();
        double totalRestant = paiementRepository.findByFactureId(facture.getId())
                .stream()
                .mapToDouble(Paiement::getMontant)
                .sum();

        if (totalRestant == 0) {
            facture.setStatutPaiement(StatutPaiement.IMPAYEE);
        } else if (totalRestant < facture.getTotalTTC()) {
            facture.setStatutPaiement(StatutPaiement.PARTIELLE);
        } else {
            facture.setStatutPaiement(StatutPaiement.PAYEE);
        }

        factureRepository.save(facture);
    }

    }

