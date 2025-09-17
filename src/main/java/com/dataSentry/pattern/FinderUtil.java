package com.dataSentry.pattern;

import java.util.List;
import java.util.regex.Pattern;

public class FinderUtil {
	
	public static String removeMatches(String input, List<String> matches) {
		return replaceMatches(input, matches, "");
	}
		
	public static String replaceMatches(String input, List<String> matches, String replacement) {
		if (matches.size() > 0) {
			StringBuilder sb = new StringBuilder("(");
			boolean first = true;
			for (String match:matches) {
				if (!first) {
					sb.append("|");
				}
				sb.append(Pattern.quote(match));
				first = false;
			}
			sb.append(")");

			return input.replaceAll(sb.toString(), replacement);
		} else {
			return input;
		}
	}
}
