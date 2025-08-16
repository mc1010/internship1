package com.ensias.facture.services.Imp;

import com.ensias.facture.dto.*;
import com.ensias.facture.exception.NotFoundException;
import com.ensias.facture.mappers.FactureMapper;
import com.ensias.facture.mappers.LigneFactureMapper;
import com.ensias.facture.models.Client;
import com.ensias.facture.models.Facture;
import com.ensias.facture.models.LigneFacture;
import com.ensias.facture.models.StatutPaiement;
import com.ensias.facture.repositories.ClientRepository;
import com.ensias.facture.repositories.FactureRepository;
import com.ensias.facture.repositories.LigneFactureRepository;
import com.ensias.facture.repositories.ProduitRepository;
import com.ensias.facture.services.FactureService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FactureServiceImpl implements FactureService {
    private final FactureRepository factureRepository;
    private final LigneFactureRepository ligneFactureRepository;
    private final ClientRepository clientRepository;
    private final ProduitRepository produitRepository;

    private final FactureMapper factureMapper;
    private final LigneFactureMapper ligneFactureMapper;

    //------------------CRUD FACTURE---------------

    @Override
    public FactureDto createFacture(CreateFactureDto dto) {
        Client client=clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new NotFoundException("Client non trouvé"));
        Facture facture =factureMapper.toEntity(dto);

        facture.setClient(client);
        facture.setDateEmission(LocalDate.now());
        facture.setConditions(dto.conditions());
        facture.setStatutPaiement(StatutPaiement.IMPAYEE);
        String numero = "FAC-" + LocalDate.now().getYear() + "-" + String.format("%04d", factureRepository.count() + 1);
        facture.setNumero(numero);
        if (dto.lignes() != null) {
            for (CreateLigneFactureRequest ligneDto : dto.lignes()) {
                var produit = produitRepository.findById(ligneDto.produitId())
                        .orElseThrow(() -> new NotFoundException("Produit avec ID " + ligneDto.produitId() + " non trouvé"));
                LigneFacture ligne = ligneFactureMapper.toEntity(ligneDto);
                ligne.setProduit(produit);
                ligne.setFacture(facture);
                ligne.setPrixUnitaireHT(produit.getPrixUnitaire());
                ligne.setMontantHT(ligneDto.quantite() * produit.getPrixUnitaire());
                ligne.setMontantTVA(ligne.getMontantHT() * produit.getTva() / 100);
                ligne.setMontantTTC(ligne.getMontantHT() + ligne.getMontantTVA());

                facture.getLignesFacture().add(ligne);
    }}
        recalculerTotaux(facture);
        Facture saved = factureRepository.save(facture);


        return factureMapper.toDto(saved);
    }


    @Override
    public FactureDto updateFacture(Long id, UpdateFactureDto dto) {
        Facture exist = factureRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Facture avec ID " + id + " non trouvé"));
        factureMapper.updateFactureFromDto(dto,exist);
        if (dto.conditions() != null) exist.setConditions(dto.conditions());
        // Ne modifier le statut que si fourni
        if (dto.statutPaiement() != null) {
            exist.setStatutPaiement(dto.statutPaiement());
        }
        if (dto.lignes() != null) {
            for (UpdateLigneFactureDto ligneDto : dto.lignes()) {
                updateLigneFacture(id, ligneDto.id(),ligneDto);
            }
        }
        recalculerTotaux(exist);
        Facture updated = factureRepository.save(exist);
        return factureMapper.toDto(updated);

    }

    @Override
    public void deleteFacture(Long id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Facture non trouvée"));
        factureRepository.delete(facture);
    }

    @Override
    public FactureDto getFactureById(Long id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Facture non trouvée"));
        return factureMapper.toDto(facture);
    }

    @Override
    public List<FactureDto> getAllFactures() {
        return factureRepository.findAll().stream()
                .map(factureMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FactureDto> getFacturesByClient(Integer clientId) {
        return factureRepository.findByClientId(clientId).stream()
                .map(factureMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FactureDto> getFacturesByDateRange(LocalDate start, LocalDate end) {
        return factureRepository.findByDateEmissionBetween(start, end).stream()
                .map(factureMapper::toDto)
                .collect(Collectors.toList());
    }
  //-------Ligne Facture---------

    @Override
    public LigneFactureDto addLigneToFacture(Long factureId, CreateLigneFactureRequest dto) {
        Facture facture = factureRepository.findById(factureId)
                .orElseThrow(() -> new NotFoundException("Facture avec ID " + factureId + " non trouvé"));

        var produit = produitRepository.findById(dto.produitId())
                .orElseThrow(() -> new NotFoundException("Produit avec ID " + dto.produitId() + " non trouvé"));

        LigneFacture ligne = ligneFactureMapper.toEntity(dto);
        ligne.setFacture(facture);
        ligne.setProduit(produit);
        ligne.setPrixUnitaireHT(produit.getPrixUnitaire());
        ligne.setMontantHT(dto.quantite() * produit.getPrixUnitaire());
        ligne.setMontantTVA(ligne.getMontantHT() * produit.getTva() / 100);
        ligne.setMontantTTC(ligne.getMontantHT() + ligne.getMontantTVA());

        facture.getLignesFacture().add(ligne);
        // recalculer les totaux avant save
        recalculerTotaux(facture);
        factureRepository.save(facture);

        return ligneFactureMapper.toDto(ligne);

    }

    @Override
    public LigneFactureDto updateLigneFacture(Long factureId, Long ligneId, UpdateLigneFactureDto dto) {
        LigneFacture ligne = ligneFactureRepository.findById(ligneId)
                .orElseThrow(() -> new NotFoundException("Ligne de facture avec ID " + ligneId + " non trouvé"));

        if (!ligne.getFacture().getId().equals(factureId)) {
            throw new NotFoundException("La ligne " + ligneId + " n'appartient pas à la facture " + factureId);
        }

        ligneFactureMapper.updateLigneFactureFromDto(dto, ligne);

        // recalculer montants
        ligne.setMontantHT(ligne.getQuantite() * ligne.getPrixUnitaireHT());
        ligne.setMontantTVA(ligne.getMontantHT() * ligne.getProduit().getTva() / 100);
        ligne.setMontantTTC(ligne.getMontantHT() + ligne.getMontantTVA());

        // mettre à jour la facture
        Facture facture = ligne.getFacture();
        recalculerTotaux(facture);
        factureRepository.save(facture);

        return ligneFactureMapper.toDto(ligneFactureRepository.save(ligne));

    }

    @Override
    public void deleteLigneFacture(Long factureId, Long ligneId) {
        Facture facture = factureRepository.findById(factureId)
                .orElseThrow(() -> new NotFoundException("Facture avec ID " + factureId + " non trouvé"));

        LigneFacture ligne = ligneFactureRepository.findById(ligneId)
                .orElseThrow(() -> new NotFoundException("Ligne de facture avec ID " + ligneId + " non trouvé"));

        if (!ligne.getFacture().getId().equals(factureId)) {
            throw new NotFoundException("La ligne " + ligneId + " n'appartient pas à la facture " + factureId);
        }

        facture.getLignesFacture().remove(ligne);
        ligneFactureRepository.delete(ligne);

        recalculerTotaux(facture);
        factureRepository.save(facture);
    }

    private void recalculerTotaux(Facture facture){
            double totalHT = facture.getLignesFacture() != null
                    ? facture.getLignesFacture().stream().mapToDouble(l -> l.getMontantHT() != null ? l.getMontantHT() : 0).sum()
                    : 0;
            double totalTVA = facture.getLignesFacture() != null
                    ? facture.getLignesFacture().stream().mapToDouble(l -> l.getMontantTVA() != null ? l.getMontantTVA() : 0).sum()
                    : 0;
            double totalTTC = facture.getLignesFacture() != null
                    ? facture.getLignesFacture().stream().mapToDouble(l -> l.getMontantTTC() != null ? l.getMontantTTC() : 0).sum()
                    : 0;

            facture.setTotalHT(totalHT);
            facture.setTotalTVA(totalTVA);
            facture.setTotalTTC(totalTTC);
    }

}

