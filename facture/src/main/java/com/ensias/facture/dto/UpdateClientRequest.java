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
public class UpdateClientRequest {
    @NotBlank(message = "Raison sociale est obligatoire")
    @Size(min = 2, max = 100)
    String raisonSociale;

    @NotBlank(message = "Coordonnées sont obligatoires")
    String coordonnees;

    @NotBlank(message = "Le mode de règlement est obligatoire")
    String modeReglement;
}
