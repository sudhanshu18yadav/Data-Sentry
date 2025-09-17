
package com.dataSentry.pattern;

import com.dataSentry.finder.CompositeFinder;
import com.dataSentry.finder.Finder;
import com.dataSentry.finder.FinderResult;

public class CompositeCreditCardFinder implements Finder {

	private CompositeFinder compositeFinder;

	public CompositeCreditCardFinder () {
		compositeFinder = new CompositeFinder("CreditCard");
		String START_BLOCK = "([^\\d\\.-]|^)";
		compositeFinder.add(new CreditCardFinder("Mastercard", START_BLOCK + "5[1-5][0-9]{2}(\\ |\\-|)[0-9]{4}(\\ |\\-|)[0-9]{4}(\\ |\\-|)[0-9]{4}(\\D|$)"));
		compositeFinder.add(new CreditCardFinder("Visa", START_BLOCK + "4[0-9]{3}(\\ |\\-|)[0-9]{4}(\\ |\\-|)[0-9]{4}(\\ |\\-|)[0-9]{4}(\\D|$)"));
		compositeFinder.add(new CreditCardFinder("AMEX", START_BLOCK + "(34|37)[0-9]{2}(\\ |\\-|)[0-9]{6}(\\ |\\-|)[0-9]{5}(\\D|$)"));
		compositeFinder.add(new CreditCardFinder("Diners Club 1", START_BLOCK + "30[0-5][0-9](\\ |\\-|)[0-9]{6}(\\ |\\-|)[0-9]{4}(\\D|$)"));
		compositeFinder.add(new CreditCardFinder("Diners Club 2", START_BLOCK + "(36|38)[0-9]{2}(\\ |\\-|)[0-9]{6}(\\ |\\-|)[0-9]{4}(\\D|$)"));
		compositeFinder.add(new CreditCardFinder("Discover", START_BLOCK + "6011(\\ |\\-|)[0-9]{4}(\\ |\\-|)[0-9]{4}(\\ |\\-|)[0-9]{4}(\\D|$)"));
		compositeFinder.add(new CreditCardFinder("JCB 1", START_BLOCK + "3[0-9]{3}(\\ |\\-|)[0-9]{4}(\\ |\\-|)[0-9]{4}(\\ |\\-|)[0-9]{4}(\\D|$)"));
		compositeFinder.add(new CreditCardFinder("JCB 2", START_BLOCK + "(2131|1800)[0-9]{11}(\\D|$)"));
	}

	@Override
	public FinderResult find(String sample) {
		return compositeFinder.find(sample);
	}

	@Override
	public String getName() {
		return compositeFinder.getName();
	}

}
