package com.ensias.facture.services.Imp;

import com.ensias.facture.dto.ClientResponse;
import com.ensias.facture.dto.CreateClientRequest;
import com.ensias.facture.dto.UpdateClientRequest;
import com.ensias.facture.exception.AlreadyExistsException;
import com.ensias.facture.exception.NotFoundException;
import com.ensias.facture.mappers.ClientMapper;
import com.ensias.facture.models.Client;
import com.ensias.facture.repositories.ClientRepository;
import com.ensias.facture.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public ClientResponse createClient(CreateClientRequest request) {
        if (clientRepository.existsByIce(request.getIce())){
            throw new AlreadyExistsException("Client with ICE " + request.getIce() + " already exists");
        }
        // Conversion DTO vers Entité
        Client client = clientMapper.toEntity(request);
        // Sauvegarde dans la base
        Client clientsaved = clientRepository.save(client);
        // Retourne le DTO réponse
        return clientMapper.toDto(clientsaved);
    }

    @Override
    public ClientResponse getClientById(Integer id) {
        Client client = clientRepository.findById(id).orElseThrow(()->new NotFoundException("Client with id " + id + " not found"));
        return clientMapper.toDto(client);
    }

    @Override
    public List<ClientResponse> searchClientsByRaisonSociale(String raisonSociale) {
        List<Client> clients=clientRepository.findByRaisonSocialeContainingIgnoreCase(raisonSociale);
        return clients.stream().map(clientMapper::toDto).toList();
    }


    @Override
    public List<ClientResponse> getAllClients() {
        List<Client> clients= clientRepository.findAll();
        return clients.stream().map(clientMapper::toDto).toList(); // transforme chaque entité Client en DTO et retourne la liste de ClientResponse
    }

    @Override
    public ClientResponse updateClient(Integer id, UpdateClientRequest request) {
        Client clientexiste= clientRepository.findById(id).orElseThrow(()-> new NotFoundException("Client with id " + id + " not found"));
        clientMapper.updateClientfromDto(request,clientexiste);
        Client updatedClient = clientRepository.save(clientexiste);
        return clientMapper.toDto(updatedClient);
    }

    @Override
    public void deleteClient(Integer id) {
        Client client = clientRepository.findById(id).orElseThrow(()-> new NotFoundException("Client with id " + id + " not found"));
        clientRepository.delete(client);
    }
}
