# jBPM 5 ksession refresh servlet for BRMS 5.3.1

This is a WAR file which allows to re-initialize the ksession instance in StatefulKnowledgeSessionUtil.SessionHolder. 
The reasons for creating this servlet is outlined in Red Hat KBase https://access.redhat.com/solutions/3111691.

- mvn clean package
- Copy target/jbpm5-refresh-ksession-servlet-<VERSION>.jar to $BRMS_HOME/server/$PROFILE/deploy/deploy/business-central-server.war/WEB-INF/lib/
- Edit $BRMS_HOME/server/$PROFILE/deploy/business-central-server.war/WEB-INF/web.xml, add:

~~~
    <!-- ADD SERVLET DEFINITION AND MAPPING -->
    <servlet>
        <servlet-name>KSessionRefreshServlet</servlet-name>
        <servlet-class>org.jbpm.integration.console.KSessionRefreshServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>KSessionRefreshServlet</servlet-name>
        <url-pattern>/ksession-refresh</url-pattern>
    </servlet-mapping>
    
    <!-- ADD URL TO RESOURCE CONSTRAINT TO PREVENT UNAUTHORIZED ACCESS -->
  <security-constraint>
    <web-resource-collection>
      <web-resource-name>Resteasy</web-resource-name>
      <!-- ADD AFTER EXISTING URL PATTERNS: -->
      <url-pattern>/ksession-refresh/*</url-pattern>
    </web-resource-collection>
~~~

- During the start of the server, the servlet emits a log to inform about it's initialization, but does not perform further actions:
2017-07-06 10:42:08,429 INFO  [org.jbpm.integration.console.KSessionRefreshServlet] (main) KSession refresh servlet initialized

To invoke the servlet, issue a GET request to the following URL:
http://$HOST:$PORT/business-central-server/ksession-refresh?action=refresh

This will result in the following logging:

10:11:50,977 INFO  [KSessionRefreshServlet] Servlet called with action: refresh
...(logging output from ksession re-initialization)...
10:11:51,853 INFO  [KSessionRefreshServlet] StatefulKnowledgeSession in SessionHolder refreshed
10:11:51,904 INFO  [KSessionRefreshServlet] Number of processes: 3
10:11:51,904 INFO  [KSessionRefreshServlet]          second-process
10:11:51,904 INFO  [KSessionRefreshServlet]          test-process
10:11:51,904 INFO  [KSessionRefreshServlet]          third-process
10:11:51,905 INFO  [KSessionRefreshServlet] Session 1 successfully refreshed

============

To build this project, you need to configure an internal repository in your settings.xml

~~~
        <repository>
          <id>internal-repository-brms5</id>
          <name>internal-repository-brms5</name>
          <url>http://download.devel.redhat.com/brewroot/repos/soa-brms-5.3-updates-build/latest/maven/</url>
          <releases>
            <enabled>true</enabled>
            <updatePolicy>never</updatePolicy>
          </releases>
          <snapshots>
            <enabled>true</enabled>
            <updatePolicy>daily</updatePolicy>
          </snapshots>
        </repository>
~~~
