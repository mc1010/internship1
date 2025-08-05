package com.ensias.facture.mappers;

import com.ensias.facture.dto.ClientResponse;
import com.ensias.facture.dto.CreateClientRequest;
import com.ensias.facture.dto.UpdateClientRequest;
import com.ensias.facture.models.Client;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper( componentModel = "spring")
public interface ClientMapper {

    // Convertit CreateClientRequest (DTO création) en Client (Entité)
    Client toEntity(CreateClientRequest dto);

    // Convertit Client (Entité) en ClientResponse (DTO réponse)
    ClientResponse toDto(Client entity);

    // Met à jour un Client existant avec UpdateClientRequest (DTO mise à jour)
    void updateClientfromDto(UpdateClientRequest dto, @MappingTarget Client entity);

}
