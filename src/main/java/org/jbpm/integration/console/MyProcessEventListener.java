package org.jbpm.integration.console;

import org.drools.event.process.ProcessCompletedEvent;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.process.ProcessNodeLeftEvent;
import org.drools.event.process.ProcessNodeTriggeredEvent;
import org.drools.event.process.ProcessStartedEvent;
import org.drools.event.process.ProcessVariableChangedEvent;

public class MyProcessEventListener implements ProcessEventListener {

    public void afterNodeLeft(ProcessNodeLeftEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void afterNodeTriggered(ProcessNodeTriggeredEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void afterProcessCompleted(ProcessCompletedEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void afterProcessStarted(ProcessStartedEvent arg0) {
        System.out.println("MyProcessEventListener.afterProcessStarted() : event = " + arg0);
    }

    public void afterVariableChanged(ProcessVariableChangedEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void beforeNodeLeft(ProcessNodeLeftEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void beforeNodeTriggered(ProcessNodeTriggeredEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void beforeProcessCompleted(ProcessCompletedEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void beforeProcessStarted(ProcessStartedEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void beforeVariableChanged(ProcessVariableChangedEvent arg0) {
        // TODO Auto-generated method stub

    }

}
