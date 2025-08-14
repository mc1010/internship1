package com.ensias.facture.services;

import com.ensias.facture.dto.*;
import com.ensias.facture.models.Statut;

import java.time.LocalDate;
import java.util.List;

public interface DevisService {
    //---CRUD DEVIS------
    DevisDto createDevis(CreateDevisDTO dto);
    DevisDto updateDevis(Long devisId, UpdateDevisDTO dto);
    void deleteDevis(Long devisId);
    DevisDto getDevisById(Long devisId);
    List<DevisDto> getAllDevis();
    List<DevisDto> getDevisByClient(Integer clientId);
    List<DevisDto> getDevisByStatut(Statut statut);
    List<DevisDto> getDevisByDateRange(LocalDate start, LocalDate end);

    //Ligne DEvis
     LigneDevisDTO addLigneToDevis(Long devisId, CreateLigneDevisRequest dto);
     LigneDevisDTO updateLigneDevis(Long ligneId,Long devisId, UpdateLigneDevisDTO dto);
     void deleteLigneDevis(Long ligneId, Long devisId);

     DevisDto changerStatut(Long devisId, Statut newStatut);
}
