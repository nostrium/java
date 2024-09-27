package advanced;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeSet;
import online.nostrium.archive.Markdown;
import online.nostrium.main.Folder;

public class MarkdownTest {

    private Markdown markdown;
    
    File folderTemp = Folder.getFolderTemp();

    @BeforeEach
    public void setUp() throws IOException {
        // Create a sample Markdown file for testing blog posts and replies
        String markdownContent = """
                # My First Blog Post
                > This is my first blog post on interesting topics.
                                                                                 
                + author: John Doe
                + date: 2024-09-25
                + tags: blog, firstPost
                
                This is the content of the blog post. It discusses various interesting topics in detail.

                ----
                                 
                ## 2024-09-26
                + author: Alice
                + likes: 0
                + dislikes: 0
                + tags: reply, comment
                
                This is the first reply to the blog post.

                ----
                                 
                ## 2024-09-28
                + author: Charlie
                + likes: 0
                + dislikes: 0
                + tags: feedback
                
                This is the third reply.
                                 
                """;

        Files.writeString(Paths.get(folderTemp.getAbsolutePath(), "testMarkdown.md"), markdownContent);
        markdown = new Markdown(new File(folderTemp, "testMarkdown.md"));
    }

    @Test
    public void testBlogPostParsing() {
        Markdown.Topic topic = markdown.getTopic();
        assertNotNull(topic);
        assertEquals("My First Blog Post", topic.getTitle());
        assertEquals("John Doe", topic.getAuthor());
        assertEquals("2024-09-25", topic.getDate());
        assertEquals("This is my first blog post on interesting topics.", topic.getDescription());
        assertEquals(List.of("blog", "firstPost"), topic.getTags()); // Check tags
        assertEquals("This is the content of the blog post. It discusses various interesting topics in detail.", topic.getContent());
    }

    @Test
    public void testRepliesParsing() {
        TreeSet<Markdown.Message> messages = markdown.getTopic().getMessages();
        assertNotNull(messages);
        assertEquals(2, messages.size()); // Two replies

        // Check first reply
        Markdown.Message firstReply = messages.first();
        assertEquals("This is the first reply to the blog post.", firstReply.getContent());
        assertEquals("2024-09-26", firstReply.getDate());
        assertEquals("Alice", firstReply.getAuthor());
        assertEquals(List.of("reply", "comment"), firstReply.getTags()); // Check tags

        // Check second reply
        Markdown.Message secondReply = messages.higher(firstReply);
        assertEquals("This is the third reply.", secondReply.getContent());
        assertEquals("2024-09-28", secondReply.getDate());
        assertEquals("Charlie", secondReply.getAuthor());
        assertEquals(List.of("feedback"), secondReply.getTags()); // Check tags
    }

//    @Test
//    public void testInvalidDateHandling() {
//        // Simulate an invalid date situation by parsing a string directly
//        // Ensure that invalid date handling works; e.g., skip invalid parsing.
//        String invalidDateContent = """
//                # Invalid Date Post
//                + author: Invalid Author
//                + date: Invalid Date
//                + description: Invalid post example.
//                + tags: invalid
//                + content: Invalid content.
//                """;
//        
//        try {
//            Files.writeString(Paths.get(folderTemp.getAbsolutePath(), "invalidDateMarkdown.md"), invalidDateContent);
//            Markdown invalidMarkdown = new Markdown(new File(folderTemp, "invalidDateMarkdown.md"));
//            assertNull(invalidMarkdown.getTopic().getDate()); // Should not have a valid date
//        } catch (IOException e) {
//            fail("IOException thrown during test: " + e.getMessage());
//        }
//    }

    @Test
    public void testMinimumFieldValidation() {
        assertTrue(markdown.validateMinimumFields());

        // Create a markdown with missing fields
        String incompleteMarkdownContent = """
                # Incomplete Topic
                + date: 2024-09-25
                ----
                + author: Sarah
                """;

        try {
            Files.writeString(Paths.get(folderTemp.getAbsolutePath(), "incompleteMarkdown.md"), incompleteMarkdownContent);
            Markdown incompleteMarkdown = new Markdown(new File(folderTemp, "incompleteMarkdown.md"));
            assertFalse(incompleteMarkdown.validateMinimumFields());
        } catch (IOException e) {
            fail("IOException thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testSaveToFile() throws IOException {
        markdown.saveToFile(new File(folderTemp, "savedMarkdown.md"));
        
        
        // Check the saved file contents
        List<String> savedLines = Files.readAllLines(Paths.get(folderTemp.getAbsolutePath(), "savedMarkdown.md"));
        assertEquals("# My First Blog Post", savedLines.get(0).trim());
        assertEquals("+ author: John Doe", savedLines.get(3).trim());
        assertEquals("+ date: 2024-09-25", savedLines.get(4).trim());
//        assertEquals("+ description: This is my first blog post on interesting topics.", savedLines.get(4+i).trim());
        assertEquals("+ tags: blog, firstPost", savedLines.get(5).trim());
        assertEquals("This is the content of the blog post. It discusses various interesting topics in detail.", savedLines.get(7).trim());
        assertEquals("----", savedLines.get(9).trim()); // Separator
        assertEquals("## 2024-09-26", savedLines.get(11).trim()); // First reply date
        assertEquals("+ author: Alice", savedLines.get(12).trim()); // First reply author
        assertEquals("This is the first reply to the blog post.", savedLines.get(17).trim()); // First reply content
        assertEquals("+ tags: reply, comment", savedLines.get(15).trim()); // First reply tags
        assertEquals("----", savedLines.get(19).trim()); // Second separator
        assertEquals("## 2024-09-28", savedLines.get(21).trim()); // Second reply date
        assertEquals("+ author: Charlie", savedLines.get(22).trim()); // Second reply author
        assertEquals("This is the third reply.", savedLines.get(27).trim()); // Second reply content
        assertEquals("+ tags: feedback", savedLines.get(25).trim()); // Second reply tags
        // Clean up the saved file after the test
        Files.deleteIfExists(Paths.get(folderTemp.getAbsolutePath(), "savedMarkdown.md"));
    }

    @Test
    public void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get(folderTemp.getAbsolutePath(), "testMarkdown.md"));
        Files.deleteIfExists(Paths.get(folderTemp.getAbsolutePath(), "incompleteMarkdown.md"));
        Files.deleteIfExists(Paths.get(folderTemp.getAbsolutePath(), "invalidDateMarkdown.md"));
    }
}
