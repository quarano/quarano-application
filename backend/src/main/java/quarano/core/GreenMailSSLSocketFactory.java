package quarano.core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * <a>https://www.oracle.com/technetwork/java/sslnotes-150073.txt</a>
 * <p>
 * Property to activate this <code>SSLSocketFactory</code>:
 * <code>spring.mail.properties.mail.smtp.socketFactory.class = quarano.core.GreenMailSSLSocketFactory</code>
 * </p>
 * <p>
 * Property to prevent falling back to one standard if this <code>SSLSocketFactory</code> has an error:
 * <code>spring.mail.properties.mail.smtp.socketFactory.fallback = false</code>
 * </p>
 */
public class GreenMailSSLSocketFactory extends SSLSocketFactory {

	private SSLSocketFactory factory;

	public GreenMailSSLSocketFactory() {
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null,
					new TrustManager[] { new GreenMailTrustManager() },
					null);
			factory = sslcontext.getSocketFactory();
		} catch (Exception ex) {
			// ignore
		}
	}

	public static SocketFactory getDefault() {
		return new GreenMailSSLSocketFactory();
	}

	@Override
	public Socket createSocket() throws IOException {
		return factory.createSocket();
	}

	@Override
	public Socket createSocket(Socket socket, String s, int i, boolean flag)
			throws IOException {
		return factory.createSocket(socket, s, i, flag);
	}

	@Override
	public Socket createSocket(InetAddress inaddr, int i,
			InetAddress inaddr1, int j) throws IOException {
		return factory.createSocket(inaddr, i, inaddr1, j);
	}

	@Override
	public Socket createSocket(InetAddress inaddr, int i)
			throws IOException {
		return factory.createSocket(inaddr, i);
	}

	@Override
	public Socket createSocket(String s, int i, InetAddress inaddr, int j)
			throws IOException {
		return factory.createSocket(s, i, inaddr, j);
	}

	@Override
	public Socket createSocket(String s, int i) throws IOException {
		return factory.createSocket(s, i);
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return factory.getDefaultCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return factory.getSupportedCipherSuites();
	}

	/**
	 * Accepts only the certificate integrated in GreenMail
	 */
	public class GreenMailTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] cert, String authType) throws CertificateException {
			// everything is trusted
		}

		@Override
		public void checkServerTrusted(X509Certificate[] cert, String authType) throws CertificateException {
			var derCert = cert[0].getEncoded();
			var pemCert = new String(Base64.getEncoder().encode(derCert));

			var trust = pemCert.equals("MIIDgTCCAmmgAwIBAgIEG8kG2jANBgkqhkiG9w0BAQsFADBxMQswCQYDVQQGEwJV" +
					"UzEeMBwGA1UEChMVSWNlZ3JlZW4gVGVjaG5vbG9naWVzMRIwEAYDVQQLEwlHcmVl" +
					"bk1haWwxLjAsBgNVBAMTJUdyZWVuTWFpbCBzZWxmc2lnbmVkIFRlc3QgQ2VydGlm" +
					"aWNhdGUwHhcNMjAwNzE5MjA0MDIwWhcNMjIxMDIyMjA0MDIwWjBxMQswCQYDVQQG" +
					"EwJVUzEeMBwGA1UEChMVSWNlZ3JlZW4gVGVjaG5vbG9naWVzMRIwEAYDVQQLEwlH" +
					"cmVlbk1haWwxLjAsBgNVBAMTJUdyZWVuTWFpbCBzZWxmc2lnbmVkIFRlc3QgQ2Vy" +
					"dGlmaWNhdGUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCTd+hNn5St" +
					"VtsJfWcUq3Q+i+jGWbtEh/LGm8g+qyoxL0YE5gTkoAZ/3qk2hWM1WG+iscPJu79d" +
					"0BHyPQGB7QHjpBmV3aDJj0AINqoLf74Q9Cs65oLtQ5TKWrmvsMKlzbg2q5g/C0hX" +
					"KoYBPoHWb/J2a05C06JJjtBxjy3dBcT241nFNUaWw9k8yojp7w9J7a+3rQMgOinc" +
					"xZSdJbNB6uRE7cnRvJaZtIOKkIRPpikV3/pUqdr/vXGsgLSBZuXCeZ81YNmdVSDw" +
					"66oJqRVHjEKA7pamDst7kRgOsOZiN8/xjibsUME7tR4xf0SU+ZW7NVBrSIAniB+M" +
					"Cjc9GGo/ER+tAgMBAAGjITAfMB0GA1UdDgQWBBTlGjYagZ/neX5LQi4YC4DdpCbM" +
					"OTANBgkqhkiG9w0BAQsFAAOCAQEASrUH+yqWJNoY4PBZMpYkcDqZfLjXdi2lBvX5" +
					"Czh5GlXQAMKEoQUGQit6CaWg3a3oxOMiDhZPfywSjvl3PxoAExbPOUTjvYtWAIxT" +
					"2DZpzkr07a/LCu3BkFXlco71pWZBXOP4QfJn5K7W/VYKK4sxbn+3WuprRmTiNOlr" +
					"sviBuSlOYME5ot814DxxI25yXqD/9mQ7b6a9TLLORBJxKk5JihShPH8xehH6V+Qu" +
					"25AJTTETw5O//q2NisClVC6UgCyE+Z/fQaVNdAGoXMARQShCOMGUbtuMx1XqHNoR" +
					"fXg+oj22QabirZg97RYeh3s7lnBRy1QmFPKu5yF9JSyAspvtuw==");

			if (!trust) {
				throw new CertificateException("Certificate isn't from mail server!");
			}
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
	}
}
