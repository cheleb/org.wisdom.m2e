package org.wisdom.m2e.core.configurator;

import java.util.HashMap;
import java.util.Map;

import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.core.project.configurator.AbstractProjectConfigurator;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.jdt.IClasspathDescriptor;
import org.eclipse.m2e.jdt.MavenJdtPlugin;
import org.eclipse.m2e.jdt.internal.AbstractJavaProjectConfigurator;
import org.eclipse.m2e.jdt.internal.ClasspathDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.m2e.core.configuration.WisdomConfiguration;
import org.wisdom.m2e.core.configuration.WisdomConfigurationHelper;

public class WisdomConfigurator extends AbstractJavaProjectConfigurator {

	private final static Logger LOGGER = LoggerFactory.getLogger(WisdomConfigurator.class);

	private static final String MOJO_GA = "org.wisdom-framework:wisdom-maven-plugin";

	@Override
	public void configure(ProjectConfigurationRequest request, IProgressMonitor monitor) throws CoreException {

		IProject project = request.getProject();

		addNature(project, JavaCore.NATURE_ID, monitor);

		IJavaProject javaProject = JavaCore.create(project);

		Map<String, String> options = new HashMap<String, String>();

		addJavaProjectOptions(options, request, monitor);

		IClasspathDescriptor classpath = new ClasspathDescriptor(javaProject);

		addProjectSourceFolders(classpath, request, monitor);

		String environmentId = getExecutionEnvironmentId(options);

		addJREClasspathContainer(classpath, environmentId);

		addMavenClasspathContainer(classpath);
		
		
		 addCustomClasspathEntries(javaProject, classpath);

		    invokeJavaProjectConfigurators(classpath, request, monitor);

		    // now apply new configuration

		    // A single setOptions call erases everything else from an existing settings file.
		    // Must invoke setOption individually to preserve previous options. 
		    for(Map.Entry<String, String> option : options.entrySet()) {
		      javaProject.setOption(option.getKey(), option.getValue());
		    }

		    IContainer classesFolder = getOutputLocation(request, project);

		    javaProject.setRawClasspath(classpath.getEntries(), classesFolder.getFullPath(), monitor);

		    MavenJdtPlugin.getDefault().getBuildpathManager().updateClasspath(project, monitor);
		
		
		
		

		MavenProject mavenProject = request.getMavenProject();

		
		
		
		
		
		Plugin plugin = mavenProject.getPlugin(MOJO_GA);
		if (plugin == null) {
			LOGGER.info("Should not occurs ... but could not set eclipse settings, consider " + MOJO_GA + "!");
		} else {
			LOGGER.info("Using " + MOJO_GA + "  configuration");

			WisdomConfiguration conf = WisdomConfigurationHelper.extractConfiguration(plugin);
			if (conf != null)
				LOGGER.debug(conf.toString());

		}
		// configureRawClasspath(projectConfigurationRequest, , monitor);

	}

	protected void addJavaProjectOptions(Map<String, String> options, ProjectConfigurationRequest request,
			IProgressMonitor monitor) throws CoreException {
		String source = null, target = null;

		// for(MojoExecution execution : getCompilerMojoExecutions(request,
		// monitor)) {
		// source = getCompilerLevel(request.getMavenProject(), execution,
		// "source", source, SOURCES, monitor); //$NON-NLS-1$
		// target = getCompilerLevel(request.getMavenProject(), execution,
		// "target", target, TARGETS, monitor); //$NON-NLS-1$
		// }

		if (source == null) {
			source = "1.8";
			LOGGER.warn("Could not determine source level, using default " + source);
		}

		if (target == null) {
			target = "1.8";
			LOGGER.warn("Could not determine target level, using default " + target);
		}

		// While "5" and "6" ... are valid synonyms for Java 5, Java 6 ...
		// source,
		// Eclipse expects the values 1.5 and 1.6 and so on.
		// source = sanitizeJavaVersion(source);
		// While "5" and "6" ... are valid synonyms for Java 5, Java 6 ...
		// target,
		// Eclipse expects the values 1.5 and 1.6 and so on.
		// target = sanitizeJavaVersion(target);

		options.put(JavaCore.COMPILER_SOURCE, source);
		options.put(JavaCore.COMPILER_COMPLIANCE, source);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, target);

		// 360962 keep forbidden_reference severity set by the user
		IJavaProject jp = JavaCore.create(request.getProject());
		if (jp != null && jp.getOption(JavaCore.COMPILER_PB_FORBIDDEN_REFERENCE, false) == null) {
			options.put(JavaCore.COMPILER_PB_FORBIDDEN_REFERENCE, "warning"); //$NON-NLS-1$
		}
	}

}
