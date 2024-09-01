/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import online.nostrium.translation.Language;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Brito
 * @date: 2024-09-01
 * @location: Germany
 */
public class LanguageTest {
    
    public LanguageTest() {
    }

    @Test
    public void helloLanguage() {
        Language lang = new Language("EN");
    
        String text = lang.jsonExport();
        assertNotNull(text);
        
    }
}
