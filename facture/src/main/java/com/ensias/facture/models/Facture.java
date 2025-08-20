package com.ensias.facture.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="factures")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Facture implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_facture", nullable = false, unique = true)
    private String numero;

    @Column(name = "date_emission", nullable = false)
    private LocalDate dateEmission;


    @Enumerated(EnumType.STRING)
    @Column(name = "statut_paiement", nullable = false)
    private StatutPaiement statutPaiement; // PAYEE, PARTIELLE, IMPAYEE

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LigneFacture> lignesFacture= new ArrayList<>();
    // ← Une facture contient plusieurs lignes (produits + quantités)
    // mappedBy = "facture" → ça veut dire que la relation est définie dans la classe LigneFacture
    // cascade = ALL → si on supprime/ajoute une facture, on le fait aussi pour ses lignes
    // orphanRemoval = true → si on retire une ligne, elle est aussi supprimée en base
    // fetch = LAZY → on ne charge les lignes que si on en a besoin

    @OneToOne
    @JoinColumn(name = "devis_id")
    private Devis devisAssocie;

    @Column(nullable=false)
    private Double totalHT;

    @Column(nullable=false)
    private Double totalTVA;

    @Column(nullable=false)
    private Double totalTTC;

    @Column(columnDefinition = "TEXT")
    private String conditions;
    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Paiement> paiements;

}