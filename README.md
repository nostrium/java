```
                               ███                                               
 ████████    ██████   █████  ███████   ████████  ████  █████ ████ █████████████  
  ███  ███  ███  ███ ███       ███      ███  ███  ███   ███  ███   ███  ███  ███ 
  ███  ███  ███  ███  █████    ███      ███       ███   ███  ███   ███  ███  ███ 
  ███  ███  ███  ███     ███   ███ ███  ███       ███   ███  ███   ███  ███  ███ 
 ████ █████  ██████  ██████     █████  █████     █████   ████████ █████ ███ █████
	
    The NOSTR BBS!

```

Nostrium is a nostr relay that provides a BBS platform.
Users can register using their usual nostr accounts (npub, nsec)
and can view forum topics, replies and everything you would
normally expect from a BSS or forum software.

One of the main goals for this project is to convert existing
BBS funcionality and forum sites into a future-proof mechanism.
Many of the forum sites have data in locked-in formats that
cannot be ported. With NOSTR we can rescue the data and preserve
the information for future generations. Each account on the old
forum is re-created as NOSTR account which can be given by the
admins to the old users that want to continue active.

All forum posts (nostr events) are stored on the filesystem
without using a database. This is intentional to permit the
forum to continue functioning even if part of the database
has become corrupted.

Another advantage is to permit the access to this forum
through different methods. Telnet/SSH are the most common
methods for a BBS, while forum sites prefer web interfaces.
Nostrium will provide basic access for these interfaces.

CLI navigation uses linux-style cd/ls commands.
Type /help to see the available commands.

Swiss-knife for off-grid services
--------------------------------

Think of Nostrium as a tool to provide Internet services
even when the Internet is not available or became limited.
For that reason, we are adding many different types of
services.


User interfaces:

+ [ ] NOSTR clients (Primal, Coracle, ...)
+ [x] web (server)
+ [x] telnet (server)
+ [ ] SSH (server)
+ [ ] IRC (server/client)
+ [ ] telegram (bot)
+ [ ] simpleX (bot)

Need other ones? Please open a ticket. 


Feature roadmap
-----------------
+ [x] basic functionality (navigation, users, chat)
+ [ ] spam control / approval of posts on relay
+ [ ] share nostr posts with other relays
+ [ ] automatic SSL registration on Let's Encrypt


Servers
-------
+ [ ] NOSTR relay
+ [x] HTTP - 80
+ [ ] HTTPS - 443
+ [ ] SSH (Secure Shell) - 22
+ [x] Telnet - 23

File sharing:
+ [ ] FTP (File Transfer Protocol) - 21
+ [ ] FTPS (FTP Secure) - 990
+ [ ] SFTP (SSH File Transfer Protocol) - 22
+ [ ] TFTP (Trivial File Transfer Protocol) - 69
+ [ ] Torrent - 6881..6889

Email service:
+ [ ] SMTP (Simple Mail Transfer Protocol) - 25
+ [ ] SMTPS (SMTP Secure) - 465
+ [ ] POP3 (Post Office Protocol v3) - 110
+ [ ] POP3S (POP3 Secure) - 995
+ [ ] IMAP (Internet Message Access Protocol) - 143
+ [ ] IMAPS (IMAP Secure) - 993

Communication:
+ [ ] IRC (Internet Relay Chat) - 194
+ [ ] IRCS (IRC Secure) - 6697
+ [ ] NNTP (Network News Transfer Protocol) - 119
+ [ ] NNTPS (NNTP Secure) - 563
+ [ ] Gopher - 70

Network basics:
+ [ ] WHOIS - 43
+ [x] Finger - 79
+ [ ] Echo Protocol - 7
+ [ ] Daytime Protocol - 13
+ [ ] Time Protocol - 37
+ [ ] Quote of the Day (QOTD) - 17

Domain/IP handling:
+ [ ] DNS (Domain Name System) - 53
+ [ ] DNS over TLS - 853
+ [ ] DNS over HTTPS - 443
+ [ ] NTP (Network Time Protocol) - 123
+ [ ] SNTP (Simple Network Time Protocol) - 123
+ [ ] DHCP (Dynamic Host Configuration Protocol) - 67/68

Contacts
+ [ ] WebDAV (Web Distributed Authoring and Versioning) - 80/443
+ [ ] CalDAV (Calendaring Extensions to WebDAV) - 80/443
+ [ ] CardDAV (Address Book Extensions to WebDAV) - 80/443



Forum conversion
----------------
Independent web forums are disappearing due to server costs
and centralization of discussion groups. One of our goals is
to provide an easy conversion of the existing forums to the
nostr platform and provide a second life to them.

+ [ ] web crawl to convert web forums (PhpBB, MyBB)
+ [ ] tools to map older users to new nostr accounts



Security considerations
-----------------------

NOSTR is critically based on the NSEC signature
to sign events and prove authenticity of a user.
NOSTRIUM never stores the NSEC signature on clear
text inside the disk. This is intentional to avoid
a leak of NSEC signatures in case of a cyberattack.

For that reason, the platform asks users to setup
a password, which is used for encrypting the NSEC.



Requisites
----------

+ Java, anything above JDK 17
+ Linux or OSX (for development)
+ Maven

The code is developed using Netbeans IDE (any version)


Development
-----------

+ Download the source code to your computer
+ create an SSH key to log into a server as root
+ make sure you have Java above version 17 on the server
+ e.g.: sudo apt install openjdk-21-jre-headless
+ create the folder /opt/nostrium/
+ edit the deploy.sh script to match your domain
+ run the deploy script: sh deploy.sh
+ on the first run telnet is available on port 23000
+ e.g. telnet myipaddress 23000
+ using ssh, edit the /opt/nostrium/config.json
+ e.g. using nano. Change "debug" field to false
+ next time you deploy with deploy.sh it is working
+ e.g. telnet myipaddress



The deploy will compile the java code, upload to
the server and then launch the binary. When there
is already another one running, it will stop it.

Please edit the config.json on your server the
"debug" option to "false".

For anything else you can open a ticket and ask.


License
-------

Apache License 2.0



