/*
 * User account in other platforms
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.user.alternative;

import com.google.gson.annotations.Expose;

/**
 * @author Brito
 * @date: 2024-09-24
 * @location: Germany
 */
public class PlatformAccount {

    @Expose
    String id = null;

    @Expose
    String username = null;

    @Expose
    String passwordEncrypted = null;
    
    String password = null;
    
    @Expose
    PlatformType platform = PlatformType.NONE;

    @Expose
    String tokenEncrypted = null;
    String token = null;

    @Expose
    long validUntil = -1;
    
}
