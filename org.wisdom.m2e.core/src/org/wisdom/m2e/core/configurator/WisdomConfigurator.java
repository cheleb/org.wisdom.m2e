package org.wisdom.m2e.core.configurator;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.project.configurator.AbstractProjectConfigurator;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.jdt.AbstractJavaProjectConfigurator;
import org.eclipse.m2e.jdt.AbstractSourcesGenerationProjectConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.wisdom.m2e.core.configuration.WisdomConfiguration;
import org.wisdom.m2e.core.configuration.WisdomConfigurationHelper;


public class WisdomConfigurator extends org.eclipse.m2e.jdt.internal.AbstractJavaProjectConfigurator {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(WisdomConfigurator.class);

	private static final String MOJO_GA = "org.wisdom-framework:wisdom-maven-plugin";

	@Override
	public void configure(
			ProjectConfigurationRequest projectConfigurationRequest,
			IProgressMonitor monitor) throws CoreException {

		IProject project = projectConfigurationRequest.getProject();

		MavenProject mavenProject = projectConfigurationRequest
				.getMavenProject();

		Plugin plugin = mavenProject
				.getPlugin(MOJO_GA);
		if (plugin == null) {
			LOGGER.info("Should not occurs ... but could not set eclipse settings, consider " + MOJO_GA + "!");
		} else {
			LOGGER.info("Using " + MOJO_GA + "  configuration");
			
			WisdomConfiguration conf = WisdomConfigurationHelper.extractConfiguration(plugin);
			
			
		}
		//configureRawClasspath(projectConfigurationRequest, , monitor);
		super.configure(projectConfigurationRequest, monitor);

	}



}
