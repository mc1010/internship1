package com.ensias.facture.controllers;


import com.ensias.facture.dto.*;
import com.ensias.facture.services.FactureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/factures")
@RequiredArgsConstructor
public class FactureController {
    private final FactureService factureService;

    //----Crud Facture---------

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FactureDto createFacture(@Valid @RequestBody CreateFactureDto dto) {
        return factureService.createFacture(dto);
    }

    @PutMapping("/{id}")
    public FactureDto updateFacture(@PathVariable Long id, @Valid @RequestBody UpdateFactureDto dto) {
        return factureService.updateFacture(id, dto);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFacture(@PathVariable Long id) {
        factureService.deleteFacture(id);
    }
    @GetMapping("/{id}")
    public FactureDto getFactureById(@PathVariable Long id) {
        return factureService.getFactureById(id);
    }
   @GetMapping
    public List<FactureDto> getAllFactures() {
        return factureService.getAllFactures();
    }
    @GetMapping("/client/{clientId}")
    public List<FactureDto> getFacturesByClient(@PathVariable Integer clientId) {
        return factureService.getFacturesByClient(clientId);
    }
    @GetMapping("/range")
    public List<FactureDto> getFacturesByDateRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return factureService.getFacturesByDateRange(start, end);
    }
//-------LigneFacture--------------

    @PostMapping("/{factureId}/lignes")
    @ResponseStatus(HttpStatus.CREATED)
    public LigneFactureDto addLigneToFacture(@PathVariable Long factureId, @Valid @RequestBody CreateLigneFactureRequest dto) {
        return factureService.addLigneToFacture(factureId, dto);
    }
    @PutMapping("/{factureId}/lignes/{ligneId}")
    public LigneFactureDto updateLigneFacture(@PathVariable Long factureId, @PathVariable Long ligneId, @Valid @RequestBody UpdateLigneFactureDto dto) {
        return factureService.updateLigneFacture(factureId, ligneId, dto);
    }
   @DeleteMapping("/{factureId}/lignes/{ligneId}")
    public void deleteLigneFacture(@PathVariable Long factureId, @PathVariable Long ligneId) {
        factureService.deleteLigneFacture(factureId, ligneId);
    }
}
