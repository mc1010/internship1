package com.ensias.facture.mappers;


import com.ensias.facture.dto.CreateProduitRequest;
import com.ensias.facture.dto.ProduitResponse;
import com.ensias.facture.dto.UpdateProduitRequest;
import com.ensias.facture.models.Produit;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper( componentModel = "spring")
public interface ProduitMapper {
    Produit toEntity(CreateProduitRequest dto);

    ProduitResponse toDto(Produit produit);

    void updateProduitFromDto(UpdateProduitRequest dto, @MappingTarget Produit produit);

}
