package com.dataSentry.finder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.dataSentry.pattern.RegexFinder;


public class DefaultFinderProvider implements FinderProvider {
	public static final String FINDERS_DEFAULT_XML = "finders_default.xml";
	private static final Log LOG = LogFactory.getLog(DefaultFinderProvider.class);
	

	List<Finder> finders = new ArrayList<>();
	boolean ignoreEnabledFlag = false;

	DefaultFinderProvider() {
		this(false);
	}

	DefaultFinderProvider(final boolean ignoreEnabledFlag) {
		this(DefaultFinderProvider.class.getClassLoader().getResourceAsStream(FINDERS_DEFAULT_XML), ignoreEnabledFlag);
	}

	public DefaultFinderProvider(InputStream in) {
		this(in, false);
	}

	DefaultFinderProvider(InputStream in, final boolean ignoreEnabledFlag) {
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				boolean bName = false;
				boolean bPattern = false;
				boolean bClass = false;
				boolean bEnabled = false;
				boolean bFlags = false;
				String name = "";
				String pattern = "";
				String className = "";
				String strFlags = "";
				int flags = RegexFinder.DEFAULT_FLAGS;
				String strEnabled = "";
				boolean enabled = true;

				public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("NAME")) {
						bName = true;
					} else if (qName.equalsIgnoreCase("PATTERN")) {
						bPattern = true;
					} else if (qName.equalsIgnoreCase("CLASS")) {
						bClass = true;
					} else if (qName.equalsIgnoreCase("FLAGS")) {
						bFlags = true;
					} else if (qName.equalsIgnoreCase("ENABLED")) {
						bEnabled = true;
					} 
				}
 

				public void characters(char ch[], int start, int length) throws SAXException {
					if (bName) {
						name += new String(ch, start, length);
					} else if (bPattern) {
						pattern += new String(ch, start, length);
					} else if (bClass) {
						className += new String(ch, start, length);
					} else if (bEnabled) {
						strEnabled += new String(ch, start, length);
					} else if (bFlags) {
						strFlags += new String(ch, start, length);
					}
				}

				public void endElement(String uri, String localName,String qName) throws SAXException {
					if (qName.equalsIgnoreCase("NAME")) {
						bName = false; name = name.trim();
					} else if (qName.equalsIgnoreCase("PATTERN")) {
						bPattern = false; pattern = pattern.trim();
					} else if (qName.equalsIgnoreCase("CLASS")) {
						bClass = false; className = className.trim();
					} else if (qName.equalsIgnoreCase("FLAGS")) {
						bFlags = false; flags = Integer.parseInt(strFlags.trim());
					} else if (qName.equalsIgnoreCase("ENABLED")) {
						bEnabled = false; enabled = Boolean.parseBoolean(strEnabled.trim()); 
					} else if (qName.equalsIgnoreCase("FINDER")) {
						if (ignoreEnabledFlag || enabled) {
							if (!className.isEmpty()) {
								try {
									Class<?> klass = Thread.currentThread().getContextClassLoader().loadClass(className);
									finders.add((Finder) klass.newInstance());
								} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
									LOG.error(e);
								}

							} else if (flags != RegexFinder.DEFAULT_FLAGS) {
								finders.add(new RegexFinder(name, pattern, flags));
							} else {
								finders.add(new RegexFinder(name, pattern));
							}
						}

						name = ""; pattern= ""; className = "";
						flags = RegexFinder.DEFAULT_FLAGS; strFlags = "";
						enabled = true; strEnabled = "";
					}

				}

			};

			saxParser.parse(in, handler);

		} catch (Exception e) {
			LOG.error(e);
		}
	}

	@Override
	public List<Finder> getFinders() {
		return finders;
	}

}
