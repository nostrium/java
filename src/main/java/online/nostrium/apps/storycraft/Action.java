/*
 * Rule applicable to the game
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.util.HashMap;
import java.util.Map;
import online.nostrium.utils.TextFunctions;
import org.mvel2.MVEL;

/**
 * @author Brito
 * @date: 2024-09-06
 * @location: Germany
 */
public class Action {

    String title = null;
    String script = null;
    String description = null;
    String anchor = "Action";
    

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Actions are things like fights and interactions between two things. We
     * don't hardcode their behaviour or what they are doing to keep the game
     * engine as flexible as possible.
     *
     * @param thingA Thing A that starts the action
     * @param thingB Thing B that replies to the action
     * @param screen
     * @return 
     */
    public String processAction(GameThing thingA, GameThing thingB, GameScreen screen) {
     
        // Initialize variables for Player A and Player B
        Map<String, Object> A = thingA.attributes;
        Map<String, Object> B = thingB.attributes;
        Map<String, Object> variables = new HashMap<>();
        
        // Add players to variables
        variables.put("A", A);
        variables.put("B", B);

        // Evaluate the script using MVEL
        try{
            MVEL.eval(script, variables);
        }catch(Exception e){
            screen.writeln("Failed to run script", e.getMessage());
            return null;   
        }
        // changes were made and should be visible on the objects
        return (String) variables.get("output");
    }
    
    
    /**
     * Extracts the needed information from the block
     * @param block 
     */
    public void parse(String block) {
        
        // parse the script
        if(block.contains("\n```")){
            script = TextFunctions.extractTextBetweenTicks(block);
        }
        
        String[] lines = block.split("\n");
        String anchorId = "# " + anchor + ": ";
        for(String line : lines){
            // get the title
            if(line.startsWith(anchorId)){
                this.title = line.substring(anchorId.length());
                continue;
            }
            
            
            // get the description
            if(line.startsWith("> ")){
                if(description == null){
                    description = line.substring("> ".length());
                }else{
                    description += "\n" + description;
                }
                continue;
            }
            
            
            // reached the next section?
            if(line.startsWith("#")){
                break;
            }
            
        }
    }

    public String getTitle() {
        return title;
    }

    public String getScript() {
        return script;
    }

    public boolean isNotValid() {
        return title == null || script == null;
    }
    
}
