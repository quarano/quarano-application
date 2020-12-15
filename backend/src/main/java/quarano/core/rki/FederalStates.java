package quarano.core.rki;

import quarano.core.rki.FederalStates.FederalState;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * @author Jens Kutzsche
 * @since 1.4
 */
public class FederalStates implements Function<String, FederalState> {
	public static enum FederalState {

		BW("Baden-Württemberg"), //
		BY("Bayern"), //
		BE("Berlin"), //
		BB("Brandenburg"), //
		HB("Bremen"), //
		HH("Hamburg"), //
		HE("Hessen"), //
		MV("Mecklenburg-Vorpommern"), //
		NI("Niedersachsen"), //
		NW("Nordrhein-Westfalen"), //
		RP("Rheinland-Pfalz"), //
		SL("Saarland"), //
		SN("Sachsen"), //
		ST("Sachsen-Anhalt"), //
		SH("Schleswig-Holstein"), //
		TH("Thüringen");//

		String name;

		FederalState(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return getName();
		}
	}

	Map<String, FederalState> rkiCodeToState = Map.ofEntries(
			Map.entry("1.01.", FederalState.SH), //
			Map.entry("1.02.", FederalState.HH), //
			Map.entry("1.03.", FederalState.NI), //
			Map.entry("1.04.", FederalState.HB), //
			Map.entry("1.05.", FederalState.NW), //
			Map.entry("1.06.", FederalState.HE), //
			Map.entry("1.07.", FederalState.RP), //
			Map.entry("1.08.", FederalState.BW), //
			Map.entry("1.09.", FederalState.BY), //
			Map.entry("1.10.", FederalState.SL), //
			Map.entry("1.11.", FederalState.BE), //
			Map.entry("1.12.", FederalState.BB), //
			Map.entry("1.13.", FederalState.MV), //
			Map.entry("1.14.", FederalState.SN), //
			Map.entry("1.15.", FederalState.ST), //
			Map.entry("1.16.", FederalState.TH));

	@Override
	public FederalState apply(@NonNull String rkiCode) {

		Assert.notNull(rkiCode, "The given RKI code must not be Null!");

		return Optional.of(rkiCode)
				.map(it -> it.substring(0, 5))
				.map(rkiCodeToState::get)
				.orElseThrow();
	}
}
