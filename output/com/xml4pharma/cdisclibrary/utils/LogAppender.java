package com.xml4pharma.cdisclibrary.utils;

import java.io.File;
import java.util.Calendar;
import java.util.Enumeration;

import org.apache.log4j.*;
import org.apache.log4j.xml.DOMConfigurator;

import com.xml4pharma.cdisclibrary.utils.*;

public class LogAppender {
	
	private Logger logger;
	
	// Properties props;  // TODO LATER
	
	public LogAppender(Logger logger) {
		this.logger = logger;
	}
	
	public void createLogAppendersIfNeeded() {
		//props = Properties.getInstance();
		Enumeration logEnum = Logger.getRootLogger().getAllAppenders();
		int numAppenders = 0;
		while ( logEnum.hasMoreElements() ){
		  Appender app = (Appender)logEnum.nextElement();
		  numAppenders++;
		  if ( app instanceof FileAppender ){
		    System.out.println("File: " + ((FileAppender)app).getFile());
		  }
		}
		System.out.println("# Log Appenders = " + numAppenders);
		if(numAppenders > 0) return;
		// No appenders found - add some
		// See whether logging level can be read from properties.dat file
		PropertiesReader propertiesReader = new PropertiesReader();
		propertiesReader.readProperties();
		System.out.println("logLevel = " + propertiesReader.getLogLevel());
	    if(propertiesReader.getLogLevel().toUpperCase().equals("FILE")) {
	    	System.out.println("Trying to read logging configuration from log4j.xml file");
	    	DOMConfigurator.configure("log4j.xml");        
    		logger.info("Log4J settings read from file log4j.xml"); 
	    } else {  // just take a default only allowing the log level to be set by the user
	    	// create a Logger
			//SimpleLayout layout = new SimpleLayout();
			// for custom layouts, see e.g. http://www.torsten-horn.de/techdocs/java-log4j.htm
	    	Calendar rightNow = Calendar.getInstance();
			PatternLayout layout = new PatternLayout( "%d{ISO8601} %-5p %m%n");
			// add a file appender
		    FileAppender file_appender = null;
	    	// The log file name contains date and time
	    	// e.g. for September 20, at 10:28:19
	    	// ODM2SDTM_LOG_2006_9_20_10-28-19.txt
	    	String logfileName = "CDASH2ODM_LOG_";
	    	logfileName += rightNow.get(Calendar.YEAR) + "_" + (rightNow.get(Calendar.MONTH)+1) + "_" + rightNow.get(Calendar.DAY_OF_MONTH);
	    	logfileName += "_" + rightNow.get(Calendar.HOUR_OF_DAY) + "-" + rightNow.get(Calendar.MINUTE) + "-" + rightNow.get(Calendar.SECOND);
	    	logfileName += ".txt";
	    	logfileName = "logs" + File.separator + logfileName; 
	    	System.out.println("logging goes to: " + logfileName);
	    	//props.setLogFile(new File(logfileName));
	    	try {
	    		file_appender = new FileAppender(layout,logfileName, false);
	    	} catch(Exception e) {
	    		System.out.println("ERROR: Cannot create log file " + logfileName);
	    	}
	    	logger.addAppender(file_appender);
	    	// add a console appender
	    	logger.addAppender(new ConsoleAppender(layout, ConsoleAppender.SYSTEM_OUT));
	    	// set level
	    	logger.setLevel((Level) Level.INFO); // minimum level for having a message in the log file
	    	//logger.setLevel((Level) Level.DEBUG); // minimum level for having a message in the log file
	    	String logLevel = propertiesReader.getLogLevel();
	    	if(logLevel != null && logLevel.length() > 0) {
	    		if(logLevel.equals("ALL")) {
	    			logger.setLevel((Level)Level.ALL);
	    			System.out.println("changing logging level to \"ALL\"");
	    		} else if(logLevel.equals("DEBUG")) {
	    			logger.setLevel((Level)Level.DEBUG);
	    			System.out.println("changing logging level to \"DEBUG\"");
	    		} else if(logLevel.equals("INFO")) {
	    			logger.setLevel((Level)Level.INFO);
	    			System.out.println("changing logging level to \"ALL\"");
	    		} else if(logLevel.equals("WARN")) {
	    			logger.setLevel((Level)Level.WARN);
	    			System.out.println("changing logging level to \"INFO\"");
	    		} else if(logLevel.equals("ERROR")) {
	    			logger.setLevel((Level)Level.ERROR);
	    			System.out.println("changing logging level to \"ERROR\"");
	    		} else if(logLevel.equals("FATAL")) {
	    			logger.setLevel((Level)Level.FATAL);
	    			System.out.println("changing logging level to \"FATAL\"");
	    		} else if(logLevel.equals("OFF")) {
	    			logger.setLevel((Level)Level.OFF);
	    			System.out.println("changing logging level to \"OFF\"");
	    		} else {
	    			System.out.println("Invalid logging level = " + logLevel);
	    		}
	    	} 
	    }
	}

}
