/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package network.nostrium.requests;

import java.net.URI;
import network.nostrium.utils.Log;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.fork.FkRegex;
import org.takes.misc.Href;
import org.takes.rq.RqHref;
import org.takes.rs.RsHtml;

/**
 * Date: 2023-02-07
 * Place: Germany
 * @author brito
 */
public class RequestIndex implements Take{

    @Override
    public Response act(Request req) throws Exception {
        Href href = new RqHref.Base(req).href();
        Iterable<String> values = href.param("key");
        String bare = href.bare();
        String path = href.path();
        
        return new RsHtml(""
                + "<html>"
                + "Requesting: " + path
                + "\r\t "
                + "Full URL: " + bare
                + "</html>"
        );
    }

}
