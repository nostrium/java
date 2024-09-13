/*
 * Defines an IF condition inside the game
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import online.nostrium.logs.Log;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.utils.MathFunctions;

/**
 * @author Brito
 * @date: 2024-09-09
 * @location: Germany
 */
public class IfCondition_Obsolete {

    final String keyRaw, valueRaw, expressionOriginal;
    String keyTranslated, valueComputed;
    final String[] sequences;
    boolean valid = false;

    public IfCondition_Obsolete(String ifCondition) {
        // basic condition check
        if (ifCondition == null || ifCondition.contains(" then ") == false
                || ifCondition.startsWith("If ") == false) {
            keyRaw = null;
            valueRaw = null;
            sequences = null;
            expressionOriginal = null;
            return;
        }

        expressionOriginal = ifCondition;
        String[] data = ifCondition.split(" then ");

        this.keyRaw = data[0].substring("If ".length());
        this.valueRaw = data[1];

        // now work on the sequences
        sequences = valueRaw.split("; ");
        if (sequences == null || sequences.length == 0) {
            return;
        }

        // can this IF condition be used?
        valid = true;
    }

    public String getKey() {
        return keyRaw;
    }

    public String getValueRaw() {
        return valueRaw;
    }

    public String[] getSequences() {
        return sequences;
    }

    public boolean isValid() {
        return valid;
    }

    public String getKeyTranslated() {
        return keyTranslated;
    }

    public void setKeyTranslated(String keyTranslated) {
        this.keyTranslated = keyTranslated;
    }

    public String getValueComputed() {
        return valueComputed;
    }

    public void setValueComputed(String valueComputed) {
        this.valueComputed = valueComputed;
    }

    /**
     * Verify if this IF condition is now applicable
     *
     * @param A
     * @param B
     * @return 
     */
    public boolean isApplicable(GameThing A, GameThing B) {
        // If A:Health < 0 then write "You have lost"; #scene-exit-game; stop
        // start by changing the variables by values
//        keyTranslated = translate(this.keyRaw, A, B);
        // e.g. 60 < 0
        // convert to expression
        try{
            return MathFunctions.evaluateExpressionBoolean(keyTranslated);
        }catch(Exception E){
            Log.write(TerminalCode.CRASH, 
                    "Failed to evaluate IF expression", 
                    expressionOriginal);
            E.printStackTrace();
            return false;
        }
    }


}
