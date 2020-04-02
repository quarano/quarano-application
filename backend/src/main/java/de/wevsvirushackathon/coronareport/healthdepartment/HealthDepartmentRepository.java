package de.wevsvirushackathon.coronareport.healthdepartment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthDepartmentRepository
        extends CrudRepository<HealthDepartment, String> {
}