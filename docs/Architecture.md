# Nostrium Platform Architecture Overview

The architecture is roughly built around an abstraction layer that permits
connecting the Core of the platform with the different ways for user to
access and interact with it.

For example, when writing a new connection like telnet then it is needed to
have a Telnet server. When users connect to the server then they are talking
with a specific Screen adapted for Telnet. This means we can create bridge
connectors to the different platforms and expect interact in similar ways
between them.


```plaintext
+---------------------+    +---------------------+    +---------------------+
|                     |    |                     |    |                     |
|     Web Server      |    |    Telnet Server    |    |    Other Channels   |
|                     |    |                     |    |                     |
+---------------------+    +---------------------+    +---------------------+
          |                          |                        |
          |                          |                        |
          +--------------------------+------------------------+
                       |
                       v
             +---------------------+
             |                     |
             |       Screen        | <--- Adapter that bridges user channels
             |                     |
             +---------------------+
                       |
                       |
                       v
             +---------------------+
             |                     |
             |        Core         |
             |                     |
             | (Apps running here) |
             |                     |
             +---------------------+
                       ^
                       |
                       |
             +---------------------+
             |                     |
             |       Users         |
             |                     |
             +---------------------+
```


In addition to servers we use "bots", these are bridge channels where we
don't host a specific server but connect to an existing platform and our
bot will interact with users there on behalf of our platform. This can be
seen for example on the [telegram bot](BotTelegram.md).

-----------

# Writing a new bridge

When you want to connect this platform to a new protocol or platform,
you will need to setup the following:

+ Write a server or bot
+ Write a screen bridge


## Writing a server or bot

To write a server or bot, look on the */servers* source code folder. Inside
you find plenty of examples and one of them is likely similar to what you need.

Please keep in mind that we typically define two ports for servers. One port
that is used while debugging and another port that is used for production.

These port definitions can be found at */main/Config.java*.


## Writing a screen bridge

Inside the folder of each server you usually find an adapted Screen source code
file that is connected to that specific server.

*Attention:* Not all commands inside a template screen are always possible to
be implemented. For example, operations to clear the screen or delete the
current line are not always possible on interfaces like messengers. If you
look on the examples available, in some interfaces they are simply not
implemented and apps are usually created with that kind of limitation in mind.

-----------

# Apps

Apps are running inside the platform. This accept as input a user and screen
that should be used. The app will accept inputs from screen and again use 
this screen to provide an output that is adequated.

The apps are used for things like chat, blogs, forum, games and so forth.

To get started, you can look inside the source code folder *apps*.

After they are created, you have to decide where they are available to users.
For example, to add them on the root you need to add your app inside
the *apps/basic/TerminalBasic.java* source code file.

The basis point of view for apps is the command line interface. Within that
point of view should be considered the limitations that some bridges like
messengers do not permit specific actions like clearing the text on screen or
delete lines from previously outputted text.

Always keep the concept that you are working with a linux-like command line.


-----------


# Users

By default all visitors receive an account based on NOSTR with public and
private keys being generated. Even if they already have an account, it is
created by default because most bridges don't have a mechanism to specify
the user and password when accessing the platform.

Once inside the platform, it is possible to login to a previously created
account using the syntax *login <user> <password>* from the command line.

Or in alternative register that account using *register <user> <password>*.

Only when the account is registered that it will be saved permanently to
disk. Please keep in mind that inactive and empty accounts are periodically
removed to keep the disk space clean.

The unique identification of a user is based on NPUB (public key), the user
can sign events that prove his identity using the NSEC (private key). This
platform never stores the NSEC in plain text, it is encrypted using the
password defined by the user. Albeit this method isn't failsafe, it is a
compromise between confort and minimum security.

The source code defining the users can be found at */user/* and this includes
commands that are connected to user login/management.




