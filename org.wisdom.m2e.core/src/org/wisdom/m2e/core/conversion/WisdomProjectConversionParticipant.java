package org.wisdom.m2e.core.conversion;

import org.apache.maven.model.Model;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.project.conversion.AbstractProjectConversionParticipant;

public class WisdomProjectConversionParticipant extends AbstractProjectConversionParticipant {

	@Override
	public boolean accept(IProject project) throws CoreException {
		return true;
	}

	@Override
	public void convert(IProject project, Model model, IProgressMonitor monitor) throws CoreException {
		System.out.println("ooo");
		
	}

}
