package quarano;

//@Configuration
public class RestTemplateConfig {

	// @Bean
	// public RestTemplate restTemplate(RestTemplateBuilder builder) throws NoSuchAlgorithmException,
	// KeyManagementException {
	//
	// TrustManager[] trustAllCerts = new TrustManager[] {
	// new X509TrustManager() {
	// public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	// return new X509Certificate[0];
	// }
	// public void checkClientTrusted(
	// java.security.cert.X509Certificate[] certs, String authType) {
	// }
	// public void checkServerTrusted(
	// java.security.cert.X509Certificate[] certs, String authType) {
	// }
	// }
	// };
	// SSLContext sslContext = SSLContext.getInstance("SSL");
	// sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
	// CloseableHttpClient httpClient = HttpClients.custom()
	// .setSSLContext(sslContext)
	// .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
	// .build();
	// HttpComponentsClientHttpRequestFactory customRequestFactory = new HttpComponentsClientHttpRequestFactory();
	// customRequestFactory.setHttpClient(httpClient);
	// return builder.requestFactory(() -> customRequestFactory).build();
	// }
}
