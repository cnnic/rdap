### rdap-webapp Install

Tested Operating environment:Red Hat Enterprise Linux Server release 5.3, CentOS release 5.7, Win7, Win8, OS X 10.8.4.

1. Install JDK6, or higher version. (Skip this step if already installed)
   
   [Download JDK6] (http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html) ,  [Install JDK6](http://www.oracle.com/technetwork/java/javase/install-142943.html), or higher verison.

   For OS X,you should use [JDK7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html) or higher version.
2. Install Mysql5, or higher version. (Skip this step if already installed)
   
   [Download and Install Mysql5](http://dev.mysql.com/downloads/mysql) or higher version. 
     
   You must get (from DBA) or generate an Mysql user/password pair, used to access RDAP database, and this user must has CREATE/DROP/SELECT/INSERT/UPDATE/DELETE/INDEX/ALTER database/table/index privilege. For Mysql privilege please ref [here](http://dev.mysql.com/doc/refman/5.1/en/grant.html).

3. Install Tomcat7, or higher version. (Skip this step if already installed)

   [Download](http://tomcat.apache.org/download-70.cgi) and [Install Tomcat7](http://tomcat.apache.org/tomcat-7.0-doc/setup.html) or higer version, and HTTP port use default port 8080 (see [here](http://tomcat.apache.org/tomcat-7.0-doc/RUNNING.txt) if use other port). 


    Installed Tomcat root folder called '$TOMCAT_HOME', which contains folders:bin,conf,lib,webapps,etc.
    
    For OS X,you should use tar.gz formated Binary Distributions.

4. Get war file 'rdap.war'. 

   There are two methods to get war file
   * Get [war file](https://github.com/cnnic/rdap/raw/dev/rdap-webapp/build/rdap.war) builded by JDK6. 
   * [Build war file from source](https://github.com/cnnic/rdap/wiki/%5Binstall%5DBuild-war-file-from-source)

5. Deploy rdap.war to tomcat.
   * Create folder 'rdap' in dir $TOMCAT_HOME/webapps/
   * Unzip rdap.war to $TOMCAT_HOME/webapps/rdap/
   * Edit database configuration file $TOMCAT_HOME/webapps/rdap/WEB-INF/classes/jdbc.properties, [see here](https://github.com/cnnic/rdap/wiki/jdbc.properties)
   * Edit global configuration file $TOMCAT_HOME/webapps/rdap/WEB-INF/classes/rdap.properties, [see here](https://github.com/cnnic/rdap/wiki/rdap.properties)

6. Init database. 
   
   This step will create database called 'rdap', and create schema, and you can insert test data into it. 

   This step will use database info in jdbc.properties you haved configured before.
     
   WARN: this step will DROP database of 'jdbc.url.dbName' if it is existing, and then recreate it.
	   
		```
   		cd $TOMCAT_HOME/webapps/rdap/WEB-INF/classes
		CLASSPATH=.:$CLASSPATH
		java -Djava.ext.dirs=../lib cn.cnnic.rdap.init.Init initschema      #DROP database 'jdbc.url.dbName', and recreate it, and create table, load base data.
	   	[If you want load some test data, execute following command:]
		java -Djava.ext.dirs=../lib cn.cnnic.rdap.init.Init initdata  init/mysql/test-data.sql      
	   	```
7. Start up tomcat
   * Start up tomcat
	   
		```
		[in Linux/OS X, open a shell and execute command:]
			cd $TOMCAT_HOME		#$TOMCAT_HOME must be replaced by real dir
			bin/startup.sh
		[in Windows, open command prompt window and execute command:]
			cd $TOMCAT_HOME/bin		#$TOMCAT_HOME must be replaced by real dir
			startup.bat
		```

   * Test if it is runing ok
	   	```
		curl -H Accept:application/rdap+json http://$RDAP_SERVER_IP:$RDAP_SERVER_PORT/rdap/autnum/2100
		```
	It's ok if response contains 'rdapConformance'. 
	
### Port43 Install
[Port43 Install](https://github.com/cnnic/rdap/wiki/Port43-install-&-usage)
### Api Doc
[Api Doc](https://github.com/cnnic/rdap/wiki/Api-Doc)
### Known Issues
[Known Issues](https://github.com/cnnic/rdap/wiki/Known%20Issues)
### License
Simplified BSD License
### Contribute
We love contributions! You can fork us on [github](https://github.com/cnnic/rdap).Please refer to [contribution guidelines](https://github.com/cnnic/rdap/wiki/Develop-Guide) for details.


Any questions please [create issue](https://github.com/cnnic/rdap/issues/new) with details.
