/*
 * A key/value pair
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.email;

/**
 * @author Brito
 * @date: 2024-10-10
 * @location: Germany
 */
public class KeyValue {

    final String key;
    final String value;

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
    
}
