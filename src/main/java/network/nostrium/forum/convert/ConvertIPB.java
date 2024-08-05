/*
 *  Convert an online IP.B forum
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.forum.convert;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.nostrium.forum.structures.Forum;
import network.nostrium.forum.structures.ForumGroup;
import network.nostrium.forum.structures.ForumNote;
import network.nostrium.forum.structures.ForumTopic;
import network.nostrium.forum.structures.ForumUser;
import network.nostrium.forum.structures.ManageTopics;
import network.nostrium.forum.structures.ManageUsers;
import network.nostrium.main.Folder;
import network.nostrium.utils.ImageFunctions;
import network.nostrium.utils.Log;
import network.nostrium.utils.TextFunctions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Date: 2023-02-09
 * Place: Germany
 *
 * @author brito
 */
public class ConvertIPB {

    Forum forum;

    String targetURL,
            forumId;

    final boolean canStart;

    public ConvertIPB(String forumId, String targetURL) {

        if (StringUtils.isAllBlank(targetURL)) {
            canStart = false;
            Log.write("Invalid target URL");
            return;
        }

        if (StringUtils.isAllBlank(forumId)) {
            canStart = false;
            Log.write("Invalid forumId");
            return;
        }

        // remove slash at the end
        if (targetURL.endsWith("/")) {
            targetURL = targetURL.substring(0, targetURL.length() - 1);
        }

        // remove index.php
        String anchor = "/index.php";
        if (targetURL.contains(anchor)) {
            targetURL = targetURL.substring(0,
                    targetURL.length() - anchor.length());
        }

        this.targetURL = targetURL;
        this.forumId = forumId;

        // create the forum
        forum = new Forum();
        forum.setId(this.forumId);

        canStart = true;
    }

