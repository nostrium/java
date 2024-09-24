/*
 * Handles multiple accounts
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.user.alternative;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import online.nostrium.user.User;

/**
 * @author Brito
 * @date: 2024-09-24
 * @location: Germany
 */
public class AccountsManager {
    
    final User user;
    
    @Expose
    ArrayList<PlatformAccount> accounts = new ArrayList<>();
    
    public AccountsManager(User user) {
        this.user = user;
    }

    public ArrayList<PlatformAccount> getAccounts() {
        return accounts;
    }
    
    
  
}
