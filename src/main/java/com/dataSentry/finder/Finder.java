package com.dataSentry.finder;


public interface Finder {

  public String getName();
  
  public FinderResult find(String input);
    
}
