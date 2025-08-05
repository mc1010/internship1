package com.ensias.facture.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientRequest {
    @NotBlank(message = "Raison sociale est obligatoire")
    private String raisonSociale;
    @NotBlank(message = "ICE est obligatoire")
    private String ice;
    @NotBlank(message = "Coordonnées sont obligatoires")
    private String coordonnees;
    @NotBlank(message = "Mode de règlement est obligatoire")
    private String modeReglement;
}
