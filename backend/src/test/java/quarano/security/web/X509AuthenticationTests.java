package quarano.security.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;

import java.security.KeyStore;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * Integration tests to verify the setup of the X509 authentication for third parties.
 *
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// Reset explicit port configuration and resort to random one to avoid
// conflicts with a currently running system on the same machine
@TestPropertySource(properties = "http.port=0")
class X509AuthenticationTests {

	private final ResourceLoader resources;
	private @LocalServerPort String port;

	@Test // CORE-613
	void grantsAccessToCertificateProtectedResourceIfCertificatePresented() throws Exception {

		var response = createClient(true).getForEntity("https://localhost:" + port + "/ext/auth", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test // CORE-613
	void rejectsAccessForCertificateProtectedResource() throws Exception {

		var withoutClientCertificate = createClient(false);

		assertThatExceptionOfType(Unauthorized.class)
				.isThrownBy(
						() -> withoutClientCertificate.getForEntity("https://localhost:" + port + "/ext/auth", String.class));
	}

	@Test // CORE-613
	void allowsAccessToPublicResourceWithoutCertificate() throws Exception {

		var response = createClient(false).getForEntity("https://localhost:" + port, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test // CORE-613
	void rootResourceReturnsLinkToVisitorSubmissionResourceIfAuthenticatedWithCertificate() throws Exception {

		var response = createClient(true).getForEntity("https://localhost:" + port, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	private RestOperations createClient(boolean withCertificate) throws Exception {

		var password = "quarano".toCharArray();
		var keystore = "classpath:security/client/acme-event-manager.p12";

		var ks = KeyStore.getInstance("JKS");

		try (var ksStream = resources.getResource(keystore).getInputStream()) {
			ks.load(ksStream, password);
		}

		var builder = new SSLContextBuilder()
				.loadTrustMaterial((chain, authType) -> true);

		if (withCertificate) {
			builder = builder.loadKeyMaterial(ks, password);
		}

		var sslContext = builder.build();

		var socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		var httpClient = HttpClientBuilder.create()
				.setSSLSocketFactory(socketFactory)
				.build();

		return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
	}
}
