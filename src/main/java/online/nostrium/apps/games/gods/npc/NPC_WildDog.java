/*
 * Defines an NPC
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.games.gods.npc;

import online.nostrium.apps.games.gods.GODS_Status;
import online.nostrium.user.User;

/**
 * @author Brito
 * @date: 2024-08-22
 * @location: Germany
 */
public class NPC_WildDog extends GODS_NPC{

    @Override
    public String getId() {
        return "wild_dog";
    }

    @Override
    public String getReadableName() {
        return "Wild Dog";
    }

    @Override
    public GODS_NPC_Type getNpcType() {
        return GODS_NPC_Type.OPPONENT;
    }

    @Override
    public void action(User user, GODS_Status status) {
        String[] options = new String[]{
            "(A) attack"
        };
    }

    @Override
    public boolean canRun() {
        return true;
    }

    @Override
    public String messageIntro() {
        return "A wild dog crosses your way";
    }
   
}
