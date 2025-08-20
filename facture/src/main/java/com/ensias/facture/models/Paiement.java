package com.ensias.facture.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDate;


@Entity
@Table(name="Paiement")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paiement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate datePaiement;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private String modePaiement; // Espèces, Virement, Carte, etc.

    @Column(name = "reference_transaction")
    private String referenceTransaction;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facture_id", nullable = false)
    private Facture facture;
}
