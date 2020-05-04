/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.security;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import quarano.account.RoleType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
class JwtToken {

	private final Claims claims;

	@SuppressWarnings("unchecked")
	public List<RoleType> getRoleTypes() {

		List<String> roles = claims.get(JwtProperties.ROLE_CLAIM, List.class);

		return roles.stream() //
				.map(RoleType::valueOf) //
				.collect(Collectors.toUnmodifiableList());
	}

	public String getUsername() {
		return claims.getSubject();
	}
}
