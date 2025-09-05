package com.hyphen.sampledata;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {
    public static final String PLUGIN_ID = "com.hyphen.sampledata";
    private static Activator instance;

    @Override public void start(BundleContext context) throws Exception {
        super.start(context);
        instance = this;
    }

    @Override public void stop(BundleContext context) throws Exception {
        instance = null;
        super.stop(context);
    }

    public static Activator getDefault() { return instance; }

    public static void info(String msg) {
        getDefault().getLog().log(new Status(Status.INFO, PLUGIN_ID, msg));
    }

    public static void error(String msg, Throwable t) {
        getDefault().getLog().log(new Status(Status.ERROR, PLUGIN_ID, msg, t));
    }
}