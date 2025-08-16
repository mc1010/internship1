package com.ensias.facture.mappers;

import com.ensias.facture.dto.CreateLigneFactureRequest;
import com.ensias.facture.dto.LigneFactureDto;
import com.ensias.facture.dto.UpdateLigneFactureDto;
import com.ensias.facture.models.LigneFacture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LigneFactureMapper {
    // Transformation d'une ligne facture en DTO
    @Mapping(source = "produit.id", target = "produitId")
    @Mapping(source = "produit.nom", target = "produitNom")
    LigneFactureDto toDto(LigneFacture entity);

    // Création d'une ligne depuis le DTO
    LigneFacture toEntity(CreateLigneFactureRequest dto);

    // Mise à jour d'une ligne existante (quantité par ex.)
    void updateLigneFactureFromDto(UpdateLigneFactureDto dto, @MappingTarget LigneFacture entity);
}

