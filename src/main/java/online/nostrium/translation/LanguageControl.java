/*
 * Control the multiple available languages, keep them updated.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.translation;

import java.io.File;
import java.util.HashMap;
import online.nostrium.folder.FolderUtils;

/**
 * 
 * This is a system permits to create new language templates and adjust
 * to new text strings as they arrive. For convenience, the default
 * language is English (US).
 * 
 * @author Brito
 * @date: 2024-09-01
 * @location: Germany
 */
public class LanguageControl {

    final String languageDefault = "EN";
    
    String languageSelected = languageDefault;
    
    HashMap<String, Language> list = new HashMap<>();
    
    public LanguageControl(){
        loadLanguages();
    }

    /**
     * Find the available languages and load them
     */
    private void loadLanguages() {
        // find all the related files inside the folder
        File[] files = FolderUtils.getFolderLang().listFiles();
        // empty folder?
        if(files.length == 0){
            Language langDefault = new Language(languageDefault);
            // save it to disk
            langDefault.save();
            list.put(langDefault.getId(), langDefault);
        }
    }

    /**
     * Translate one language to another
     * @param text
     * @return 
     */
    public String translate(String text) {
        Language lang = list.get(languageSelected);
        // first time finding this language
        if(lang == null){
            lang = new Language(languageSelected);
            lang.save();
            list.put(languageSelected, lang);
        }
        
        if(lang.hasText(text) == false){
            lang.add(text, text);
            lang.save();
        }
        
        return lang.get(text);
    }
    
    
}
