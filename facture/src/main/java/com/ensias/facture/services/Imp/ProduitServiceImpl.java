package com.ensias.facture.services.Imp;

import com.ensias.facture.dto.CreateProduitRequest;
import com.ensias.facture.dto.ProduitResponse;
import com.ensias.facture.dto.UpdateProduitRequest;
import com.ensias.facture.exception.AlreadyExistsException;
import com.ensias.facture.exception.NotFoundException;
import com.ensias.facture.mappers.ProduitMapper;
import com.ensias.facture.models.Produit;
import com.ensias.facture.repositories.ProduitRepository;
import com.ensias.facture.services.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitServiceImpl implements ProduitService {
    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;
    @Autowired
    public ProduitServiceImpl(ProduitRepository produitRepository, ProduitMapper produitMapper) {
        this.produitRepository = produitRepository;
        this.produitMapper = produitMapper;
    }


    @Override
    public ProduitResponse createProduit(CreateProduitRequest request) {
       if (produitRepository.existsByNom(request.nom())){
           throw new AlreadyExistsException("Produit avec nom '" + request.nom() + "' existe déjà");
       }
        Produit produit = produitMapper.toEntity(request);
       Produit saved = produitRepository.save(produit);
       return produitMapper.toDto(saved);
    }

    @Override
    public ProduitResponse getProduitById(Long id) {
       Produit produit = produitRepository.findById(id).orElseThrow(()-> new NotFoundException("Produit avec ID " + id + " non trouvé"));
       return produitMapper.toDto(produit);
    }

    @Override
    public List<ProduitResponse> searchProduitByNom(String nom) {
        List<Produit> produits = produitRepository.findByNomContainingIgnoreCase(nom);
        return produits.stream().map(produitMapper::toDto).toList();
    }

    @Override
    public List<ProduitResponse> getAllProduits() {
        List<Produit> produits = produitRepository.findAll();
        return produits.stream().map(produitMapper::toDto).toList();
    }

    @Override
    public ProduitResponse updateProduit(Long id, UpdateProduitRequest request) {
        Produit existing = produitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produit avec ID " + id + " non trouvé"));

        produitMapper.updateProduitFromDto(request, existing);
        Produit updated = produitRepository.save(existing);
        return produitMapper.toDto(updated);
    }

    @Override
    public void deleteProduit(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produit avec ID " + id + " non trouvé"));
        produitRepository.delete(produit);

    }
}
