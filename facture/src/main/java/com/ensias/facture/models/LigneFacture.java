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

    @Column(name="quantite", nullable = false)
    private int quantite;

    @Column(name="prixTotal", nullable = false)
    private Double prixTotal;

    @ManyToOne
    @JoinColumn(name="produit_id",nullable = false)
    private Produit produit;

    @ManyToOne
    @JoinColumn(name="facture_id",nullable = false)
    private Facture facture;


}
