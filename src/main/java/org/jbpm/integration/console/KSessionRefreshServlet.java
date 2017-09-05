package org.jbpm.integration.console;

import java.lang.reflect.Field;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.drools.definition.process.Process;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class KSessionRefreshServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(KSessionRefreshServlet.class);

    @Override
    public void init() throws ServletException {
        logger.info("KSession refresh servlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        String action = req.getParameter("action");
        log("Servlet called with action: " + action, writer, "h3");
        try {
            if ("refresh".equals(action)) {

                // dispose session and agent
                StatefulKnowledgeSessionUtil.getStatefulKnowledgeSession().dispose();
                StatefulKnowledgeSessionUtil.getKagent().dispose();

                // stop existing scanner/notifier to avoid threak leak
                ResourceFactory.getResourceChangeScannerService().stop();
                ResourceFactory.getResourceChangeNotifierService().stop();

                // make private SessionHolder accessible by reflection
                Class<?> sessionHolderClass = StatefulKnowledgeSessionUtil.class.getDeclaredClasses()[0];
                Field statefulKnowledgeSessionField = sessionHolderClass.getField("statefulKnowledgeSession");
                statefulKnowledgeSessionField.setAccessible(true);

                // re-initialize the SessionHolder.statefulKnowledgeSession
                // property
                statefulKnowledgeSessionField.set(StatefulKnowledgeSessionUtil.class, StatefulKnowledgeSessionUtil.initializeStatefulKnowledgeSession());
                log("StatefulKnowledgeSession in SessionHolder refreshed", writer, "p");

                // display the number of processes
                List<Process> processes = CommandDelegate.getProcesses();
                log("Number of processes: " + (processes != null ? processes.size() : "null"), writer, "p");
                for (Process p : processes) {
                    log("         " + p.getName(), writer, "li");
                }
                log("Session " + StatefulKnowledgeSessionUtil.getStatefulKnowledgeSession().getId() + " successfully refreshed", writer, "p");
            } else if ("add-event-listener".equals(action)) {
                StatefulKnowledgeSession ksession = StatefulKnowledgeSessionUtil.getStatefulKnowledgeSession();
                ksession.addEventListener(new MyProcessEventListener());
                logger.info("MyProcessEventListener is added");
            } else if ("stop-scanner".equals(action)) {
                ResourceFactory.getResourceChangeScannerService().stop();
                logger.info("ResourceChangeScanner stoppped");
            } else if ("scan".equals(action)) {
                ResourceFactory.getResourceChangeScannerService().scan();
                logger.info("ResourceChangeScanner manually scan");
            } else {
                log("Unknown action " + action + "! To force a session re-initialization, call the servlet with ?action=refresh", writer, "p");
            }
        } catch (Exception ex) {
            logger.error(ex);
            log("ERROR: " + ex.getMessage(), writer, "b");
        } finally {
            writer.close();
        }
    }

    // helper method to print to log and to http response
    private void log(String msg, PrintWriter writer, String htmlDecoration) {
        logger.info(msg);
        writer.write("<" + htmlDecoration + ">" + msg + "</" + htmlDecoration + ">");
    }
}
