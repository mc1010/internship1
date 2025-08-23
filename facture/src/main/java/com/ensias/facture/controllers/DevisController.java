package com.ensias.facture.controllers;

import com.ensias.facture.dto.*;
import com.ensias.facture.models.Statut;
import com.ensias.facture.services.ClientService;
import com.ensias.facture.services.DevisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/devis")
@CrossOrigin(origins = "http://localhost:5173")
public class DevisController {
    private final DevisService devisService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DevisDto createDevis( @Valid @RequestBody CreateDevisDTO dto) {
        return devisService.createDevis(dto);
    }

    @GetMapping("/{id}")
    public DevisDto getDevisById(@PathVariable Long id) {
        return devisService.getDevisById(id);
    }

   @PutMapping("/{id}")
    public DevisDto updateDevis(@PathVariable Long id, @Valid @RequestBody UpdateDevisDTO dto) {
        return devisService.updateDevis(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDevis(@PathVariable Long id) {
        devisService.deleteDevis(id);
    }

    @GetMapping
    public List<DevisDto> getAllDevis() {
        return devisService.getAllDevis();
    }

    @GetMapping("/client/{clientId}")
    public List<DevisDto> getDevisByClient(@PathVariable Integer clientId) {
        return devisService.getDevisByClient(clientId);
    }
   @GetMapping("/statut/{statut}")
    public List<DevisDto> getDevisByStatut(@PathVariable Statut statut) {
        return devisService.getDevisByStatut(statut);
    }
    @GetMapping("/date")
    public List<DevisDto> getDevisByDateRange(@RequestParam("start") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate start, @RequestParam("end") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)  LocalDate end) {
        return devisService.getDevisByDateRange(start, end);
    }
    @PostMapping("{devisId}/lignes")
    @ResponseStatus(HttpStatus.CREATED)
    public LigneDevisDTO addLigneToDevis(@PathVariable Long devisId, @Valid @RequestBody CreateLigneDevisRequest dto) {
        return devisService.addLigneToDevis(devisId, dto);
    }
   @PutMapping("/{devisId}/lignes/{ligneId}")
    public LigneDevisDTO updateLigneDevis( @PathVariable Long devisId,@PathVariable Long ligneId,@Valid @RequestBody UpdateLigneDevisDTO dto) {
        return devisService.updateLigneDevis(devisId,ligneId, dto);
    }
   @PutMapping("/{id}/statut")
    public DevisDto changerStatut( @PathVariable Long id, @RequestParam Statut newStatut) {
        return devisService.changerStatut(id, newStatut);
    }
   @DeleteMapping("/{devisId}/lignes/{ligneId}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLigneDevis(@PathVariable Long devisId, @PathVariable Long ligneId) {
        devisService.deleteLigneDevis(devisId,ligneId);
    }
}
