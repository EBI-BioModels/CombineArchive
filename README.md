Combine Archive
===============
This provides an experimental implementation of a Java API to the 
[COMBINE Archive](http://co.mbine.org/documents/archive).

The archive maintains the manifest of the archive and provides access to the metadata file to enable custom annotation
of the archive. It provides basic annotation of the archive including the creation and modification dates of the contents.
This could be extended to include, for example, the date elements were added to the archive etc.

The API is relatively simple to use. There are three examples files in the test directory that show how to create, 
populate and iterate a new archive
and also how to update an existing archive. These are called:

* CreateNewArchiveTest.java
* UpdateArchiveTest.java
* ExtractArchiveTest.java

An example of its usage is downloading COMBINE Archive files from 
[BioModels repository](https://www.ebi.ac.uk/biomodels).
When users do not select a single file to download and want to download the whole files, they can click on the Download 
button to obtain the COMBINE Archive file which is implemented this library. 

Build Instructions
------------------

The project is built by using Apache Ant and uses Apache Ivy for dependency resolution. Recently, we have created 
```pom.xml``` file to allow developers able to build and deploy the library with Maven.

<h3> Building from command line </h3>

    # Ant
    # fetch dependencies
    ant resolve
    # create the JARS containing binaries, sources and documentation
    ant jarAll

For a list of all available targets please use `ant -p`.
 
    # Maven
    mvn clean compile test verify package install

<h3> IDE Support </h3>

Eclipse and IntelliJ IDEA project files are available in the project's root folder.
Ensure you perform dependency resolution (by running `ant resolve`) so that your classpath
is configured correctly.

Existing Maven based deployments
--------------------------------
A maven-based deployments of this library are available at [EBI's
Sonatype Nexus Repository Manager](https://www.ebi.ac.uk/Tools/maven/repos/#browse/welcome) in either [releases](https://www.ebi.ac.uk/Tools/maven/repos/#browse/browse:pst-release:org%2Fmbine%2Fco%2FlibCombineArchive) or [snapshots](https://www.ebi.ac.uk/Tools/maven/repos/#browse/browse:pst-snapshots:org%2Fmbine%2Fco%2FlibCombineArchive).

Contact
--------
All feedback and suggestions are welcome via either raising an issue on this repository or sending us a message to  
[biomodels-developers@ebi.ac.uk](biomodels-developers@ebi.ac.uk).

Developed by Stuart Moodie. Maintained by [Mihai Glonț](https://github.com/mglont) and [Tung Nguyen](https://github.com/ntung). Please raise 
issues or feature
requests [on EBI-BioModels' GitHub](https://github.com/EBI-BioModels/CombineArchive/issues).

License
-------
Copyright EMBL-EBI 2017 - 2023. This code is licensed under Apache V2.0. See LICENSE for more details.
