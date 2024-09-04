/*
 * Utils for story telling
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class StoryUtils {

    public static void writeln(String text, String... vars) {
        String formattedText = String.format(text, (Object[]) vars);
        String[] parts = formattedText.split("\\.", 2); // Split at the first period
        if (parts.length >= 2) {
            System.out.println(parts[0].trim() + ".");
            System.out.println(parts[1].trim());
        } else {
            System.out.println(formattedText);
        }
    }

}
