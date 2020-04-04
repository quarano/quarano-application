package de.wevsvirushackathon.coronareport.report;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.wevsvirushackathon.coronareport.diary.DiaryEntry;
import de.wevsvirushackathon.coronareport.diary.DiaryEntryRepository;
import de.wevsvirushackathon.coronareport.client.Client;
import de.wevsvirushackathon.coronareport.client.ClientRepository;

@Service
public class HDReportService {

	private final Logger log = LoggerFactory.getLogger(HDReportService.class);

	private ModelMapper modelMapper;
    private ClientRepository clientRepository;
	private DiaryEntryRepository diaryRepository;

    @Autowired
    public HDReportService(ModelMapper modelMapper,
						   ClientRepository clientRepository,
						   DiaryEntryRepository diaryRepository) {
    	this.modelMapper = modelMapper;
        this.clientRepository = clientRepository;
        this.diaryRepository = diaryRepository;
    }

    public List<HDClient> getClientsByHDId(String healthDepartmentId) {
    	
        List<Client> clients = this.clientRepository.findAllByHealthDepartmentId(healthDepartmentId);
        
        if(clients == null) {
        	log.info("No clients found for hd: " + healthDepartmentId);
        	return new ArrayList<>();
        }

		return clients.stream().map(client -> {

			HDClient hdClient = modelMapper.map(client, HDClient.class);

			List<DiaryEntry> diaryEntries =  diaryRepository.findAllByClientOrderByDateTimeDesc(client);
			hdClient.setDiaryEntires(diaryEntries);

			if(!diaryEntries.isEmpty()) {

				if(diaryEntries.get(0).getDateTime() != null) {
					hdClient.setDateTimeOfLastReport(diaryEntries.get(0).getDateTime().toLocalDateTime());
				}

				hdClient.setCurrentBodyTemperature(diaryEntries.get(0).getBodyTemperature());

			}

			determineStatus(hdClient);

			return hdClient;

		}).collect(Collectors.toList());
    }

	private void determineStatus(HDClient hdClient) {
		
		if(hdClient.getCurrentBodyTemperature() > 39.0) {
        	hdClient.getMonitoringStatus().add(MonitoringStatus.CHECK_HEALTH);
        	hdClient.setMonitoringMessage(MonitoringStatus.CHECK_HEALTH.getMessage());
		}
		
//		if(hdClient.getDateTimeOfLastReport()!= null && hdClient.) {
//			
//		}
		
		if(hdClient.getMonitoringStatus().isEmpty()) {
			hdClient.getMonitoringStatus().add(MonitoringStatus.OK);
			hdClient.setMonitoringMessage(MonitoringStatus.OK.getMessage());
		}
	}

}
