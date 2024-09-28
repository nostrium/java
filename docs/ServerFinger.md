# Server: Finger

Launches a finger server on port 79.

When you are launching in debug mode it will use port 7900 by default.

Finger is one of the oldest ways to know details about
a specific person/user on a server. On this platform we
support Finger requests based on username and eventually
based on npub key.

The reply is the list of public data for that user such
as public-facing email, npub, time of last login and any
type of messages the user wants to share.


# Access the finger server

From linux there is the "finger" command that you can
install on Ubuntu with *sudo apt install finger*. For
other operating systems and distributions, please look
for an adequated tool

After that, use the following syntax:

```
finger brito@nostrium.online
```

This is will query the server and provide information
about the user.
