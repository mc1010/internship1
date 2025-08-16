package com.ensias.facture.dto;

public record LigneFactureDto(
        Long id,
        Long produitId,
        String produitNom,
        Integer quantite,
        Double prixUnitaireHT,
        Double montantHT,
        Double montantTVA,
        Double montantTTC
) {}

