package com.ensias.facture.services;

import com.ensias.facture.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface FactureService {
    FactureDto createFacture(CreateFactureDto dto);
    FactureDto updateFacture(Long id, UpdateFactureDto dto);
    void deleteFacture(Long id);
    FactureDto getFactureById(Long id);
    List<FactureDto> getAllFactures();
    List<FactureDto> getFacturesByClient(Integer clientId);
    List<FactureDto> getFacturesByDateRange(LocalDate start, LocalDate end);

    // Lignes Facture

    LigneFactureDto addLigneToFacture(Long factureId, CreateLigneFactureRequest dto);
    LigneFactureDto updateLigneFacture(Long factureId, Long ligneId, UpdateLigneFactureDto dto);
    void deleteLigneFacture(Long factureId, Long ligneId);


}
