package quarano.core.support;

import java.util.Locale;
import java.util.Optional;

public enum Language {
    DE,
    EN,
    TR,
    RU;

    public static Language fromLocale(Locale locale) {
        return  Optional.ofNullable(valueOf(locale.getLanguage().toUpperCase())).orElse(DE);
    }
}
