package de.wevsvirushackathon.coronareport.firstReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirstReportService {

    private FirstReportRepository firstReportRepository;

    @Autowired
    public FirstReportService(FirstReportRepository firstReportRepository) {
        this.firstReportRepository = firstReportRepository;
    }


    public void addFirstReport(FirstReport report) {
        this.firstReportRepository.save(report);
    }

}
