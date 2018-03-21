package org.terrameta.plasma.diagram.ui;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.uml.diagram.wizards.CreateModelWizard;
import org.eclipse.papyrus.uml.diagram.wizards.pages.SelectDiagramCategoryPage;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.terrameta.plasma.diagram.ui.Activator;
import org.terrameta.plasma.diagram.common.commands.CreatePlasmaModelCommand;
import org.xml.sax.SAXException;
import org.plasma.provisioning.cli.OptionPair;
import org.plasma.provisioning.cli.ProvisioningToolOption;
import org.plasma.provisioning.cli.RDBDialect;
import org.plasma.provisioning.cli.RDBTool;
import org.plasma.provisioning.cli.RDBToolAction;
import org.plasma.provisioning.cli.UMLPlatform;
import org.plasma.provisioning.cli.UMLTool;
import org.plasma.provisioning.cli.UMLToolSource;

public class NewPlasmaModelReverseWizard extends CreateModelWizard {

	/** The new project page. */
	private NewReverseSourceWizardPage projectReverseSourcePage;
	private IWorkbench wizWorkbench;

	
	/**
	 * Instantiates a new new Plasma model wizard.
	 */
	public NewPlasmaModelReverseWizard() {
		super();
	}
	
	/**
	 * @see org.eclipse.papyrus.wizards.CreateModelWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 * 
	 * @param workbench
	 * @param selection
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		this.wizWorkbench = workbench;
		setWindowTitle("New Plasma Model");
		projectReverseSourcePage = createNewProjectCreationPage();
		
		
	}
	
    protected IProject getSelectedProject() {
        ISelectionService ss= this.wizWorkbench.getActiveWorkbenchWindow().getSelectionService();
        String projExpID = "org.eclipse.ui.navigator.ProjectExplorer";
        ISelection sel = ss.getSelection(projExpID);
        Object selectedObject=sel;
        if(sel instanceof IStructuredSelection) {
              selectedObject= ((IStructuredSelection)sel).getFirstElement();
        }
        if (selectedObject instanceof IAdaptable) {
              IResource res = (IResource) ((IAdaptable) selectedObject)
                          .getAdapter(IResource.class);
              IProject project = res.getProject();
              
              System.out.println("Project found: "+project.getName());
               
              return project;
        }
        return null;
    }	
	
	@Override
	public boolean performFinish() {
		
		IProject project = getSelectedProject();
		IPath path = project.getRawLocation().makeAbsolute();
        System.out.println("raw path found: "+path);
        File projectFile = path.toFile();
        if (!projectFile.exists())
        	System.out.println("file not exists: " + projectFile);
         
        File configFile = new File(projectFile, "plasma-config.xml");
        String urlstr = configFile.toURI().toString();
        Properties props = System.getProperties();
        System.out.println("setting plasma.configuration property: " + urlstr);
        props.setProperty("plasma.configuration", urlstr);
		
    	File outputFile = new File(projectFile.getParent(), 
    			this.projectReverseSourcePage.getOutputDirectory() + "/" + this.projectReverseSourcePage.getOutputFile());
    	
    	List<OptionPair> pairs = new ArrayList<OptionPair>();
    	pairs.add(new OptionPair(ProvisioningToolOption.verbose, "true"));
		pairs.add(new OptionPair(ProvisioningToolOption.dialect, this.projectReverseSourcePage.getDialect()));
		pairs.add(new OptionPair(ProvisioningToolOption.dest, outputFile.getAbsolutePath()));
		pairs.add(new OptionPair(ProvisioningToolOption.platform, UMLPlatform.papyrus.name()));
		pairs.add(new OptionPair(ProvisioningToolOption.sourceType, UMLToolSource.rdb.name()));

    	if (this.projectReverseSourcePage.getNamespaces() != null) {
    		pairs.add(new OptionPair(ProvisioningToolOption.namespaces, this.projectReverseSourcePage.getNamespaces()));
    	}
    	else {
    		pairs.add(new OptionPair(ProvisioningToolOption.namespaces, "http://" + this.projectReverseSourcePage.getOutputFile()));
    	}
    	
    	if (this.projectReverseSourcePage.getSchemaNames() != null)
    		pairs.add(new OptionPair(ProvisioningToolOption.schemas, this.projectReverseSourcePage.getSchemaNames()));
		
		try {
			UMLTool.main(toArgs(pairs));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return true;
	}

	/**
	 * Creates the new project creation page.
	 *
	 * @return the wizard new project creation page
	 */
	protected NewReverseSourceWizardPage createNewProjectCreationPage() {
		NewReverseSourceWizardPage newProjectPage = new NewReverseSourceWizardPage(); //$NON-NLS-1$
		return newProjectPage;
	}

	
	/**
	 * Adds the pages.
	 *
	 * {@inheritDoc}
	 */
	//@Override
	public void addPages() {
		addPage(projectReverseSourcePage);
		super.addPages();
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

    public static String[] toArgs(List<OptionPair> pairs) {
    	List<String> list = new ArrayList<String>();
    	for (int i = 0; i < pairs.size(); i++) {
    		OptionPair pair = pairs.get(i);
    		list.add("--" + pair.getOption().name()); 
    		list.add(pair.getValue());
    	}
    	String[] result = new String[list.size()];
    	list.toArray(result);
    	return result;
    }
}
