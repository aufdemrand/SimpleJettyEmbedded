package net.aufdemrand.simplejetty;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class HelloWorld extends AbstractHandler {


    int counter = 0;


    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)

            throws IOException, ServletException {


        // Should probably respect this convention, but it can be adhered to later.
        // For now, just handle _everything_ as OK text/HTML, unless otherwise set.
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);


        // Have to catch all errors here or else Jetty seems to catch and hide
        // them itself.

        try {

            //
            // Get POST content (if any)
            // Note: If getting post data as bytes, do not use this method.
            //

            StringBuilder content = new StringBuilder();
            String line = request.getReader().readLine();

            while (line != null) {
                content.append(line);
                line = request.getReader().readLine();
            }


            //
            // Provide DEBUG information
            //

            System.out.println("\nNEW Request { \nHost: " + request.getRemoteHost());
            System.out.println("Target: " + target);
            System.out.println("Query: " + request.getQueryString());
            System.out.println("Session: " + request.getSession().getId());
            System.out.println("Content: " + content.toString());
            System.out.println("}\n");

            // Only increase counter if the target isn't a favicon.ico request
            if (!target.equals("/favicon.ico"))
                counter++;


            //
            // Set headers
            //
            // Content served below should never be cached because of its dynamic
            // nature. Downloads/res, as handled above, should be cached whenever possible
            // since they tend to be larger files -- at least until it presents any
            // problems (I've not come across any yet).
            //

            // Disable page caching from here out
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            // And allow cross-domain (!!!)
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, OPTIONS, DELETE");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type");



            //
            // Do stuff here
            //


            // Build HTML document with HTML object.

            HTML html = new HTML();

            // <html>
            html.open(new HTML.Container("html"));

            // <head>
            html.open(new HTML.Container("head"));

            // <style>
            html.open(new HTML.Container("style")
            .setContent(IOUtils.toString(HelloWorld.class.getResourceAsStream("/style.css"))));

            // </style> </head>
            html.close(2);

            // <body>
            html.open(new HTML.Container("body"));

            // <div class=container>
            html.open(new HTML.Container("div.container"));

            // Get the text query, if any (will check for null before outputting)
            String text_query = getQuery(request.getQueryString(), "text");

            // <span class=text> ... </span>
            html.open(new HTML.Container("span.text").setContent("Hello, world! " +
                    "This server has been hit " + counter + " times. " +
                    "Text query was: " + (text_query != null ? text_query : "none"))).close();

            // </div>
            html.close();

            // </body> </html>
            html.close(2);

            // Send the HTML to the response for output
            response.getWriter().append(html.toString());


        } catch (Exception e) {

            // Handle any errors here

            // ...
            // ...

            // Log the StackTrace
            e.printStackTrace();


        }




    }



    //
    // Get a query from the query string, just provide a query_string and query_id to be 'gotten'

    public static String getQuery(String query_string, String query_id) {
        if (query_string == null) return null;

        for (String string : query_string.split("&")) {
            String[] split = string.split("=", 2);
            if (query_id.replace("&","").equalsIgnoreCase(split[0]))
                return split[1];
        }

        return null;
    }


}
