package org.terrameta.plasma.perspective;

import org.eclipse.papyrus.uml.perspective.PapyrusPerspective;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.cheatsheets.OpenCheatSheetAction;

public class PlasmaPerspective extends PapyrusPerspective {

	protected static final String ID_CHEAT_SHEETS = "org.terrameta.plasma.doc.cheatsheet1051923325";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		// TODO Auto-generated method stub
		super.createInitialLayout(layout);
		new OpenCheatSheetAction(ID_CHEAT_SHEETS).run();
	}

	@Override
	public void defineActions(IPageLayout layout) {
		// TODO Auto-generated method stub
		//super.defineActions(layout);
		layout.addNewWizardShortcut("org.terrameta.plasma.wizards.createrobotmlwizard");
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
		layout.addNewWizardShortcut("org.terrameta.plasma.wizards.createrobotmlmodel");
		// Add "show views".
		layout.addShowViewShortcut(IPageLayout.ID_PROJECT_EXPLORER);
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);


		layout.addActionSet("org.eclipse.debug.ui.launchActionSet");

		// add perspectives
		layout.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective");
		layout.addPerspectiveShortcut("org.eclipse.jdt.ui.JavaPerspective");

	}

	@Override
	public void defineLayout(IPageLayout layout) {

		// Editors are placed for free.
		String editorArea = layout.getEditorArea();


		// Place the ModelExplorer under the Navigator
		layout.addView(ID_MODELEXPLORER, IPageLayout.LEFT, 0.2f, editorArea);

		// Place the the Resource Navigator to the top left of editor area.
		layout.addView(IPageLayout.ID_PROJECT_EXPLORER, IPageLayout.BOTTOM, 0.33f, ID_MODELEXPLORER);


		// place outline under the model explorer
		layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.BOTTOM, 0.5f, IPageLayout.ID_PROJECT_EXPLORER);

		// place properties under the editor
		layout.addView(IPageLayout.ID_PROP_SHEET, IPageLayout.BOTTOM, (float)0.70, editorArea);

		// bottom.addView("org.eclipse.pde.runtime.LogView");
	}

}
