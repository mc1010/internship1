package com.ensias.facture.repositories;

import com.ensias.facture.models.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement,Long> {
    List<Paiement> findByFactureId(Long factureId);
}
