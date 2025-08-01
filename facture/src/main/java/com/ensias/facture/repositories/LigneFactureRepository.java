package com.ensias.facture.repositories;


import com.ensias.facture.models.LigneFacture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LigneFactureRepository extends JpaRepository<LigneFacture,Long> {
    List<LigneFacture> findByFactureId(Long factureId);


}
