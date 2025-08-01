package com.ensias.facture.repositories;

import com.ensias.facture.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client,Integer> {

    // Verifie si un client existe deja avec cet ICE
    boolean existsByICE(String ice);

    // Trouve un client par raison sociale exacte
    Optional<Client> findByRaisonSocial(String raisonSociale);

    // Recherche de clients dont la raison sociale contient une chaîne, insensible à la casse
    List<Client> findByRaisonSocialeContainingIgnoreCase(String raison);

}
