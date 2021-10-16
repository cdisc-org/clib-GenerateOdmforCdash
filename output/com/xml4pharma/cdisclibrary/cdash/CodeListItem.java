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

/** Class representing a codelist item */
public class CodeListItem {
	
	private String nciCode;
	private String submissionValue;
	private String definition;
	private String preferredTerm;
	private String synonyms;
	
	/** Constructor */
	public CodeListItem(String nciCode, String submissionValue) {
		this.nciCode = nciCode;
		this.submissionValue = submissionValue;
	}

	/** Returns the NCI code of the codelist-item */
	public String getNciCode() {
		return nciCode;
	}
	
	/** Sets the NCI code of the codelist-item */
	public void setNciCode(String nciCode) {
		this.nciCode = nciCode;
	}

	/** Returns the submission value */
	public String getSubmissionValue() {
		return submissionValue;
	}

	/** Sets the submission value */
	public void setSubmissionValue(String submissionValue) {
		this.submissionValue = submissionValue;
	}

	/** Returns the definition of the codelist-item */
	public String getDefinition() {
		return definition;
	}

	/** Sets the definition of the codelist-item */
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

	/** Returns the synonyms as a single string */
	public String getSynonyms() {
		return synonyms;
	}

	/** Sets the synonyms as a single string */
	public void setSynonyms(String synonyms) {
		this.synonyms = synonyms;
	}
	
	/** Creates the CodeListItem XML and returns it as a string */
	public String toXML() {
		String xml = "<CodeListItem CodedValue=\"" + submissionValue + "\">"
			+ "\n<Decode><TranslatedText xml:lang=\"en\">" + preferredTerm + "</TranslatedText></Decode>"
			+ "\n<Alias Context=\"nci:ExtCodeID\" Name=\"" + nciCode + "\"/>"
			+ "\n</CodeListItem>";

		return xml;
	}

}
