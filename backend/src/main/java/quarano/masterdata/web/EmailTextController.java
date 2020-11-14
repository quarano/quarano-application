package quarano.masterdata.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.masterdata.EmailTextRepository;
import quarano.masterdata.web.TextRepresentations.TextDto;

import java.util.Locale;
import java.util.Optional;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jens Kutzsche
 */
@RestController
@RequiredArgsConstructor
class EmailTextController {

	private final @NonNull TextRepresentations representations;
	private final @NonNull EmailTextRepository texts;

	/**
	 * @param key The key of a email text to get only one special text.
	 * @param lang The language of the frontend texts.
	 */
	@GetMapping(path = "/emailtexts")
	public RepresentationModel<?> getText(@RequestParam("key") Optional<String> key,
			@RequestParam("lang") Optional<Locale> lang) {

		var locale = lang.map(Locale::getLanguage)
				.map(Locale::new)
				.orElseGet(LocaleContextHolder::getLocale);

		var dtos = key
				.map(it -> texts.findByTextKey(it, locale).stream())
				.orElseGet(() -> texts.findAll(locale).stream())
				.map(it -> representations.toRepresentation(it));

		return HalModelBuilder.emptyHalModel()
				.embed(dtos, TextDto.class)
				.build();
	}

	/**
	 * @param key The key of the email text to be replaced.
	 * @param lang The language of the email text.
	 * @param text The text to be set for the given key.
	 */
	@PutMapping(path = "/admin/emailtexts/{key}")
	public HttpEntity<?> putText(@PathVariable String key, @RequestHeader("content-language") Locale lang,
			@RequestBody String text) {

		return texts.findByTextKey(key, new Locale(lang.getLanguage()))
				.map(it -> it.setText(text))
				.map(texts::save)
				.map(__ -> ResponseEntity.ok().build())
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
}
