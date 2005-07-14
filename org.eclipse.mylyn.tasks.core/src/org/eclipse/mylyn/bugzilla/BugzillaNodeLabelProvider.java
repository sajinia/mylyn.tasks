/*******************************************************************************
 * Copyright (c) 2004 - 2005 University Of British Columbia and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     University Of British Columbia - initial API and implementation
 *******************************************************************************/
/*
 * Created on Apr 18, 2005
  */
package org.eclipse.mylar.bugzilla;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.mylar.bugzilla.core.BugReport;
import org.eclipse.mylar.bugzilla.ui.tasks.BugzillaReportNode;
import org.eclipse.mylar.core.IMylarContextNode;
import org.eclipse.mylar.ui.MylarImages;
import org.eclipse.swt.graphics.Image;

/**
 * @author Mik Kersten
 */
public class BugzillaNodeLabelProvider implements ILabelProvider {

    public Image getImage(Object element) {
        return MylarImages.getImage(MylarImages.BUG); 
    }

    /**
     * TODO: slow?
     */
    public String getText(Object element) {
        IMylarContextNode node = (IMylarContextNode)element;
        
        // try to get from the cache before downloading
        Object report;
    	BugzillaReportNode reportNode = MylarBugzillaPlugin.getReferenceProvider().getCached(node.getElementHandle());
    	BugReport cachedReport = MylarBugzillaPlugin.getDefault().getStructureBridge().getCached(node.getElementHandle());
    	if(reportNode != null && cachedReport == null){
    		report = reportNode;
    	} else{
    		report = MylarBugzillaPlugin.getDefault().getStructureBridge().getObjectForHandle(node.getElementHandle());
    	}
        return MylarBugzillaPlugin.getDefault().getStructureBridge().getName(report);
    }

    public void addListener(ILabelProviderListener listener) {
    	// don't need to worry about listeners
    }

    public void dispose() {
    	// don't care about dispose
    }

    public boolean isLabelProperty(Object element, String property) {
        return false; 
    }

    public void removeListener(ILabelProviderListener listener) {
    	// don't need to worry about listeners 
    }
} 
