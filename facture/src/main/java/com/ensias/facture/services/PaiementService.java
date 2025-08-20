package com.ensias.facture.services;

import com.ensias.facture.dto.CreatePaiementDto;
import com.ensias.facture.dto.PaiementDto;

import java.util.List;

public interface PaiementService {
    PaiementDto createPaiement(CreatePaiementDto dto);
    List<PaiementDto> getPaiementsByFacture(Long factureId);
    PaiementDto getPaiement(Long id);
    void deletePaiement(Long id);
}
