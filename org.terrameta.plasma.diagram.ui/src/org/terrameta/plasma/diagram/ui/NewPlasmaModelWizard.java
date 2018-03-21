package org.terrameta.plasma.diagram.ui;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.uml.diagram.wizards.CreateModelWizard;
import org.eclipse.papyrus.uml.diagram.wizards.pages.SelectDiagramCategoryPage;
import org.eclipse.ui.IWorkbench;
import org.terrameta.plasma.diagram.common.commands.CreatePlasmaModelCommand;

public class NewPlasmaModelWizard extends CreateModelWizard {

	/**
	 * @see org.eclipse.papyrus.wizards.CreateModelWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 * 
	 * @param workbench
	 * @param selection
	 */

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		setWindowTitle("New Plasma Model");
	}

	/**
	 * Instantiates a new new Plasma model wizard.
	 */
	public NewPlasmaModelWizard() {
		super();

	}

	@Override
	public String getModelKindName() {
		// TODO Auto-generated method stub

		return "Plasma Model";
	}

	@Override
	protected String[] getDiagramCategoryIds() {
		return new String[]{ CreatePlasmaModelCommand.COMMAND_ID };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected SelectDiagramCategoryPage createSelectDiagramCategoryPage() {
		// here Plasma is the only available category
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveDiagramCategorySettings() {
		// here Plasma is the only available category
	}

}
