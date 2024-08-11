/*
 *  Test the copy and paste detection on ASCII text terminals
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import online.nostrium.servers.terminal.telnet.TelnetDetectCopyAndPaste;
import org.junit.jupiter.api.Test;

/**
 * Date: 2023-02-11 Place: Germany
 *
 * @author brito
 */
public class CopyPasteDetectTest {

    // Test data: multiple lines to simulate paste operations
    String[] lines = new String[]{
        "line1",
        "line2",
        "line3",
        "line4",
        "line5",
        "line6",
        "line7"
    };

    @Test
    public void tryOutPaste() throws InterruptedException {
        TelnetDetectCopyAndPaste copyPaste = new TelnetDetectCopyAndPaste();

        // Simulate typing/pasting of multiple lines
        for (String line : lines) {
            for (char ch : line.toCharArray()) {
                // Process each character
                copyPaste.append(ch);
            }
            // Simulate pressing Enter at the end of each line
            copyPaste.append('\n');

            // Introduce intentional delay for testing
            Thread.sleep(50); // Short delay to simulate rapid typing/pasting

            if (copyPaste.isReadyForProcessing()) {
                System.out.println("Detected input:\n" + copyPaste.getOutputAndReset());
            }
        }

        // Handle any remaining output
        String finalOutput = copyPaste.getOutputAndReset();
        if (!finalOutput.isEmpty()) {
            System.out.println("Final output:\n" + finalOutput);
        }
    }

    @Test
    public void tryOutPasteWithDelay() throws InterruptedException {
        TelnetDetectCopyAndPaste copyPaste = new TelnetDetectCopyAndPaste();

        int lineCounter = 0;

        // Simulate typing/pasting of multiple lines with a delay after the third line
        for (String line : lines) {
            for (char ch : line.toCharArray()) {
                // Process each character
                copyPaste.append(ch);
            }
            // Simulate pressing Enter at the end of each line
            copyPaste.append('\n');

            lineCounter++;

            // Introduce intentional delay after the third line
            if (lineCounter == 3) {
                Thread.sleep(300); // Delay long enough to exceed the PASTE_THRESHOLD_MS
            } else {
                Thread.sleep(50); // Short delay to simulate rapid typing/pasting
            }

            if (copyPaste.isReadyForProcessing()) {
                System.out.println("Detected input:\n" + copyPaste.getOutputAndReset());
            }
        }

        // Handle any remaining output
        String finalOutput = copyPaste.getOutputAndReset();
        if (!finalOutput.isEmpty()) {
            System.out.println("Final output:\n" + finalOutput);
        }
    }
}
