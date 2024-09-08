/*
 * Rule applicable to the game
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.storycraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.utils.MathFunctions;
import online.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-09-06
 * @location: Germany
 */
public class Action {

    String action = null;
    String description = null;
    @SuppressWarnings("unchecked")
    ArrayList<String> rules = new ArrayList();

    public void addRule(String rule){
        rules.add(rule);
    }

    public ArrayList<String> getRules() {
        return rules;
    }
    
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Actions are things like fights and interactions between two things.
     * We don't hardcode their behaviour or what they are doing to keep
     * the game engine as flexible as possible.
     * 
     * @param A Thing A that starts the action
     * @param B Thing B that replies to the action
     * @param actionId the action to execute
     */
    public void processAction(GameThing A, GameThing B, String actionId){
        
        // example of the syntax being executed
        //# Action: Attack
        //> Define what happens when Player A attacks player B
        //
        //- AttackPower = A:Attack + (A:Experience / (A:Attack * 0.5))
        //- DefendPower = B:Defense + (B:Experience / (B:Defense * 0.5))
        //- B:Health = B:Health - (AttackPower - DefendPower)
        
        // where we place the computed values
        HashMap<String, String> tempVars = new HashMap<>();
        
        // go through each rule, they are read sequentially because
        // it is possible to create variables that are used by later
        // rules. This keeps the expressions more readable to users.
        for (String rule : getRules()) {
            processRule(rule, A, B, tempVars);
        }
        
        // get the final results for each rule
        for(String key : tempVars.keySet()){
            // only apply changes to permanent variables like Health, Attack and so on
            if(key.contains(":") == false){
                continue;
            }
            // make the changes
            String varName = key.substring("X:".length());
            String varValue = tempVars.get(key);
            
            // final computation of the rules
            try {
                // expressions fail often, new functions need to be registered
                // inside MathFunctions::evaluateExpression();
                long computedExpression = MathFunctions.evaluateExpression(varValue);
                varValue = computedExpression + "";
            } catch (Exception ex) {
                Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
            }
            // are we changing A or B?
            if(key.startsWith("A")){
                A.attributes.put(varName, varValue);
            }else{
                B.attributes.put(varName, varValue);
            }
        }

       // System.gc();
    }
    
    
     @SuppressWarnings("UnnecessaryTemporaryOnConversionFromString")
    private void processRule(
            String rule, GameThing A, GameThing B,
            HashMap<String, String> tempVars) {
        // only support simple expressions for the moment
        if (rule.contains(" = ") == false) {
            return;
        }
        // get the variable name and expression to run
        String[] data = rule.split(" = ");
        if (data.length != 2) {
            return;
        }
        //- AttackPower = A:Attack + (A:Experience / (A:Attack * 0,5))
        String varName = data[0].trim();
        String expression = data[1].trim();

        String valueText = translate(expression, A, B);
        // try to change all values where applicable
        try {
            // test if we can convert to a number
            Long.parseLong(valueText);
            // change this varName in all expressions
            for (String ruleToChange : getRules()) {
                // only change the internal variables
                if(ruleToChange.startsWith("A:") == false
                        && ruleToChange.startsWith("B:") == false){
                    continue;
                }
                String originalRule = ruleToChange;
                if (ruleToChange.contains(varName) == false) {
                    continue;
                }
                
                data = ruleToChange.split(" = ");
                // make the changes
                data[1] = data[1].replace(varName, valueText);
                // combine them
                String combinedResult = data[0] + " = " + data[1];
                
                // update the final rules with values
                ArrayList<String> rules = getRules();
                TextFunctions.updateLineByContent(
                        rules, originalRule, combinedResult);
            }
        } catch (NumberFormatException e) {
        }

        // store the translated expression
        tempVars.put(varName, valueText);

    }

    /**
     * Compute the expression between two things
     *
     * @param expression
     * @param A
     * @param B
     * @return
     */
    private String translate(String expression, GameThing A, GameThing B) {
        // A:Attack + (A:Experience / (A:Attack * 0,5))
        String result = expression;

        // change all A-related expressions
        for (String attr : A.getAttributes().keySet()) {
            String tag = "A:" + attr;
            if (result.contains(tag) == false) {
                continue;
            }
            String valueText = A.getAttributes().get(attr);
            result = result.replace(tag, valueText);
        }

        // change all B-related expressions
        for (String attr : B.getAttributes().keySet()) {
            String tag = "B:" + attr;
            if (result.contains(tag) == false) {
                continue;
            }
            String value = B.getAttributes().get(attr);
            result = result.replace(tag, value);
        }

        // correct any commas
        //result = result.replace(",", ".");

        // now calculate the formula
        boolean convertedOK = false;
        long value = 0;
        try {
            value = MathFunctions.evaluateExpression(result);
            convertedOK = true;
        } catch (Exception ex) {
            Logger.getLogger(GameThing.class.getName()).log(Level.SEVERE, null, ex);
        }

        // don't change the value when the output failed
        if (convertedOK) {
            result = value + "";
        }
        return result;
    }

    
    
}
