package org.terrameta.plasma.diagram.ui;

import static org.eclipse.papyrus.uml.diagram.wizards.utils.WizardsHelper.getSelectedResourceURI;

import org.eclipse.papyrus.uml.diagram.wizards.InitModelWizard;
import org.terrameta.plasma.diagram.common.commands.CreatePlasmaModelCommand;


public class InitPlasmaModelWizard extends InitModelWizard
{
	@Override
	protected String[] getDiagramCategoryIds() {
		return new String[]{ CreatePlasmaModelCommand.COMMAND_ID };
	}
	
}
