package quarano.util;

import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author Oliver Drotbohm
 */
public class TestUtils {

	public static void fakeRequest(HttpMethod method, String uri, WebApplicationContext context) {

		var request = new MockHttpServletRequest(method.toString(), uri);
		request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);

		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}
}
