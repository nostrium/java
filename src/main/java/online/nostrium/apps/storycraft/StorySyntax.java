/*
 * Syntax valid for stories
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.storycraft;

/**
 * @author Brito
 * @date: 4 Sept 2024
 * @location: Germany
 */
public class StorySyntax {
    
    public static String text =
            """
            
    [Scene]
            
            # Scene: Azurath Entrance
            > Welcome to the ancient ruins of Azurath. The air is thick with the scent of damp stone and decaying leaves. You are a treasure hunter, drawn by the legends of untold riches hidden within.
            > The entrance to the ruins is overgrown with vines. Faded carvings on the stone walls hint at the once-great civilization that lived here.
            
            ## Intro
            You step into the ruins, the sound of your footsteps echoing off the stone walls. The air is cooler inside, and a sense of foreboding settles over you.
            
            -----
                        
    [User choices]
                
            ## Choices:
            - [Explore the main hall](#scene-main-hall)
            - [Investigate the side chamber](#scene-side-chamber)
            - [Leave the ruins](#scene-leave-ruins)
            
            -----
            
    [Random choices]
            
            ## Random:
            - 30% [Fight a Skeleton Warrior](#scene-fight-skeleton)
            - 30% [Find a hidden alcove with a shield](#scene-find-shield)
            - 20% [Nothing happens](#scene-nothing-happens)
            - 20% [Find a pot of coins](#scene-find-coins)
            
            -----
            
           
    
            
            
            
            
            
            """;

}
