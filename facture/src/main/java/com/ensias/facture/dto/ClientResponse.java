package com.ensias.facture.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private Integer id;
    private String raisonSociale;
    private String ice;
    private String coordonnees;
    private String modeReglement;
}
