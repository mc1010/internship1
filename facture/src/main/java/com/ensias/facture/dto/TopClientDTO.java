package com.ensias.facture.dto;

public record TopClientDTO(
        Integer clientId,
        String raisonSociale,
        Double totalPaye
) {
}
