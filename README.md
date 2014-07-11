### Operating environment
Red Hat Enterprise Linux Server release 5.3; CentOS release 5.7; Windows7; Windows8
### Install Instruction
1. Install JDK6(Java SE Development Kit 6), or higer verison: [Download JDK6] (http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html) ,  [Install JDK6](http://www.oracle.com/technetwork/java/javase/install-142943.html)
(Skip this step if JDK6 already installed)
2. Install Mysql and init database. (Skip this step if Mysql5 or higer version already installed)
   [Download and Install Mysql5](http://dev.mysql.com/downloads/mysql) or higer version. 
     
   You must get an user/password pair, we called $MYSQL_USERNAME/$MYSQL_PASSWORD, used for RDAP database, and this user must has CREATE/DROP/SELECT/INSERT/UPDATE/DELETE/INDEX/ALTER database/table/index privilege. See [Mysql Ref](http://dev.mysql.com/doc/refman/5.1/en/grant.html).

3. [Download](http://tomcat.apache.org/download-70.cgi) and [Install Tomcat7](http://tomcat.apache.org/tomcat-7.0-doc/setup.html) or higer version, and HTTP port use default port 8080 (see [here](http://tomcat.apache.org/tomcat-7.0-doc/RUNNING.txt) if use other port).
Installed Tomcat root folder called '$TOMCAT_HOME', which contains folders:bin,conf,lib,webapps,etc.).

4. Get war file 'rdap.war'. There are two methods to get war file
   * Get [war file](https://github.com/cnnic/rdap/raw/master/rdap-webapp/build/rdap.war) builded by JDK6.
   * Build war file from source
      *  [Install maven3] (http://maven.apache.org/download.cgi#Installation) or higer version
      *  make a dir used to download source code and build, which is called 'WORK_DIR'
      *  [Download source zip file](https://github.com/cnnic/rdap/archive/master.zip), unzip it to $WORK_DIR
      *  build project:
		```
		[in Linux, open a shell and execute command:]
			cd $WORK_DIR/rdap-master/rdap-webapp		# $WORK_DIR must be replaced by real dir
			mvn package -Dmaven.test.skip=true	# mvn must in system variable. Option '-Dmaven.compiler.target' can be used for higher jdk version, such as '-Dmaven.compiler.target=1.7' for jdk7
		[in Windows7 or Windows8, open command prompt window and execute command:]
			cd $WORK_DIR/rdap-master/rdap-webapp		# $WORK_DIR must be replaced by real dir
			mvn.bat package -Dmaven.test.skip=true # mvn.bat must in system variable
		(target/rdap.war  is the build war file)
	
		```
5. Deploy rdap.war to tomcat.
   * create folder 'rdap' in dir $TOMCAT_HOME/webapps/
   * unzip rdap.war to $TOMCAT_HOME/webapps/rdap/
		
   * Edit database configuration file: $TOMCAT_HOME/webapps/rdap/WEB-INF/classes/jdbc.properties:
	
		```
		jdbc.url.hostPort=jdbc:mysql://$MYSQL_HOST_OR_IP:3306/:  change $MYSQL_HOST_OR_IP to Mysql host or ip
		jdbc.url.dbName=rdap: value change to RDAP database name
		jdbc.username=: value change to Mysql username
		jdbc.password=: value change to Mysql password
		```
		
   * Edit global configuration file: $TOMCAT_HOME/webapps/rdap/WEB-INF/classes/rdap.properties:
	
		```
		localServiceUrl=rdap.restfulwhois.org: value change to local RDAP service url, This value is used in redirect service to check if redirect url is local service url, and will ignore the redirect if is local service url.
		inTlds=cn;xn--fiqs8sx;: value change to puny name of tlds in this registry, splited by ';'.Only in this list can query.
		notInTlds=edu.cn: value change to puny name of tlds NOT in this registry, splited by ';'. Tlds  in this list can NOT be query, and will query redirect instead.
		```
6. Init database. 
   There are two methods, if you are familiar with Mysql, you may use first method, and if not you should use second.
   * Use Mysql client to init
      *  Login mysql server use mysql client, $MYSQL_USERNAME must be replaced by real Mysql username.
     
		```
		cd $MYSQL_HOME     #$MYSQL_HOME must be replaced by real Mysql home dir
		[in Linux, open a shell and execute command:]
	   		mysql -h127.0.0.1 -u$MYSQL_USERNAME  -p
		[in Windows, open command prompt window and execute command:]
			mysql.exe -h127.0.0.1 -u$MYSQL_USERNAME  -p	
		```
      *  Download [rdap-db-init-schema.sql](https://raw.githubusercontent.com/cnnic/rdap/master/rdap-webapp/build/rdap-db-init-schema.sql) and [rdap-db-test-data.sql](https://raw.githubusercontent.com/cnnic/rdap/master/rdap-webapp/build/rdap-db-test-data.sql). Execute: 
      
	   	```
	   	source rdap-db-init-schema.sql;    # init database schema
		(If you want load some test data, execute following command:)
		source rdap-db-test-data.sql;    #insert test data
	   	```
   * Use init tool to init
	   
		```
   		cd $TOMCAT_HOME/webapps/rdap/WEB-INF/classes
		CLASSPATH=.:$CLASSPATH
		java -Djava.ext.dirs=../lib cn.cnnic.rdap.init.Init initschema      #this will DROP database for 'jdbc.url.dbName', and recreate this database, and create table, load base data.
	   	(If you want load some test data, execute following command:)
		java -Djava.ext.dirs=../lib cn.cnnic.rdap.init.Init initdata  init/mysql/test-data.sql      
	   	```

7. Start up tomcat
   * Start up tomcat
	   
		```
		[in Linux, open a shell and execute command:]
			cd $TOMCAT_HOME		#$TOMCAT_HOME must be replaced by real dir
			bin/startup.sh
		[in Windows, open command prompt window and execute command:]
			cd $TOMCAT_HOME/bin		#$TOMCAT_HOME must be replaced by real dir
			startup.bat
		```

   * Test if it is runing ok
	   	```
		curl -H Accept:application/rdap+json http://$RDAP_SERVER_IP:$RDAP_SERVER_PORT/rdap/.well-known/rdap/autnum/2100
		(change '8080' to real tomcat HTTP port if it's not 8080)
		```
	It's ok if response contains 'rdapConformance'. 

Any questions please [create issue](https://github.com/cnnic/rdap/issues/new) with details.
