/*
 * Well-known platform types
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.user.alternative;

/**
 * @author Brito
 * @date: 2024-09-24
 * @location: Germany
 */
public enum PlatformType {
    // Please add more as needed
    
    // Walled-garden platforms
    X, FACEBOOK, INSTAGRAM, GOOGLE, YOUTUBE, 
    
    // Open data plaforms
    NOSTR, MASTODON, BLUESKY,
    
    // Walled-garden Messengers
    TELEGRAM, DISCORD, WHATSAPP,
    
    // open data messengers
    SIMPLEX, IRC, RATTLEGRAM,
    
    // Legacy voice / text
    PHONE, EMAIL,
    
    // terminals
    SSH, TELNET,
    
    // code sharing/mirror
    GITHUB, GITLAB,
    
    // generic account elsewhere
    MISC, NONE
}
