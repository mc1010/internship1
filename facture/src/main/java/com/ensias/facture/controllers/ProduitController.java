package com.ensias.facture.controllers;


import com.ensias.facture.dto.CreateProduitRequest;
import com.ensias.facture.dto.ProduitResponse;
import com.ensias.facture.dto.UpdateProduitRequest;
import com.ensias.facture.services.ProduitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin(origins = "http://localhost:5174")
public class ProduitController {
    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @PostMapping
    public ResponseEntity<ProduitResponse> createProduit(@Valid @RequestBody CreateProduitRequest request) {
        ProduitResponse produit = produitService.createProduit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(produit);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<ProduitResponse> getProduitById(@PathVariable Long id) {
        ProduitResponse produit= produitService.getProduitById(id);
        return ResponseEntity.ok(produit);
    }
    @GetMapping("/search")
    public ResponseEntity<List<ProduitResponse>> searchProduitByNom(@RequestParam String nom) {
        List<ProduitResponse> produits= produitService.searchProduitByNom(nom);
        return ResponseEntity.ok(produits);
    }
    @GetMapping
    public ResponseEntity<List<ProduitResponse>> getAllProduits() {
        List<ProduitResponse> produits= produitService.getAllProduits();
        return ResponseEntity.ok(produits);
    }
    @PutMapping("/id/{id}")
    public ResponseEntity<ProduitResponse> updateProduit( @PathVariable Long id, @Valid @RequestBody UpdateProduitRequest request) {
        ProduitResponse upd =produitService.updateProduit(id,request);
        return ResponseEntity.ok(upd);
    }
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return  ResponseEntity.noContent().build();
    }
}
