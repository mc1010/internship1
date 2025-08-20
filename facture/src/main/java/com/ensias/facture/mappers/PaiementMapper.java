package com.ensias.facture.mappers;

import com.ensias.facture.dto.CreatePaiementDto;
import com.ensias.facture.dto.PaiementDto;
import com.ensias.facture.models.Facture;
import com.ensias.facture.models.Paiement;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PaiementMapper {

    @Mapping(source = "facture.id", target = "factureId")
    PaiementDto toDto(Paiement paiement);

    // Ici on enlève la tentative de mapping automatique sur facture

    @Mapping(target = "facture", ignore = true) // on le gère dans afterMapping
    @Mapping(source = "dto.datePaiement", target = "datePaiement")
    @Mapping(source = "dto.montant", target = "montant")
    @Mapping(source = "dto.modePaiement", target = "modePaiement")
    @Mapping(source = "dto.referenceTransaction", target = "referenceTransaction")
    Paiement toEntity(CreatePaiementDto dto, @Context Facture facture);

    @AfterMapping
    default void linkFacture(@MappingTarget Paiement paiement, @Context Facture facture){
        paiement.setFacture(facture);
    }
}

