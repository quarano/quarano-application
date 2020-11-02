package quarano.masterdata.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.masterdata.FrontendText;
import quarano.masterdata.FrontendTextRepository;
import quarano.masterdata.web.FrontendTextRepresentations.FrontendTextDto;

import java.util.Optional;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jens Kutzsche
 */
@RestController
@RequiredArgsConstructor
public class FrontendTextController {

	private final @NonNull FrontendTextRepresentations representations;
	private final @NonNull FrontendTextRepository texts;

	/**
	 * @param key The key of a frontend text to get only one special text. If it does not exist, the key is returned as
	 *          text.
	 */
	@GetMapping(path = "/frontendtexts")
	public RepresentationModel<?> getText(@RequestParam("key") Optional<String> key) {

		var locale = LocaleContextHolder.getLocale();

		var dtos = key
				.map(it -> texts.findByTextKey(it, locale)
						.recover(__ -> FrontendText.of(it, locale, it))
						.toJavaStream())
				.orElseGet(() -> texts.findAll(locale).stream())
				.map(representations::toRepresentation);

		return HalModelBuilder.emptyHalModel()
				.embed(dtos, FrontendTextDto.class)
				.build();
	}
}
