/*
 *  Test the folder functions
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import java.io.File;
import online.nostrium.main.Folder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Date: 2023-02-08
 * Place: Germany
 * @author brito
 */
public class FolderTest {
    
    public FolderTest() {
    }

    @Test
    public void getFolder() {
        // we are in development/test mode.
        // make sure we get the run folder
        File folder = Folder.getFolderBase();
        assertNotNull(folder);
        assertEquals("run", folder.getName());
        System.out.println(folder.getPath());
    
    }
}
