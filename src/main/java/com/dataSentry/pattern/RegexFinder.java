package com.dataSentry.pattern;


import com.dataSentry.finder.Finder;
import com.dataSentry.finder.FinderResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RegexFinder implements Finder {
	static final Log LOG = LogFactory.getLog(RegexFinder.class);
	public static final int DEFAULT_FLAGS = Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE;

	private String name;
	Pattern pattern;
	
	public RegexFinder() {}

	public RegexFinder(String name, String pattern) {
		this(name, pattern,DEFAULT_FLAGS);
		System.out.println("flags: "+DEFAULT_FLAGS);
	}

	public RegexFinder(String name, String pattern, int flags) {
		this.name = name;
		this.pattern = Pattern.compile(pattern, flags);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public FinderResult find(String input) {
		List<String> matches = new ArrayList<>();
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			matches.add(input.substring(matcher.start(), matcher.end()));
		}
		return new FinderResult(matches, replace(input,""));
	}
	

	static String removeCommas(String match) {
		if (match.endsWith(",")) {
			match = match.substring(0, match.length() - 1);
		}
		if (match.startsWith(",")) {
			match = match.substring(1);
		}
		return match;
	}


	private String replace(String input, String replacement) {
		Matcher matcher = pattern.matcher(input);
		return matcher.replaceAll(replacement);
	}

}