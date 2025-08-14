package com.ensias.facture.dto;

public record LigneDevisDTO(
        Long id,
        Long produitId,
        String produitNom,
        Integer quantite,
        Double prixUnitaire,
        Double tva,
        Double prixTotal
) {
}
