package com.ensias.facture.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 2, max = 100, message = "Raison sociale doit contenir entre 2 et 100 caractères")
    private String raisonSociale;

    @Size(min = 5, max = 15, message = "ICE doit contenir entre 5 et 15 chiffres")
    @NotBlank(message = "ICE est obligatoire")
    private String ice;

    @NotBlank(message = "Coordonnées sont obligatoires")
    private String coordonnees;
    @NotBlank(message = "Mode de règlement est obligatoire")
    private String modeReglement;
}
