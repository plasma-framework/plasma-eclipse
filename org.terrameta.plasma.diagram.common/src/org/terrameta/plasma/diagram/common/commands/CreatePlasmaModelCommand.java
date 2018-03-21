package org.terrameta.plasma.diagram.common.commands;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.uml.diagram.common.commands.ModelCreationCommandBase;
import org.eclipse.papyrus.uml.tools.utils.PackageUtil;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.UMLFactory;
import org.terrameta.plasma.diagram.common.Activator;

public class CreatePlasmaModelCommand extends ModelCreationCommandBase {
	

	public static final String COMMAND_ID = "Plasma"; // the perspective name

	public CreatePlasmaModelCommand() {
	}


	public static final String PROFILES_PATHMAP = "pathmap://PLASMA_PROFILES/"; //$NON-NLS-1$
	public static final String PROFILE_URI = PROFILES_PATHMAP + "PlasmaSDO.profile_v1_3.uml"; //$NON-NLS-1$


	/**
	 * @see org.eclipse.papyrus.core.extension.commands.ModelCreationCommandBase#createRootElement()
	 * 
	 * @return
	 */

	@Override
	protected EObject createRootElement() {
		Activator.log.info("creating model");
		return UMLFactory.eINSTANCE.createModel();
	}

	/**
	 * @see org.eclipse.papyrus.core.extension.commands.ModelCreationCommandBase#initializeModel(org.eclipse.emf.ecore.EObject)
	 * 
	 * @param owner
	 */

	@Override
	protected void initializeModel(EObject owner) {
		Activator.log.info("init model");
		super.initializeModel(owner);
		((org.eclipse.uml2.uml.Package)owner).setName(getModelName());
		 
		Activator.log.info("loading profile package: " + PROFILE_URI);
		// Retrieve Plasma profile and apply  
		org.eclipse.uml2.uml.Package profile = PackageUtil.loadPackage(URI.createURI(PROFILE_URI), owner.eResource().getResourceSet());
		if((profile != null) && (profile instanceof Profile)) {
			Activator.log.info("applying profile package: " + PROFILE_URI);
			PackageUtil.applyProfile(((org.eclipse.uml2.uml.Package)owner), (org.eclipse.uml2.uml.Profile)profile, true);
		}
	}

	/**
	 * Gets the model name.
	 * 
	 * @return the model name
	 */
	protected String getModelName() {
		return "PlasmaModel";
	}


}