    public void start() {
        if (canStart == false) {
            Log.write("Unable to start converting");
            return;
        }

        try {
            // get the main page
            Document doc = Jsoup.connect(targetURL).get();

            if (doc != null) {
                Log.write("Target URL exists: " + targetURL);
            }

            // get the categories
            getListOfAvailableForumGroups(doc);

            if (forum.getForumGroups().isEmpty()) {
                Log.write("No groups to extract posts");
                return;
            }

            // create a folder for each forum group
            forum.createFolders();

            // start extracting posts
            extractPostsFromForumGroups();

        } catch (IOException ex) {
            Logger.getLogger(ConvertIPB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Download all categories based on the information from the frontpage
     *
     * @param doc
     */
    @SuppressWarnings("SizeReplaceableByIsEmpty")
    private void getListOfAvailableForumGroups(Document doc) {
        String anchor = "col_c_forum";
        Elements elements = doc.getElementsByClass(anchor);
        if (elements == null) {
            return;
        }
        // parse each category
        for (Element element : elements) {
            // make sure it is a category
            Elements h4 = element.select("h4");
            if (h4 == null || h4.isEmpty()) {
                continue;
            }

            ForumGroup forumGroup = new ForumGroup();
            Element link = h4.select("a").first();
            if (link == null) {
                continue;
            }
            // get the id from the link
            String id = link.attr("href");
            int i = id.lastIndexOf("=");
            id = id.substring(i + 1);

            forumGroup.setTitle(h4.text());
            forumGroup.setId(id);

            System.out.println(forumGroup.print());
            forum.addForumGroup(forumGroup);

            // are there subcategories?
            Elements sub = element.select("ol");
            if (sub != null && sub.size() > 0) {
                addSubGroups(forumGroup, sub);
            }

            //System.out.println("---------------------");
        }

    }

    /**
     * Add the sub categories found on a frontpage
     *
     * @param group
     * @param sub
     */
    private void addSubGroups(ForumGroup group, Elements sub) {
        Elements lis = sub.select("li");
        for (Element li : lis) {
            Element link = li.selectFirst("a");
            if (link == null) {
                continue;
            }
            String title = link.text();
            String id = link.attr("href");
            int i = id.lastIndexOf("=");
            id = id.substring(i + 1);
            ForumGroup subForum = new ForumGroup();
            subForum.setId(id);
            subForum.setTitle(title);
            subForum.setForumParent(group);
            forum.addForumGroup(subForum);
            System.out.println("\t" + subForum.print());
        }
    }

    /**
     * Goes online to get the respective conversations
     */
    @SuppressWarnings("null")
    private void extractPostsFromForumGroups() {
        for (ForumGroup forumGroup : forum.getForumGroups()) {
            extractPostsFromForumGroup(forumGroup);
        }
    }

    @SuppressWarnings("null")
    public void extractPostsFromForumGroup(ForumGroup forumGroup) {
        File folder = forumGroup.getFolder();

        if (folder.exists() == false) {
            try {
                FileUtils.forceMkdir(folder);
            } catch (IOException ex) {
                Logger.getLogger(ConvertIPB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // create the URL that is default for IP.B
        String url = targetURL + "/index.php?showforum=" + forumGroup.getId();

        try {

            Document doc = Jsoup.connect(url).get();

            // when there are no pages in IP.B, this is displayed
            Element single = doc.select("p[class*=pagination]").first();
            boolean hasMultiplePages = single == null;

            int maxPages = 1;
            // get the number of pages
            if (hasMultiplePages) {
                Elements pages = doc.getElementsByClass("pagejump");
                Element data = pages.select("a").first();
                String text = data.text();
                String anchor = "of ";
                int i = text.indexOf(anchor);
                String temp = text.substring(i);
                int x = temp.indexOf(" ");
                String value = temp.substring(anchor.length());
                maxPages = Integer.parseInt(value);
                //System.out.println("Number of pages: " + value);
            }

            // iterate all pages
            for (int currentPage = 1; currentPage < maxPages + 1; currentPage++) {
                getListOfTopicsInsidePage(forumGroup, currentPage, maxPages, url);
            }

        } catch (IOException ex) {
            Logger.getLogger(ConvertIPB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Forum getForum() {
        return forum;
    }

    public boolean isCanStart() {
        return canStart;
    }

    /**
     * Inside each page of the forum group
     *
     * @param forumGroup
     * @param currentPage
     * @param maxPages
     * @param url
     */
    private void getListOfTopicsInsidePage(
            ForumGroup forumGroup, int currentPage, int maxPages, String url) throws IOException {

        String urlPage = url + "&page=" + currentPage;

        Document doc = Jsoup.connect(urlPage).get();

        // get the box with topics
        Elements topics = doc.getElementsByClass("ipsBox");

        if (topics == null) {
            Log.write("Failed to download: " + url);
            return;
        }

        // get each topic
        Elements h4 = topics.select("h4");
        if (h4 == null || h4.isEmpty()) {
            Log.write("No topics were found: " + url);
            return;
        }

        // calculate the folder where to place it
        int folderId = maxPages - currentPage + 1;

        // go through the list and extract each topic
        for (Element topic : h4) {
            extractPostFromForumGroup(topic, forumGroup, folderId);
        }
        
        // add pinned topics (when any)
        markPinnedTopics(topics);
        
    }

    /**
     * Goes inside each HTML list of topics and gets their data
     *
     * @param h4
     * @param forumGroup
     */
    @SuppressWarnings("null")
    private void extractPostFromForumGroup(Element h4, ForumGroup forumGroup, int folderId) {
        Element link = h4.select("a[itemprop]").first();
        if (link == null) {
            return;
        }
        String id = link.attr("href");
        int i = id.lastIndexOf("=");
        id = id.substring(i + 1);

        Element titleElement = h4.select("span").first();
        String title = titleElement.text();

        // create the new forum topic
        ForumTopic forumTopic = new ForumTopic();
        forumTopic.setId(id);
        forumTopic.setTitle(title);
        // extract the content from the topic
        extractContentFromTopic(forumTopic, targetURL);

        // add the new id inside the group
        forumGroup.getForumTopics().add(id);
        // save the topic to disk
        forumGroup.saveTopicToDisk(forumTopic, folderId);

        //System.out.println(id + " -> " + title);
    }

    /**
     * Go inside a topic and read it
     *
     * @param forumTopic
     * @param forumURL
     */
    @SuppressWarnings("null")
    public static void extractContentFromTopic(
            ForumTopic forumTopic, String forumURL) {

        // example https://reboot.pro/index.php?showtopic=35
        String urlPage = forumURL + "/index.php?showtopic=" + forumTopic.getId();

        Document doc = null;

        try {
            doc = Jsoup.connect(urlPage).get();
        } catch (IOException ex) {
            Logger.getLogger(ConvertIPB.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (doc == null) {
            return;
        }

        // assume a single page conversation
        int maxPages = 1;

        // when there are more pages, they include this specific class name
        Element pageCount = doc.getElementsByClass("ipsList_inline left pages").first();
        if (pageCount != null) {
            Element link = pageCount.selectFirst("a[href*=#]");
            String anchor = " of ";
            int i = link.text().indexOf(anchor);
            String temp = link.text().substring(i + anchor.length());
            maxPages = Integer.parseInt(temp);
        }

        // for muliple pages this happens
        // http://reboot.pro/index.php?showtopic=22277&page=2
        // extract the notes on the first page
        extractNotesFromPage(forumTopic, doc);

        // go for the other pages
        if (maxPages > 1) {
            for (int i = 1; i < maxPages + 1; i++) {
                String nextPageURL = urlPage + "&page=" + i;
                Document nextPageDoc = null;
                try {
                    nextPageDoc = Jsoup.connect(nextPageURL).get();

                } catch (IOException ex) {
                    Logger.getLogger(ConvertIPB.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (nextPageDoc == null) {
                    continue;
                }
                Log.write("Crawling page: " + nextPageURL);
                extractNotesFromPage(forumTopic, nextPageDoc);
            }
        }

    }

    /**
     * Convert all replies inside a topic to notes
     *
     * @param forumTopic
     * @param doc
     */
    private static void extractNotesFromPage(ForumTopic forumTopic, Document doc) {

        Elements noteElements = doc.getElementsByClass("post_block");
        for (Element noteElement : noteElements) {
            extractNoteFromPage(noteElement, forumTopic);
        }
    }

    /**
     * Inside a conversation page, process a specific note
     *
     * @param noteElement
     * @param forumTopic
     */
    @SuppressWarnings("null")
    private static void extractNoteFromPage(Element noteElement, ForumTopic forumTopic) {
        // create the note itself
        ForumNote note = new ForumNote();

        // get id
        String id = noteElement.attr("id").replace("post_id_", "");
        note.setId(id);
        
        // ignore cases like "best answer"
        if(id == null || isEmpty(id)){
            return;
        }
        
        // get timestamp
        Element timeElement = 
                noteElement.getElementsByTag("abbr").first();
            //noteElement.getElementsByClass("published").first();
        String timestamp = timeElement.attr("title");
        note.setTimestamp(timestamp);

        // get the content as raw HTML
        Element contentElement = noteElement.getElementsByClass("entry-content").first();
        String content = contentElement.html();
        // convert to markdown
        String markdown = TextFunctions.convertHtmlToMarkdown(content);
        note.setContent(markdown);

        // get the author
        Element contentAuthor = noteElement.getElementsByClass("author vcard").first();
        String authorId;
        // text from deleted author
        if (contentAuthor == null) {
            Element contentAuthor1 = noteElement.getElementsByClass("author_info").first();
            Element contentAuthor2 = contentAuthor1.getElementsByClass("hide").first();
            authorId = contentAuthor2.text();
        } else {
            authorId = contentAuthor.text();
        }
        note.setAuthor(authorId);

        // add the note to the conversation topic
        forumTopic.addNote(note);

        // add the user to our database if it hasn't yet
        if (ManageUsers.hasUser(authorId) == false) {
            addUser(noteElement, authorId);
        }

    }

    /**
     * Adds a new user on the database when it is not yet added
     *
     * @param contentAuthor
     * @param authorId
     */
    @SuppressWarnings("null")
    private static void addUser(Element e, String authorId) {
        // create the new user
        ForumUser user = new ForumUser();

        Element title = e.getElementsByClass("member_title").first();

        if (title != null) {
            String memberLevel = title.text();
            user.setMemberLevel(memberLevel);
        }

        Element avatar = e.getElementsByClass("ipsUserPhoto").first();
        String avatarURL = avatar.attr("src");
        // clean up the URL (not sure if applicable to all cases)
        if (avatarURL.contains("?")) {
            int i = avatarURL.indexOf("?");
            avatarURL = avatarURL.substring(0, i);
        }

        Element titleGroup = e.getElementsByClass("group_title").first();
        String memberGroup = titleGroup.text();

        user.setId(authorId);
        user.setMemberGroup(memberGroup);

        // adjust the user profile to be available locally
        downloadUserProfileImage(user, avatarURL);

        // add the custom fields
        Element customFieldTag = e.getElementsByClass("custom_fields").first();
        if (customFieldTag != null) {
            Elements customFields = customFieldTag.getElementsByTag("li");
            for (Element customField : customFields) {
                Element keyEl = customField.getElementsByClass("ft").first();
                String key = keyEl.text();
                if (isNotEmpty(key) && key.endsWith(":")) {
                    key = key.substring(0, key.length() - 1);
                }
                Element valueEl = customField.getElementsByClass("fc").first();
                String value = valueEl.text();
                user.getCustomFields().put(key, value);
            }
        }

        // save the user to disk
        ManageUsers.addUser(user);
    }

    /**
     * Download the user profile image from a remote server to become available
     * locally.
     *
     * @param user
     * @param avatarURL
     */
    @SuppressWarnings("ConvertToTryWithResources")
    private static void downloadUserProfileImage(ForumUser user, String avatarURL) {
        // use the default image location
        user.setImageURL(avatarURL);

        try (BufferedInputStream inputStream
                = new BufferedInputStream(new URL(avatarURL).openStream())) {
            // read all the bytes
            byte[] data = inputStream.readAllBytes();

            // Generate the SHA-1 hash for the data
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hash = messageDigest.digest(data);

            // Convert the hash to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02X", b));
            }

            // Use the hexadecimal string as the file name
            String fileName = hexString.toString().toLowerCase();

            // try to guess the file extension based on magic signature
            String fileExtension = ImageFunctions.getExtension(data);
            if (fileExtension != null) {
                fileName += fileExtension;
            }

            // get the folder
            File file = new File(Folder.getFolderProfileImages(), fileName);
            // write to disk
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();

            

            // save the result
            String webName = Folder.makeWebCompatible(file);
            user.setImageURL(webName);
            Log.write("Wrote profile image: " + webName);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Finds the information about pinned topics
     * and marks them on the database as such
     * @param topics 
     */
    private void markPinnedTopics(Elements topics) {
        Elements spans = topics.select("span");
        for(Element span : spans){
            if(span.text().equalsIgnoreCase("pinned") == false){
                continue;
            }
            // we have a match, continue
            Element h4 = span.nextElementSibling();
            Element link = h4.select("a").first();
            String id = link.attr("id");
            String anchor = "tid-link-";
            id = id.substring(anchor.length());
            ForumTopic topic = ManageTopics.getForumTopic(id);
            if(topic == null){
                continue;
            }
            // make changes
            topic.setIsPinned(true);
            ManageTopics.writeTopic(topic);
            Log.write("Pinned topic: " + topic.getId());
            System.gc();
        }
    }

}
