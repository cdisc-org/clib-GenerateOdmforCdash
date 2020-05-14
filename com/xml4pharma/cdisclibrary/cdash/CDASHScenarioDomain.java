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

/** Represents a CDISC Library CDASH Scenario */
public class CDASHScenarioDomain {
	
	private String title;
	private String href;
	private String domainShortName;
	private String domainLongName;
	private String description;
	private ArrayList<CDASHField> cdashFields;
	
	/** Constructor */
	public CDASHScenarioDomain(String title, String href) {
		this.title = title;
		this.href = href;
	}

	/** Returns the title */
	public String getTitle() {
		return title;
	}

	/** Sets the title */
	public void setTitle(String title) {
		this.title = title;
	}

	/** Returns the reference */
	public String getHref() {
		return href;
	}

	/** Sets the reference */
	public void setHref(String href) {
		this.href = href;
	}

	/** Returns the list of CDASH fields */
	public ArrayList<CDASHField> getCdashFields() {
		return cdashFields;
	}

	/** Sets the list of CDASH fields */
	public void setCdashFields(ArrayList<CDASHField> cdashFields) {
		this.cdashFields = cdashFields;
	}

	/** Returns the domain short name */
	public String getDomainShortName() {
		return domainShortName;
	}

	/** Sets the domain short name */
	public void setDomainShortName(String domainShortName) {
		this.domainShortName = domainShortName;
	}

	/** Returns the domain long name */
	public String getDomainLongName() {
		return domainLongName;
	}

	/** Returns the domain long name */
	public void setDomainLongName(String domainLongName) {
		this.domainLongName = domainLongName;
	}

	/** Returns the domain description */
	public String getDescription() {
		return description;
	}

	/** Sets the domain description */
	public void setDescription(String description) {
		this.description = description;
	}

}
