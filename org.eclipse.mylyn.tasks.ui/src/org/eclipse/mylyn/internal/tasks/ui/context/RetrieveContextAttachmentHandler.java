/*******************************************************************************
 * Copyright (c) 2004, 2011 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.internal.tasks.ui.context;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.mylyn.internal.tasks.ui.util.AttachmentUtil;
import org.eclipse.mylyn.tasks.core.ITaskAttachment;
import org.eclipse.ui.PlatformUI;

/**
 * @author Steffen Pingel
 */
public class RetrieveContextAttachmentHandler extends AbstractTaskAttachmentCommandHandler {

	@Override
	protected void execute(ExecutionEvent event, ITaskAttachment attachment) {
		AttachmentUtil.downloadContext(attachment.getTask(), attachment, PlatformUI.getWorkbench().getProgressService());
	}

}
