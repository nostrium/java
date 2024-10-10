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
import online.nostrium.logs.Log;
import online.nostrium.folder.FolderUtils;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.web.ServerWeb.sendError;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import static online.nostrium.user.UserUtils.isVirtualFolder;
import online.nostrium.utils.FileFunctions;
import online.nostrium.utils.TextFunctions;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;

/**
 * @author Brito
 * @date: 2024-09-21
 * @location: Germany
 */
public class FilesWeb {

    static String css1 = "<style>"
            + "body { "
            + "background-color: black; " // Dark background for retro effect
            + "color: white; " // White text for normal body text
            + "font-family: 'Courier New', monospace; " // Retro pixelated font
            + "text-align: left; " // Left-align the text
            + "padding: 20px; "
            + "}"
            + "h1 { "
            + "color: #00FF00; " // Neon green for headers
            + "text-shadow: 0 0 1px #00FF00, 0 0 2px #00FF00; " // Tiny, subtle glow for the heading
            + "font-size: 36px; " // Adjust heading size
            + "}"
            + "h2 { "
            + "color: #00FF00; " // Same neon green for subheadings
            + "text-shadow: 0 0 1px #00FF00, 0 0 2px #00FF00; " // Tiny, subtle glow for subheadings
            + "font-size: 28px; "
            + "}"
            + "p { "
            + "color: white; " // White color for normal text
            + "font-size: 18px; " // Normal font size for paragraphs
            + "}"
            + "a { "
            + "color: #FFD700; " // Softer yellow for links
            + "text-decoration: none; "
            + "font-weight: bold; "
            + "}"
            + "a:hover { "
            + "color: #FF8C00; " // Softer orange on hover for links
            + "}"
            + "</style>";

    public static // Add inline CSS to style the HTML content
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
        if (fileCSS.exists() && fileCSS.isFile() && fileCSS.length() > 0) {
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
        Tika tika = new Tika();
        return tika.detect(uri);
    }

