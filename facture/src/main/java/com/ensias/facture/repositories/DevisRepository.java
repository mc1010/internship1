package com.ensias.facture.repositories;

import com.ensias.facture.models.Devis;
import com.ensias.facture.models.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface DevisRepository extends JpaRepository<Devis,Long> {
    Optional<Devis> findById(Long id);

    List<Devis> findByClientId(Integer clientId);

    List<Devis> findByStatut(Statut statut); //devis acceptés, refusés, etc.

    List<Devis> findByDateCreationBetween(LocalDate start, LocalDate end);
}
