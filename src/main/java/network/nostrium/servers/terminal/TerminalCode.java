/*
 * Handles specific types of terminals
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.servers.terminal;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class TerminalCode {
    
    final public static int
            OK = 200,
            CHANGE_APP = -1,
            EXIT_APP = -10,
            EXIT_CLIENT = -100,
            FAIL = 500,
            INCOMPLETE = 300;
}
