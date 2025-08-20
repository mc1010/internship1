package com.ensias.facture.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



import java.time.LocalDate;



public record CreatePaiementDto(
        @NotNull(message = "Le montant est obligatoire")
        @Min(value = 1, message = "Le montant doit être supérieur à 0")
        Double montant,

        @NotNull(message = "La date du paiement est obligatoire")
        LocalDate datePaiement,

        @NotNull(message = "L'id de la facture est obligatoire")
        Long factureId,

        @Size(max = 50, message = "Le mode de paiement doit avoir max 50 caractères")
        String modePaiement,

        String referenceTransaction
) {
}
