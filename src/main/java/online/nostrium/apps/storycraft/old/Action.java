/*
 * Something is happening
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft.old;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class Action {

    String name;
    int skillRequired;
    String successMessage;
    int successExperience;
    String failureMessage;
    int failureDamage;

    public Action(String name, int skillRequired, String successMessage, 
            int successExperience, String failureMessage, int failureDamage) {
        this.name = name;
        this.skillRequired = skillRequired;
        this.successMessage = successMessage;
        this.successExperience = successExperience;
        this.failureMessage = failureMessage;
        this.failureDamage = failureDamage;
    }
}
