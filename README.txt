These java programs allow to generate CDASH forms in CDISC-ODM v.1.3.1 format.
In order to work them, you will need to:
- Add CDISC Library username and pass key to class BasicCDISCLibraryClient.java (class variables "username" and "pass")
- Add the Jersey 2.x libraries for RESTful web services (available from https://eclipse-ee4j.github.io/jersey/)
- Add Saxon9-HE libraries for parsing (available from http://saxon.sourceforge.net/)
- Compile the source using a Java compiles (at least version 8)

The main class to start from is named Main, and resides in package com.xml4pharma.cdisclibrary.cdash

There are a few options, which you need to set in the source of the class Main:
- Version of the CDASH-IG in class variable "cdashVersion"
- Whether codelists should be loaded and included, in class variable "includeCodeLists"
- Codelist (CDISC-CT) version in class variable "codeListVersion"

As the software is "open source", with a very liberal MIT license, you can of course alter the source code, and use it in your applications

If you need more detailed information, please contact me at Jozef.Aerts@XML4Pharma.com

Jozef Aerts, May 2020