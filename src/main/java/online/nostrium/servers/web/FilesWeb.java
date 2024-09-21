/*
 * Serve files to web visitors
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.web;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import static online.nostrium.servers.web.ServerWeb.sendError;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import org.apache.commons.io.FileUtils;

/**
 * @author Brito
 * @date: 2024-09-21
 * @location: Germany
 */
public class FilesWeb {
    
    
    // Add inline CSS to style the HTML content
public static String css1 = "<style>" +
    "body { " +
        "background-color: black; " + 
        "color: #00FF00; " + // Neon green text
        "font-family: 'Courier New', monospace; " + // Pixelated font
        "text-align: center; " + // Centered text for a retro feel
        "padding: 50px; " +
    "}" +
    "h1 { " +
        "color: #FF1493; " + // Neon pink for headings
        "text-shadow: 0 0 10px #FF1493, 0 0 20px #FF1493, 0 0 30px #FF1493;" + // Neon glow effect
        "font-size: 48px; " +
    "}" +
    "p { " +
        "color: #00FFFF; " + // Neon cyan for paragraphs
        "font-size: 20px; " +
        "text-shadow: 0 0 10px #00FFFF, 0 0 20px #00FFFF, 0 0 30px #00FFFF;" + // Neon glow effect for paragraphs
    "}" +
    "a { " +
        "color: #FFFF00; " + // Bright yellow for links
        "text-decoration: none; " +
        "font-weight: bold; " +
    "}" +
    "a:hover { " +
        "color: #FF4500; " + // Orange on hover
    "}" +
    "</style>";

public static  // Add inline CSS to style the HTML content
            String css2 = "<style>"
                    + "body { font-family: Arial, sans-serif; }"
                    + "h1 { color: blue; }"
                    + "p { color: gray; }"
                    + "</style>";
    

    public static void sendMarkDown(File file, ChannelHandlerContext ctx) throws IOException {
        if (file.exists() == false || file.isDirectory() || file.length() == 0) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }
        
        String filenameCSS = file.getName().replace(".md", ".css");
        String css = css1;
        File fileCSS = new File(file.getParent(), filenameCSS);
        if(fileCSS.exists() && fileCSS.isFile() && fileCSS.length() > 0){
            css = FileUtils.readFileToString(fileCSS, "UTF-8");
        }
        
        // convert to markdown
        String text = convertMarkdownToHtml(file, css);
        if (text == null || text.length() == 0) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }
        // send the text
        sendText(text, ctx, ".html");
    }

    public static void sendText(String text, ChannelHandlerContext ctx, String extension) {
        // Convert the String to bytes
        byte[] contentBytes = text.getBytes(StandardCharsets.UTF_8);
        long contentLength = contentBytes.length;

        // Prepare the HTTP response
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpUtil.setContentLength(response, contentLength);

        // Set the content type as plain text (or adjust as needed)
        String contentType = FilesWeb.getMimeType(extension);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);

        // Write the initial response (status line and headers)
        ctx.write(response);

        // Write the content
        ctx.write(Unpooled.copiedBuffer(contentBytes), ctx.newProgressivePromise());

        // Write the end marker (empty last content)
        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

        // Close the connection after the content is written out
        lastContentFuture.addListener(ChannelFutureListener.CLOSE);
    }

    public static void sendFile(File file, ChannelHandlerContext ctx) throws IOException {
        // Check if the file exists and is readable
        if (file.isHidden() || !file.exists() || !file.isFile()) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long fileLength = raf.length();

            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            HttpUtil.setContentLength(response, fileLength);

            // Determine the content type
            String contentType = FilesWeb.getMimeType(file.getName());
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);

            // Write the initial line and the header.
            ctx.write(response);

            // Write the content.
            ctx.write(new ChunkedFile(raf, 0, fileLength, 8192), ctx.newProgressivePromise());

            // Write the end marker.
            ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

            // Close the connection once the whole content is written out.
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static String getMimeType(String uri) {
        String contentType = "text/html; charset=UTF-8";
        if (uri.endsWith(".js")) {
            contentType = "application/javascript";
        } else if (uri.endsWith(".css")) {
            contentType = "text/css";
        } else if (uri.endsWith(".txt")) {
            contentType = "text/plain";
        } else if (uri.endsWith(".json")) {
            contentType = "application/json";
        } else if (uri.endsWith(".png")) {
            contentType = "image/png";
        } else if (uri.endsWith(".jpg") || uri.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (uri.endsWith(".gif")) {
            contentType = "image/gif";
        }
        return contentType;
    }

    public static void sendFileFromUser(String uri, ChannelHandlerContext ctx) throws IOException {
        // https://nostrium.online/brito/
        String[] data = uri.split("/");
        // the position 1 should contain the username
        String username = data[1];
        User user = UserUtils.getUserByUsername(username);
        if (user == null) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
        }
        // the user exists
        @SuppressWarnings("null")
        File folder = new File(user.getFolder(true), "www");
        if (folder.exists() == false) {
            FileUtils.forceMkdirParent(folder);
        }
        // folder needs to really exist
        if (folder.exists() == false) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }

        // looking for the index pages
        if (data.length == 2) {
            launchIndexDefault(folder, ctx);
            return;
        }

        String path = "";
        for (int i = 3; i <= data.length; i++) {
            int index = i - 1;
            path += "/" + data[index];
        }
        path = path.substring(1);
        File file = new File(folder, path);

        // is this a directory?
        if (file.isDirectory()) {
            launchIndexDefault(file, ctx);
            return;
        }

        // it is a file
        if (file.getName().endsWith(".md")) {
            // convert Markdown files
            sendMarkDown(file, ctx);
        } else {
            sendFile(file, ctx);
        }
    }

   

    public static String convertMarkdownToHtml(File markdownFile, String css) {
        try {
            // Read the content of the markdown file into a string
            String markdownContent = new String(Files.readAllBytes(markdownFile.toPath()));

            // Initialize the Flexmark parser and renderer
            Parser parser = Parser.builder().build();
            HtmlRenderer renderer = HtmlRenderer.builder().build();

            // Parse the markdown to a Node object
            Node document = parser.parse(markdownContent);
            // Render the Node object to HTML
            String htmlContent = renderer.render(document);

            // Combine the CSS with the HTML content
            return "<html><head>" + css + "</head><body>" + htmlContent + "</body></html>";
        } catch (IOException e) {
            // Return the original markdown content if an exception occurs
            try {
                return new String(Files.readAllBytes(markdownFile.toPath()));
            } catch (IOException ex) {
                return ""; // Return an empty string if both attempts fail
            }
        }
    }

    private static void launchIndexDefault(File folder, ChannelHandlerContext ctx) throws IOException {
        File file = new File(folder, "index.html");
        File fileMarkdown = new File(folder, "index.md");

        if (file.exists()) {
            sendFile(file, ctx);
            return;
        }
        if (fileMarkdown.exists()) {
            sendMarkDown(fileMarkdown, ctx);
            return;
        }

        // show the files inside the folder
    }

}
