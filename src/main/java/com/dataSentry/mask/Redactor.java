/*
 * Copyright 2016, DataApps Corporation (http://dataApps.io) .
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dataSentry.mask;

import com.dataSentry.finder.Finder;
import com.dataSentry.finder.FinderEngine;
import com.dataSentry.finder.FinderResult;
import com.dataSentry.pattern.FinderUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Redactor implements Masker {
	private static final Log LOG = LogFactory.getLog(Redactor.class);

	Map<String,String> replacements ;
	FinderEngine engine;

	public void init(FinderEngine engine, Map<String,String> replacements) {
		this.engine = engine;
		this.replacements = replacements;
	}

	public void init(FinderEngine engine, String configurationFileName) {
		init(engine, readReplacements(configurationFileName));
	}

	private static Map<String,String> readReplacements(String configurationFileName) {
		Properties properties =new Properties();
		Map<String,String> map = new HashMap<>();
		try (final InputStream stream = MaskFactory.class.getClassLoader().getResourceAsStream(configurationFileName)) {
			properties.load(stream);	
			for (final String name: properties.stringPropertyNames()) {
				map.put(name, properties.getProperty(name));	
			}
		}catch (IOException e) {
			LOG.error(e);
		}
		return map;
	}

	@Override
	public String mask(String input) {
		String temp = input;
		String masked = input;
		List<Finder> finders = engine.getFinders();
		for (Finder finder:finders) {
			String replacement = replacements.get(finder.getName());
			if (replacement != null) {
				FinderResult result = finder.find(temp);
				masked = FinderUtil.replaceMatches(masked, result.getMatches(), replacement);
				temp = result.getMatchesRemoved();
			}
		}
		return masked;
	}
  
	public Map<String, String> getReplacements() {
		return replacements;
	}
	public void setReplacements(Map<String, String> replacements) {
		this.replacements = replacements;
	}
	public FinderEngine getEngine() {
		return engine;
	}
	public void setEngine(FinderEngine engine) {
		this.engine = engine;
	}


}
