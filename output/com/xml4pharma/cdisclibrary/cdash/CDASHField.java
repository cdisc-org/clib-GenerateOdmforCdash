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

/** Represents a CDISC Library CDASH field */
public class CDASHField {

	private String href;
	private String name;
	private String label;
	private String definition;
	private String questionText;
	private String prompt;
	private String completionInstructions;
	private String implementationNotes;
	private String simpleDatatype;
	private String mappingInstructions;
	private String core;  // can be "O", "R/C", "HR"
	
	private CodeList associatedCodeList;
	
	/** Constructor */
	public CDASHField(String name, String simpleDatatype, String href) {
		this.name = name;
		this.simpleDatatype = simpleDatatype;
		this.href = href;
	}

	/** Returns the CDISC Library reference to the codelist */
	public String getHref() {
		return href;
	}
	
	/** Sets the CDISC Library reference to the codelist */
	public void setHref(String href) {
		this.href = href;
	}

	/** Returns the name of the field  */
	public String getName() {
		return name;
	}

	/** Sets the name of the field  */
	public void setName(String name) {
		this.name = name;
	}

	/** Returns the field label  */
	public String getLabel() {
		return label;
	}

	/** Sets the field label  */
	public void setLabel(String label) {
		this.label = label;
	}

	/** Returns the field definition  */
	public String getDefinition() {
		return definition;
	}

	/** Sets the field definition  */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/** Returns the field question text */
	public String getQuestionText() {
		return questionText;
	}

	/** Sets the field question text */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	/** Returns the field prompt text */
	public String getPrompt() {
		return prompt;
	}

	/** Sets the field prompt text */
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	/** Returns the implementation notes text */
	public String getImplementationNotes() {
		return implementationNotes;
	}

	/** Sets the implementation notes text */
	public void setImplementationNotes(String implementationNotes) {
		this.implementationNotes = implementationNotes;
	}

	/** Returns the simple datatype (char, num) */
	public String getSimpleDatatype() {
		return simpleDatatype;
	}

	/** Sets the simple datatype (char, num) */
	public void setSimpleDatatype(String simpleDatatype) {
		this.simpleDatatype = simpleDatatype;
	}

	/** Returns the mapping instructions */
	public String getMappingInstructions() {
		return mappingInstructions;
	}

	/** Sets the simple datatype (char, num) */
	public void setMappingInstructions(String mappingInstructions) {
		this.mappingInstructions = mappingInstructions;
	}

	/** Returns the core properties (Req,Exp,Perm) */
	public String getCore() {
		return core;
	}

	/** Sets the core properties (Req,Exp,Perm) */
	public void setCore(String core) {
		this.core = core;
	}

	/** Returns the completion instructions */
	public String getCompletionInstructions() {
		return completionInstructions;
	}

	/** Sets the completion instructions */
	public void setCompletionInstructions(String completionInstructions) {
		this.completionInstructions = completionInstructions;
	}

	/** Returns the field associated CodeList object */
	public CodeList getAssociatedCodeList() {
		return associatedCodeList;
	}

	/** Sets the field associated CodeList object */
	public void setAssociatedCodeList(CodeList associatedCodeList) {
		this.associatedCodeList = associatedCodeList;
	}
	
	/** Generates an ItemRef string*/
	public String toODMItemRef() {
		String mandatory = "No";
		if(core.equals("HR")) mandatory = "Yes";
		String xml = "<ItemRef ItemOID=\"" + "IT." + name + "\" Mandatory=\"" + mandatory + "\"/>";
		return xml;
	}
	
	/** Generates an ItemDef string */
	public String toODMItemDef() {
		String xml = "<ItemDef OID=\"" + "IT." + name + "\" Name=\"" + name + "\" ";
		// add a datatype - TODO: improve - TODO: we cannot assign Length
		String dataType = "text";
		if(simpleDatatype.equals("Char") == false) dataType = simpleDatatype;
		// we set "time" for variables ending with "TIM" and "date" for variables ending with "DAT"
		if(name.endsWith("DAT")) {
			dataType = "date";
		} else if(name.endsWith("TIM")) {
			dataType = "time";
		} else if(simpleDatatype.equals("Num")) {
			dataType = "integer";  // best guess
		}
		xml += "DataType=\"" + dataType + "\" ";
		// SDSVarName when possible
		if(mappingInstructions.startsWith("Maps directly to the SDTMIG variable listed in the column")) {
			xml += "SDSVarName=\"" + name + "\" ";
		}
		xml += ">";
		// Description
		xml += "\n<Description><TranslatedText xml:lang=\"en\">"
				+ label + "</TranslatedText></Description>";
		// Question
		xml += "\n<Question><TranslatedText xml:lang=\"en\">"
				+ replaceSpecialXMLChars(questionText) + "</TranslatedText></Question>";
		// TODO: codelist
		if(associatedCodeList != null) xml += "\n" + associatedCodeList.toCodeListRef();
		// Aliases: prompt, completionInstructions, implementationNotes, mappingInstructions
		if(prompt != null && prompt.trim().length() > 0) xml += "\n<Alias Context=\"prompt\" Name=\"" + replaceSpecialXMLChars(prompt) + "\"/>";
		if(completionInstructions != null && completionInstructions.trim().length() > 0) xml += "\n<Alias Context=\"completionInstructions\" Name=\"" + replaceSpecialXMLChars(completionInstructions) + "\"/>";
		if(implementationNotes != null && implementationNotes.trim().length() > 0) xml += "\n<Alias Context=\"implementationNotes\" Name=\"" + replaceSpecialXMLChars(implementationNotes) + "\"/>";
		if(mappingInstructions != null && mappingInstructions.trim().length() > 0) xml += "\n<Alias Context=\"mappingInstructions\" Name=\"" + replaceSpecialXMLChars(mappingInstructions) + "\"/>";
		// trying to generate a machine-readable mapping instruction, using an Alias for "SDTM"
		if(mappingInstructions.contains("populate the SDTMIG variable")) {
			int posSDTMIGVarPos = mappingInstructions.indexOf("populate the SDTMIG variable") + "populate the SDTMIG variable".length();
			System.out.println("index posSDTMIGVarPos = " + posSDTMIGVarPos);
			if(posSDTMIGVarPos > -1) {
				String sdtmIGVar = mappingInstructions.substring(posSDTMIGVarPos).trim(); // remove blanks at start and end
				// now find the first blank, and take the word until that first blank
				int posFirstBlank = sdtmIGVar.indexOf(" ");
				if(posFirstBlank > -1) {
					sdtmIGVar = sdtmIGVar.substring(0,posFirstBlank).trim();
					System.out.println("Alias SDTM = " + sdtmIGVar);
					xml += "\n" + "<Alias Context=\"SDTM\" Name=\"" + sdtmIGVar + "\"/>";
				}
			}
		// field that is "not submitted"
		} else if(mappingInstructions.contains("NOT SUBMITTED")) {
			xml += "\n" + "<Alias Context=\"SDTM\" Name=\"" + "NOT SUBMITTED" + "\"/>";
		}
		// close the ItemDef element
		xml += "\n</ItemDef>";
		return xml;
	}
	
	/** Replacing special XML characters  */
	private static String replaceSpecialXMLChars(String xml) {
		xml = xml.replaceAll("&", "&amp;");
		xml = xml.replaceAll("<", "&lt;");
		xml = xml.replaceAll(">", "&gt;");
		xml = xml.replaceAll("\"", "&quot;");
		xml = xml.replaceAll("'", "&apos;");
        return xml;
    }

}
