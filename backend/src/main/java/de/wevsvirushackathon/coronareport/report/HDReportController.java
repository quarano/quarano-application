package de.wevsvirushackathon.coronareport.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class HDReportController {


    private HDReportService reportService;

    @Autowired
    public HDReportController(HDReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/{healthDepartmentId}")
    public ResponseEntity<List<HDClient>> getGAClientsOfHealthDepartment(@PathVariable String healthDepartmentId) {
    	
    	List<HDClient> clients = reportService.getClientsByHDId(healthDepartmentId);

        return ResponseEntity.ok(clients);
    }

}
