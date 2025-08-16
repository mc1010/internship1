package com.ensias.facture.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Entity
@Table(name="ligne_facture")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LigneFacture implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="produit_id",nullable = false)
    private Produit produit;

    @ManyToOne
    @JoinColumn(name="facture_id",nullable = false)
    private Facture facture;

    @Column(name="quantite", nullable = false)
    private Integer quantite;
    @Column(nullable=false)
    private Double prixUnitaireHT;

    @Column(nullable=false)
    private Double montantHT;

    @Column(nullable=false)
    private Double montantTVA;

    @Column(nullable=false)
    private Double montantTTC;
}





