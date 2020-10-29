package quarano.util;

import java.util.Locale;

import org.assertj.core.api.SoftAssertionsProvider.ThrowingRunnable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.Assert;
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

	/**
	 * Executes the given {@link ThrowingRunnable} in the context of the given {@link Locale}.
	 *
	 * @param locale must not be {@literal null}.
	 * @param runnable must not be {@literal null}.
	 * @throws Exception
	 */
	public static void executeWithLocale(Locale locale, ThrowingRunnable runnable) throws Exception {

		Assert.notNull(locale, "Locale must not be null!");
		Assert.notNull(runnable, "Runnable must not be null!");

		executeWithLocale(locale, () -> {
			runnable.run();
			return null;
		});
	}

	/**
	 * Executes the given {@link ThrowingSupplier} in the context of the given {@link Locale}.
	 *
	 * @param locale must not be {@literal null}.
	 * @param supplier must not be {@literal null}.
	 * @throws Exception
	 */
	public static <T> T executeWithLocale(Locale locale, ThrowingSupplier<T> supplier) throws Exception {

		var original = LocaleContextHolder.getLocale();

		LocaleContextHolder.setLocale(locale);

		try {
			return supplier.get();
		} finally {
			LocaleContextHolder.setLocale(original);
		}
	}

	public interface ThrowingSupplier<T> {
		T get() throws Exception;
	}
}
