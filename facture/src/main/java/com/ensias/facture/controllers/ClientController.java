package com.ensias.facture.controllers;


import com.ensias.facture.dto.ClientResponse;
import com.ensias.facture.dto.CreateClientRequest;
import com.ensias.facture.dto.UpdateClientRequest;
import com.ensias.facture.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody CreateClientRequest request){
        ClientResponse resp = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/id/{id}")
    public  ResponseEntity<ClientResponse> getClientById(@PathVariable Integer id){
        ClientResponse resp= clientService.getClientById(id);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ClientResponse>> searchClients(@RequestParam String raisonSociale){
        List<ClientResponse> clients =clientService.searchClientsByRaisonSociale(raisonSociale);
        return ResponseEntity.ok(clients);
    }
    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients(){
        List<ClientResponse> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Integer id, @RequestBody UpdateClientRequest request){
        ClientResponse upd = clientService.updateClient(id,request);
        return ResponseEntity.ok(upd);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void>  deleteClient(@PathVariable Integer id){
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }



}
