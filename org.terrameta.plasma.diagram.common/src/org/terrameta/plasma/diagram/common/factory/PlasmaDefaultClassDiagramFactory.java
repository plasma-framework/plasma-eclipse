package org.terrameta.plasma.diagram.common.factory;

import org.eclipse.papyrus.infra.gmfdiag.common.GmfEditorFactory;
import org.eclipse.papyrus.uml.diagram.clazz.UmlClassDiagramForMultiEditor;


/**
 * This class is part of the migration of the Plasma diagrams, which,
 * since Papyrus 1.0 should be opened by the corresponding UML diagram editors,
 * in this particular case, {@link UmlClassDiagramForMultiEditor}
 * <p/>
 * This class registers the corresponding UML editor to the Plasma type and allows the migration framework to proceed.
 * <p/>
 * The other component of the process is {@link PlasmaReconciler} which actually performs migration.
 */
public class PlasmaDefaultClassDiagramFactory extends GmfEditorFactory {

	public static final String OLD_PLASMA_ML_TYPE = "classdef";

	public PlasmaDefaultClassDiagramFactory() {
		super(UmlClassDiagramForMultiEditor.class, OLD_PLASMA_ML_TYPE);
	}

}
