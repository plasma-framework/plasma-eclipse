package org.terrameta.plasma.eclipse;


import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.plasma.provisioning.cli.SDOTool;
import org.xml.sax.SAXException;

public class PluginBridge {

    private static Log log =LogFactory.getLog(
    		PluginBridge.class); 

        
    public static void main(String[] args) throws JAXBException, SAXException, IOException {
    	SDOTool.main(args);
    }	
}
