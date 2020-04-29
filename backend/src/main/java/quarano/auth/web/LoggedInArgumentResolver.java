/*
 * Copyright 2017-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.auth.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.auth.Account;
import quarano.auth.AuthenticationManager;
import quarano.department.Department;
import quarano.department.Department.DepartmentIdentifier;
import quarano.department.DepartmentRepository;
import quarano.tracking.TrackedPerson;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * {@link HandlerMethodArgumentResolver} to inject the {@link TrackedPerson} of the currently logged in user into Spring
 * MVC controller method parameters annotated with {@link LoggedIn}.
 *
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class LoggedInArgumentResolver implements HandlerMethodArgumentResolver, WebMvcConfigurer {

	private static final String USER_ACCOUNT_EXPECTED = "Expected to find a current %s but none available!";

	private static final Set<Class<?>> ALL_TYPES = Set.of(TrackedPerson.class, Account.class, Department.class,
			DepartmentIdentifier.class);

	private final @NonNull AuthenticationManager authenticationManager;
	private final @NonNull DepartmentRepository departments;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		Class<?> type = parameter.getParameterType();

		return resolve(type).orElseThrow(
				() -> new ServletRequestBindingException(String.format(USER_ACCOUNT_EXPECTED, type.getSimpleName())));
	}

	private Optional<?> resolve(Class<?> type) {

		if (type.equals(TrackedPerson.class)) {
			return authenticationManager.getLoggedInTrackedPerson(); //
		}

		Optional<Account> account = authenticationManager.getCurrentUser();

		if (type.equals(Account.class)) {
			return account;
		} else if (type.equals(DepartmentIdentifier.class)) {
			return account.map(Account::getDepartmentId);
		} else if (type.equals(Department.class)) {
			return account.map(Account::getDepartmentId).flatMap(departments::findById);
		}

		throw new IllegalStateException("Unsupported user account type!");
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#supportsParameter(org.springframework.core.MethodParameter)
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {

		return parameter.hasParameterAnnotation(LoggedIn.class) //
				&& ALL_TYPES.contains(parameter.getParameterType());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer#addArgumentResolvers(java.util.List)
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(this);
	}
}
