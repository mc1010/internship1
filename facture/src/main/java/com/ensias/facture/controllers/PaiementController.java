package com.ensias.facture.controllers;

import com.ensias.facture.dto.CreatePaiementDto;
import com.ensias.facture.dto.PaiementDto;
import com.ensias.facture.services.PaiementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PaiementController {
    private final PaiementService paiementService;
    @PostMapping
    public ResponseEntity<PaiementDto> createPaiement(@RequestBody @Valid CreatePaiementDto dto) {
        PaiementDto paiement = paiementService.createPaiement(dto);
        return new ResponseEntity<>(paiement, HttpStatus.CREATED);
    }

    @GetMapping("/facture/{factureId}")
    public ResponseEntity<List<PaiementDto>> getPaiementsByFacture(@PathVariable Long factureId) {
        List<PaiementDto> paiements = paiementService.getPaiementsByFacture(factureId);
        return ResponseEntity.ok(paiements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaiementDto> getPaiement(@PathVariable Long id) {
        PaiementDto paiement = paiementService.getPaiement(id);
        return ResponseEntity.ok(paiement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaiement(@PathVariable Long id) {
        paiementService.deletePaiement(id);
        return ResponseEntity.noContent().build();
    }
}

