package com.ensias.facture.services.Imp;

import com.ensias.facture.dto.*;
import com.ensias.facture.exception.NotFoundException;
import com.ensias.facture.mappers.DevisMapper;
import com.ensias.facture.mappers.LigneDevisMapper;
import com.ensias.facture.models.Client;
import com.ensias.facture.models.Devis;
import com.ensias.facture.models.LigneDevis;
import com.ensias.facture.models.Statut;
import com.ensias.facture.repositories.ClientRepository;
import com.ensias.facture.repositories.DevisRepository;
import com.ensias.facture.repositories.LigneDevisRepository;
import com.ensias.facture.repositories.ProduitRepository;
import com.ensias.facture.services.DevisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DevisServiceImpl implements DevisService {

    private final DevisRepository devisRepository;
    private final LigneDevisRepository ligneDevisRepository;
    private final ClientRepository clientRepository;
    private final ProduitRepository produitRepository;

    private final DevisMapper devisMapper;
    private final LigneDevisMapper ligneDevisMapper;


    @Override
    public DevisDto createDevis(CreateDevisDTO dto) {
        Client client = clientRepository.findById(dto.clientId()).orElseThrow(()->new NotFoundException("Client with id" + dto.clientId() + "not found"));
        Devis devis = devisMapper.toEntity(dto);
        devis.setClient(client);
        devis.setConditions(dto.conditions());
        devis.setDateCreation(LocalDate.now());
        devis.setDateExpiration(dto.dateExpiration());
        devis.setStatut(Statut.EN_ATTENTE);
        //GEneration du numero
        int year = LocalDate.now().getYear();
        Devis lastDevis = devisRepository.findTopByNumeroDevisStartingWithOrderByIdDesc("DEV-" + year + "-");
        int next = 1;
        if (lastDevis != null) {
            String[] parts = lastDevis.getNumeroDevis().split("-");
            next = Integer.parseInt(parts[2]) + 1;
        }
        devis.setNumeroDevis("DEV-" + year + "-" + String.format("%04d", next));
        // Créer les lignes si elles existent dans le DTO
        if (dto.lignes() != null) {
            for (CreateLigneDevisRequest ligneDto : dto.lignes()) {
                var produit = produitRepository.findById(ligneDto.produitId())
                        .orElseThrow(() -> new NotFoundException("Produit avec ID " + ligneDto.produitId() + " non trouvé"));
                LigneDevis ligne = ligneDevisMapper.toEntity(ligneDto);
                ligne.setProduit(produit);
                ligne.setDevis(devis);
                ligne.setPrixTotal(ligneDto.quantite() * produit.getPrixUnitaire() * (1 + produit.getTva() / 100));
                devis.getLignesDevis().add(ligne);
            }
        }

       Devis saved = devisRepository.save(devis);
        recalculerMontantTotal(devis);
       return devisMapper.toDto(saved);

    }

    @Override
    public DevisDto updateDevis(Long devisId, UpdateDevisDTO dto) {
        Devis exist = devisRepository.findById(devisId).orElseThrow(()-> new NotFoundException("Devis avec ID" + devisId + " non trouvé"));
        devisMapper.updateDevisFromDto(dto,exist);
        if (dto.conditions() != null) {
            exist.setConditions(dto.conditions());
        }

        if (dto.lignes() != null) {
            for (UpdateLigneDevisDTO ligneDto : dto.lignes()) {
                updateLigneDevis(devisId,ligneDto.id(),ligneDto);
            }
        }

        Devis upd= devisRepository.save(exist);
        recalculerMontantTotal(upd);
        return devisMapper.toDto(upd);
    }

    @Override
    public void deleteDevis(Long devisId) {
        Devis devis= devisRepository.findById(devisId).orElseThrow(()-> new NotFoundException("Devis avec ID" + devisId + " non trouvé"));
        devisRepository.delete(devis);
    }

    @Override
    public DevisDto getDevisById(Long devisId) {
        Devis devis= devisRepository.findById(devisId).orElseThrow(()-> new NotFoundException("Devis avec ID" + devisId + " non trouvé"));
        return devisMapper.toDto(devis);
    }

    @Override
    public List<DevisDto> getAllDevis() {
        return devisRepository.findAll().stream().map(devisMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<DevisDto> getDevisByClient(Integer clientId) {
        return devisRepository.findByClientId(clientId).stream().map(devisMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<DevisDto> getDevisByStatut(Statut statut) {
        return devisRepository.findByStatut(statut).stream().map(devisMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<DevisDto> getDevisByDateRange(LocalDate start, LocalDate end) {
        System.out.println("📅 Filtre demandé: start=" + start + " / end=" + end);
        List<Devis> devisList = devisRepository.findByDateCreationBetweenDates(start, end);
        System.out.println("📦 Nombre de devis trouvés: " + devisList.size());
        return devisRepository.findByDateCreationBetweenDates(start,end).stream().map(devisMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public LigneDevisDTO addLigneToDevis(Long devisId, CreateLigneDevisRequest dto) {
        Devis devis =devisRepository.findById(devisId).orElseThrow(()-> new NotFoundException("Devis avec ID" + devisId + " non trouvé"));

        var produit = produitRepository.findById(dto.produitId()).orElseThrow(()-> new NotFoundException("Produit avec ID"+ dto.produitId() + " non trouvé"));

        LigneDevis ligne = ligneDevisMapper.toEntity(dto);
        ligne.setDevis(devis);
        ligne.setProduit(produit);
        ligne.setPrixTotal(dto.quantite()* produit.getPrixUnitaire()*(1+produit.getTva()/100));


        LigneDevis saved = ligneDevisRepository.save(ligne);

        recalculerMontantTotal(devis);

        return ligneDevisMapper.toDto(saved);
    }

    @Override
    public LigneDevisDTO updateLigneDevis(Long devisId, Long ligneId,UpdateLigneDevisDTO dto) {
        LigneDevis ligne = ligneDevisRepository.findById(ligneId).orElseThrow(()-> new NotFoundException("Ligne de Devis avec ID" + ligneId + " non trouvé"));
        if (!ligne.getDevis().getId().equals(devisId)) {
            throw new NotFoundException("La ligne " + ligneId + " n'appartient pas au devis " + devisId);
        }
        ligneDevisMapper.updateLigneDevisFromDto(dto,ligne);

        ligne.setPrixTotal(ligne.getQuantite()*ligne.getProduit().getPrixUnitaire() * (1+ligne.getProduit().getTva()/100));
        LigneDevis up =ligneDevisRepository.save(ligne);

        recalculerMontantTotal(ligne.getDevis());
        return ligneDevisMapper.toDto(up);
    }

    @Override
    public void deleteLigneDevis(Long devisId,Long ligneId) {
        Devis devis = devisRepository.findById(devisId)
                .orElseThrow(() -> new NotFoundException("Devis avec ID " + devisId + " non trouvé"));
        LigneDevis ligne = ligneDevisRepository.findById(ligneId).orElseThrow(()-> new NotFoundException("Ligne de Devis avec ID" + ligneId + " non trouvé"));
        if (!ligne.getDevis().getId().equals(devisId)) {
            throw new NotFoundException("La ligne " + ligneId + " n'appartient pas au devis " + devisId);
        }
        devis.getLignesDevis().remove(ligne);

        recalculerMontantTotal(devis);
        devisRepository.save(devis);

    }

    @Override
    public DevisDto changerStatut(Long devisId, Statut newStatut) {
        Devis devis= devisRepository.findById(devisId).orElseThrow(()-> new NotFoundException("Devis avec ID" + devisId + " non trouvé"));
        devis.setStatut(newStatut);
        Devis up=devisRepository.save(devis);
        return devisMapper.toDto(up);
    }

    private void recalculerMontantTotal(Devis devis ){
        double total = devis.getLignesDevis()!=null ? devis.getLignesDevis().stream().mapToDouble(l ->l.getPrixTotal()!=null ? l.getPrixTotal() : 0).sum():0;
        devis.setMontantTotal(total);
    }
}
