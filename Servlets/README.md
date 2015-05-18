Some java servlets.

Instructions:

Once Tomcat has been downloaded, the JAVA_HOME and CATALINA_HOME environment variables need to be set. If using the BASH shell this can be done by appending to the .bashrc file the following 

export JAVA_HOME=path/to/jdk # eg /user/Downloads/jdk1.8.0_45
export CATALINA_HOME=path/to/tomcat # eg /user/Downloads/apache-tomcat-8.0.22

The standard edition jdk does not include the servlet api, however Tomcat includes the relevant jar. Add

export CLASSPATH=$CATALINA_HOME/lib/servlet-api.jar:$CLASSPATH

to the .bashrc file to add it the classpath. This will allow javac to find the needed class definitions. If using an IDE like eclipse you'll need to add the jars to the build path. 

To use the userForm servlet you'll need to have mysql installed. Also you'll need to download and place a copy of mysql-connector-java-5.1.23-bin.jar in $CATALINA_HOME/lib. Finally you'll have to create the necessary database and table. I used (and is hardcoded into the servlet) the following 
database: training
user: george
password: georgepswd
table: users
Of course the servlet can be altered.

To deploy the servlet, add the class file to $CATALINA_HOME/webapps/ROOT/WEB-INF/classes and add alter the web.xml file to the one given in this repo.

To use it add the file form.htm to $CATALINA_HOME/webapps/ROOT, start tomcat and navigate to http://localhost:8080/form.htm in a webbrowser. Put an email and password and click submit. If all goes well the resulting page will welcome you if the email and password are in the table, say no access if it is not, and give an error message if there has been a sql or classnotfound exception. 


