```
                               ███                                               
 ████████    ██████   █████  ███████   ████████  ████  █████ ████ █████████████  
  ███  ███  ███  ███ ███       ███      ███  ███  ███   ███  ███   ███  ███  ███ 
  ███  ███  ███  ███  █████    ███      ███       ███   ███  ███   ███  ███  ███ 
  ███  ███  ███  ███     ███   ███ ███  ███       ███   ███  ███   ███  ███  ███ 
 ████ █████  ██████  ██████     █████  █████     █████   ████████ █████ ███ █████
	Nostr powered forum platform: https://github.com/nostrium

```

Nostrium is a nostr relay that provides a basic forum software.
Users can register using their usual nostr accounts (npub, nsec)
and can view forum topics, replies and everything you would
normally expect from a forum software.

All forum posts (nostr events) are stored on the filesystem
without using a database. This is intentional to permit the
forum to continue functioning even if part of the database
has become corrupted.

Another advantage is to permit the access to this forum
through different methods.

Planned methods:
+ NOSTR clients
+ web
+ telnet/SSH
+ telegram


To be implemented
-----------------
[] basic functionality
[] spam or manual approval to post on relay
[] access through telnet
[] web crawl and convert older forums
[] admin tools to map older users to new nostr accounts


Requisites
----------

+ Java, anything above JDK 7


License
-------

Apache License 2.0



