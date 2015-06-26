package org.wisdom.m2e.core.configuration;

import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WisdomConfigurationHelper {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WisdomConfigurationHelper.class);


	public static WisdomConfiguration extractConfiguration(Plugin eclipsePlugin) {

		Xpp3Dom configurationXpp3Dom = (Xpp3Dom) eclipsePlugin
				.getConfiguration();

		if (configurationXpp3Dom == null) {
			LOGGER.warn("No configuration provided.");
			return null;
		}
        WisdomConfiguration conf = new WisdomConfiguration();
		return conf;
	}


}
