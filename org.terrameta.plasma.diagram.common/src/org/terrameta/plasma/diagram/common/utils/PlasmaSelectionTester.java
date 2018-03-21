package org.terrameta.plasma.diagram.common.utils;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.resource.ModelsReader;
import org.eclipse.papyrus.infra.emf.utils.ServiceUtilsForSelection;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.terrameta.plasma.diagram.common.Activator;


/**
 * This class is a Property tester used to check if current model (meaning the model currently opened in Papyrus) is a Proteus Model.
 * This class is used in order to create test for deciding whether a diagram creation command should be visible or not.
 * This property tester assumes that currently active editor is Papyrus, it should be used with care (simultaneously with a test to ensure Papyrus is
 * currently opened and active).
 *
 */
public class PlasmaSelectionTester extends PropertyTester {

	/** Tester ID for UML Model nature */
	public final static String IS_PLASMA_MODEL = "isPlasmaModel";
	public final static String PLASMA_PROFILE_ELEM_NAME = "Plasma SDO Profile";


	//public static String ROBOTML_ID = "RobotML";

	/** Default constructor */
	public PlasmaSelectionTester() {
	}

	/** Test the receiver against the selected property */
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        boolean result = false;
		
		// Ensure Papyrus is the active editor
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if((editor == null) || (!(editor instanceof IMultiDiagramEditor))) {
			//Activator.log.info("returning false");
			return false;
		}

		Object currentValue = null;
		if(IS_PLASMA_MODEL.equals(property)) {
			currentValue = testRobotmlModelNature(receiver);
			result = (currentValue == expectedValue);
		}
		//Activator.log.info("returning result: " + result);
		return result;
	}

	/** True is root object is a UML Model with RobotML Profile (and sub profiles) applied */
	protected boolean testRobotmlModelNature(Object receiver) {
		boolean isRobotmlModel = false;



		EObject root = getRoot(receiver);
		if(root instanceof Package) {
			Package rootPackage = (Package)root;
			Profile profile = rootPackage.getAppliedProfile(PLASMA_PROFILE_ELEM_NAME);
			return profile != null;

			//FIX: UMLUtil.getProfile() loads the profile into the resource set. This is not desired.
			//
			//			Profile robotml = UMLUtil.getProfile(RobotMLPackage.eINSTANCE, root);
			//
			//			if(((Package)root).isProfileApplied(robotml)) {
			//				isRobotmlModel = true;
			//			}
		}


		return isRobotmlModel;
	}


	/** Returns the root EObject of currently opened model */
	private EObject getRoot(Object receiver) {
		EObject root = null;

		if(receiver instanceof ISelection) {
			ISelection selection = (ISelection)receiver;
			if(selection.isEmpty()) {
				return null;
			}

			try {
				//this is the case where the selection is on the Project Explorer
				IStructuredSelection selectionstructured = (IStructuredSelection)selection;

				Object selectedElement = selectionstructured.getFirstElement();

				Object selectedAdapter = Platform.getAdapterManager().getAdapter(selectedElement, IFile.class);


				if(selectedAdapter instanceof IFile) {
					final IFile selectedFile = (IFile)selectedAdapter;
					ModelSet modelSet = new ModelSet();
					ModelsReader reader = new ModelsReader();
					reader.readModel(modelSet);


					IPath workspacePath = selectedFile.getFullPath();

					URI workspaceURI = URI.createPlatformResourceURI(workspacePath.toString(), true);
					modelSet.loadModels(workspaceURI);

					UmlModel openedModel = (UmlModel)modelSet.getModel(UmlModel.MODEL_ID);
					if(openedModel != null) {
						root = openedModel.lookupRoot();
					}
				} else {
					//this is the case where the selection is on the Model Explorer
					ServiceUtilsForSelection serviceUtils = ServiceUtilsForSelection.getInstance();
					UmlModel openedModel = (UmlModel)serviceUtils.getModelSet(selection).getModel(UmlModel.MODEL_ID);
					if(openedModel != null) {
						root = openedModel.lookupRoot();
					}
				}

			} catch (Exception e) {
				//Ignored: The selection cannot be used to retrieve the ServicesRegistry.
				//Do not log exceptions: this is just not a Papyrus/RobotML model
			}
		}

		return root;
	}


}
