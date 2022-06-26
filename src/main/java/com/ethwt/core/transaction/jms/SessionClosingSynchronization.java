/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2016, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.ethwt.core.transaction.jms;


import javax.transaction.Synchronization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Synchronization to close JMS session at the end of the transaction.
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class SessionClosingSynchronization implements Synchronization {
	
	private static Logger log = LoggerFactory.getLogger(SessionClosingSynchronization.class);

    private final AutoCloseable session;

    /**
     * @param session session to be closed.
     */
    public SessionClosingSynchronization(AutoCloseable session) {
        this.session = session;
    }

    @Override
    public void beforeCompletion() {
        // Nothing to do
    }

    /**
     * Close the session no matter what the status of the transaction is.
     *
     * @param status the status of the completed transaction
     */
    @Override
    public void afterCompletion(int status) {
        if (log.isTraceEnabled()) {
            log.trace("Closing session " + session);
        }

        try {
            session.close();
        } catch (Exception e) {
            log.warn("Failed to close JMS session {0}",session.toString(), e);
        }
    }

}
