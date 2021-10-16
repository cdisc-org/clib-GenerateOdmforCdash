package com.xml4pharma.cdisclibrary.utils;


import java.io.*;
import javax.swing.*;

import org.apache.log4j.Logger;

//import com.xml4pharma.VerticalFlowLayout;

import java.awt.LayoutManager;

/**
 * Reads the file "properties.dat" and retrieves the language for the menus.<br/
 * The GUI part of this class is not used in the current version
 * 
 * @author Jozef Aerts - XML4Pharma
 */

public class PropertiesReader {
	
	//private Properties props;  // 2021-10-06: TODO LATER
	private String[] lines = new String[20]; // up to 20 parameter lines are allowed
	String[] parameterValue;
	private File propertiesFile;
	private String logfilePath;
	private String logLevel = "ALL";  // will take this as default logging level
	
	/** Constructor */
	public PropertiesReader() {
		//props = Properties.getInstance();
		//
		propertiesFile = new File("properties.dat");
	}
	
	/** Reads the file "properties.dat" and retrieves the information */
	public void readProperties() {
		// Read the file properties.dat containing the properties
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(propertiesFile));
		} catch (FileNotFoundException fne) {
			//logger.error("File properties.dat could not be found");
			System.out.println("ERROR: File properties.dat could not be found");
			return;
		}
	    String line;
	    if(reader != null) {
	    	try {
	    		int counter = -1;
	    		while((line = reader.readLine()) != null) {
	    			counter++;
	    			lines[counter] = line;  // store the line for later use
	    			System.out.println("line = " + line);
	    			// split the string in 2 with the '=' as separator
	    			parameterValue = line.split("=",2);
	    			if(parameterValue.length == 2 && parameterValue[0].trim().equals("logfilepath")) {
	    				logfilePath = parameterValue[1].trim();
	    			} else if(parameterValue.length == 2 && parameterValue[0].trim().equals("loglevel")) {
	    				logLevel = parameterValue[1].trim();
	    			/* } else if(parameterValue.length == 2 && parameterValue[0].trim().equals("sasviewerlocation")) {
	    				props.setSasViewerLocation(parameterValue[1]);
	    			} else if(parameterValue.length == 2 && parameterValue[0].trim().equals("adobereaderlocation")) {
	    				props.setAdobeReaderLocation(parameterValue[1]);
	    			} else if(parameterValue.length == 2 && parameterValue[0].trim().equals("advancedusage")) {
	    				props.setAdvancedUsage((new Boolean(parameterValue[1].trim())).booleanValue());
	    			} else if(parameterValue.length == 2 && parameterValue[0].trim().equals("define1stylesheet")) {
	    				props.setDefine1StylesheetLocation(parameterValue[1].trim());
	    			} else if(parameterValue.length == 2 && parameterValue[0].trim().equals("define2stylesheet")) {
	    				props.setDefine2StylesheetLocation(parameterValue[1].trim());
	    			} else if(parameterValue.length == 2 && parameterValue[0].trim().equals("define21stylesheet")) {
	    				props.setDefine21StylesheetLocation(parameterValue[1].trim());	
	    			} else if(parameterValue.length == 2 && parameterValue[0].trim().equals("pinnacle21location")) {
	    				props.setPinnacle21Location(parameterValue[1]);
	    			} else if(parameterValue.length == 2 && parameterValue[0].trim().equals("pinnacle21CLIcommand")) {
	    				props.setPinnacle21CLICommand(parameterValue[1].trim());
	    			} else if(parameterValue.length == 2 && parameterValue[0].trim().equals("cdisclibraryuser")) {
	    				props.setCdiscLibraryUserName(parameterValue[1].trim());
	    			} else if(parameterValue.length == 2 && parameterValue[0].trim().equals("cdisclibrarypass")) {
	    				props.setCdiscLibraryPass(parameterValue[1].trim()); */
	    			/* 
	    			else if(parameterValue.length == 2 && parameterValue[0].trim().equals("cdisclibraryapikey")) {
	    				props.setCdiscLibraryAPIKey(parameterValue[1].trim());  */
	    			} 
	    			//System.out.println("CDISC Library user name = " + props.getCdiscLibraryUserName());
	    			//System.out.println("CDISC Library pass = " + props.getCdiscLibraryPass());
	    			
	    			// Get the language property
	    			/*
	    			if(parameterValue.length > 1 && parameterValue[0].equals("language")) {
	    				if(parameterValue[1].trim().equals("en")) language = props.ENGLISH;
	    				if(parameterValue[1].trim().equals("de")) language = props.GERMAN;
	    				props.setLanguage(language);
	    			} else if(parameterValue.length > 1 && parameterValue[0].equals("languagefixed")) {
	    				if(parameterValue[1].trim().equals("false")) languageFixed = false;
	    				if(parameterValue[1].trim().equals("true")) languageFixed = true;
	    			}*/
	    		}
	    	} catch(IOException ioe) {
	    		//logger.error("Could not correctly read file with properties");
	    		System.out.println("ERROR: Could not correctly read file with properties");
	    	}
	    }
	    
	}
	
	/** Reads the minimum logging level for which logging needs to be done */
	public String getLogLevel() {
		return logLevel;
	}
	
	/** Reads the file path to which the logging needs to be written */
	public String getLoggingFilePath() {
	    return logfilePath;
	}
	
	/** Sets the properties */
	/* public void setProperties(Properties props) {
		this.props = props;
	} */
}  // end of class
