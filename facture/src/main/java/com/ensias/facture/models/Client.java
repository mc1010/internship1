package com.ensias.facture.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="clients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "raison_sociale", nullable = false)
    private String raisonSociale;

    @Column(name = "ice", nullable = false, unique = true)
    private String ice;

    @Column(name = "coordonnees", nullable = false)
    private String coordonnees;

    @Column(name = "mode_reglement", nullable = false)
    private String modeReglement;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Facture> factures;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Devis> devis;
}
