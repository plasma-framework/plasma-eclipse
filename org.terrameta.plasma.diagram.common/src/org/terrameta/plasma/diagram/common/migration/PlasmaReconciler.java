package org.terrameta.plasma.diagram.common.migration;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.infra.gmfdiag.common.reconciler.DiagramReconciler;
import org.eclipse.papyrus.infra.gmfdiag.common.utils.DiagramUtils;
import org.eclipse.papyrus.infra.viewpoints.policy.PolicyChecker;
import org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype;
import org.terrameta.plasma.diagram.common.factory.PlasmaDefaultClassDiagramFactory;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ModelEditPart;
import org.eclipse.papyrus.uml.diagram.composite.edit.parts.CompositeStructureDiagramEditPart;


public class PlasmaReconciler extends DiagramReconciler {


	private static final String OLD_CLASS_TYPE = PlasmaDefaultClassDiagramFactory.OLD_PLASMA_ML_TYPE;

	private static final String NEW_CLASS_DIAGRAM = ModelEditPart.MODEL_ID;

	private static final String NEW_COMPOSITE_DIAGRAM = CompositeStructureDiagramEditPart.MODEL_ID;

	
	private static final MigrationData CLASSTYPE = new MigrationData(OLD_CLASS_TYPE, NEW_CLASS_DIAGRAM, "Plasma Class");

	/**
	 * Original code in {@link MigrateDiagramsHandler} uses {@link String#equalsIgnoreCase(String)}.
	 * <p/>
	 * So the keys in this map are old types toLoserCase.
	 */
	private static final Map<String, MigrationData> ourMigrations;

	static {
		ourMigrations = new HashMap<String, MigrationData>();
		for(MigrationData next : new MigrationData[]{ CLASSTYPE }) {
			ourMigrations.put(safeToLowerCase(next.getOldType()), next);
		}
	}

	@Override
	public ICommand getReconcileCommand(Diagram diagram) {
		MigrationData data = findMigration(diagram);
		return data == null ? null : new RobotMLReconcileCommand(diagram, data);
	}

	/**
	 * Ignores case
	 */
	private MigrationData findMigration(Diagram diagram) {
		return ourMigrations.get(safeToLowerCase(diagram.getType()));
	}

	private static String safeToLowerCase(String text) {
		return text == null ? null : text.toLowerCase();
	}

	private static class RobotMLReconcileCommand extends AbstractCommand {

		private final Diagram myDiagram;

		private final MigrationData myMigrationData;

		public RobotMLReconcileCommand(Diagram diagram, MigrationData data) {
			super("Migrate old RobotML diagram");
			myDiagram = diagram;
			myMigrationData = data;
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor progressMonitor, IAdaptable info) throws ExecutionException {
			DiagramUtils.setPrototype(myDiagram, myMigrationData.getPrototype());
			DiagramUtils.setOwner(myDiagram, myDiagram.getElement());
			myDiagram.setType(myMigrationData.getNewType());

			return CommandResult.newOKCommandResult();
		}

		@Override
		public boolean canUndo() {
			return false;
		}

		@Override
		public boolean canRedo() {
			return false;
		}

		@Override
		protected CommandResult doRedoWithResult(IProgressMonitor progressMonitor, IAdaptable info) throws ExecutionException {
			throw new ExecutionException("Should not be called, canRedo false");
		}

		@Override
		protected CommandResult doUndoWithResult(IProgressMonitor progressMonitor, IAdaptable info) throws ExecutionException {
			throw new ExecutionException("Should not be called, canUndo false");
		}

	}

	/**
	 * Encapsulates migration scheme from given old RobotML, new UML diagram type and given prototype.
	 * <p/>
	 * Instances of this class are considered invalid if the prototype is not known.
	 */
	private static class MigrationData {

		private ViewPrototype myCachedPrototype;

		private final String myNewType;

		private final String myOldType;

		private final String myPrototypeLabel;

		public MigrationData(String oldType, String newType, String prototypeLabel) {
			myOldType = oldType;
			myNewType = newType;
			myPrototypeLabel = prototypeLabel;
		}

		public String getPrototypeLabel() {
			return myPrototypeLabel;
		}

		public String getOldType() {
			return myOldType;
		}

		public String getNewType() {
			return myNewType;
		}

		public ViewPrototype getPrototype() {
			if(myCachedPrototype == null) {
				myCachedPrototype = findPrototype(myPrototypeLabel);
			}
			return myCachedPrototype;
		}

		private static ViewPrototype findPrototype(String label) {
			for(ViewPrototype proto : PolicyChecker.getCurrent().getAllPrototypes()) {
				if(proto.getLabel().contains(label)) {
					return proto;
				}
			}
			return null;
		}

	}
}
