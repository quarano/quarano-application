package quarano.department;

import static org.assertj.core.api.Assertions.*;

import de.quarano.sormas.client.api.CaseControllerApi;
import de.quarano.sormas.client.invoker.SormasIntegrationConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;

import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.JsonPath;
import org.springframework.data.web.ProjectedPayload;
import org.springframework.data.web.ProjectingJackson2HttpMessageConverter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.Base64Utils;

@WithQuaranoUser("agent1")
@QuaranoWebIntegrationTest
@RequiredArgsConstructor
@Import(SormasIntegrationConfig.class)
public class SormasTest {

	private final @NonNull CaseControllerApi sormasCaseController;

	@Test
	void withOpenApiExport_and_restTemplate() {

		var sormasCaseDtos = sormasCaseController.getAllCases(0L);

		assertThat(sormasCaseDtos).isNotEmpty();
	}

	@Test
	void withProjection_and_restTemplate() throws NoSuchAlgorithmException, KeyManagementException {

		TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					@Override
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return new X509Certificate[0];
					}

					@Override
					public void checkClientTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {}

					@Override
					public void checkServerTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {}
				}
		};
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLContext(sslContext)
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
				.build();
		HttpComponentsClientHttpRequestFactory customRequestFactory = new HttpComponentsClientHttpRequestFactory();
		customRequestFactory.setHttpClient(httpClient);

		var builder = new RestTemplateBuilder()//
				.additionalMessageConverters(new ProjectingJackson2HttpMessageConverter());

		var template = builder.requestFactory(() -> customRequestFactory).build();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());

		String str = "SurvOff:SurvOff";
		headers.add(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString(str.getBytes(StandardCharsets.UTF_8)));

		var payload = template.exchange("https://sormas-docker-test.com/sormas-rest/cases/all/0", HttpMethod.GET,
				new HttpEntity<Void>(headers), UserPayload.class).getBody();

		var users = payload.users();
		System.out.println(users.size());

		assertThat(payload).isNotNull();
	}

	@ProjectedPayload
	interface UserPayload {

		@JsonPath("$..person")
		List<Users> users();

		class Users {

			String lastName, firstName;

			public String getFirstName() {
				return firstName;
			}

			public String getLastName() {
				return lastName;
			}
		}
	}
}
