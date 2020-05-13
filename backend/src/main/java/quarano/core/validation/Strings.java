package quarano.core.validation;

/**
 * @author Oliver Drotbohm
 */
public class Strings {

	static final String LETTERS = "[\\p{L}]*";
	static final String NUMBERS = "[0-9]*";
	static final String LETTERS_AND_NUMBERS = "[\\p{L}\\s\\d]*";
	static final String TEXTUAL = "[\\p{L}\\s\\d\\.\\,\\?\\!\\(\\)\\-\\n\\r]*";

	private static final String LETTERS_AND_SPACES = "\\p{L}\\s";	
	private static final String DOTS = "\\.";
	private static final String DASHES = "\\-";
	private static final String UNDERSCORE = "\\_";	
	private static final String PARENTHESIS = "\\(\\)";
	private static final String SLASHES = "\\/";
	
	public static final String CITY = "[" + LETTERS_AND_SPACES + DOTS + DASHES + PARENTHESIS + SLASHES + "]*";
	public static final String STREET = "[" + LETTERS_AND_SPACES + DOTS + DASHES + NUMBERS + "]*";
	public static final String NAMES = "[" + LETTERS_AND_SPACES + DASHES + "]*";
	
	public static final String USERNAME = "[" + LETTERS + NUMBERS + DASHES + UNDERSCORE + "]*";
}
