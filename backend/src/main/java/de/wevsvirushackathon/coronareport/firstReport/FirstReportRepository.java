package de.wevsvirushackathon.coronareport.firstReport;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirstReportRepository extends CrudRepository<FirstReport, Long> {

}
