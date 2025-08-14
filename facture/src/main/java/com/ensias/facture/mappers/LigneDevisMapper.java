package com.ensias.facture.mappers;

import com.ensias.facture.dto.CreateLigneDevisRequest;
import com.ensias.facture.dto.LigneDevisDTO;
import com.ensias.facture.dto.UpdateLigneDevisDTO;
import com.ensias.facture.models.LigneDevis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper( componentModel = "spring")
public interface LigneDevisMapper {

    @Mapping(target = "produitId", source="produit.id")
    @Mapping(target = "produitNom", source = "produit.nom")
    @Mapping(target = "prixUnitaire", source = "produit.prixUnitaire")
    @Mapping(target = "tva", source = "produit.tva")

    LigneDevisDTO toDto(LigneDevis entity);
    LigneDevis toEntity(CreateLigneDevisRequest dto);
    void updateLigneDevisFromDto(UpdateLigneDevisDTO dto,@MappingTarget LigneDevis entity);


}
