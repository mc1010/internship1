package com.ensias.facture.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateFactureDto(
        @NotNull(message = "Le client est obligatoire")
        Integer clientId,

        @Size(max = 500, message = "Les conditions ne peuvent pas dépasser 500 caractères")
        String conditions,

        List<CreateLigneFactureRequest> lignes,

        Long devisAssocieId // facultatif
) {
}
