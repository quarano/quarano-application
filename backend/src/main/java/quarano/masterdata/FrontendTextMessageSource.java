package quarano.masterdata;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.context.support.AbstractMessageSource;
import org.springframework.lang.Nullable;

@RequiredArgsConstructor
public class FrontendTextMessageSource extends AbstractMessageSource {

	private final @NonNull FrontendTextRepository texts;

	@Override
	@Nullable
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		// texts.
		//
		// Map<Locale, MessageHolder> localeMap = this.messageMap.get(code);
		// if (localeMap == null) {
		// return null;
		// }
		// MessageHolder holder = localeMap.get(locale);
		// if (holder == null) {
		// return null;
		// }
		// return holder.getMessage();
		return null;
	}

	@Override
	@Nullable
	protected MessageFormat resolveCode(String code, Locale locale) {
		// Map<Locale, MessageHolder> localeMap = this.messageMap.get(code);
		// if (localeMap == null) {
		// return null;
		// }
		// MessageHolder holder = localeMap.get(locale);
		// if (holder == null) {
		return null;
		// }
		// return holder.getMessageFormat();
	}
}
