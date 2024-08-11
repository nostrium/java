/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.basic;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import online.nostrium.main.core;
import online.nostrium.servers.apps.chat.ChatRoom;
import online.nostrium.servers.apps.chat.ChatUtils;
import online.nostrium.servers.apps.user.UserUtils;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;

/**
 * @author Brito
 * @date: 2024-08-11
 * @location: Germany
 */
public class CommandStatus extends TerminalCommand {

    public CommandStatus(TerminalApp app) {
        super(app);
        this.requireSlash = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        
        String output = "";
        
        // get server running stats
        output += getUptime() + "\n";

        // get the number of chat rooms and users
        output += getChatRoomCount() + "\n";
        output += "Users: " + UserUtils.countUsers() + "\n";
        output += "Online: " + core.sessions.countSessions();
        

        return reply(TerminalCode.OK,  output);
    }

    
    private String getUptime(){
        Date dateStart = core.uptime;
        Date dateNow = new Date();

        // Convert Date to LocalDateTime
        LocalDateTime startLocalDateTime = Instant.ofEpochMilli(dateStart.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime nowLocalDateTime = Instant.ofEpochMilli(dateNow.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Calculate the period between the two dates
        long years = ChronoUnit.YEARS.between(startLocalDateTime, nowLocalDateTime);
        startLocalDateTime = startLocalDateTime.plusYears(years);

        long months = ChronoUnit.MONTHS.between(startLocalDateTime, nowLocalDateTime);
        startLocalDateTime = startLocalDateTime.plusMonths(months);

        long days = ChronoUnit.DAYS.between(startLocalDateTime, nowLocalDateTime);
        startLocalDateTime = startLocalDateTime.plusDays(days);

        // Calculate remaining time in hours, minutes, and seconds
        Duration duration = Duration.between(startLocalDateTime, nowLocalDateTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        StringBuilder timeRunningBuilder = new StringBuilder();

        if (years > 0) {
            timeRunningBuilder.append(years).append(" year").append(years > 1 ? "s" : "").append(", ");
        }
        if (months > 0) {
            timeRunningBuilder.append(months).append(" month").append(months > 1 ? "s" : "").append(", ");
        }
        if (days > 0) {
            timeRunningBuilder.append(days).append(" day").append(days > 1 ? "s" : "").append(", ");
        }
        if (hours > 0) {
            timeRunningBuilder.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(", ");
        }
        if (minutes > 0) {
            timeRunningBuilder.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(", ");
        }
        if (seconds > 0 || timeRunningBuilder.length() == 0) { // Show seconds if all other units are zero
            timeRunningBuilder.append(seconds).append(" second").append(seconds != 1 ? "s" : "");
        } else {
            // Remove the trailing comma and space
            timeRunningBuilder.setLength(timeRunningBuilder.length() - 2);
        }

        String timeRunning = timeRunningBuilder.toString();
        return "Uptime: " + timeRunning;
    }
    
    @Override
    public String commandName() {
        return "status";
    }

    @Override
    public String oneLineDescription() {
        return "Basic status data";
    }

    private String getChatRoomCount() {
        ArrayList<ChatRoom> rooms = ChatUtils.getChatRooms();
        return "Chatrooms: " + rooms.size();
    }

}
