/*
 * Define names for the services running on specific ports
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.ports;

/**
 * @author Brito
 * @date: 2024-10-17
 * @location: Germany
 */
public enum PortId {
    
    // Common protocols
    HTTP(80),
    HTTP_Debug(8000),
    HTTPS(443),
    HTTPS_Debug(44300),
    FTP(21),
    FTP_Debug(2100),
    FTPS(990),
    FTPS_Debug(9900),
    Telnet(23),
    Telnet_Debug(2300),
    SSH(220),
    SSH_Debug(2200),
    Gopher(70),
    Gopher_Debug(7000),
    QOTD(17),
    QOTD_Debug(1700),
    Finger(79),
    Finger_Debug(7900),

    // Email-related protocols
    SMTP(25),
    SMTP_Debug(2500),
    SMTPS(465),
    SMTPS_Debug(4650),
    IMAP(143),
    IMAP_Debug(14300),
    IMAPS(993),
    IMAPS_Debug(9930),
    POP3(110),
    POP3_Debug(11000),
    POP3S(995),
    POP3S_Debug(9950);

    private final int portNumber;

    // Constructor to set the port number
    PortId(int portNumber) {
        this.portNumber = portNumber;
    }

    // Getter to retrieve the port number
    public int getPortNumber() {
        return this.portNumber;
    }
    
    // Returns the name of the enum constant
    @Override
    public String toString() {
        return name();  // This returns the enum constant name, e.g., "HTTP"
    }
}
