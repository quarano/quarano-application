package de.wevsvirushackathon.coronareport.healthdepartment;

import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Api(tags = "hd-controller")
@RestController
@RequestMapping("/healthdepartments")
public class HealthDepartmentController {

    private HealthDepartmentRepository healthDepartmentRepository;
    private ModelMapper modelMapper;

    @Autowired
    public HealthDepartmentController(
            HealthDepartmentRepository healthDepartmentRepository,
            ModelMapper modelMapper) {
        this.healthDepartmentRepository = healthDepartmentRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<HealthDepartmentDTO>> getHealthDepartments() {
        Iterable<HealthDepartment> all = this.healthDepartmentRepository.findAll();
        List<HealthDepartmentDTO> dtoList = StreamSupport
                .stream(all.spliterator(), false).map(h -> modelMapper.map(h, HealthDepartmentDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

}
