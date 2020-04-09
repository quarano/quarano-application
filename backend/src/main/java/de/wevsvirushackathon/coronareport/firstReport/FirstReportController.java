package de.wevsvirushackathon.coronareport.firstReport;

import de.wevsvirushackathon.coronareport.client.Client;
import de.wevsvirushackathon.coronareport.client.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/firstreport")
public class FirstReportController {

    private ClientRepository clientRepository;
    private FirstReportService firstReportService;

    @Autowired
    public FirstReportController(ClientRepository clientRepository, FirstReportService firstReportService) {
        this.clientRepository = clientRepository;
        this.firstReportService = firstReportService;
    }

    @PostMapping("/{clientCode}")
    public ResponseEntity<Void> addFirstReport(@PathVariable String clientCode, @RequestBody FirstReport firstReportDto) {
        Client client = this.clientRepository.findByClientCode(clientCode);
        if (client == null) {
            return ResponseEntity.badRequest().build();
        }
        if (client.getComments() != null) {
            return ResponseEntity.badRequest().build();
        }

        this.firstReportService.addFirstReport(firstReportDto);
        return ResponseEntity.ok().build();
    }
}
