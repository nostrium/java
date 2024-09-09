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
import static online.nostrium.apps.storycraft.StoryUtils.translate;
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

    final String anchorIf = "If ";

    public void addRule(String rule) {
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
     * Actions are things like fights and interactions between two things. We
     * don't hardcode their behaviour or what they are doing to keep the game
     * engine as flexible as possible.
     *
     * @param A Thing A that starts the action
     * @param B Thing B that replies to the action
     */
    public void processAction(GameThing A, GameThing B) {

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
        for (String key : tempVars.keySet()) {
            // only apply changes to permanent variables like Health, Attack and so on
            if (key.contains(":") == false) {
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
            if (key.startsWith("A")) {
                A.attributes.put(varName, varValue);
            } else {
                B.attributes.put(varName, varValue);
            }
        }
    }

    @SuppressWarnings("UnnecessaryTemporaryOnConversionFromString")
    private void processRule(
            String rule, GameThing A, GameThing B,
            HashMap<String, String> tempVars) {
        // only support simple expressions for the moment
        if (rule.contains(" = ") == false) {
            return;
        }

        // don't support IF statements
        if (rule.startsWith(anchorIf)) {
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
                if (ruleToChange.startsWith("A:") == false
                        && ruleToChange.startsWith("B:") == false) {
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
                ArrayList<String> rulesLocal = getRules();
                TextFunctions.updateLineByContent(
                        rulesLocal, originalRule, combinedResult);
            }
        } catch (NumberFormatException e) {
            // it was not a simple number
            // cause an exception to happen
        }

        // store the translated expression
        tempVars.put(varName, valueText);

    }

    /**
     * Stop the action when one of the IF statements becomes true
     *
     * @return null when nothing found, or sequence of next actions
     */
    public String[] canStop(GameThing A, GameThing B) {
        for (String rule : rules) {
            if (rule.startsWith(anchorIf) == false) {
                continue;
            }
            IfCondition ifCondition = new IfCondition(rule);
            boolean isApplicable = ifCondition.isApplicable(A, B);
            if (ifCondition.isValid() && isApplicable) {
                return ifCondition.getSequences();
            }
        }
        return null;
    }

}
