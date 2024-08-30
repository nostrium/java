/*
 *  User types
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.user;

import com.google.gson.annotations.Expose;

/**
 * Date: 2024-08-04
 * Place: Germany
 * @author brito
 */
public enum UserType {
    @Expose
    ANON,       // logged without registering
    MEMBER,     // registered user
    MOD,        // assigned as moderator to specific apps
    JANITOR,    // global moderator
    ADMIN,      // founder or owner of the platform
}
