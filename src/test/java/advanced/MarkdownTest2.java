package advanced;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import online.nostrium.archive.Markdown2;
import online.nostrium.main.Folder;

public class MarkdownTest2 {

    private Markdown2 markdown;
    
    File folderTemp = Folder.getFolderTemp();

    @BeforeEach
    public void setUp() throws IOException {
        // Create a sample Markdown file for testing blog posts and replies
        String markdownContent = """
                # My First Blog Post

                + author: John Doe
                + date: 2024-09-25
                + description: This is my first blog post on interesting topics.
                + tags: blog, firstPost
                + content: This is the content of the blog post. It discusses various interesting topics in detail.

                ----
                                 
                ## 2024-09-26
                + author: Alice
                + likes: 0
                + dislikes: 0
                + tags: reply, comment
                + content: This is the first reply to the blog post.

                ----
                                 
                ## 2024-09-28
                + author: Charlie
                + likes: 0
                + dislikes: 0
                + tags: feedback
                + content: This is the third reply.
                """;

        Files.writeString(Paths.get(folderTemp.getAbsolutePath(), "testMarkdown.md"), markdownContent);
        markdown = new Markdown2(new File(folderTemp, "testMarkdown.md"));
    }

    @Test
    public void testTopicParsing() {
        Markdown2.Topic topic = markdown.getTopic();
        assertNotNull(topic);
        assertEquals("My First Blog Post", topic.getTitle());
        assertEquals("John Doe", topic.getAuthor());
        assertEquals("2024-09-25", topic.getDate());
        assertEquals("This is my first blog post on interesting topics.", topic.getDescription());
        assertEquals(List.of("blog", "firstPost"), topic.getTags()); // Check tags
        assertEquals("This is the content of the blog post. It discusses various interesting topics in detail.", topic.getContent());
    }

    @Test
    public void testMessagesParsing() {
        Markdown2.Topic topic = markdown.getTopic();
        assertNotNull(topic);
        List<Markdown2.Message> messages = topic.getMessages();
        assertNotNull(messages);
        assertEquals(2, messages.size()); // Two replies

        // Check first reply
        Markdown2.Message firstReply = messages.get(0);
        assertEquals("Alice", firstReply.getAuthor());
        assertEquals("2024-09-26", firstReply.getDate());
        assertEquals("This is the first reply to the blog post.", firstReply.getContent());
        assertEquals(List.of("reply", "comment"), firstReply.getTags()); // Check tags

        // Check second reply
        Markdown2.Message secondReply = messages.get(1);
        assertEquals("Charlie", secondReply.getAuthor());
        assertEquals("2024-09-28", secondReply.getDate());
        assertEquals("This is the third reply.", secondReply.getContent());
        assertEquals(List.of("feedback"), secondReply.getTags()); // Check tags
    }

    @Test
    public void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get(folderTemp.getAbsolutePath(), "testMarkdown.md"));
    }
}
