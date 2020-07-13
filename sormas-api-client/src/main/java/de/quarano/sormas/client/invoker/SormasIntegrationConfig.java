package de.quarano.sormas.client.invoker;

import de.quarano.sormas.client.api.CaseControllerApi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SormasIntegrationConfig {

	@Bean
	public CaseControllerApi caseControllerApi() {

		return new CaseControllerApi(apiClient());
	}

	@Bean
	public ApiClient apiClient() {

		ApiClient apiClient = new ApiClient();
		apiClient.setUsername("SurvOff");
		apiClient.setPassword("SurvOff");

		return apiClient;
	}
}
