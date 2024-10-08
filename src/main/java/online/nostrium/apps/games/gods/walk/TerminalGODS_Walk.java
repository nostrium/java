/*
 * GODS: Guardians of Decentralized Systems
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.games.gods.walk;

import online.nostrium.session.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.terminal.TerminalColor.GREEN;
import online.nostrium.user.User;
import online.nostrium.servers.terminal.TerminalUtils;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-08-19
 * @location: Germany
 */
public class TerminalGODS_Walk extends TerminalApp {

    public TerminalGODS_Walk(Session session) {
        super(session);
        // add some specific commands
        addCommand(new CommandGodsWalk(this, session));
//        addCommand(new CommandLs(this));
//        addCommand(new CommandGodsWalk(this));
    }

    @Override
    public String getDescription() {
        return "Walk around";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        return reply(TerminalCode.NOT_FOUND);
    }

    @Override
    public String getIntro() {
        
        
       String text = "Exploring the Digital Wilderness\n\n" +
              "Beyond the safety of the known realms lies the vast and untamed wilderness of\n" +
              "the digital world. As a Guardian, your journey through these lands is essential\n" +
              "for honing your skills, gaining experience, and uncovering hidden treasures.\n\n" +
              "Wander through these unpredictable territories, where every encounter presents\n" +
              "an opportunity to prove your might. Whether you face cunning adversaries or\n" +
              "stumble upon valuable resources, each step forward brings you closer to your\n" +
              "ultimate goal.\n\n" +
              "Stay vigilant, for the wilderness is as rewarding as it is dangerous. Success\n" +
              "in these encounters will not only increase your strength but also fill your\n" +
              "coffers, making you a formidable force among the Guardians.\n\n" +
              "Embrace the unknown, explore the wild, and let every challenge you face shape\n" +
              "you into a true Guardian of the realms.";

        
        
        String intro = paint(GREEN, text);
        
        return intro;
    }

    @Override
    public String getIdName() {
        return "walk";
    }

    @Override
    public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
        session.getScreen().writeln("Received a notification");
    }
    
    @Override
    public String getPathVirtual() {
        return session.getCurrentLocation().getPath();
    }

}
