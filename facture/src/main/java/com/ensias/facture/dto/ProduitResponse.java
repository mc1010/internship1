package com.ensias.facture.dto;

public record ProduitResponse(
        Long id,
        String nom,
        String description,
        Double prixUnitaire,
        String uniteMesure,
        Double tva
) {
}
