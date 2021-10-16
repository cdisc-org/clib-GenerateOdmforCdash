/* 
Copyright 2020 Jozef Aerts, XML4Pharma
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.xml4pharma.cdisclibrary.cdash;

import java.io.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.*;

import com.xml4pharma.cdisclibrary.BasicCDISCLibraryClient;
import com.xml4pharma.cdisclibrary.utils.*;

/** The main class for generating CDISC-ODM CDASH forms using the CDISC Library */
public class Main {
	
	private BasicCDISCLibraryClient client;

	private String message = ""; // for messaging
	
	// the codelist version selected (should be done by the user)
	//private static String codeListVersion = "2020-03-27";
	//private String cdashVersion = "2-1";
	private static String codeListVersion = "2021-09-24";
	private String cdashVersion = "2-2";
	// whether "CodeList" elements need to be generated
	private boolean includeCodeLists = true;  
	
	// we keep a list of unique codelist CDISC Library codelist references
	// in order to limit the number of CDISC Library calls
	private ArrayList<CodeList> uniqueCodeLists = new ArrayList<CodeList>();
	
	// output 
	// TODO generate the output file name automatically from the CDASH version
	private String outputFileLocation = "C:\\CDISC_Library\\CDASH_retrievals_ODM\\CDASH_2_2_CRFs.xml";
	
	// FOR TESTING PURPOSES ONLY
	//private static int maxScenarios = -1;  // negative value means no limitations on the maximum number of scenarios
	
	private ArrayList<CDASHScenarioDomain> scenarios = new ArrayList<CDASHScenarioDomain>();
	private XPath xpath = XPathFactory.newInstance().newXPath();
	
	// for output
	private String cdashItemGroupDefODMXML = "";
	private String cdashFormDefODMXML = "";
	private String cdashItemDefODMXML = "";
	private String codeListODMXML = "";
	
	// Logging
	static Logger logger = Logger.getLogger("MYLOGGER");
	
	/** Constructor */
	public Main() {
		// new mechanism for logger appender
		LogAppender appender = new LogAppender(logger);
		appender.createLogAppendersIfNeeded();
	}
	
	/** Main method */
	public static void main(String[] args) {
		Main m = new Main();
		m.init();
	}
	
