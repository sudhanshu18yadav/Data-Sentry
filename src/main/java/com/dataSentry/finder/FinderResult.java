package com.dataSentry.finder;

import java.util.List;

public class FinderResult {
	
	List<String> matches;
	String matchesRemoved;
	
	public FinderResult() {}
	
	public FinderResult (List<String> matches, String matchesRemoved) {
		this.matches = matches;
		this.matchesRemoved = matchesRemoved;
	}
	public List<String> getMatches() {
		return matches;
	}
	public void setMatches(List<String> matches) {
		this.matches = matches;
	}
	public String getMatchesRemoved() {
		return matchesRemoved;
	}
	public void setMatchesRemoved(String matchesRemoved) {
		this.matchesRemoved = matchesRemoved;
	}
}
