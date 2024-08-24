/*
 * Defines an NPC
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.games.gods.npc;

import online.nostrium.apps.games.gods.GODS_Status;
import online.nostrium.apps.user.User;

/**
 * @author Brito
 * @date: 2024-08-22
 * @location: Germany
 */
public abstract class GODS_NPC {
    
   public abstract String getId();
   
   public abstract String getReadableName();
   
   public abstract String messageIntro();
   
   public abstract GODS_NPC_Type getNpcType();
   
   public abstract void action(User user, GODS_Status status);
   
   public abstract boolean canRun();
   
   protected void displayOptions(String... options){
       for(String option : options){
           String text = option + "\n";
           
       }
   }

}
