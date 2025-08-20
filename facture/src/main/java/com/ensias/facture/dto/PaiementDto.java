package com.ensias.facture.dto;


import java.time.LocalDate;


public record PaiementDto(
        Long id,
        LocalDate datePaiement,
        Double montant,
        String modePaiement,
        String referenceTransaction,
        Long factureId
) {
}
