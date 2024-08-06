/*
 * Defines a response to a command
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.terminal;


/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class CommandResponse {

    final int code;
    final String text;
    private TerminalApp app;
    
    public CommandResponse(TerminalApp app) {
        this.code = -1;
        this.text = "";
        this.app = app;
    }

    public CommandResponse(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public TerminalApp getApp() {
        return app;
    }

}
