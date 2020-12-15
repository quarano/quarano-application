package de.quarano.sormas.client;

import de.quarano.sormas.client.api.CaseControllerApi;
import de.quarano.sormas.client.invoker.ApiClient;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SormasIntegrationConfig {

	@Bean
	public CaseControllerApi caseControllerApi() {

		return new CaseControllerApi(apiClient());
	}

	@Bean
	public ApiClient apiClient() {

		ApiClient apiClient = new QuaranoApiClient();
		apiClient.setBasePath("https://sormas-docker-test.com/sormas-rest");
		apiClient.setUsername("SurvOff");
		apiClient.setPassword("SurvOff");

		return apiClient;
	}

	static class QuaranoApiClient extends ApiClient {

		@Override
		protected RestTemplate buildRestTemplate() {

			var restTemplate = super.buildRestTemplate();

			var customRequestFactory = new HttpComponentsClientHttpRequestFactory();

			try {

				var builder = new SSLContextBuilder();
				builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
				var sslsf = new SSLConnectionSocketFactory(builder.build());
				var httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

				customRequestFactory.setHttpClient(httpClient);

			} catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(customRequestFactory));

			return restTemplate;
		}
	}
}