    public static void sendFileFromUser(String uri, ChannelHandlerContext ctx) throws IOException {
        // https://nostrium.online/brito/
        String[] data = uri.split("/");

        if (data.length < 2) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            Log.write("WWW", TerminalCode.NOT_FOUND, "URI not understood", uri);
            return;
        }
        // the position 1 should contain the username
        String username = data[1];
        User user = UserUtils.getUserByUsername(username);
        if (user == null) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            Log.write("WWW", TerminalCode.NOT_FOUND, "User not found", username);
            return;
        }
        // the user exists, there needs to exist a WWW folder (called public)
        File folder = new File(user.getFolder(true), FolderUtils.nameFolderWWW);
        if (folder.exists() == false) {
            FileUtils.forceMkdir(folder);
        }
        // public folder needs to really exist
        if (folder.exists() == false) {
            Log.write(TerminalCode.FAIL, "Unable to create WWW folder", folder.getPath());
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            return;
        }

        // no arguments? looking for the index pages
        if (data.length == 2) {
            launchIndexDefault(folder, ctx, uri, user);
            return;
        }

        
        // build the path after the username
        String path = "";
        for (int i = 3; i <= data.length; i++) {
            int index = i - 1;
            path += "/" + data[index];
        }
        path = path.substring(1);

        // check if the path is talking about an app
        sendFileFolderOrApp(folder, path, user, ctx, data, uri);
        // http://nostrium.online/brito/blog
    }
    
    
    /**
     * Deliver the requested service, the processing for this function
     * is more CPU-intensive than desired, but a first implementation
     * is needed even if not optimally programmed
     * @param folder
     * @param path
     * @param user
     * @param ctx
     * @param data
     * @param uri
     * @throws IOException 
     */
    private static void sendFileFolderOrApp(
            File folder, String path, 
            User user, ChannelHandlerContext ctx,
            String[] data, String uri) throws IOException {
        
        // try to create a file to the expected locaion
        File file = new File(folder, path);
        
        // is this a virtual directory?
        if (data.length >= 3) {
            // harcoded cases for the blog and forum sites
            if (isVirtualFolder(data[2])) {
                // what happens when we want to serve a website?
                file = new File(user.getFolder(false), path);
            }
        }

        // is this a directory?
        if (file.isDirectory()) {
            launchIndexDefault(file, ctx, uri, user);
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

    private static void launchIndexDefault(File folder,
            ChannelHandlerContext ctx, String url, User user) throws IOException {
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
        String fileList = listFilesInFolderAsHtml(folder, url, user);
        sendText(fileList, ctx, "index.html");
    }
    

    public static String listFilesInFolderAsHtml(File folder, String requestUrl, User user) {
        // Check if the input is a directory
        if (!folder.isDirectory()) {
            return "<html><body><h1>Error: Not a directory!</h1></body></html>";
        }

        // Ensure requestUrl always ends with a "/" for consistent URL construction
        if (!requestUrl.endsWith("/")) {
            requestUrl += "/";
        }

        // Start building the HTML with a retro 80's style
        String css = "<style>"
                + "body { background-color: black; color: #d3d3d3; font-family: 'Courier New', monospace; padding: 20px; }" // White-grey text
                + "desc { font-size: 12px; }" // Set normal paragraph text size to 12px
                + "h1 { color: #00FF00; text-shadow: 0 0 1px #00FF00, 0 0 2px #00FF00; font-size: 36px; }" // Reduced glow for header
                + "a { color: #00FF99; text-decoration: none; font-weight: bold; }" // Soft green for links
                + "a:hover { color: #33FF99; }" // Lighter green for hover effect
                + "ul { list-style-type: none; padding-left: 0; }"
                + "li { margin-bottom: 10px; }"
                + "</style>";

        String title = "<h1>File Listing for " + folder.getName() + "</h1>";
        if (folder.getName().equals(FolderUtils.nameFolderWWW)) {
            title = "<h1>File Listing" + "</h1>";
        }

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><head><meta charset=\"UTF-8\">").append(css).append("</head><body>");
        htmlContent.append(title);
        htmlContent.append("<ul>\n");

        // Add a ".." link to go back to the parent directory (if applicable)
        File parent = folder.getParentFile();
        if (parent != null && !parent.getName().startsWith("npub")) {
            htmlContent.append("<li><a href=\"").append(requestUrl).append("../\">..</a></li>");
        } 
        
        // test if we are on the root level
        String rootTest = requestUrl.replace("/", "");
        // list the virtual folders, but only on the root
        if(user.getUsername().equalsIgnoreCase(rootTest)){
            for (String folderName : UserUtils.virtualFolderNames) {
                File folderVirtual = new File(user.getFolder(false), folderName);
                addHTMLFolder(folderVirtual, htmlContent, requestUrl);
            }
        }

        // List files and directories
        File[] files = folder.listFiles();

        // list the existing folders
        if (files != null) {
            for (File item : files) {
                addHTMLFolder(item, htmlContent, requestUrl);
            }
        }

        // list second the files
        if (files != null) {
            for (File file : files) {
                String filename = file.getName();
                if (file.isDirectory()) {
                    continue;
                }
                String size = TextFunctions.humanReadableFileSize(file);
                String date = TextFunctions.getLastModifiedISO(file);
                // Add a clickable link for files
                htmlContent.append("\n<li><a href=\"")
                        .append(requestUrl)
                        .append(filename)
                        .append("\">")
                        .append("📄 ")
                        .append(filename)
                        .append("</a><desc> [")
                        .append(size)
                        .append(" | ")
                        .append(date)
                        .append("]</desc></li>");

            }
        }
        htmlContent.append("\n</ul></body></html>");
        return htmlContent.toString();
    }

    /**
     * Add the HTML related to a folder and count the files inside the folder
     * @param folder
     * @param htmlContent
     * @param requestUrl 
     */
    private static void addHTMLFolder(File folder,
            StringBuilder htmlContent, String requestUrl) {
        if (folder.exists() == false || folder.isFile()) {
            return;
        }
        String filename = folder.getName();
        // count the number of files inside
        long countFiles = FileFunctions.countFiles(folder);
        String fileCount = " (" + countFiles + ")";
        // Add a clickable link for directories
        htmlContent.append("\n<li><a href=\"")
                .append(requestUrl)
                .append(filename)
                .append("/\">📁 ")
                .append(filename)
                .append("</a>"
                        + fileCount
                        + "</li>");
    }


}
