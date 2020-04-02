package de.wevsvirushackathon.coronareport.client;

import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/client")
public class ClientController {

    private ClientRepository clientRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ClientController(ClientRepository clientRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerClient(@RequestBody ClientDto clientDto) {
        Client client = this.registerClientAndCreateExternalId(clientDto);
        		
        return ResponseEntity.ok(client.getClientCode());
    }

    @GetMapping("/{clientCode}")
    public ResponseEntity<ClientDto> getClient(@PathVariable String clientCode) {
        Client client = this.clientRepository.findByClientCode(clientCode);
        if (client == null) {
            throw new EntityNotFoundException("Client with clientCode '" + clientCode +"' does not exist.");
        }
        return ResponseEntity.ok(modelMapper.map(client, ClientDto.class));
    }

    
    private Client registerClientAndCreateExternalId(ClientDto clientDto) {
        Client newClient = modelMapper.map(clientDto, Client.class);
        newClient.setClientCode(createNewClientId());
        this.clientRepository.save(newClient);
        return newClient;
    }

    /**
     * Creates a new unique client id
     * @return
     */
    private String createNewClientId() {
        Client possiblyExistingClient;
        String newClientCode;
        do {
            newClientCode = UUID.randomUUID().toString();
            possiblyExistingClient = this.clientRepository.findByClientCode(newClientCode);
        } while (possiblyExistingClient != null);
        return newClientCode;
    }
}
