package com.ensias.facture.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="devis")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Devis implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="numero_devis", nullable = false,unique=true)
    private String numeroDevis;

    @Column(name="date_creation", nullable = false)
    private LocalDate dateCreation;

    @Column(name="date_expiration", nullable = false)
    private LocalDate dateExpiration;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;


    @Column(name = "montant_total")
    private Double montantTotal= 0.0;

    @Column(columnDefinition = "TEXT")
    private String conditions;

    @Enumerated(EnumType.STRING)
    @Column(name="statut", nullable=false)
    private Statut statut;

    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LigneDevis> lignesDevis = new ArrayList<>();


}
