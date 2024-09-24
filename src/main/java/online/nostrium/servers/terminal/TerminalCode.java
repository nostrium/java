/*
 * Handles specific types of terminals
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.terminal;

/**
 * Represents terminal codes with specific integer values.
 * 
 * Author: Brito
 * Date: 2024-08-05
 * Location: Germany
 */
public enum TerminalCode {
    OK(200),
    BOOT(201),
    ROUTINE(202),
    VISIT(203),
    LOGIN(204),
    LOGOUT(205),
    EVENT(206),
    CHANGE_APP(-1),
    EXIT_APP(-10),
    EXIT_CLIENT(-100),
    FAIL(500),
    INCOMPLETE(300),
    INVALID(301),
    CRASH(302),
    DENIED(501),
    NOT_FOUND(404),
    INFO(400),
    WARNING(401);
    
    
    private final int code;

    TerminalCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
