package org.junit.junit4copy.runner.notification;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

/**
 * Created by qinjw on 2016/7/14.
 */
public class Failure implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Description fDescription;
    private final Throwable fThrownExcetion;

    public Failure(Description description, Throwable fThrownExcetion) {
        this.fDescription = description;
        this.fThrownExcetion = fThrownExcetion;
    }

    public String getTestHeader() {
        return fDescription.getDisplayName();
    }

    public Description getDescription() {
        return fDescription;
    }

    public String toString() {
        return getTestHeader() + ": " + fThrownExcetion.getMessage();
    }

    public String getTrace() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        getException().printStackTrace(writer);
        return stringWriter.toString();
    }

    public String getMessage() {
        return  getException().getMessage();
    }
}
