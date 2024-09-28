```
                               ███                                               
 ████████    ██████   █████  ███████   ████████  ████  █████ ████ █████████████  
  ███  ███  ███  ███ ███       ███      ███  ███  ███   ███  ███   ███  ███  ███ 
  ███  ███  ███  ███  █████    ███      ███       ███   ███  ███   ███  ███  ███ 
  ███  ███  ███  ███     ███   ███ ███  ███       ███   ███  ███   ███  ███  ███ 
 ████ █████  ██████  ██████     █████  █████     █████   ████████ █████ ███ █████
	
    The NOSTR BBS!

```

Internet doesn't last forever. So we provide you Nostrium
to preserve your own piece of the network and to rebuild
the network access for other humans that want to stay free.

In essence, Nostrium [bridges](docs/Architecture.md) 
+40 types of servers and different protocols. If the server
is relevant, we will add it.

Nostrium is a retro-style nostr relay that brings back BBS,
alongside with text games and multiple ways to connect with
other people on different platforms. Want to save your data?
We make it easy to rescue your own data at other platforms.


Everything is a file with text
-----------------------------

This platform uses ZERO databases, everything is a file,
which is easy to transfer and parse by other tools. In case
part of this data becomes damaged, the rest still works.
All data is stored on plain text JSON and mardown files.

Please don't try to convince us otherwise. We are from
the 80s, fancy databases are lame 90s. Just ain't our thing.
Same philosophy applies to our visual graphics. Everything
here is WYSIWG because text is beautiful and king in hard times.



Off-grid platform
--------------------------------

Below is our roadmap of services and protocols to be supported.
If you see one that you'd like to see added, just tell us.


User interfaces:

+ [ ] NOSTR clients (Amethyst, Primal, Coracle, ...)
+ [x] [web (server)](docs/WebServer.md)
+ [x] [telnet (server)](docs/ServerTelnet.md)
+ [ ] Gopher (server/client)
+ [~] SSH (server)
+ [ ] IRC (server/client)
+ [x] [telegram (bot)](docs/BotTelegram.md)
+ [ ] API (server)
+ [ ] Android
+ [ ] Desktop GUI

Need other ones? Please open a ticket. 


Feature roadmap
-----------------
+ [x] basic functionality (navigation, users, chat)
+ [ ] WoT spam control / approval of posts on relay
+ [ ] share nostr posts with other relays
+ [x] automatic SSL registration on Let's Encrypt
+ [~] file sharing with a quota (FTP, WWW)
+ [ ] Android reverse proxy to a subdomain
+ [ ] launch virtual machines on browser/android
+ [ ] define personal NOSTR/NNTP/IRC/Gopher servers
+ [ ] add x-file messages
+ [~] write text-based games
+ [ ] read/write emails
+ [x] [display static mardown files on www as HTML](docs/WebPages.md)
+ [ ] display images on text terminals as text
+ [ ] queue of message notifications when login
+ [ ] share/update file collections
+ [ ] marketplace / wallet
+ [ ] pay/transfer crypto between users
+ [ ] engine for text games
+ [ ] bridge posts across social networks (NOSTR, X, ..)
+ [ ] personal blog
+ [ ] personal forum
+ [ ] internal search engine


Servers
-------
+ [ ] NOSTR
+ [ ] Gopher - 70
+ [x] [HTTP](docs/ServerWeb.md) - 80
+ [x] [HTTPS](docs/ServerWeb.md) - 443
+ [~] SSH (Secure Shell) - 22
+ [x] Telnet - 23

File sharing:
+ [x] [FTP](docs/ServerFTP.md) (File Transfer Protocol) - 21
+ [ ] FTPS (FTP Secure) - 990
+ [ ] SFTP (SSH File Transfer Protocol) - 22
+ [ ] TFTP (Trivial File Transfer Protocol) - 69
+ [ ] Torrent - 6881..6889
+ [ ] Git server

Email service:
+ [ ] SMTP (Simple Mail Transfer Protocol) - 25
+ [ ] SMTPS (SMTP Secure) - 465
+ [ ] POP3 (Post Office Protocol v3) - 110
+ [ ] POP3S (POP3 Secure) - 995
+ [ ] IMAP (Internet Message Access Protocol) - 143
+ [ ] IMAPS (IMAP Secure) - 993
+ [ ] HKP (HTTP Keyserver Protocol) - 11371

Communication:
+ [ ] IRC (Internet Relay Chat) - 194
+ [ ] IRCS (IRC Secure) - 6697
+ [ ] NNTP (Network News Transfer Protocol) - 119
+ [ ] NNTPS (NNTP Secure) - 563
+ [ ] Rattlegram - sound
+ [ ] XMMP - 5222, 5269


Specific messengers
+ [ ] SimpleX

Network basics:
+ [?] WHOIS - 43
+ [x] [Finger](docs/ServerFinger.md) - 79
+ [ ] Daytime Protocol - 13
+ [ ] Time Protocol - 37
+ [ ] SNTP (Simple Network Time Protocol) - 123
+ [x] [Quote of the Day](docs/ServerQOTD.md) (QOTD) - 17

Broadcast:
+ [ ] RSS
+ [ ] Web radio with user voting/suggestions

Cybersec:
+ [ ] I2P
+ [ ] TOR
+ [ ] VPN
+ [ ] SneakerNet

Domain/IP handling:
+ [ ] DNS (Domain Name System) - 53
+ [ ] DNS over TLS - 853
+ [ ] DNS over HTTPS - 443
+ [ ] NTP (Network Time Protocol) - 123
+ [ ] SNTP (Simple Network Time Protocol) - 123
+ [ ] DHCP (Dynamic Host Configuration Protocol) - 67/68


Mining/Nodes
+ [ ] Monero node
+ [ ] P2Pool monero mining
+ [ ] Signa plot farming


Contacts:
+ [ ] WebDAV (Web Distributed Authoring and Versioning) - 80/443
+ [ ] CalDAV (Calendaring Extensions to WebDAV) - 80/443
+ [ ] CardDAV (Address Book Extensions to WebDAV) - 80/443


Other services:
+ [ ] DMOZ web directory
+ [ ] AI chat
+ [ ] SearX search engine (or similar)
+ [ ] DICT https://datatracker.ietf.org/doc/html/rfc2229


Forum conversion
----------------
Independent web forums are disappearing due to server costs
and centralization of discussion groups. One of our goals is
to provide an easy conversion of the existing forums to the
nostr platform and provide a second life to them.

+ [ ] web crawl to convert web forums (Invision, PhpBB, MyBB)
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

Details about license and components for this platform
are documented at the [Bill of Materials](docs/bom.md).



Help wanted!
------------

Are you able to program in Java? You are welcome.
We need help implementing the features that you
see on the lists of this page.

Just open a ticket so we can talk about it.


Nostr compatibility
------------------

More details on the specific [NOSTR page](docs/ArchNOSTR.md).