/*
 *  User types
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.user;

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
    MOD,        // assigned as moderator the overall platform
    SYSOP,      // founder or owner of the platform
}