	/** Initialization: starts the generation of the CDASH-ODM */
	public void init() {
		client = new BasicCDISCLibraryClient();
		String odmXMLText = "<ODM xmlns=\"http://www.cdisc.org/ns/odm/v1.3\"\r\n" 
				 + "\txmlns:xlink=\"http://www.w3.org/1999/xlink\"\r\n" 
				 + "\txmlns:xs=\"http://www.w3.org/2001/XMLSchema\"\r\n" 
				 + "\txmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"" 
				 + "\tCreationDateTime=\"" + createDateTimeStamp() + "\""
				 + "\tDescription=\"" + "CDASH-" + cdashVersion + "\""
				 + "\tFileOID=\"" + "CDASH-" + cdashVersion + "\""
				 + "\tFileType=\"Snapshot\" " + " Granularity=\"Metadata\" " + " ODMVersion=\"1.3.1\""
				 + ">\n";
		String studyText = "<Study OID=\"" + "CDASH-ODM-" + cdashVersion + "\">" 
				+ "<GlobalVariables>\n"
				+ "<StudyName>" + "CDASH-" + cdashVersion + " Template</StudyName>\n" 
				+ "<StudyDescription>" + "Template for CDASH-" + cdashVersion + "</StudyDescription>\n"
				+ "<ProtocolName>" + "Template for CDASH-" + cdashVersion + "</ProtocolName>\n"
				+ "</GlobalVariables>\n";
		// create the "header" MetaDataVersion element
		String metaDataVersionText = "<MetaDataVersion OID=\"MV.CDASHIG_" + cdashVersion + "\" "
			+ "Name=\"Metadata for CDASH-IG version " + cdashVersion + "\">";

		// we must iterate over all the classes
		String classesXML = client.getCDASHClasses(cdashVersion.replace("_","-"));
		System.out.println("classesXML = " + classesXML);
		Document classesDOMDocument = getDOMDocument(classesXML);
		// construct the query
		String query = "/mdr/cdashig/" + cdashVersion + "/classes";
		String rootElement = classesDOMDocument.getDocumentElement().getNodeName();
		message += "Query = " + query + " - root element = " + rootElement + "\n";
		
		// get the list of classes as a DOM NodeList
		NodeList classesList = getNodeList(classesDOMDocument, "//class");
		// Iterate over the nodes
		for(int i0=0; i0<classesList.getLength(); i0++) {
			Node classNode = classesList.item(i0);
			String className = getNodeTextContent(classNode, "./title");
			System.out.println("Class = " + className);
			String classHref = getNodeTextContent(classNode, "./href");
			// single class: get the domains
			String classXML = client.getCDISCLibraryXML(classHref);
			System.out.println("classXML = " + classXML);
			Document singleClassDOMDocument = getDOMDocument(classXML);
			System.out.println("singleClassDOMDocument = " + singleClassDOMDocument);
			// get the root
			rootElement = singleClassDOMDocument.getDocumentElement().getNodeName();
			message += "Query = " + classHref + " - root element = " + rootElement + "\n";
			System.out.println("message = " + message);
			// get the list of domains
			logger.info("Now trying to get the domains for class " + className);
			NodeList domainNodeList = getNodeList(singleClassDOMDocument, "/cdiscLibrary/product/domain");
			logger.info("# of domains = " + domainNodeList.getLength());
			// iterate over the domains
			for(int i1=0; i1<domainNodeList.getLength(); i1++) {
				Node singleDomainNode = domainNodeList.item(i1);
				String domainName = getNodeTextContent(singleDomainNode, "./name");
				String domainLongName = getNodeTextContent(singleDomainNode, "./label");
				logger.info("Treating Domain = " + domainName);
				// we first need to check whether this domain has scenarios
				// If so, we need to iterate over the scenarios
				// If NOT, we can get the fields directly
				NodeList scenarioNodeList = getNodeList(singleDomainNode, "./_links/scenario");
				int numScenarios = scenarioNodeList.getLength();
				// there are no scenarios for this domain
				logger.info("# of scenarios = " + numScenarios);
				if(numScenarios == 0) {
					logger.info("Starting generating CDASH form for domain = " + domainName);
					String domainHref = getNodeTextContent(singleDomainNode, "./_links/self/href");
					CDASHScenarioDomain sc = generateScenarioDomain(singleDomainNode, domainName, domainLongName, domainHref);
					sc.setDomainLongName(domainLongName);
					scenarios.add(sc);
				// there are one or more scenarios for this domain
				} else {
					for(int i2=0; i2<scenarioNodeList.getLength(); i2++) {
						Node scenarioNode = scenarioNodeList.item(i2);
						String scenarioHref = getNodeTextContent(scenarioNode, "./href");
						String scenarioXML = client.getCDISCLibraryXML(scenarioHref);
						logger.debug("scenarioXML = " + scenarioXML);
						Document scenarioDOMDocument = getDOMDocument(scenarioXML);
						String scenarioName = getNodeTextContent(scenarioDOMDocument, "/cdiscLibrary/product/_links/self/title");
						System.out.println("Starting generating CDASH form for scenario = " + scenarioName);
						//Node scenarioDetailsNode = getNodeList(scenarioDOMDocument, "/cdiscLibrary/scenario").item(0);
						// 2021-10-06: /cdiscLibrary/product/scenario delivers the scenario title
						//Node scenarioDetailsNode = getNodeList(scenarioDOMDocument, "/cdiscLibrary/product/scenario").item(0);
						Node scenarioDetailsNode = getNodeList(scenarioDOMDocument, "/cdiscLibrary/product").item(0);
						CDASHScenarioDomain sc = generateScenarioDomain(scenarioDetailsNode, domainName, scenarioName, scenarioHref);
						sc.setDomainLongName(domainLongName);
						scenarios.add(sc);
					}
				}
			}
		}
		// all scenarios and domains have been collected, start generating FormDef elements
		int count = 0;
		logger.info("# of scenarios/domains = " + scenarios.size());
		for(CDASHScenarioDomain sc : scenarios) {
			count++;
			// now start generating Forms and ItemGroups
			// in the current setting, there is a single ItemGroup per form
			// another possibility would be to have a "header" ItemGroup that is identical 
			// for each Form
			String name = sc.getTitle();
			if(name.startsWith(sc.getDomainShortName()) == false) name = sc.getDomainShortName() + " - " + name;
			String formOID = "CDASH_" + cdashVersion + "_FO_" + count;
			String itemGroupOID = "CDASH_" + cdashVersion + "_IG_" + count;
			// TODO?: should we put more info into FormDef through Alias? 
			cdashFormDefODMXML += "<FormDef OID=\"" + formOID + "\" Name=\"Form " + name + "\" Repeating=\"No\">\n"
					+ "<Description><TranslatedText xml:lang=\"en\">" + name + "</TranslatedText></Description>\n"
					+ "<ItemGroupRef ItemGroupOID=\"" + itemGroupOID + "\" Mandatory=\"Yes\" />\n"
					+ "</FormDef>";
			// generate the ODM ItemGroupDef element
			cdashItemGroupDefODMXML += "<ItemGroupDef OID=\"" + itemGroupOID + "\" "
					+ " Name=\"" + name + "\" Repeating=\"No\" Domain=\"" + sc.getDomainShortName() + "\">\n";
			// add a description
			String description = sc.getDomainLongName();
			if(description.startsWith(sc.getDomainShortName()) == false) {
				description = sc.getDomainShortName() + " - " + description;
			}
			cdashItemGroupDefODMXML += "<Description><TranslatedText xml:lang=\"en\">" + description + "</TranslatedText></Description>\n";
			// add the ItemRefs
			logger.debug("# of ItemRefs for ItemGroupDef with OID " + itemGroupOID + " = " + sc.getCdashFields().size());
			if(sc.getCdashFields().size() == 0) logger.warn("No ItemRefs for ItemGroup with Name = " + name);
			for(CDASHField f : sc.getCdashFields()) {
				cdashItemGroupDefODMXML += "\n" + f.toODMItemRef();
			}
			// close the ItemGroup
			cdashItemGroupDefODMXML += "\n</ItemGroupDef>";
		}
		// get the unique fields, then generate ItemDefs from them
		ArrayList<CDASHField> uniqueFields = new ArrayList<CDASHField>();
		for(CDASHScenarioDomain sc : scenarios) {
			for(CDASHField field : sc.getCdashFields()) {
				String fieldName = field.getName();
				boolean isAlreadyPresent = false;
				for(CDASHField uniqueField : uniqueFields) {
					if(uniqueField.getName().equals(fieldName)) {
						isAlreadyPresent = true;
						break;
					}
				}
				if(isAlreadyPresent == false) uniqueFields.add(field);
			}
		}
		// generate the ODM ItemDef elements
		for(CDASHField uniqueField : uniqueFields) {
			cdashItemDefODMXML += "\n" + uniqueField.toODMItemDef();
		}
		// generate the CodeList (definitions)
		// but only when the boolean "includeCodeLists" is set to "true"
		if(includeCodeLists == true) {
			for(CodeList cl : uniqueCodeLists) {
				codeListODMXML += "\n" + cl.toCodeListDef();
			}
		}
		// Output
		System.out.println("NOW STARTING WRITING ODM to file");
		File file = new File(outputFileLocation);
		if(file.exists()) file.delete();  // clear it up before starting writing
		// start writing using a BufferedWriter
		try {
		    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileLocation, false));
		    writer.write(odmXMLText);
		    writer.write(studyText);
		    writer.write(metaDataVersionText + "\n");
		    writer.append(cdashFormDefODMXML);
		    writer.append(cdashItemGroupDefODMXML);
		    writer.append(cdashItemDefODMXML);
		    writer.append(codeListODMXML);  // can be empty when includeCodeLists=false
		    writer.append("\n</MetaDataVersion>");
		    writer.append("\n</Study>");
		    writer.append("\n</ODM>");
		    writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// For testing purposes only: write a file with the queries for the classes used
		File debugFile = new File("C:\\CDISC_Library\\CDASH_retrievals_ODM\\CDASH_" + cdashVersion + "_queries_with_fields.txt");
		if(debugFile.exists()) debugFile.delete();
		try {
		    BufferedWriter writer = new BufferedWriter(new FileWriter(debugFile, false));
		    writer.write(message);
		    writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR writing ODM to file = " + outputFileLocation);
		}
		//
		System.out.println("Finished writing ODM file to = " + outputFileLocation);
		System.out.println("DONE");
	}
	
	/** Generates a CDASH scenario domain */
	private CDASHScenarioDomain generateScenarioDomain(Node node, String domainName, String title, String href) {
		CDASHScenarioDomain sc = new CDASHScenarioDomain(title, href);
		sc.setDescription(title);
		sc.setDomainShortName(domainName);
		// now get the fields
		NodeList fieldNodes = getNodeList(node, "./field");
		System.out.println("# fields = " + fieldNodes.getLength());
		ArrayList<CDASHField> fields = new ArrayList<CDASHField>();
		for(int j=0; j<fieldNodes.getLength(); j++) {
			Node fieldNode = fieldNodes.item(j);
			String fieldName = getNodeTextContent(fieldNode, "./name");
			String simpleDatatype = getNodeTextContent(fieldNode, "./simpleDatatype");
			System.out.println("simpleDatatype = " + simpleDatatype);
			String fieldHref = getNodeTextContent (fieldNode, "./_links/self/href");
			System.out.println("fieldName = " + fieldName);
			System.out.println("fieldHref = " + fieldHref);
			CDASHField field = new CDASHField(fieldName, simpleDatatype, fieldHref);
			fields.add(field);
			// some additional info is already available
			String definition = getNodeTextContent(fieldNode, "./definition");
			field.setDefinition(definition);
			String questionText = getNodeTextContent(fieldNode, "./questionText");
			field.setQuestionText(questionText);
			String prompt = getNodeTextContent(fieldNode, "./prompt");
			field.setPrompt(prompt);
			String implementationNotes = getNodeTextContent(fieldNode, "./implementationNotes");
			field.setImplementationNotes(implementationNotes);
			String core = getNodeTextContent(fieldNode, "./core");
			field.setCore(core);
			String completionInstructions = getNodeTextContent(fieldNode, "./completionInstructions");
			field.setCompletionInstructions(completionInstructions);
			String mappingInstructions = getNodeTextContent(fieldNode, "./mappingInstructions");
			field.setMappingInstructions(mappingInstructions);
			// We cannot get the label here. We need to dive into the field definition
			String label = fieldName;  // 2020-03-02
			field.setLabel(label);
			// codelist, get the reference
			String codeListHref = getNodeTextContent(fieldNode, "./_links/codelist/href");
			System.out.println("Associated CodeList ref = " + codeListHref);
			// check whether we do already have this codelist defined
			if(codeListHref != null && codeListHref.trim().length() > 0) {
				boolean isAlreadyCodeListPresent = false;
				for(CodeList cl : uniqueCodeLists) {
					if(cl.getHrefUnversioned().equals(codeListHref)) {
						isAlreadyCodeListPresent = true;
						field.setAssociatedCodeList(cl);
						System.out.println("Added EXISTING codeList with OID = " + cl.getCodeListOID());
						break;
					}
				}
				// codelist was not defined yet, get it
				if(isAlreadyCodeListPresent == false) {
					CodeList newCodeList = getCodeList(codeListHref);
					if(newCodeList != null) {
						field.setAssociatedCodeList(newCodeList);
						uniqueCodeLists.add(newCodeList);
						System.out.println("Added NEW codeList with OID = " + newCodeList.getCodeListOID());
					}
				}
			}
		}
		sc.setCdashFields(fields);
		return sc;
	}
	
	/** Creates a datetimestamp */
	private String createDateTimeStamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Instant instant = timestamp.toInstant();
		String dateTimeStampISO8601 = instant.toString();
		System.out.println("Creating ODM document with datetimestamp = " + dateTimeStampISO8601);
		return dateTimeStampISO8601;
	}
	
	/** Returns a CodeList from the provided CDISC Library reference String */
	private CodeList getCodeList(String codeListRef) {
		if(codeListRef == null || codeListRef.trim().length() == 0) return null;
		System.out.println("Looking for codelist for href = " + codeListRef);
		CodeList codeList = null;
		String codeListXML = client.getCDISCLibraryXML(codeListRef);
		System.out.println("codeListXML = " + codeListXML);
		Document codeListDoc = getDOMDocument(codeListXML);
		System.out.println("codeListDoc = " + codeListDoc);
		// get all the versions
		NodeList versionReferences = getNodeList(codeListDoc, "//versions/href");
		System.out.println("# of associated codelist versions = " + versionReferences.getLength());
		// now get the desired one, and when not found, the last (latest)
		String codeListRefToUse = versionReferences.item(versionReferences.getLength()-1).getTextContent();
		System.out.println("last available codelist reference = " + codeListRefToUse);
		for(int k=0; k<versionReferences.getLength(); k++) {
			String versionRef = versionReferences.item(k).getTextContent();
			if(versionRef.contains(codeListVersion)) {
				codeListRefToUse = versionRef;
				break;
			}
		}
		System.out.println("to be used codelist reference = " + codeListRefToUse);
		if(codeListRefToUse != null && codeListRefToUse.length()>0) {
			codeListXML = client.getCDISCLibraryXML(codeListRefToUse);
			//System.out.println("codeListXML = " + codeListXML);
			printStringToFile(codeListXML, new File("C:\\temp\\test.xml"));
			// now get the VERSIONED codelist
			if(codeListXML != null && codeListXML.length() > 0) {
				codeListDoc = getDOMDocument(codeListXML);
				// get the information of the whole codelist itself
				// and store and assign it to the field
				String codeListNCICode = getNodeTextContent(codeListDoc, "/cdiscLibrary/product/conceptId");
				String name = getNodeTextContent(codeListDoc, "/cdiscLibrary/product/name");
				codeList = new CodeList(name,codeListNCICode,codeListRefToUse);
				// also add the unversioned href od this codelist
				codeList.setHrefUnversioned(codeListRef);
				// add all other information
				String extensibleString = getNodeTextContent(codeListDoc, "/cdiscLibrary/product/extensible");
				if(extensibleString.equals("Yes")) codeList.setExtensible(true);  // default is "false"
				String codeListSubmissionValue = getNodeTextContent(codeListDoc, "/cdiscLibrary/product/submissionValue");
				codeList.setSubmissionValue(codeListSubmissionValue);
				String codeListDefinition = getNodeTextContent(codeListDoc, "/cdiscLibrary/product/definition");
				codeList.setDefinition(codeListDefinition);
				String codeListPreferredTerm = getNodeTextContent(codeListDoc, "/cdiscLibrary/product/preferredTerm");
				codeList.setPreferredTerm(codeListPreferredTerm);
				String codeListSynonyms = getNodeTextContent(codeListDoc, "/cdiscLibrary/product/synonyms");
				codeList.setSynonyms(codeListSynonyms);
				System.out.println("Generating codelist with NCI code = " + codeListNCICode);
				// get the codelist items
				NodeList codeListItemNodes = getNodeList(codeListDoc, "//term");
				System.out.println("# of items in codelist = " + codeListItemNodes.getLength());
				ArrayList<CodeListItem> codeListItems = new ArrayList<CodeListItem>();
				for(int l=0; l<codeListItemNodes.getLength(); l++) {
					Node singleCodeListItem = codeListItemNodes.item(l);
					String nciCodeItem = getNodeTextContent(singleCodeListItem, "./conceptId");
					String submissionValue = getNodeTextContent(singleCodeListItem, "./submissionValue");
					String definition = getNodeTextContent(singleCodeListItem, "./definition");
					String preferredTerm = getNodeTextContent(singleCodeListItem, "./preferredTerm");
					String synonyms = getNodeTextContent(singleCodeListItem, "./synonyms");
					CodeListItem codeListItem = new CodeListItem(nciCodeItem,submissionValue);
					codeListItems.add(codeListItem);
					if(definition != null && definition.length() > 0) codeListItem.setDefinition(definition);
					if(preferredTerm != null && preferredTerm.length() > 0) codeListItem.setPreferredTerm(preferredTerm);
					if(synonyms != null && synonyms.length() > 0) codeListItem.setSynonyms(synonyms);
				} 
				codeList.setCodeListItems(codeListItems);
			}
		}
		return codeList;
	}
	
	/** Prints a string (e.g. XML received from the CDISC Library) to a file.
	 * Mostly used for testing and debugging */
	private void printStringToFile(String s, File f) {
		try {
			PrintWriter out = new PrintWriter(f);
			out.print(s);
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("File cannot be found or generated = " + f.getAbsolutePath());
			e.printStackTrace();
		} 
	}
	
	/** Generates a DOM document from an XML string */
	public Document getDOMDocument(String xml) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = null;
		try {
			DocumentBuilder dBuilder = factory.newDocumentBuilder();
			doc = dBuilder.parse(new InputSource(new StringReader(xml)));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return doc;
	}
	
	/** Generates a list of DOM nodes from a parent node and an XPath expression  */
	public NodeList getNodeList(Node parentNode, String xpathExpression) {
		NodeList n = null;
		try {
			n = (NodeList)xpath.evaluate(xpathExpression, parentNode, XPathConstants.NODESET);
		} catch (XPathExpressionException xpe) {
			System.out.println("Invalid XPath Expression = " + xpathExpression);
		}
		return n;
	}
	
	/** Generates a string from a parent node and an XPath expression that requests for a text node  */
	public String getNodeTextContent(Node parentNode, String xpathExpression) {
		String text = null;
		try {
			text = (String)xpath.evaluate(xpathExpression, parentNode, XPathConstants.STRING);
		} catch (XPathExpressionException xpe) {
			System.out.println("Invalid XPath Expression = " + xpathExpression);
		}
		return text;
	}

}
