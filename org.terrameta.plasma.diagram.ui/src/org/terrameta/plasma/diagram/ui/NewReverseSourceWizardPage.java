package org.terrameta.plasma.diagram.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.plasma.provisioning.cli.RDBDialect;

public class NewReverseSourceWizardPage extends WizardPage {

    /**
    * The target vendor DDL dialect for generated source.
    */
    private Combo dialect;

    /**
    * The target directory for generated artifacts
    */
    private Text outputDirectory;
    
    /**
    * The target file for generated artifacts
    */
    private Text outputFile;

        
    /**
    * The destination or target namespace URIs. These are
    * separated by commas and mapped (in order) to schema
    * names in the resulting document.
    */
    private Text namespaces;

    /**
    * The names for RDB schema(s) to process, separated
    * by commas.   
    */
    private Text schemaNames;

	private Composite container;

	public NewReverseSourceWizardPage() {
		super("First Page");
		setTitle("First Page");
		setDescription("Fake Wizard: First page");
	}

	//@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;		
		
		Label dialectLabel = new Label(container, SWT.NONE);
		dialectLabel.setText("DB Dialect: ");
		//dialect = new Text(container, SWT.BORDER | SWT.SINGLE);
		//dialect.setText("mysql");
		
		dialect = new Combo(container, SWT.DROP_DOWN | SWT.BORDER);
		for (RDBDialect value : RDBDialect.values())
			dialect.add(value.name());
		dialect.setToolTipText("the source relational database vendor");

		Label namespacesLabel = new Label(container, SWT.NONE);
		namespacesLabel.setText("Namespaces: ");
		namespaces = new Text(container, SWT.BORDER | SWT.SINGLE);
		namespaces.setText("");
		namespaces.setToolTipText("the target namespace(s) seperated by commas to be used within the target UML artifact");

		Label schemaNamesLabel = new Label(container, SWT.NONE);
		schemaNamesLabel.setText("Schema Names: ");
		schemaNames = new Text(container, SWT.BORDER | SWT.SINGLE);
		schemaNames.setText("");
		schemaNames.setToolTipText("the relational database schema name(s), seperated by commas, to be used to interrogate the source database");
		
		Label outputDirectoryLabel = new Label(container, SWT.NONE);
		outputDirectoryLabel.setText("Output Directory: ");
		outputDirectory = new Text(container, SWT.BORDER | SWT.SINGLE);
		outputDirectory.setText(".");
		outputDirectory.setToolTipText("the output directory for generated artifacts");
		
		Label outputFileLabel = new Label(container, SWT.NONE);
		outputFileLabel.setText("Output File: ");
		outputFile = new Text(container, SWT.BORDER | SWT.SINGLE);
		outputFile.setText("wordnet.uml");
		outputFile.setToolTipText("the output file name");

		
		outputFile.addKeyListener(new KeyListener() {

			//@Override
			public void keyPressed(KeyEvent e) {
			}

			//@Override
			public void keyReleased(KeyEvent e) {
				if (!outputFile.getText().isEmpty()) {
					setPageComplete(true);
				}
			}
		});
		
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		outputFile.setLayoutData(gd);
		
		
		// required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}

	public String getOutputFile() {
		return outputFile.getText();
	}
	
	public String getOutputDirectory() {
		return outputDirectory.getText();
	}

	public String getDialect() {
		return dialect.getText();
	}

	public String getNamespaces() {
		return namespaces.getText();
	}

	public String getSchemaNames() {
		return schemaNames.getText();
	}

}
