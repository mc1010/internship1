package com.ensias.facture.repositories;

import com.ensias.facture.models.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProduitRepository extends JpaRepository<Produit,Long> {
    boolean existsByNom(String nom);
    Optional<Produit> findByNom(String nom);
    List<Produit> findAll();

    List<Produit> findByNomContainingIgnoreCase(String nom);//chercher produit par nom

}
