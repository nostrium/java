# Server: Web

Launches a web server on port 80 (HTTP) and port 443 (HTTPS).

When you are launching in debug mode, these ports are 8000 and 4430 respectively.


# Shell

Nostrium isn't good graphic design, so we use command line and ASCII art almost everywhere.
Same concept applies to the web interface, it is mostly command line based.

The set of commands is the same as in other terminals, just type **help** to see
list of the available commands.


# Static files

Web server provides files to the world through a web browser, you can navigate
to the public spaces of users through their username. Inside the public space
you can host static HTML, mardown or binary files.

More details can be found at [WebPages](WebPages.md)


# SSL certificates

This platform is capable of self-certifying itself with Let's Encrypt.

You need to run the certificate generation using your administration account.

Then do the following two commands:

```
cd admin
ssl
```

Wait for a bit until the process is completed. You should now have SSL activated.