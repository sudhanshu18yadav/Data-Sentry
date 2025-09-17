package com.dataSentry.pattern;

import com.dataSentry.finder.FinderResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class CreditCardFinder extends RegexFinder {
	public CreditCardFinder(String name, String pattern) {
		super(name, pattern);
	}

	@Override
	public FinderResult find(String input) {
		List<String> matches = new ArrayList<>();
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {

			String match = input.substring(matcher.start()+1, matcher.end()-1);

			if (postMatchCheck(match)) {
				matches.add(match);
			}
		}
		return new FinderResult(matches, 
				FinderUtil.removeMatches(input, matches));
	}


	protected boolean postMatchCheck(String match) {
		String numerics = match.replaceAll("[^\\d.]", "");
		int[] digits = new int[numerics.length()];

		for (int i=0; i < digits.length; i++) {
			char c = numerics.charAt(i);
			digits[i] = c - '0';
		}

		return luhnCheck(digits);
	}

	private static boolean luhnCheck(int[] digits) {
		int sum = 0;
		int length = digits.length;
		for (int i = 0; i < length; i++) {

			int digit = digits[length - i - 1];
			if (i % 2 == 1) {
				digit *= 2;
			}
			sum += digit > 9 ? digit - 9 : digit;
		}
		return sum % 10 == 0;
	}
}
