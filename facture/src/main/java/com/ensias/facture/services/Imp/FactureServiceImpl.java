package com.ensias.facture.services.Imp;

import com.ensias.facture.dto.*;
import com.ensias.facture.exception.NotFoundException;
import com.ensias.facture.mappers.FactureMapper;
import com.ensias.facture.mappers.LigneFactureMapper;
import com.ensias.facture.models.*;
import com.ensias.facture.repositories.*;
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
    private final DevisRepository devisRepository;

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

        // Génération du numéro avec logique similaire aux devis
        int year = LocalDate.now().getYear();
        Facture lastFacture = factureRepository.findTopByNumeroStartingWithOrderByIdDesc("FAC-" + year + "-");
        int next = 1;
        if (lastFacture != null) {
            String[] parts = lastFacture.getNumero().split("-");
            next = Integer.parseInt(parts[2]) + 1;
        }
        facture.setNumero("FAC-" + year + "-" + String.format("%04d", next));

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
    @Override
    public FactureDto transformerDevisEnFacture(Long devisId) {
        Devis devis = devisRepository.findById(devisId)
                .orElseThrow(() -> new NotFoundException("Devis avec ID " + devisId + " non trouvé"));

        // Vérifier que le devis est accepté
        if (!devis.getStatut().equals(Statut.ACCEPTE)) {
            throw new IllegalStateException("Seuls les devis acceptés peuvent être transformés en facture");
        }

        Facture facture = new Facture();
        facture.setClient(devis.getClient());
        facture.setDateEmission(LocalDate.now());
        facture.setConditions(devis.getConditions());
        facture.setStatutPaiement(StatutPaiement.IMPAYEE);
        String numero = "FAC-" + LocalDate.now().getYear() + "-" + String.format("%04d", factureRepository.count() + 1);
        facture.setNumero(numero);
        facture.setDevisAssocie(devis);

        // Copier les lignes
        for (LigneDevis ligneDevis : devis.getLignesDevis()) {
            LigneFacture ligneFacture = getLigneFacture(ligneDevis, facture);
            facture.getLignesFacture().add(ligneFacture);
        }

        recalculerTotaux(facture);
        Facture saved = factureRepository.save(facture);

        return factureMapper.toDto(saved);
    }

    private  LigneFacture getLigneFacture(LigneDevis ligneDevis, Facture facture) {
        LigneFacture ligneFacture = new LigneFacture();
        ligneFacture.setFacture(facture);
        ligneFacture.setProduit(ligneDevis.getProduit());
        ligneFacture.setQuantite(ligneDevis.getQuantite());
        ligneFacture.setPrixUnitaireHT(ligneDevis.getProduit().getPrixUnitaire());
        ligneFacture.setMontantHT(ligneDevis.getQuantite() * ligneDevis.getProduit().getPrixUnitaire());
        ligneFacture.setMontantTVA(ligneFacture.getMontantHT() * ligneDevis.getProduit().getTva() / 100);
        ligneFacture.setMontantTTC(ligneFacture.getMontantHT() + ligneFacture.getMontantTVA());
        return ligneFacture;
    }


}

