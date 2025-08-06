package com.ensias.facture.services;

import com.ensias.facture.dto.CreateProduitRequest;
import com.ensias.facture.dto.ProduitResponse;
import com.ensias.facture.dto.UpdateProduitRequest;

import java.util.List;

public interface ProduitService {
    ProduitResponse createProduit(CreateProduitRequest request);
    ProduitResponse getProduitById(Long id);
    List<ProduitResponse> searchProduitByNom(String nom);
    List<ProduitResponse> getAllProduits();
    ProduitResponse updateProduit(Long id, UpdateProduitRequest request);
    void deleteProduit(Long id);

}
