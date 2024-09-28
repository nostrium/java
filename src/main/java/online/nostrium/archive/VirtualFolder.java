/*
 * Defines a virtual folder that is used by an application
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.archive;

/**
 * @author Brito
 * @date: 2024-09-28
 * @location: Germany
 */
public class VirtualFolder {

    final String name;
    final String location;

    public VirtualFolder(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
