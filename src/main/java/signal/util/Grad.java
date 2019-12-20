package signal.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Grad {

	public static String akoJe(String grad) {
		List<String> gradovi = Arrays.asList("Podgorica", "Nikšić", "Pljevlja", "Bijelo Polje", "Cetinje", "Bar", "Herceg Novi", "Berane", "Budva", "Ulcinj", "Tivat",
				"Rožaje", "Kotor", "Danilovgrad", "Mojkovac", "Plav", "Kolašin", "Žabljak", "Plužine", "Andrijevica", "Šavnik");
		Optional<String> optGrad = gradovi.stream().filter(g -> g.equalsIgnoreCase(grad)).findFirst();
		if(optGrad.isPresent()) {
			return optGrad.get();
		} else {
			return null;
		}
	}

}
