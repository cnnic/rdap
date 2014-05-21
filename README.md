#### Install Instruction
1. Install JDK6(Java SE Development Kit 6), or higer verison: [Download JDK6] (http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html) ,  [Install JDK6](http://www.oracle.com/technetwork/java/javase/install-142943.html)
(Skip this step if JDK6 already installed)
1. Install Mysql and init database.
   * [Download and Install Mysql5](http://dev.mysql.com/downloads/mysql) or higer version.  
     (Skip this step if Mysql5 or higer version already installed)
   * Download sql init file [rdap.sql](https://raw.githubusercontent.com/cnnic/rdap/develop/rdap-webapp/build/rdap.sql)
   * Login into Mysql with mysql client, and add user, and init database schema: 
      * Login mysql server use mysql client,this step will prompt password, and password for 'root' should be inputed, default password is null. Commands:
	   ```
		[in Linux:]
	   	mysql -h127.0.0.1 -uroot -p	# mysql must be in system  'PATH' variable	
		[in Windows:]
		mysql.exe -h127.0.0.1 -uroot -p		# mysql.exe must be in system 'PATH' variable
	   ```
      * Add user, and init database schema: 
	   ```
	   	GRANT ALL PRIVILEGES ON *.* TO 'whois'@'%' IDENTIFIED BY 'cnnic'	#'whois' and 'cnnic' are username and password used to login
	   	FLUSH PRIVILEGES;
	   	source rdap.sql;
	   ```

1. [Download](http://tomcat.apache.org/download-70.cgi) and [Install Tomcat7](http://tomcat.apache.org/tomcat-7.0-doc/setup.html) or higer version, and HTTP port use default port 8080 (see [here](http://tomcat.apache.org/tomcat-7.0-doc/RUNNING.txt) if use other port).
Installed Tomcat root folder called 'TOMCAT_HOME', which contains folders:bin,conf,lib,webapps,etc.).

1. Get war file 'rdap.war'. There are two methods to get war file
   * Get [war file](https://github.com/cnnic/rdap/raw/develop/rdap-webapp/build/rdap.war) builded by JDK6.(Higer JDK version is not supported, and must build from source)
   * Build war file from source
      *  [Install maven3] (http://maven.apache.org/download.cgi#Installation) or higer version
      *  make a dir used to download source code and build, which is called 'WORK_DIR'
      *  [Download source zip file](https://github.com/cnnic/rdap/archive/develop.zip), unzip it to WORK_DIR
      *  execute commands:
          ```
		cd WORK_DIR		#WORK_DIR must be real dir
		cd rdap-develop/rdap-develop/rdap-webapp
		mvn package -Dmaven.test.skip=true	 #for windows7 and windows8, you may use 'mvn.bat' instead of 'mvn'
		(target/rdap.war  is the build war file)

          ```
1. Deploy rdap.war to tomcat
   * unzip rdap.war to $TOMCAT_HOME/webapps/, This step will create folder 'rdap' in $TOMCAT_HOME/webapps/
		
   * Edit database configuration file: $TOMCAT_HOME/webapps/rdap/WEB-INF/classes/jdbc.properties:
	
		```
			jdbc.url: value change to installed Mysql url in step 'Install Mysql and init database'
			jdbc.username: value change to Mysql username in step 'Install Mysql and init database', default is 'whois'
			jdbc.password: value change to Mysql password in step 'Install Mysql and init database', default is 'cnnic'
		```

   * Start up tomcat
	   
		```
			cd $TOMCAT_HOME		#$TOMCAT_HOME must be replaced by real dir
			bin/startup.sh
		```

   * Test if it is runing ok
	   	```
			curl -H Accept:application/rdap+json http://127.0.0.1:8080/rdap/.well-known/rdap
			(change '8080' to real tomcat HTTP port if it's not 8080)
		```
	It's ok if response contains 'rdapConformance'. 
