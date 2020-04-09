package de.wevsvirushackathon.coronareport.symptomes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class SymptomsDataInputBean implements ApplicationListener<ContextRefreshedEvent>, Ordered {

    private final Logger log = LoggerFactory.getLogger(SymptomsDataInputBean.class);

    private SymptomRepository repository;

    public SymptomsDataInputBean(SymptomRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final String jsonFileName = "masterdata/symptoms.json";
        final InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream(jsonFileName);

        if (this.repository.count() > 0) {
            log.info("Symptom data already exists, skipping JSON import");
            return;
        }

        log.info("Importing symptoms from JSON file: " + jsonFileName);

        final ObjectMapper objectMapper = new ObjectMapper();

        //read json file and convert to customer object
        try {
            final List<Symptom> symptoms = objectMapper.readValue(in, new TypeReference<List<Symptom>>(){});
            Long i = 1L;
            for (final Symptom symptom : symptoms) {
                symptom.setId(i);
                i++;
            }
            this.repository.saveAll(symptoms);
        } catch (IOException e) {
           throw new IllegalStateException("Unable to parse masterdata file", e);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
