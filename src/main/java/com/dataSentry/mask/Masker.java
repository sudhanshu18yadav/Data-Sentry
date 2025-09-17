package com.dataSentry.mask;

import com.dataSentry.finder.FinderEngine;


 // A class which implements a masking operation using a FinderEngine

public interface Masker {
	
	void init (FinderEngine engine, String configuration);
		
	String mask(String input);
}
