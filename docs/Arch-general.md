# General architecture

This page details the functioning of the Session block.

The platform is based on key components:

+ Person (which can be a registered user or just a visitor)
+ Channel (the medium used to talk with the platform)
+ Session (memory of what the user is doing or can do)
+ User / Visitor (all data related to the user)
+ Screen (abstraction to send/receive input through the channel)
+ Map (virtual map of files/apps that can be used)
+ Request/Result (actual request and result back to the person)
+ Events/Action (actions triggered by specific events)

With these blocks you connect different communication
channels using a template and can exchange data on both
directions.


# Person

The entry point are persons, humans that want to connect
with this platform. In some cases, the communication can
be automated through the usage of bots but typically we
assume our focus centered around human interaction.

Instead of asking these persons to install specific tools,
the platform aims to adapt to the tools used by them, and
to bridge that communication with other persons using other
channels of communication.


# Channel

Channel is the physical medium that is used for exchange
of data between the persons and the platform. It can either
be web, FTP, telnet as traditional methods. Or it can be
through other means like Telegram bots. Or even be from 
FM radio waves, or plain pieces of paper. Whatever channel
is used, means that it will be used for passing data
from one side to the other.


# Session

This is what holds the memory of each channel per user.

+ contains the Screen, User and Apps
+ can have time limit sessions and automatically recognize
people that access the plaform through different channels
+ distinguishes between logged persons or visitors
+ builds a view of what the type of user can see and use
+ permits multiple sessions of the same user, up to 10
+ builds a virtual map of files and apps that are reachable
+ can be saved to disk, using less CPU/time to restart
+ can define quotas and restrictions, for example the max
number of pages or requested within a minute

The session template is found at [Session.java](src/main/java/online/nostrium/session/Session.java).

You find examples of the usage at:
+ [SessionTest.java](src/test/java/basic/SessionTest.java)

