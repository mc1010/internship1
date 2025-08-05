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
public class UpdateClientRequest {
    private String raisonSociale;
    private String ice;
    private String coordonnees;
    private String modeReglement;
}
