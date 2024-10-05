/*
 * Provide Auto-complete suggestions
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.session.maps;

import java.util.TreeSet;

/**
 * @author Brito
 * @date: 2024-10-04
 * @location: Germany
 */
public class AutoComplete {

    public static String autoComplete(String text, MapBox map) {
        return autoComplete(text, map, true);
    }

    private static String autoComplete(String text, MapBox map, boolean addCommands) {
        TreeSet<String> matches = new TreeSet<>();
        // typical command: cd user
        // first part is a command
        // second part is almost always a folder, app or link
        String output = "";
        if (text.isEmpty()
                || text.contains(" ") == false) {
            // this is likely a command
            if (addCommands) {
                for (MapCommand cmd : map.commands) {
                    if (cmd.getName().startsWith(text)) {
                        matches.add(cmd.getName());
                    }
                }
            }

            // no selectable app, so give all options
            if (text.isEmpty()) {
                return giveAllOptions(map);
            }

            // not a command, let's then try an app or folder
            for (Map item : map.apps) {
                if (item.getName().startsWith(text)) {
                    matches.add(item.getName());
                }
            }
            for (Map item : map.folders) {
                if (item.getName().startsWith(text)) {
                    matches.add(item.getName());
                }
            }
            for (Map item : map.links) {
                if (item.getName().startsWith(text)) {
                    matches.add(item.getName());
                }
            }
            // return the matches
            if (matches.isEmpty() == false) {
                return String.join(" | ", matches);
            } else {
                return "";
            }
        }

        // the text already has a space
        // get text between the last space and end of the string
        int i = text.lastIndexOf(" ");
        String parameter = text.substring(i + 1, text.length());
        return autoComplete(parameter, map, false);
    }

    private static String giveAllOptions(MapBox map) {
        TreeSet<String> matches = new TreeSet<>();
        // list all qualified items
        for (Map item : map.apps) {
            matches.add(item.getName());
        }
        for (Map item : map.folders) {
            matches.add(item.getName());
        }
        for (Map item : map.links) {
            matches.add(item.getName());
        }
        return String.join(" | ", matches);
    }

}
