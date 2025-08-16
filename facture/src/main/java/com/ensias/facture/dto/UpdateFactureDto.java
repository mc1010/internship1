package com.ensias.facture.dto;

import com.ensias.facture.models.StatutPaiement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateFactureDto(
        @Size(max = 500, message = "Les conditions ne peuvent pas dépasser 500 caractères")
        String conditions,

        List< UpdateLigneFactureDto> lignes,

        StatutPaiement statutPaiement
) {
}
