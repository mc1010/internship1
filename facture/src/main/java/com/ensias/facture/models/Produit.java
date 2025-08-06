package com.ensias.facture.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Entity
@Table(name="produit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="nom",nullable = false, unique = true)
    private String nom;

    @Column(name="description",columnDefinition = "TEXT")
    private String description;

    @Column(name="prix_unitaire",nullable = false)
    private Double prixUnitaire;

    @Column(name="unite_mesure",nullable = false)
    private String uniteMesure;

    @Column(name="tva",nullable = false)
    private Double tva;





}
