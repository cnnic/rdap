1. Install JDK6(Java SE Development Kit 6), or higer verison: [Download JDK6] (http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html) ,  [Install JDK6](http://www.oracle.com/technetwork/java/javase/install-142943.html)
1. Install Mysql and init database.
   * [Download and Install Mysql5](http://dev.mysql.com/downloads/mysql) or higer version.  
   * Download sql init file [whois.sql](https://github.com/cnnic/rdap/blob/develop/rdap-webapp/doc/rdap.sql)
   * Login into Mysql, and add user: 
   * 
   ```
   	mysql -h127.0.0.1 -uroot -p
   	ALL PRIVILEGES ON *.* TO 'whois'@'%' IDENTIFIED BY 'cnnic';
   	PRIVILEGES;
   	source whois.sql;
   ```

2. 

1. Get war file. There are two methods to get war file:
   * Get [war file](https://github.com/cnnic/rdap/tree/develop/rdap-webapp/target/rdap-webapp.war) builded by JDK6.(Higer JDK version is not supported, and must build from source)
      
   * Build war file from source
   
      *   [Install Git](http://git-scm.com/book/en/Getting-Started-Installing-Git)

      *  enter into a folder where is used for download source code and build, which is called 'WORK_DIR', execute command:
          ```
		git  config --global http.sslVerify false 
		git clone https://github.com/cnnic/rdap.git
		cd rdap/rdap-webapp
		mvn package (for windows7 and windows8, you may execute: 'mvn.bat package')
		ll target/whois.war (whois.war is the artifact)
	        
          ```
      *  [Install maven3] (http://maven.apache.org/download.cgi#Installation) or higer version
1. Get war file.
