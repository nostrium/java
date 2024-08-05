/*
 *  Functions related to text manipulation
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.utils;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.profile.pegdown.Extensions;
import com.vladsch.flexmark.profile.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.data.DataHolder;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Date: 2023-02-09
 * Place: Germany
 * @author brito
 */
public class TextFunctions {

    /**
     * Cleans a string from non-alphanumeric letters Finds equivalents for
     * letters with accents.
     *
     * @param input
     * @return
     */
    public static String cleanString(String input) {
        String text = Normalizer
                .normalize(input, Normalizer.Form.NFD)
                //.replaceAll("[^\\p{ASCII}]", "");
                .replaceAll("[^A-Za-z0-9\\s]", "");
        text = text.replaceAll("\\s", "_");
        return text.replaceAll("__", "_");
    }

    public static String convertHtmlToMarkdown(String input) {
        DataHolder options = PegdownOptionsAdapter.flexmarkOptions(true,
                Extensions.ALL);
        return FlexmarkHtmlConverter.builder(options).build().convert(input);
    }

    /**
     * Get the current year, month and day in ISO format
     *
     * @return e.g. 2024-08-03_22:01
     */
    public static String getDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
        return now.format(formatter);
    }

}
