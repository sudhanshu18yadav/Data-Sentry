package com.dataSentry.finder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CompositeFinder implements Finder {

	private String name;
	private List<Finder> finders = new ArrayList<>();

	public CompositeFinder(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setFinders(List<Finder> finders) {
		this.finders = finders;
	}
	public List<Finder> getFinders() {
		return finders;
	}

	public void add(Finder finder) {
		finders.add(finder);
	}

	public FinderResult find(String input) {
		List<String> list = new ArrayList<>();
		String temp = input;
		for (Finder finder : finders) {
			FinderResult result = finder.find(temp);
			list.addAll(result.getMatches());
			temp = result.getMatchesRemoved();
		}
		return new FinderResult(list, temp);
	}

	public Map<String, List<String>> findWithType(String input) {
		Map<String, List<String>> map = new HashMap<>();
		String temp = input;
		for (Finder finder : finders) {
			FinderResult result = finder.find(temp);
			addToMap(map, finder, result.getMatches());
			temp = result.getMatchesRemoved();
		}
		return map;
	}

	private void addToMap(Map<String, List<String>> map, Finder finder, List<String> matches) {
		if (!matches.isEmpty()) {
			List<String> existingMatches = map.get(finder.getName());
			if (existingMatches == null) {
				existingMatches = new ArrayList<>();
				map.put(finder.getName(), existingMatches);
			}
			existingMatches.addAll(matches);
		}
	}
}
