package com.ensias.facture.repositories;

import com.ensias.facture.models.Devis;
import com.ensias.facture.models.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface DevisRepository extends JpaRepository<Devis,Long> {
    Optional<Devis> findById(Long id);

    List<Devis> findByClientId(Integer clientId);

    List<Devis> findByStatut(Statut statut); //devis acceptés, refusés, etc.


    @Query("SELECT d FROM Devis d WHERE d.dateCreation >= :start AND d.dateCreation <= :end")
    List<Devis> findByDateCreationBetweenDates(@Param("start") LocalDate start, @Param("end") LocalDate end);

}
