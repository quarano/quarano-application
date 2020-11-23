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
package quarano.user.web;

import org.springframework.hateoas.LinkRelation;

/**
 * @author Oliver Drotbohm
 */
public class UserLinkRelations {

	public static final String PASSWORD_RESET_URI_TEMPLATE = "/password/reset/{token}";

	public static final LinkRelation CHANGE_PASSWORD = LinkRelation.of("change-password");
	public static final LinkRelation RESET_PASSWORD = LinkRelation.of("reset-password");
}
