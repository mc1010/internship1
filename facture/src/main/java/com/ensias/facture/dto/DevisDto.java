package com.ensias.facture.dto;

import java.time.LocalDate;
import java.util.List;

public record DevisDto(
        Long id,
        String numeroDevis,
        LocalDate dateCreation,
        LocalDate dateExpiration,
        String statut,
        Integer clientId,
        String clientRaisonSociale,
        String clientIce,
        String conditions,
        List<LigneDevisDTO> lignes,
        double montantTotal
) {
}
