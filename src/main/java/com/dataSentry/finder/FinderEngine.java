
package com.dataSentry.finder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class FinderEngine extends CompositeFinder {
	private static final Log LOG = LogFactory.getLog(FinderEngine.class);

	public FinderEngine() {
		this((List<Finder>)null, true);
	}
	
	public FinderEngine(String fileName) {
		this(fileName, false);
	}
	
	public FinderEngine(String fileName, boolean fromClassPath) {
		this(fileName, fromClassPath, true);
	}
	
	public FinderEngine(String fileName, boolean fromClassPath, boolean addDefaultFinders) {
		this(new DefaultFinderProvider(getInputStream(fileName, fromClassPath)), addDefaultFinders);
	}
	
	private static InputStream getInputStream(String fileName, boolean fromClassPath) {
		InputStream in = null;
		if (fromClassPath) {
			 in = FinderEngine.class.getClassLoader().getResourceAsStream(fileName);
		} else {
			 try {
				in = new FileInputStream(fileName);
			} catch (FileNotFoundException e) {
				LOG.warn(e.getMessage());
			}
		}
		return in;
	}

	public FinderEngine(FinderProvider finderProvider, boolean addDefaultFinders) {
		this(finderProvider.getFinders(), addDefaultFinders);
	}

	public FinderEngine(List<Finder> finders, boolean addDefaultFinders) {
		this(finders, addDefaultFinders, false);
	}

	public FinderEngine(List<Finder> finders, boolean addDefaultFinders, boolean ignoreEnabledFlag) {
		super("FinderEngine");
		List<Finder> list = new ArrayList<>();
		if (finders != null) {
			list.addAll(finders);
		}
		if (addDefaultFinders) {
			list.addAll(new DefaultFinderProvider(ignoreEnabledFlag).getFinders());
		}
		setFinders(list);
	}

	private static List<Finder> createFinders(Set<FinderProvider> finderProviders) {
		List<Finder> finders = new ArrayList<>();
		for (FinderProvider provider: finderProviders) {
			finders.addAll(provider.getFinders());
		}
		return finders;
	}
}
