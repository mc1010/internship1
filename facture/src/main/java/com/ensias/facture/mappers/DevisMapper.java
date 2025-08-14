package com.ensias.facture.mappers;

import com.ensias.facture.dto.CreateDevisDTO;
import com.ensias.facture.dto.DevisDto;
import com.ensias.facture.dto.UpdateDevisDTO;
import com.ensias.facture.models.Devis;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
@Mapper(componentModel = "spring", uses = {LigneDevisMapper.class})
public interface DevisMapper {

    Devis toEntity(CreateDevisDTO dto);
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.raisonSociale", target = "clientRaisonSociale")
    @Mapping(source = "client.ice", target = "clientIce")
    @Mapping(source = "numeroDevis", target = "numeroDevis")
    @Mapping(source = "lignesDevis", target = "lignes")
    DevisDto toDto(Devis entity);
    void updateDevisFromDto(UpdateDevisDTO dto, @MappingTarget Devis entity);

}
