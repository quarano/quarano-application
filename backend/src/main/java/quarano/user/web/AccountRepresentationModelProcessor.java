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

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import quarano.account.web.AccountRepresentation;

import java.util.function.Supplier;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.MvcLink;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
class AccountRepresentationModelProcessor implements RepresentationModelProcessor<AccountRepresentation> {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.hateoas.server.RepresentationModelProcessor#process(org.springframework.hateoas.RepresentationModel)
	 */
	@Override
	@SuppressWarnings("null")
	public AccountRepresentation process(AccountRepresentation model) {

		Supplier<Object> putPassword = () -> on(UserController.class).putPassword(null, null, null);

		var links = Links.NONE
				.andIf(model.isPasswordExpired(), () -> MvcLink.of(putPassword, IanaLinkRelations.NEXT))
				.andIf(model.isPasswordExpired(), () -> MvcLink.of(putPassword, UserLinkRelations.CHANGE_PASSWORD))
				.andIf(!model.isPasswordExpired(),
						() -> MvcLink.of(on(UserController.class).getMe(model.getAccount()), IanaLinkRelations.NEXT));

		model.add(links);

		return model;
	}
}
