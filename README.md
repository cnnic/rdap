1. Install JDK6(Java SE Development Kit 6), or higer verison: [Download JDK6] (http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html) ,  [Install JDK6](http://www.oracle.com/technetwork/java/javase/install-142943.html)
1. [Download and Install Mysql5](http://dev.mysql.com/downloads/mysql) or higer version.  

1. Get war file. 
   * Get [war file](https://github.com/cnnic/rdap/tree/develop/rdap-webapp/target/rdap-webapp.war) builded by JDK6.(Higer JDK version is not supported, and must build from source)
      
   * Build war file from source
   
      *   [Install Git](http://git-scm.com/book/en/Getting-Started-Installing-Git)

      *  execute command:
          ```
          git  config --global http.sslVerify false 
	      git clone https://github.com/cnnic/rdap.git
	        
          ```
      *  [Install maven3] (http://maven.apache.org/download.cgi#Installation) or higer version

1. [Develop Guide](https://github.com/cnnic/rdap/wiki/Develop%20Guide)
