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

import java.util.ArrayList;

/** Represents a CDASH/SDTM codelist */
public class CodeList {
	
	private String href;
	private String hrefUnversioned;  // href to unversioned href of the current codelist
	private String nciCode;
	private boolean extensible = false;
	private String name;
	private String submissionValue;
	private String definition;
	private String preferredTerm;
	private String synonyms;
	// list of the codelist items
	private ArrayList<CodeListItem> codeListItems = new ArrayList<CodeListItem>();
	
	private String version;  // if provided, this is not the generic codelist, but a versioned one
	
	public CodeList(String name, String nciCode, String href) {
		this.name = name; 
		this.nciCode = nciCode;
		this.href = href;
	}

	/** Creates and returns a CodeList OID according to the CDISC-CT conventions */
	public String getCodeListOID() {
		String codeListOID = "CL." + nciCode + "." + submissionValue;
		return codeListOID;
	}

	/** Returns the CDISC Library reference to the codelist */
	public String getHref() {
		return href;
	}

	/** Sets the CDISC Library reference to the codelist */
	public void setHref(String href) {
		this.href = href;
	}

	/** Returns the NCI code of the codelist */
	public String getNciCode() {
		return nciCode;
	}

	/** Sets the NCI code of the codelist */
	public void setNciCode(String nciCode) {
		this.nciCode = nciCode;
	}

	/** Returns the version of the codelist */
	public String getVersion() {
		return version;
	}

	/** Sets the version of the codelist */
	public void setVersion(String version) {
		this.version = version;
	}

	/** Returns whether the codelist is extensible */
	public boolean isExtensible() {
		return extensible;
	}

	/** Sets whether the codelist is extensible */
	public void setExtensible(boolean extensible) {
		this.extensible = extensible;
	}

	/** Returns the name */
	public String getName() {
		return name;
	}

	/** Sets the name */
	public void setName(String name) {
		this.name = name;
	}

	/** Returns the submission value */
	public String getSubmissionValue() {
		return submissionValue;
	}

	/** Sets the submission value */
	public void setSubmissionValue(String submissionValue) {
		this.submissionValue = submissionValue;
	}

	/** Returns the definition */
	public String getDefinition() {
		return definition;
	}

	/** Sets the definition */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/** Returns the preferred term */
	public String getPreferredTerm() {
		return preferredTerm;
	}

	/** Sets the preferred term */
	public void setPreferredTerm(String preferredTerm) {
		this.preferredTerm = preferredTerm;
	}

	/** Returns the list of synonyms */
	public String getSynonyms() {
		return synonyms;
	}

	/** Sets the list of synonyms */
	public void setSynonyms(String synonyms) {
		this.synonyms = synonyms;
	}

	/** Returns the list of codelist items */
	public ArrayList<CodeListItem> getCodeListItems() {
		return codeListItems;
	}

	/** Sets the list of codelist items */
	public void setCodeListItems(ArrayList<CodeListItem> codeListItems) {
		this.codeListItems = codeListItems;
	}
	
	/** Generates a CodeListRef ODM-XML element */
	public String toCodeListRef() {
		String codeListRef = "<CodeListRef CodeListOID=\"CL." + nciCode + "." + submissionValue + "\"/>";
		return codeListRef;
	}
	
	/** Geenrates a CodeList definition as ODM-XML */
	public String toCodeListDef() {
		String codeListDef = "<CodeList OID=\"" + getCodeListOID() + "\"" 
				+ " Name=\"" + name + "\" DataType=\"text\">";
		// Description
		codeListDef += "\n<Description><TranslatedText xml:lang=\"en\">" + preferredTerm + "</TranslatedText></Description>";
		// TODO?: codelist items with decode
		for(CodeListItem clItem : codeListItems) codeListDef += clItem.toXML();
		// IMPORTANT: NCI code in Alias
		codeListDef += "\n<Alias Context=\"nci:ExtCodeID\" Name=\"" + nciCode + "\"/>";
		codeListDef += "\n</CodeList>";
		return codeListDef;
	}

	public String getHrefUnversioned() {
		return hrefUnversioned;
	}

	public void setHrefUnversioned(String hrefUnversioned) {
		this.hrefUnversioned = hrefUnversioned;
	}
	
}
