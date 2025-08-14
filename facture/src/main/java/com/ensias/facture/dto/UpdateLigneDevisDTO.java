package com.ensias.facture.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateLigneDevisDTO(
        @NotNull(message = "Id de la ligne est obligatoire")
        Long id,
        @NotNull(message = "Quantité est obligatoire")
        @Min(value = 1, message = "La quantité doit être au moins 1")
        Integer quantite
) {}