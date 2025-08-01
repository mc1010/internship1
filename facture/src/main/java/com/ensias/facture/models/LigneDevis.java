package com.ensias.facture.models;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "ligne_devis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneDevis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "prix_total", nullable = false)
    private Double prixTotal;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "devis_id", nullable = false)
    private Devis devis;
}
