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


Planned interfaces:
+ NOSTR clients
+ web
+ telnet/SSH
+ IRC/telegram/SimpleX


To be implemented
-----------------
[] basic functionality
[] spam or manual approval to post on relay
[x] access through telnet
[] web crawl and convert older forums
[] admin tools to map older users to new nostr accounts


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

+ Java, anything above JDK 7
+ Linux or OSX (for development)
+ Maven

The code is developed using Netbeans IDE (any version)


Development
-----------

+ Download the source code to your computer
+ create an SSH key to log into a server as root
+ edit the deploy.sh script to match your domain
+ run the deploy script: sh deploy.sh

This will compile the java code, upload to the server
and then launch.

Please edit the config.json on your server the
"debug" option to "false".


License
-------

Apache License 2.0



