# Server: Telnet

Launches a FTP server on port 21.

When you are launching in debug mode it will use
port 2100 by default.

FTP means File Transport Protocol, it was created
in 1971 and documented on RFC114. It is typically
used for transferring files from a local computer
to a remote server and vice-versa.

More details are found on the [Wikipedia](https://en.wikipedia.org/wiki/File_Transfer_Protocol)
page.


# Access the FTP server

You will need an FTP client, there are plenty of
options for any operating system. Our recommendation
is to install [FileZilla](https://filezilla-project.org/)

After that, use the following details:

```
address: nostrium.online
login: <user name>
password: <user password>
```

For the moment the FTP connection is only permitting
registered users to upload/download files. In the future
we will permit anonymous downloads from the server.

# Privacy consideration

+ FTP is a clear text protocol. This means that anyone
listening to you network connection can intercept and
read your data in transit.