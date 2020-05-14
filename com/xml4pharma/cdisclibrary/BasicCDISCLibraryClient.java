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

package com.xml4pharma.cdisclibrary;

import javax.ws.rs.client.*;  // Client and ClientBuilder - javax.ws.rs-api-2.1.jar
import javax.ws.rs.core.*;

// Jersey-2 imports
import org.glassfish.jersey.client.*;
import org.glassfish.jersey.client.authentication.*;
import org.glassfish.jersey.logging.LoggingFeature;  // when adding logging

/** The class for connecting to and querying the CDISC Library
 * requires Jersey 2.x Java library: https://eclipse-ee4j.github.io/jersey/  */

public class BasicCDISCLibraryClient {
	
	private String base = "https://library.cdisc.org/api";  // the base of the service
	private String username = "xxxx";  // your user name
	private String pass = "yyyy";  // your passkey
	
	private Client client;
	
	/** Constructor  */
	public BasicCDISCLibraryClient() {
		ClientConfig clientConfig = new ClientConfig();
		// see e.g. https://jersey.github.io/documentation/latest/logging_chapter.html for how to add logging
		//clientConfig.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY);
		client = ClientBuilder.newClient(clientConfig);
		// Basic Authentication
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username,pass);
		client.register(feature);
	}
	
	/** Returns the CDASH classes as XML for the given CDASH-IG version */
	public String getCDASHClasses(String cdashIgVersion) {
		WebTarget webTarget = client.target(base).path("mdr").path("cdashig").path(cdashIgVersion).path("classes");
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
		// Response requires javax.annotation-api-1.2.jar, hk2-api-2.5.0-b42.jar, hk2-locator-2.5.0-b42.jar, hk2-utils-2.5.0-b42.jar
		// and javax.inject-1.jar, javax.inject-2.5.0-b42.jar
		// and jersey-media-json-binding.jar
		// from the Jersey-2 library - see https://jersey.github.io/
		Response response = invocationBuilder.get();
		System.out.println("HTTP Response Status = " + response.getStatus());
		String xml = response.readEntity(String.class);
		return xml;
	}
	
	/** Returns the CDASH scenarios as XML for the given CDASH-IG version
	 * ATTENTION: this only seems to provide answers for "Findings" */
	public String getCDASHScenarios(String cdashIgVersion) {
		WebTarget webTarget = client.target(base).path("mdr").path("cdashig").path(cdashIgVersion).path("scenarios");
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
		// Response requires javax.annotation-api-1.2.jar, hk2-api-2.5.0-b42.jar, hk2-locator-2.5.0-b42.jar, hk2-utils-2.5.0-b42.jar
		// and javax.inject-1.jar, javax.inject-2.5.0-b42.jar
		// and jersey-media-json-binding.jar
		// from the Jersey-2 library - see https://jersey.github.io/
		Response response = invocationBuilder.get();
		System.out.println("HTTP Response Status = " + response.getStatus());
		String xml = response.readEntity(String.class);
		return xml;
	}
	
	/** Returns the CDASH variable properties as XML for the given CDASH-IG version, domain and given CDASH field */
	public String getCDASHVariablePropertiesXML(String cdashIgVersion, String domain, String field) {
		WebTarget webTarget = client.target(base).path("mdr").path("cdashig").path(cdashIgVersion).path("domains").path(domain).path("fields").path(field);
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
		// Response requires javax.annotation-api-1.2.jar, hk2-api-2.5.0-b42.jar, hk2-locator-2.5.0-b42.jar, hk2-utils-2.5.0-b42.jar
		// and javax.inject-1.jar, javax.inject-2.5.0-b42.jar
		// and jersey-media-json-binding.jar
		// from the Jersey-2 library - see https://jersey.github.io/
		Response response = invocationBuilder.get();
		System.out.println("HTTP Response Status = " + response.getStatus());
		String xml = response.readEntity(String.class);
		return xml;
	}
	
	/** Returns the CDASH variable properties as JSON for the given CDASH-IG version, domain and given CDASH field */
	public String getCDASHVariablePropertiesJSON(String cdashIgVersion, String domain, String field) {
		//WebTarget webTarget = client.target(base).path("mdr").path("sdtmig").path(igVersion).path("datasets").path(domain).path("variables").path(variable);
		WebTarget webTarget = client.target(base).path("mdr").path("cdashig").path(cdashIgVersion).path("domains").path(domain).path("fields").path(field);
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		// Response requires javax.annotation-api-1.2.jar, hk2-api-2.5.0-b42.jar, hk2-locator-2.5.0-b42.jar, hk2-utils-2.5.0-b42.jar
		// and javax.inject-1.jar, javax.inject-2.5.0-b42.jar
		// and jersey-media-json-binding.jar
		// from the Jersey-2 library - see https://jersey.github.io/
		Response response = invocationBuilder.get();
		System.out.println("HTTP Response Status = " + response.getStatus());
		String xml = response.readEntity(String.class);
		//System.out.println(xml);
		return xml;
	}
	
	/** Returns the scenario fields as XML - uses HATEAOS */
	public String getCDISCLibraryXML(String query) {
		WebTarget webTarget = client.target(base).path(query);
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
		// Response requires javax.annotation-api-1.2.jar, hk2-api-2.5.0-b42.jar, hk2-locator-2.5.0-b42.jar, hk2-utils-2.5.0-b42.jar
		// and javax.inject-1.jar, javax.inject-2.5.0-b42.jar
		// and jersey-media-json-binding.jar
		// from the Jersey-2 library - see https://jersey.github.io/
		Response response = invocationBuilder.get();
		System.out.println("HTTP Response Status = " + response.getStatus());
		String xml = response.readEntity(String.class);
		return xml;
	}
	
	
	////////////////////////////////////////
	// CDISC/CDASH Controlled Terminology //
	////////////////////////////////////////
	
	/** Returns the CDISC Controlled Terminology codelists for the given model (SDTM,SEND, ...) and CT version */
	public String getControlledTerminologyCodeLists(String model, String ctVersion) {
		System.out.println("Getting supported codelists for model = " + model + " and CT version = " + ctVersion);
		String packageVersion = model + "ct-" + ctVersion;
		WebTarget webTarget = client.target(base).path("mdr").path("ct").path("packages").path(packageVersion).path("codelists");
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
		// Response requires javax.annotation-api-1.2.jar, hk2-api-2.5.0-b42.jar, hk2-locator-2.5.0-b42.jar, hk2-utils-2.5.0-b42.jar
		// and javax.inject-1.jar, javax.inject-2.5.0-b42.jar
		// and jersey-media-json-binding.jar
		// from the Jersey-2 library - see https://jersey.github.io/
		Response response = invocationBuilder.get();
		System.out.println("HTTP Response Status = " + response.getStatus());
		String xml = response.readEntity(String.class);
		//System.out.println(xml);
		return xml;
	}
	
	/** Returns CDISC Controlled Terminology details for the given model (SDTM, SEND, ...) and the given codelist NCI code */
	public String getControlledTerminologyDetails(String model, String ctVersion, String codeListNCICode) {
		String packageVersion = model + "ct-" + ctVersion;
		System.out.println("packageVersion = " + packageVersion);
		WebTarget webTarget = client.target(base).path("mdr").path("ct").path("packages").path(packageVersion).path("codelists").path(codeListNCICode);
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
		Response response = invocationBuilder.get();
		System.out.println("HTTP Response Status = " + response.getStatus());
		String xml = response.readEntity(String.class);
		//System.out.println(xml);
		return xml;
	}

	/** Main method - can be used for testing */
	/* public static void main(String[] args) {
		BasicCDISCLibraryClient cl = new BasicCDISCLibraryClient();
		String response;
		long begin = System.currentTimeMillis();  // performance measurement
		String scenariosXML = cl.getCDASHScenarios("2-1");
		System.out.println("scenarios = " + scenariosXML);
	} */

}
