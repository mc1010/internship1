package com.ensias.facture.services;

import com.ensias.facture.dto.ClientResponse;
import com.ensias.facture.dto.CreateClientRequest;
import com.ensias.facture.dto.UpdateClientRequest;

import java.util.List;

public interface ClientService {

    ClientResponse createClient(CreateClientRequest request);

    ClientResponse getClientById(Integer id); //Récupérer un client

    List<ClientResponse> searchClientsByRaisonSociale(String raisonSociale);

    List<ClientResponse> getAllClients(); //Liste de tous les clients

    ClientResponse updateClient(Integer id, UpdateClientRequest request); // Modifier un client

    void deleteClient(Integer id); //Supprimer un client
}
