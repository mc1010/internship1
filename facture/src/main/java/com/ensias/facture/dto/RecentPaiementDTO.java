package com.ensias.facture.dto;

import java.time.LocalDate;

public record RecentPaiementDTO(
        Long id,
        Double montant,
        LocalDate datePaiement,
        String referenceTransaction,
        Long factureId
) {
}
