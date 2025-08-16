package com.ensias.facture.mappers;

import com.ensias.facture.dto.CreateFactureDto;
import com.ensias.facture.dto.FactureDto;
import com.ensias.facture.dto.UpdateFactureDto;
import com.ensias.facture.models.Facture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel ="spring", uses={LigneFactureMapper.class})
public interface FactureMapper {
    Facture toEntity(CreateFactureDto dto);
    @Mapping(source= "client.id",target = "clientId")
    @Mapping(source = "client.raisonSociale", target = "clientRaisonSociale")
    @Mapping(source = "client.ice", target = "clientIce")
    @Mapping(source = "devisAssocie.id", target = "devisAssocieId")
    @Mapping(source = "lignesFacture", target = "lignes")
    FactureDto toDto(Facture entity);

    // Mise à jour d'une facture existante
    void updateFactureFromDto(UpdateFactureDto dto, @MappingTarget Facture entity);
}

