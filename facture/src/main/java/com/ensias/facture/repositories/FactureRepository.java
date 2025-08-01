package com.ensias.facture.repositories;

import com.ensias.facture.models.Facture;
import com.ensias.facture.models.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface FactureRepository extends JpaRepository<Facture,Long> {
    Optional<Facture> findById(Long id); //chercher une facture par ID

    List<Facture> findByClientId(Integer clientId); //toutes les factures d’un client

    List<Facture> findByStatutPaiement(StatutPaiement statut); //factures par statut (payée, impayée, etc.)

    List<Facture> findByDateEmissionBetween(LocalDate start, LocalDate end); // factures émises dans une plage de dates


}
