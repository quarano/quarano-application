package quarano.core;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import quarano.QuaranoUnitTest;
import quarano.account.Account;
import quarano.account.AuthenticationManager;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;
import quarano.user.LocaleConfiguration;

@QuaranoUnitTest
public class LocaleConfigurationUnitTests {
	@Mock AuthenticationManager accounts;
	@Mock TrackedPersonRepository persons;
	@Mock LocaleConfiguration.LocaleResolver localeResolver;
	@Mock HttpServletRequest request;
	@Mock HttpServletResponse response;
	@Mock FilterChain filterChain;
	@Mock Account account;
	@Mock TrackedPerson person;

	LocaleConfiguration configuration;
	LocaleConfiguration.AdoptLanguageChangeFilter filter;
	Method doFilterInternal;

	@BeforeEach
	void setup() throws NoSuchMethodException {
		configuration = new LocaleConfiguration(accounts, persons);
		localeResolver = configuration.new LocaleResolver();
		filter = configuration.new AdoptLanguageChangeFilter();
		doFilterInternal = filter.getClass().getDeclaredMethod("doFilterInternal", HttpServletRequest.class,
				HttpServletResponse.class, FilterChain.class);
		doFilterInternal.setAccessible(true);
	}

	@ParameterizedTest
	@ValueSource(strings = { "de", "de_DE", "en", "en_GB", "tr", "tr_TR", "en_CA" })
	void processCorrectLocalesSucceeds(String locale) throws Exception {

		prepareCommonResults(locale);
		doFilterInternal.invoke(filter, request, response, filterChain);

		verify(person, atMostOnce()).setLocale(any(Locale.class));
		verify(persons, atMostOnce()).save(eq(person));
		verify(filterChain, atMostOnce()).doFilter(eq(request), eq(response));
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "d", "en,de", "en_US,tr_TR", "en_CAN" })
	void processInvalidLocalesRejects(String locale) throws Exception {

		prepareCommonResults(locale);
		doFilterInternal.invoke(filter, request, response, filterChain);

		verify(person, never()).setLocale(any(Locale.class));
		verify(persons, never()).save(eq(person));
		verify(filterChain, atMostOnce()).doFilter(eq(request), eq(response));
	}

	void prepareCommonResults(String locale) {

		if (locale != null) {
			when(request.getParameter(eq("locale"))).thenReturn(locale);
		}

		var optAccount = Optional.of(account);
		when(accounts.getCurrentUser()).thenReturn(optAccount);

		var optPerson = Optional.of(person);
		when(persons.findByAccount(eq(account))).thenReturn(optPerson);
	}
}
