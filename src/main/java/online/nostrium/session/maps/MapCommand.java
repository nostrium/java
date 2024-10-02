/*
 * Define a command on the virtual file system
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.session.maps;

import online.nostrium.servers.terminal.TerminalCommand;

/**
 * @author Brito
 * @date: 2024-10-02
 * @location: Germany
 */
public class MapCommand extends Map{
    
    final TerminalCommand cmd;
    
    public MapCommand(TerminalCommand cmd) {
        super(MapType.COMMAND, cmd.getPathWithName());
        this.cmd = cmd;
    }

    @Override
    public void index() {
    }

    public TerminalCommand getCmd() {
        return cmd;
    }
    
    
}
