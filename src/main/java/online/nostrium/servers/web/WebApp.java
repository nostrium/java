/*
 * Defines a web application
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.web;

import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-10-08
 * @location: Germany
 */
public abstract class WebApp extends App{

    public WebApp(Session session) {
        super(session);
    }

    // define the URL to where it starts. e.g. <user>/blog
    public abstract String virtualPath(); 
}
