/*******************************************************************************
 * Copyright (c) 2004 - 2006 University Of British Columbia and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     University Of British Columbia - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylar.tasks.tests.connector;

import org.eclipse.mylar.tasks.core.AbstractRepositoryTask;

/**
 * @author Mik Kersten
 */
public class MockRepositoryTask extends AbstractRepositoryTask {
	
	private String ownerId;
	
	public MockRepositoryTask(String handle) {
		super(handle, "label for " + handle, true);
	}

	@Override
	public String getRepositoryKind() {
		return "mock";
	}

	public void setOwner(String ownerId) {
		this.ownerId = ownerId;
	}
	
	@Override
	public String getOwner() {
		if(ownerId == null) {
			return super.getOwner();
		} else {
			return ownerId;
		}
	}

}
