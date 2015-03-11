### rdap-webapp Install  

Tested Operating environment : Red Hat Enterprise Linux Server release 5.3, CentOS release 5.7, Win7, Win8, OS X 10.8.4.  

1. Install [JDK7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html), or higher version. (Skip this step if already installed)   

2. Install [Mysql5](http://dev.mysql.com/downloads/mysql), or higher version. (Skip this step if already installed)   

   The user used for RDAP must has CREATE/DROP/SELECT/INSERT/UPDATE/DELETE/INDEX/ALTER database/table/index privilege.  
   For Mysql privilege please ref [here](http://dev.mysql.com/doc/refman/5.1/en/grant.html).  
   
3. Install Tomcat7, or higher version. (Skip this step if already installed)   

   [Download](http://tomcat.apache.org/download-70.cgi) and [Install Tomcat7](http://tomcat.apache.org/tomcat-7.0-doc/setup.html) or higer version, and HTTP port use default port 8080 (see [here](http://tomcat.apache.org/tomcat-7.0-doc/RUNNING.txt) if use other port).  
    Installed Tomcat root folder called '$TOMCAT_HOME', which contains folders:bin,conf,lib,webapps,etc.    
4. Get war file.  
   There are two methods to get RDAP war file 
   * Get [war file](https://github.com/cnnic/rdap/raw/dev/rdap-service/build/rdap-service-1.0.war) builded by JDK7. 
   * [Build war file from source](https://github.com/cnnic/rdap/wiki/%5Binstall%5DBuild-war-file-from-source)   
5. Deploy RDAP war to tomcat. 
   * Create folder 'rdap' in dir $TOMCAT_HOME/webapps/
   * Unzip RDAP war file to $TOMCAT_HOME/webapps/rdap/
   * Edit database configuration file [jdbc.properties](https://github.com/cnnic/rdap/wiki/jdbc.properties)
   * Edit global configuration file [rdap.properties](https://github.com/cnnic/rdap/wiki/rdap.properties) 
6. Init database.  
   This step will create database named 'rdap', and you can insert test data into it.   
   This step will use database info in jdbc.properties you haved configured before.   
   WARN: this step will DROP database of 'jdbc.url.dbName' if it is existing, and then recreate it.   

	```
   		cd $TOMCAT_HOME/webapps/rdap/WEB-INF/classes
		CLASSPATH=.:$CLASSPATH
		java -Djava.ext.dirs=../lib cn.cnnic.rdap.init.Init initschema      #DROP database 'jdbc.url.dbName', and recreate it, and create table, load base data.
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
	
### rdap-port43 Install
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
