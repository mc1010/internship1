package com.ensias.facture.dto;

import com.ensias.facture.models.StatutPaiement;

import java.time.LocalDate;
import java.util.List;

public record FactureDto(
        Long id,
        String numero,
        LocalDate dateEmission,
        StatutPaiement statutPaiement,
        Integer clientId,
        String clientRaisonSociale,
        String clientIce,
        List<LigneFactureDto> lignes,
        Double totalHT,
        Double totalTVA,
        Double totalTTC,
        String conditions,
        Long devisAssocieId
) {

}
