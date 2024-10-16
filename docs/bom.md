# Bill of Materials

This document provides a detailed Bill of Materials (BoM)
including metadata, component details, and licensing
information for the project.

We would like to thank the open source community for the
available software that permits this platform to exist.


Generated on 2024-10-16.

## Summary of Licenses Used by Components

```
    +------------------------------+-------+
    | License                      | Count |
    +------------------------------+-------+
    | Apache-2.0                   |   111 |
    | BSD-2-Clause                 |    49 |
    | MIT                          |    13 |
    | BSD-3-Clause                 |     8 |
    | EPL-1.0                      |     7 |
    | Bouncy Castle Licence        |     4 |
    | LGPL-2.1-or-later            |     4 |
    | GPL-2.0-with...th-exception  |     3 |
    | EPL-2.0                      |     2 |
    | BSD-4-Clause                 |     1 |
    | LGPL-2.1-only                |     1 |
    | Unicode/ICU License          |     1 |
    | CDDL-1.0                     |     1 |
    | Public Domain                |     1 |
    | GNU Lesser Public License    |     1 |
    | Go License                   |     1 |
    | CDDL-1.1                     |     1 |
    | MPL-1.1                      |     1 |
    | Modified BSD TelnetD license |     1 |
    +------------------------------+-------+
```

## Metadata

- BOM Format: CycloneDX
- Spec Version: 1.4
- Serial Number: urn:uuid:71827a67-def0-4410-bce5-209ff9878d67
- Version: 1
- Timestamp: 2024-10-15T23:13:48Z
- Tool: CycloneDX Maven plugin
- Component: nostrium
  - Version: 1.0.1
  - Description: The BBS for Nostr

## Summary of Source Code

- Number of source files: 246
- Lines of Code: 27610
- Overall project size: 812.89 KB
- 3rd party components: 224


# Detailed Components

+ acme4j 3.4.0
+ acme4j-utils 2.16 (Apache-2.0)
+ acme4j-client 2.16 (Apache-2.0)
+ jose4j 0.9.3 (Apache-2.0)
+ bcprov-jdk18on 1.72 (Bouncy Castle Licence)
+ bcpkix-jdk18on 1.72 (Bouncy Castle Licence)
+ bcutil-jdk18on 1.72 (Bouncy Castle Licence)
+ nostr-java-api 0.6.1-SNAPSHOT
+ nostr-java-base 0.6.1-SNAPSHOT
+ nostr-java-client 0.6.1-SNAPSHOT
+ nostr-java-connection 0.6.1-SNAPSHOT
+ nostr-java-controller 0.6.1-SNAPSHOT
+ nostr-java-encryption 0.6.1-SNAPSHOT
+ nostr-java-encryption-nip04 0.6.1-SNAPSHOT
+ nostr-java-encryption-nip44 0.6.1-SNAPSHOT
+ nostr-java-command-provider 0.6.1-SNAPSHOT
+ nostr-java-context 0.6.1-SNAPSHOT
+ nostr-java-command-interface 0.6.1-SNAPSHOT
+ nostr-java-event 0.6.1-SNAPSHOT
+ openbeans 1.0.2 (Apache-2.0)
+ lombok 1.18.30 (MIT)
+ nostr-java-util 0.6.1-SNAPSHOT
+ nostr-java-context-interface 0.6.1-SNAPSHOT
+ nostr-java-id 0.6.1-SNAPSHOT
+ nostr-java-crypto 0.6.1-SNAPSHOT
+ netty-all 4.1.97.Final (Apache-2.0)
+ netty-buffer 4.1.97.Final (Apache-2.0)
+ netty-codec 4.1.97.Final (Apache-2.0)
+ netty-codec-dns 4.1.97.Final (Apache-2.0)
+ netty-codec-haproxy 4.1.97.Final (Apache-2.0)
+ netty-codec-http 4.1.97.Final (Apache-2.0)
+ netty-codec-http2 4.1.97.Final (Apache-2.0)
+ netty-codec-memcache 4.1.97.Final (Apache-2.0)
+ netty-codec-mqtt 4.1.97.Final (Apache-2.0)
+ netty-codec-redis 4.1.97.Final (Apache-2.0)
+ netty-codec-smtp 4.1.97.Final (Apache-2.0)
+ netty-codec-socks 4.1.97.Final (Apache-2.0)
+ netty-codec-stomp 4.1.97.Final (Apache-2.0)
+ netty-codec-xml 4.1.97.Final (Apache-2.0)
+ netty-common 4.1.97.Final (Apache-2.0)
+ netty-handler 4.1.97.Final (Apache-2.0)
+ netty-transport-native-unix-common 4.1.97.Final (Apache-2.0)
+ netty-handler-proxy 4.1.97.Final (Apache-2.0)
+ netty-handler-ssl-ocsp 4.1.97.Final (Apache-2.0)
+ netty-resolver 4.1.97.Final (Apache-2.0)
+ netty-resolver-dns 4.1.97.Final (Apache-2.0)
+ netty-transport 4.1.97.Final (Apache-2.0)
+ netty-transport-rxtx 4.1.97.Final (Apache-2.0)
+ netty-transport-sctp 4.1.97.Final (Apache-2.0)
+ netty-transport-udt 4.1.97.Final (Apache-2.0)
+ netty-transport-classes-epoll 4.1.97.Final (Apache-2.0)
+ netty-transport-classes-kqueue 4.1.97.Final (Apache-2.0)
+ netty-resolver-dns-classes-macos 4.1.97.Final (Apache-2.0)
+ netty-transport-native-epoll 4.1.97.Final (Apache-2.0)
+ netty-transport-native-epoll 4.1.97.Final (Apache-2.0)
+ netty-transport-native-kqueue 4.1.97.Final (Apache-2.0)
+ netty-transport-native-kqueue 4.1.97.Final (Apache-2.0)
+ netty-resolver-dns-native-macos 4.1.97.Final (Apache-2.0)
+ netty-resolver-dns-native-macos 4.1.97.Final (Apache-2.0)
+ bcprov-jdk15on 1.68 (Bouncy Castle Licence)
+ takes 1.24.4 (MIT)
+ cactoos 0.54.0 (MIT)
+ jaxb-api 2.4.0-b180830.0359 (CDDL-1.1, GPL-2.0-with-classpath-exception)
+ javax.activation-api 1.2.0
+ jaxb-core 4.0.0 (BSD-3-Clause)
+ jakarta.xml.bind-api 4.0.0 (BSD-3-Clause)
+ jaxb-impl 4.0.0 (BSD-3-Clause)
+ gson 2.10.1 (Apache-2.0)
+ commons-io 2.7 (Apache-2.0)
+ commons-lang3 3.12.0 (Apache-2.0)
+ jsoup 1.15.3 (MIT)
+ flexmark-all 0.64.0 (BSD-2-Clause)
+ flexmark 0.64.0 (BSD-2-Clause)
+ flexmark-ext-abbreviation 0.64.0 (BSD-2-Clause)
+ flexmark-util 0.64.0 (BSD-2-Clause)
+ flexmark-ext-admonition 0.64.0 (BSD-2-Clause)
+ flexmark-ext-anchorlink 0.64.0 (BSD-2-Clause)
+ flexmark-ext-aside 0.64.0 (BSD-2-Clause)
+ flexmark-ext-attributes 0.64.0 (BSD-2-Clause)
+ flexmark-ext-autolink 0.64.0 (BSD-2-Clause)
+ autolink 0.6.0 (MIT)
+ flexmark-ext-definition 0.64.0 (BSD-2-Clause)
+ flexmark-ext-emoji 0.64.0 (BSD-2-Clause)
+ flexmark-ext-enumerated-reference 0.64.0 (BSD-2-Clause)
+ flexmark-ext-escaped-character 0.64.0 (BSD-2-Clause)
+ flexmark-ext-footnotes 0.64.0 (BSD-2-Clause)
+ flexmark-ext-gfm-issues 0.64.0 (BSD-2-Clause)
+ flexmark-ext-gfm-strikethrough 0.64.0 (BSD-2-Clause)
+ flexmark-ext-gfm-tasklist 0.64.0 (BSD-2-Clause)
+ flexmark-ext-gfm-users 0.64.0 (BSD-2-Clause)
+ flexmark-ext-gitlab 0.64.0 (BSD-2-Clause)
+ flexmark-ext-jekyll-front-matter 0.64.0 (BSD-2-Clause)
+ flexmark-ext-jekyll-tag 0.64.0 (BSD-2-Clause)
+ flexmark-ext-media-tags 0.64.0 (BSD-2-Clause)
+ flexmark-ext-resizable-image 0.64.0 (BSD-2-Clause)
+ flexmark-ext-macros 0.64.0 (BSD-2-Clause)
+ flexmark-ext-ins 0.64.0 (BSD-2-Clause)
+ flexmark-ext-xwiki-macros 0.64.0 (BSD-2-Clause)
+ flexmark-ext-superscript 0.64.0 (BSD-2-Clause)
+ flexmark-ext-tables 0.64.0 (BSD-2-Clause)
+ flexmark-ext-toc 0.64.0 (BSD-2-Clause)
+ flexmark-ext-typographic 0.64.0 (BSD-2-Clause)
+ flexmark-ext-wikilink 0.64.0 (BSD-2-Clause)
+ flexmark-ext-yaml-front-matter 0.64.0 (BSD-2-Clause)
+ flexmark-ext-youtube-embedded 0.64.0 (BSD-2-Clause)
+ flexmark-html2md-converter 0.64.0 (BSD-2-Clause)
+ flexmark-jira-converter 0.64.0 (BSD-2-Clause)
+ flexmark-pdf-converter 0.64.0 (BSD-2-Clause)
+ openhtmltopdf-core 1.0.10 (LGPL-2.1-or-later)
+ openhtmltopdf-pdfbox 1.0.10 (LGPL-2.1-or-later)
+ pdfbox 2.0.24 (Apache-2.0)
+ fontbox 2.0.24 (Apache-2.0)
+ xmpbox 2.0.24 (Apache-2.0)
+ graphics2d 0.32 (Apache-2.0)
+ openhtmltopdf-rtl-support 1.0.10 (LGPL-2.1-or-later)
+ icu4j 59.1 (Unicode/ICU License)
+ openhtmltopdf-jsoup-dom-converter 1.0.0 (LGPL-2.1-or-later)
+ flexmark-profile-pegdown 0.64.0 (BSD-2-Clause)
+ flexmark-util-ast 0.64.0 (BSD-2-Clause)
+ annotations 15.0 (Apache-2.0)
+ flexmark-util-builder 0.64.0 (BSD-2-Clause)
+ flexmark-util-collection 0.64.0 (BSD-2-Clause)
+ flexmark-util-data 0.64.0 (BSD-2-Clause)
+ flexmark-util-dependency 0.64.0 (BSD-2-Clause)
+ flexmark-util-format 0.64.0 (BSD-2-Clause)
+ flexmark-util-html 0.64.0 (BSD-2-Clause)
+ flexmark-util-misc 0.64.0 (BSD-2-Clause)
+ flexmark-util-options 0.64.0 (BSD-2-Clause)
+ flexmark-util-sequence 0.64.0 (BSD-2-Clause)
+ flexmark-util-visitor 0.64.0 (BSD-2-Clause)
+ flexmark-youtrack-converter 0.64.0 (BSD-2-Clause)
+ core 3.4.1 (Apache-2.0)
+ sshd-core 2.8.0 (Apache-2.0)
+ sshd-common 2.8.0 (Apache-2.0)
+ jcl-over-slf4j 1.7.32 (Apache-2.0)
+ sshd-netty 2.8.0 (Apache-2.0)
+ sshd-sftp 2.8.0 (Apache-2.0)
+ slf4j-simple 2.0.7 (MIT)
+ greenmail 2.1.0-rc-1 (Apache-2.0)
+ jakarta.mail-api 2.1.3 (EPL-2.0, GPL-2.0-with-classpath-exception, BSD-3-Clause)
+ jakarta.mail 2.0.3 (EPL-2.0, GPL-2.0-with-classpath-exception, BSD-3-Clause)
+ jakarta.activation-api 2.1.3 (BSD-3-Clause)
+ angus-activation 2.0.2 (BSD-3-Clause)
+ junit 4.13.2 (EPL-1.0)
+ exp4j 0.4.8 (Apache-2.0)
+ commons-jexl3 3.2 (Apache-2.0)
+ commons-logging 1.2 (Apache-2.0)
+ jlama-core 0.3.1 (Apache-2.0)
+ jackson-databind 2.15.2 (Apache-2.0)
+ jackson-annotations 2.15.2 (Apache-2.0)
+ jackson-core 2.15.2 (Apache-2.0)
+ guava 32.0.1-jre (Apache-2.0)
+ failureaccess 1.0.1 (Apache-2.0)
+ listenablefuture 9999.0-empty-to-avoid-conflict-with-guava (Apache-2.0)
+ jsr305 3.0.2 (Apache-2.0)
+ checker-qual 3.33.0 (MIT)
+ error_prone_annotations 2.18.0 (Apache-2.0)
+ j2objc-annotations 2.8 (Apache-2.0)
+ jctools-core 4.0.1 (Apache-2.0)
+ jinjava 2.7.2
+ javassist 3.24.1-GA (MPL-1.1, LGPL-2.1-only, Apache-2.0)
+ re2j 1.2 (Go License)
+ commons-net 3.9.0 (Apache-2.0)
+ java-ipv6 0.17 (Apache-2.0)
+ annotations 3.0.1 (GNU Lesser Public License)
+ jackson-dataformat-yaml 2.14.0 (Apache-2.0)
+ snakeyaml 1.33 (Apache-2.0)
+ big-math 2.0.0 (MIT)
+ jemoji 1.4.1 (Apache-2.0)
+ jspecify 0.3.0 (Apache-2.0)
+ mvel2 2.4.7.Final (Apache-2.0)
+ telegrambots-client 7.10.0 (MIT)
+ telegrambots-meta 7.10.0 (MIT)
+ jackson-datatype-jsr310 2.17.2 (Apache-2.0)
+ okhttp 4.12.0 (Apache-2.0)
+ okio 3.6.0 (Apache-2.0)
+ okio-jvm 3.6.0 (Apache-2.0)
+ kotlin-stdlib-common 1.9.10 (Apache-2.0)
+ kotlin-stdlib-jdk8 1.8.21 (Apache-2.0)
+ kotlin-stdlib 1.8.21 (Apache-2.0)
+ kotlin-stdlib-jdk7 1.8.21 (Apache-2.0)
+ telegrambots-longpolling 7.10.0 (MIT)
+ slf4j-api 2.0.7 (MIT)
+ minimalftp 1.0.6 (Apache-2.0)
+ cyclonedx-maven-plugin 2.7.9 (Apache-2.0)
+ cyclonedx-core-java 7.3.2 (Apache-2.0)
+ packageurl-java 1.4.1 (MIT)
+ jackson-dataformat-xml 2.14.2 (Apache-2.0)
+ stax2-api 4.2.1 (BSD-4-Clause)
+ woodstox-core 6.5.0 (Apache-2.0)
+ json-schema-validator 1.0.77 (Apache-2.0)
+ itu 1.7.0 (Apache-2.0)
+ commons-codec 1.15 (Apache-2.0)
+ maven-dependency-tree 3.2.1 (Apache-2.0)
+ aether-util 1.0.0.v20140518 (EPL-1.0)
+ aether-api 1.0.0.v20140518 (EPL-1.0)
+ maven-dependency-analyzer 1.13.2 (Apache-2.0)
+ maven-core 3.2.5 (Apache-2.0)
+ maven-settings 3.2.5 (Apache-2.0)
+ maven-settings-builder 3.2.5 (Apache-2.0)
+ maven-repository-metadata 3.2.5 (Apache-2.0)
+ maven-plugin-api 3.2.5 (Apache-2.0)
+ maven-model-builder 3.2.5 (Apache-2.0)
+ maven-aether-provider 3.2.5 (Apache-2.0)
+ aether-spi 1.0.0.v20140518 (EPL-1.0)
+ aether-impl 1.0.0.v20140518 (EPL-1.0)
+ org.eclipse.sisu.plexus 0.3.0.M1 (EPL-1.0)
+ cdi-api 1.0 (Apache-2.0)
+ jsr250-api 1.0 (CDDL-1.0)
+ org.eclipse.sisu.inject 0.3.0.M1 (EPL-1.0)
+ sisu-guice 3.2.3 (Apache-2.0)
+ aopalliance 1.0 (Public Domain)
+ plexus-interpolation 1.21 (Apache-2.0)
+ plexus-utils 3.0.20 (Apache-2.0)
+ plexus-classworlds 2.5.2 (Apache-2.0)
+ plexus-component-annotations 1.5.5 (Apache-2.0)
+ plexus-sec-dispatcher 1.3 (Apache-2.0)
+ plexus-cipher 1.4 (Apache-2.0)
+ javax.inject 1 (Apache-2.0)
+ asm 9.5 (BSD-3-Clause)
+ tika-core 2.9.2 (Apache-2.0)
+ mina-core 2.1.5 (Apache-2.0)
+ telnetd-x 2.1.1 (Modified BSD TelnetD license)
+ log4j 1.2.9

### 1. acme4j
- Group: org.shredzone.acme4j
- Type: library
- Version: 3.4.0
- PURL: pkg:maven/org.shredzone.acme4j/acme4j@3.4.0?type=pom

### 2. acme4j-utils
- Group: org.shredzone.acme4j
- Type: library
- Version: 2.16
- PURL: pkg:maven/org.shredzone.acme4j/acme4j-utils@2.16?type=jar
- Description: acme4j utilities
- License: Apache-2.0

### 3. acme4j-client
- Group: org.shredzone.acme4j
- Type: library
- Version: 2.16
- PURL: pkg:maven/org.shredzone.acme4j/acme4j-client@2.16?type=jar
- Description: ACME client for Java
- License: Apache-2.0

### 4. jose4j
- Group: org.bitbucket.b_c
- Type: library
- Version: 0.9.3
- PURL: pkg:maven/org.bitbucket.b_c/jose4j@0.9.3?type=jar
- Description: The jose.4.j library is a robust and easy to use open source implementation of JSON Web Token (JWT) and the JOSE specification suite (JWS, JWE, and JWK). It is written in Java and relies solely on the JCA APIs for cryptography. Please see https://bitbucket.org/b_c/jose4j/wiki/Home for more info, examples, etc..
- License: Apache-2.0

### 5. bcprov-jdk18on
- Group: org.bouncycastle
- Type: library
- Version: 1.72
- PURL: pkg:maven/org.bouncycastle/bcprov-jdk18on@1.72?type=jar
- Description: The Bouncy Castle Crypto package is a Java implementation of cryptographic algorithms. This jar contains JCE provider and lightweight API for the Bouncy Castle Cryptography APIs for JDK 1.8 and up.
- License: Bouncy Castle Licence

### 6. bcpkix-jdk18on
- Group: org.bouncycastle
- Type: library
- Version: 1.72
- PURL: pkg:maven/org.bouncycastle/bcpkix-jdk18on@1.72?type=jar
- Description: The Bouncy Castle Java APIs for CMS, PKCS, EAC, TSP, CMP, CRMF, OCSP, and certificate generation. This jar contains APIs for JDK 1.8 and up. The APIs can be used in conjunction with a JCE/JCA provider such as the one provided with the Bouncy Castle Cryptography APIs.
- License: Bouncy Castle Licence

### 7. bcutil-jdk18on
- Group: org.bouncycastle
- Type: library
- Version: 1.72
- PURL: pkg:maven/org.bouncycastle/bcutil-jdk18on@1.72?type=jar
- Description: The Bouncy Castle Java APIs for ASN.1 extension and utility APIs used to support bcpkix and bctls. This jar contains APIs for JDK 1.8 and up.
- License: Bouncy Castle Licence

### 8. nostr-java-api
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-api@0.6.1-SNAPSHOT?type=jar

### 9. nostr-java-base
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-base@0.6.1-SNAPSHOT?type=jar

### 10. nostr-java-client
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-client@0.6.1-SNAPSHOT?type=jar

### 11. nostr-java-connection
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-connection@0.6.1-SNAPSHOT?type=jar

### 12. nostr-java-controller
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-controller@0.6.1-SNAPSHOT?type=jar

### 13. nostr-java-encryption
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-encryption@0.6.1-SNAPSHOT?type=jar

### 14. nostr-java-encryption-nip04
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-encryption-nip04@0.6.1-SNAPSHOT?type=jar

### 15. nostr-java-encryption-nip44
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-encryption-nip44@0.6.1-SNAPSHOT?type=jar

### 16. nostr-java-command-provider
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-command-provider@0.6.1-SNAPSHOT?type=jar

### 17. nostr-java-context
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-context@0.6.1-SNAPSHOT?type=jar

### 18. nostr-java-command-interface
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-command-interface@0.6.1-SNAPSHOT?type=jar

### 19. nostr-java-event
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-event@0.6.1-SNAPSHOT?type=jar

### 20. openbeans
- Group: me.champeau.openbeans
- Type: library
- Version: 1.0.2
- PURL: pkg:maven/me.champeau.openbeans/openbeans@1.0.2?type=jar
- Description: Openbeans for Android
- License: Apache-2.0

### 21. lombok
- Group: org.projectlombok
- Type: library
- Version: 1.18.30
- PURL: pkg:maven/org.projectlombok/lombok@1.18.30?type=jar
- Description: Spice up your java: Automatic Resource Management, automatic generation of getters, setters, equals, hashCode and toString, and more!
- License: MIT

### 22. nostr-java-util
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-util@0.6.1-SNAPSHOT?type=jar

### 23. nostr-java-context-interface
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-context-interface@0.6.1-SNAPSHOT?type=jar

### 24. nostr-java-id
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-id@0.6.1-SNAPSHOT?type=jar

### 25. nostr-java-crypto
- Group: nostr-java
- Type: library
- Version: 0.6.1-SNAPSHOT
- PURL: pkg:maven/nostr-java/nostr-java-crypto@0.6.1-SNAPSHOT?type=jar
- Description: A simple Java implementation (no external libs) of Sipa's Python reference implementation test vectors for BIP340 Schnorr signatures for secp256k1. Sources: https://code.samourai.io/samouraidev/BIP340_Schnorr and https://github.com/unclebob/more-speech/tree/bdd2f32b37264f20bf6abb4887489e70d2b0fdf1

### 26. netty-all
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-all@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 27. netty-buffer
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-buffer@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 28. netty-codec
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-codec@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 29. netty-codec-dns
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-codec-dns@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 30. netty-codec-haproxy
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-codec-haproxy@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 31. netty-codec-http
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-codec-http@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 32. netty-codec-http2
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-codec-http2@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 33. netty-codec-memcache
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-codec-memcache@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 34. netty-codec-mqtt
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-codec-mqtt@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 35. netty-codec-redis
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-codec-redis@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 36. netty-codec-smtp
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-codec-smtp@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 37. netty-codec-socks
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-codec-socks@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 38. netty-codec-stomp
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-codec-stomp@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 39. netty-codec-xml
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-codec-xml@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 40. netty-common
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-common@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 41. netty-handler
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-handler@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 42. netty-transport-native-unix-common
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-transport-native-unix-common@4.1.97.Final?type=jar
- Description: Static library which contains common unix utilities.
- License: Apache-2.0

### 43. netty-handler-proxy
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-handler-proxy@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 44. netty-handler-ssl-ocsp
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-handler-ssl-ocsp@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 45. netty-resolver
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-resolver@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 46. netty-resolver-dns
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-resolver-dns@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 47. netty-transport
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-transport@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 48. netty-transport-rxtx
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-transport-rxtx@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 49. netty-transport-sctp
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-transport-sctp@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 50. netty-transport-udt
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-transport-udt@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 51. netty-transport-classes-epoll
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-transport-classes-epoll@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 52. netty-transport-classes-kqueue
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-transport-classes-kqueue@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 53. netty-resolver-dns-classes-macos
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-resolver-dns-classes-macos@4.1.97.Final?type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 54. netty-transport-native-epoll
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-transport-native-epoll@4.1.97.Final?classifier=linux-x86_64&type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 55. netty-transport-native-epoll
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-transport-native-epoll@4.1.97.Final?classifier=linux-aarch_64&type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 56. netty-transport-native-kqueue
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-transport-native-kqueue@4.1.97.Final?classifier=osx-x86_64&type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 57. netty-transport-native-kqueue
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-transport-native-kqueue@4.1.97.Final?classifier=osx-aarch_64&type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 58. netty-resolver-dns-native-macos
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-resolver-dns-native-macos@4.1.97.Final?classifier=osx-x86_64&type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 59. netty-resolver-dns-native-macos
- Group: io.netty
- Type: library
- Version: 4.1.97.Final
- PURL: pkg:maven/io.netty/netty-resolver-dns-native-macos@4.1.97.Final?classifier=osx-aarch_64&type=jar
- Description: Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers and clients.
- License: Apache-2.0

### 60. bcprov-jdk15on
- Group: org.bouncycastle
- Type: library
- Version: 1.68
- PURL: pkg:maven/org.bouncycastle/bcprov-jdk15on@1.68?type=jar
- Description: The Bouncy Castle Crypto package is a Java implementation of cryptographic algorithms. This jar contains JCE provider and lightweight API for the Bouncy Castle Cryptography APIs for JDK 1.5 and up.
- License: Bouncy Castle Licence

### 61. takes
- Group: org.takes
- Type: library
- Version: 1.24.4
- PURL: pkg:maven/org.takes/takes@1.24.4?type=jar
- Description: True Object-Oriented and Immutable Java Web Framework
- License: MIT

### 62. cactoos
- Group: org.cactoos
- Type: library
- Version: 0.54.0
- PURL: pkg:maven/org.cactoos/cactoos@0.54.0?type=jar
- Description: Object-Oriented Java Primitives
- License: MIT

### 63. jaxb-api
- Group: javax.xml.bind
- Type: library
- Version: 2.4.0-b180830.0359
- PURL: pkg:maven/javax.xml.bind/jaxb-api@2.4.0-b180830.0359?type=jar
- Description: JAXB (JSR 222) API
- License: CDDL-1.1, GPL-2.0-with-classpath-exception

### 64. javax.activation-api
- Group: javax.activation
- Type: library
- Version: 1.2.0
- PURL: pkg:maven/javax.activation/javax.activation-api@1.2.0?type=jar
- Description: JavaBeans Activation Framework API jar

### 65. jaxb-core
- Group: com.sun.xml.bind
- Type: library
- Version: 4.0.0
- PURL: pkg:maven/com.sun.xml.bind/jaxb-core@4.0.0?type=jar
- Description: Old JAXB Core module. Contains sources required by XJC, JXC and Runtime modules with dependencies.
- License: BSD-3-Clause

### 66. jakarta.xml.bind-api
- Group: jakarta.xml.bind
- Type: library
- Version: 4.0.0
- PURL: pkg:maven/jakarta.xml.bind/jakarta.xml.bind-api@4.0.0?type=jar
- Description: Jakarta XML Binding API
- License: BSD-3-Clause

### 67. jaxb-impl
- Group: com.sun.xml.bind
- Type: library
- Version: 4.0.0
- PURL: pkg:maven/com.sun.xml.bind/jaxb-impl@4.0.0?type=jar
- Description: Old JAXB Runtime module. Contains sources required for runtime processing.
- License: BSD-3-Clause

### 68. gson
- Group: com.google.code.gson
- Type: library
- Version: 2.10.1
- PURL: pkg:maven/com.google.code.gson/gson@2.10.1?type=jar
- Description: Gson JSON library
- License: Apache-2.0

### 69. commons-io
- Group: commons-io
- Type: library
- Version: 2.7
- PURL: pkg:maven/commons-io/commons-io@2.7?type=jar
- Description: The Apache Commons IO library contains utility classes, stream implementations, file filters, file comparators, endian transformation classes, and much more.
- License: Apache-2.0

### 70. commons-lang3
- Group: org.apache.commons
- Type: library
- Version: 3.12.0
- PURL: pkg:maven/org.apache.commons/commons-lang3@3.12.0?type=jar
- Description: Apache Commons Lang, a package of Java utility classes for the classes that are in java.lang's hierarchy, or are considered to be so standard as to justify existence in java.lang.
- License: Apache-2.0

### 71. jsoup
- Group: org.jsoup
- Type: library
- Version: 1.15.3
- PURL: pkg:maven/org.jsoup/jsoup@1.15.3?type=jar
- Description: jsoup is a Java library for working with real-world HTML. It provides a very convenient API for fetching URLs and extracting and manipulating data, using the best of HTML5 DOM methods and CSS selectors. jsoup implements the WHATWG HTML5 specification, and parses HTML to the same DOM as modern browsers do.
- License: MIT

### 72. flexmark-all
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-all@0.64.0?classifier=lib&type=jar
- Description: flexmark-java core and all extension modules and converters
- License: BSD-2-Clause

### 73. flexmark
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark@0.64.0?type=jar
- Description: Core of flexmark-java (implementation of CommonMark for parsing markdown and rendering to HTML)
- License: BSD-2-Clause

### 74. flexmark-ext-abbreviation
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-abbreviation@0.64.0?type=jar
- Description: flexmark-java extension for defining abbreviations and turning appearance of these abbreviations in text into links with titles consisting of the expansion of the abbreviation
- License: BSD-2-Clause

### 75. flexmark-util
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-util@0.64.0?type=jar
- Description: flexmark-java utility classes
- License: BSD-2-Clause

### 76. flexmark-ext-admonition
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-admonition@0.64.0?type=jar
- Description: flexmark-java extension for admonition syntax
- License: BSD-2-Clause

### 77. flexmark-ext-anchorlink
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-anchorlink@0.64.0?type=jar
- Description: flexmark-java extension for generating anchor links for headings using GitHub compatible algorithm
- License: BSD-2-Clause

### 78. flexmark-ext-aside
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-aside@0.64.0?type=jar
- Description: flexmark-java extension for converting | to aside tags
- License: BSD-2-Clause

### 79. flexmark-ext-attributes
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-attributes@0.64.0?type=jar
- Description: flexmark-java extension for attributes
- License: BSD-2-Clause

### 80. flexmark-ext-autolink
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-autolink@0.64.0?type=jar
- Description: flexmark-java extension for turning plain URLs and email addresses into links
- License: BSD-2-Clause

### 81. autolink
- Group: org.nibor.autolink
- Type: library
- Version: 0.6.0
- PURL: pkg:maven/org.nibor.autolink/autolink@0.6.0?type=jar
- Description: Java library to extract links (URLs, email addresses) from plain text; fast, small and smart about recognizing where links end
- License: MIT

### 82. flexmark-ext-definition
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-definition@0.64.0?type=jar
- Description: flexmark-java extension for definition
- License: BSD-2-Clause

### 83. flexmark-ext-emoji
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-emoji@0.64.0?type=jar
- Description: flexmark-java extension for emoji shortcuts using Emoji-Cheat-Sheet.com http://www.emoji-cheat-sheet.com/
- License: BSD-2-Clause

### 84. flexmark-ext-enumerated-reference
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-enumerated-reference@0.64.0?type=jar
- Description: flexmark-java extension for enumerated reference processing
- License: BSD-2-Clause

### 85. flexmark-ext-escaped-character
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-escaped-character@0.64.0?type=jar
- Description: flexmark-java extension for escaped_character
- License: BSD-2-Clause

### 86. flexmark-ext-footnotes
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-footnotes@0.64.0?type=jar
- Description: flexmark-java extension for footnote inline elments and footnote definitions
- License: BSD-2-Clause

### 87. flexmark-ext-gfm-issues
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-gfm-issues@0.64.0?type=jar
- Description: flexmark-java extension for GitHub issue syntax
- License: BSD-2-Clause

### 88. flexmark-ext-gfm-strikethrough
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-gfm-strikethrough@0.64.0?type=jar
- Description: flexmark-java extension for GFM strikethrough using ~~ (GitHub Flavored Markdown)
- License: BSD-2-Clause

### 89. flexmark-ext-gfm-tasklist
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-gfm-tasklist@0.64.0?type=jar
- Description: flexmark-java extension to convert bullet list items that start with [ ] to a TaskListItem node
- License: BSD-2-Clause

### 90. flexmark-ext-gfm-users
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-gfm-users@0.64.0?type=jar
- Description: flexmark-java extension for GitHub user syntax
- License: BSD-2-Clause

### 91. flexmark-ext-gitlab
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-gitlab@0.64.0?type=jar
- Description: flexmark-java extension for GitLab Flavoured Markdown
- License: BSD-2-Clause

### 92. flexmark-ext-jekyll-front-matter
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-jekyll-front-matter@0.64.0?type=jar
- Description: flexmark-java extension for jekyll_front_matter
- License: BSD-2-Clause

### 93. flexmark-ext-jekyll-tag
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-jekyll-tag@0.64.0?type=jar
- Description: flexmark-java extension for jekyll tag parsing
- License: BSD-2-Clause

### 94. flexmark-ext-media-tags
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-media-tags@0.64.0?type=jar
- Description: flexmark-java extension parsing and rendering HTML5 media tags
- License: BSD-2-Clause

### 95. flexmark-ext-resizable-image
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-resizable-image@0.64.0?type=jar
- Description: flexmark-java extension to set the size of the images
- License: BSD-2-Clause

### 96. flexmark-ext-macros
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-macros@0.64.0?type=jar
- Description: flexmark-java extension for processing macros
- License: BSD-2-Clause

### 97. flexmark-ext-ins
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-ins@0.64.0?type=jar
- Description: flexmark-java extension for ins
- License: BSD-2-Clause

### 98. flexmark-ext-xwiki-macros
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-xwiki-macros@0.64.0?type=jar
- Description: flexmark-java extension for xwiki application specific macros
- License: BSD-2-Clause

### 99. flexmark-ext-superscript
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-superscript@0.64.0?type=jar
- Description: flexmark-java extension for superscript
- License: BSD-2-Clause

### 100. flexmark-ext-tables
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-tables@0.64.0?type=jar
- Description: flexmark-java extension for tables using "|" pipes with optional column spans and table caption
- License: BSD-2-Clause

### 101. flexmark-ext-toc
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-toc@0.64.0?type=jar
- Description: flexmark-java extension for toc
- License: BSD-2-Clause

### 102. flexmark-ext-typographic
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-typographic@0.64.0?type=jar
- Description: flexmark-java extension for typographic
- License: BSD-2-Clause

### 103. flexmark-ext-wikilink
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-wikilink@0.64.0?type=jar
- Description: flexmark-java extension parsing and rendering wiki links
- License: BSD-2-Clause

### 104. flexmark-ext-yaml-front-matter
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-yaml-front-matter@0.64.0?type=jar
- Description: flexmark-java extension for YAML front matter
- License: BSD-2-Clause

### 105. flexmark-ext-youtube-embedded
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-ext-youtube-embedded@0.64.0?type=jar
- Description: Java re-implementation of commonmark-java based parser, with AST reflecting source elements, full source position tracking, greater parser extensibility.
- License: BSD-2-Clause

### 106. flexmark-html2md-converter
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-html2md-converter@0.64.0?type=jar
- Description: flexmark-java customizable extension to convert HTML to Markdown
- License: BSD-2-Clause

### 107. flexmark-jira-converter
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-jira-converter@0.64.0?type=jar
- Description: flexmark-java extension for jira_converter
- License: BSD-2-Clause

### 108. flexmark-pdf-converter
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-pdf-converter@0.64.0?type=jar
- Description: flexmark-java extension for markdown to pdf conversion
- License: BSD-2-Clause

### 109. openhtmltopdf-core
- Group: com.openhtmltopdf
- Type: library
- Version: 1.0.10
- PURL: pkg:maven/com.openhtmltopdf/openhtmltopdf-core@1.0.10?type=jar
- Description: Open HTML to PDF is a CSS 2.1 renderer written in Java. This artifact contains the core rendering and layout code.
- License: LGPL-2.1-or-later

### 110. openhtmltopdf-pdfbox
- Group: com.openhtmltopdf
- Type: library
- Version: 1.0.10
- PURL: pkg:maven/com.openhtmltopdf/openhtmltopdf-pdfbox@1.0.10?type=jar
- Description: Openhtmltopdf is a CSS 2.1 renderer written in Java. This artifact supports PDF output with Apache PDF-BOX 2.
- License: LGPL-2.1-or-later

### 111. pdfbox
- Group: org.apache.pdfbox
- Type: library
- Version: 2.0.24
- PURL: pkg:maven/org.apache.pdfbox/pdfbox@2.0.24?type=jar
- Description: The Apache PDFBox library is an open source Java tool for working with PDF documents.
- License: Apache-2.0

### 112. fontbox
- Group: org.apache.pdfbox
- Type: library
- Version: 2.0.24
- PURL: pkg:maven/org.apache.pdfbox/fontbox@2.0.24?type=jar
- Description: The Apache FontBox library is an open source Java tool to obtain low level information from font files. FontBox is a subproject of Apache PDFBox.
- License: Apache-2.0

### 113. xmpbox
- Group: org.apache.pdfbox
- Type: library
- Version: 2.0.24
- PURL: pkg:maven/org.apache.pdfbox/xmpbox@2.0.24?type=jar
- Description: The Apache XmpBox library is an open source Java tool that implements Adobe's XMP(TM) specification. It can be used to parse, validate and create xmp contents. It is mainly used by subproject preflight of Apache PDFBox. XmpBox is a subproject of Apache PDFBox.
- License: Apache-2.0

### 114. graphics2d
- Group: de.rototor.pdfbox
- Type: library
- Version: 0.32
- PURL: pkg:maven/de.rototor.pdfbox/graphics2d@0.32?type=jar
- Description: Graphics2D Bridge for Apache PDFBox
- License: Apache-2.0

### 115. openhtmltopdf-rtl-support
- Group: com.openhtmltopdf
- Type: library
- Version: 1.0.10
- PURL: pkg:maven/com.openhtmltopdf/openhtmltopdf-rtl-support@1.0.10?type=jar
- Description: Open HTML to PDF is a CSS 2.1 renderer written in Java. This artifact supports right-to-left text mixed with left-to-right text.
- License: LGPL-2.1-or-later

### 116. icu4j
- Group: com.ibm.icu
- Type: library
- Version: 59.1
- PURL: pkg:maven/com.ibm.icu/icu4j@59.1?type=jar
- Description: International Component for Unicode for Java (ICU4J) is a mature, widely used Java library providing Unicode and Globalization support
- License: Unicode/ICU License

### 117. openhtmltopdf-jsoup-dom-converter
- Group: com.openhtmltopdf
- Type: library
- Version: 1.0.0
- PURL: pkg:maven/com.openhtmltopdf/openhtmltopdf-jsoup-dom-converter@1.0.0?type=jar
- Description: DEPRECATED MODULE FOR REMOVAL: Use Jsoup provided W3CDom helper class instead. Open HTML to PDF is a CSS 2.1 renderer written in Java. This artifact supports converting a Jsoup HTML5 instance into a DOM supported by Open HTML to PDF.
- License: LGPL-2.1-or-later

### 118. flexmark-profile-pegdown
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-profile-pegdown@0.64.0?type=jar
- Description: flexmark-java extension for setting flexmark options by using pegdown extension flags
- License: BSD-2-Clause

### 119. flexmark-util-ast
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-util-ast@0.64.0?type=jar
- Description: flexmark-java ast utility classes
- License: BSD-2-Clause

### 120. annotations
- Group: org.jetbrains
- Type: library
- Version: 15.0
- PURL: pkg:maven/org.jetbrains/annotations@15.0?type=jar
- Description: A set of annotations used for code inspection support and code documentation.
- License: Apache-2.0

### 121. flexmark-util-builder
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-util-builder@0.64.0?type=jar
- Description: flexmark-java builder utility classes
- License: BSD-2-Clause

### 122. flexmark-util-collection
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-util-collection@0.64.0?type=jar
- Description: flexmark-java collection utility classes
- License: BSD-2-Clause

### 123. flexmark-util-data
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-util-data@0.64.0?type=jar
- Description: flexmark-java data utility classes
- License: BSD-2-Clause

### 124. flexmark-util-dependency
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-util-dependency@0.64.0?type=jar
- Description: flexmark-java dependency utility classes
- License: BSD-2-Clause

### 125. flexmark-util-format
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-util-format@0.64.0?type=jar
- Description: flexmark-java format utility classes
- License: BSD-2-Clause

### 126. flexmark-util-html
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-util-html@0.64.0?type=jar
- Description: flexmark-java html utility classes
- License: BSD-2-Clause

### 127. flexmark-util-misc
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-util-misc@0.64.0?type=jar
- Description: flexmark-java misc utility classes
- License: BSD-2-Clause

### 128. flexmark-util-options
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-util-options@0.64.0?type=jar
- Description: flexmark-java options utility classes
- License: BSD-2-Clause

### 129. flexmark-util-sequence
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-util-sequence@0.64.0?type=jar
- Description: flexmark-java sequence utility classes
- License: BSD-2-Clause

### 130. flexmark-util-visitor
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-util-visitor@0.64.0?type=jar
- Description: flexmark-java visitor utility classes
- License: BSD-2-Clause

### 131. flexmark-youtrack-converter
- Group: com.vladsch.flexmark
- Type: library
- Version: 0.64.0
- PURL: pkg:maven/com.vladsch.flexmark/flexmark-youtrack-converter@0.64.0?type=jar
- Description: flexmark-java extension for YouTrack conversion
- License: BSD-2-Clause

### 132. core
- Group: com.google.zxing
- Type: library
- Version: 3.4.1
- PURL: pkg:maven/com.google.zxing/core@3.4.1?type=jar
- Description: Core barcode encoding/decoding library
- License: Apache-2.0

### 133. sshd-core
- Group: org.apache.sshd
- Type: library
- Version: 2.8.0
- PURL: pkg:maven/org.apache.sshd/sshd-core@2.8.0?type=jar
- Description: The Apache Software Foundation provides support for the Apache community of open-source software projects. The Apache projects are characterized by a collaborative, consensus based development process, an open and pragmatic software license, and a desire to create high quality software that leads the way in its field. We consider ourselves not simply a group of projects sharing a server, but rather a community of developers and users.
- License: Apache-2.0

### 134. sshd-common
- Group: org.apache.sshd
- Type: library
- Version: 2.8.0
- PURL: pkg:maven/org.apache.sshd/sshd-common@2.8.0?type=jar
- Description: The Apache Software Foundation provides support for the Apache community of open-source software projects. The Apache projects are characterized by a collaborative, consensus based development process, an open and pragmatic software license, and a desire to create high quality software that leads the way in its field. We consider ourselves not simply a group of projects sharing a server, but rather a community of developers and users.
- License: Apache-2.0

### 135. jcl-over-slf4j
- Group: org.slf4j
- Type: library
- Version: 1.7.32
- PURL: pkg:maven/org.slf4j/jcl-over-slf4j@1.7.32?type=jar
- Description: JCL 1.2 implemented over SLF4J
- License: Apache-2.0

### 136. sshd-netty
- Group: org.apache.sshd
- Type: library
- Version: 2.8.0
- PURL: pkg:maven/org.apache.sshd/sshd-netty@2.8.0?type=jar
- Description: The Apache Software Foundation provides support for the Apache community of open-source software projects. The Apache projects are characterized by a collaborative, consensus based development process, an open and pragmatic software license, and a desire to create high quality software that leads the way in its field. We consider ourselves not simply a group of projects sharing a server, but rather a community of developers and users.
- License: Apache-2.0

### 137. sshd-sftp
- Group: org.apache.sshd
- Type: library
- Version: 2.8.0
- PURL: pkg:maven/org.apache.sshd/sshd-sftp@2.8.0?type=jar
- Description: The Apache Software Foundation provides support for the Apache community of open-source software projects. The Apache projects are characterized by a collaborative, consensus based development process, an open and pragmatic software license, and a desire to create high quality software that leads the way in its field. We consider ourselves not simply a group of projects sharing a server, but rather a community of developers and users.
- License: Apache-2.0

### 138. slf4j-simple
- Group: org.slf4j
- Type: library
- Version: 2.0.7
- PURL: pkg:maven/org.slf4j/slf4j-simple@2.0.7?type=jar
- Description: SLF4J Simple binding
- License: MIT

### 139. greenmail
- Group: com.icegreen
- Type: library
- Version: 2.1.0-rc-1
- PURL: pkg:maven/com.icegreen/greenmail@2.1.0-rc-1?type=jar
- Description: GreenMail - Email Test Servers
- License: Apache-2.0

### 140. jakarta.mail-api
- Group: jakarta.mail
- Type: library
- Version: 2.1.3
- PURL: pkg:maven/jakarta.mail/jakarta.mail-api@2.1.3?type=jar
- Description: Jakarta Mail API 2.1 Specification API
- License: EPL-2.0, GPL-2.0-with-classpath-exception, BSD-3-Clause

### 141. jakarta.mail
- Group: org.eclipse.angus
- Type: library
- Version: 2.0.3
- PURL: pkg:maven/org.eclipse.angus/jakarta.mail@2.0.3?type=jar
- Description: Angus Mail default provider
- License: EPL-2.0, GPL-2.0-with-classpath-exception, BSD-3-Clause

### 142. jakarta.activation-api
- Group: jakarta.activation
- Type: library
- Version: 2.1.3
- PURL: pkg:maven/jakarta.activation/jakarta.activation-api@2.1.3?type=jar
- Description: Jakarta Activation API 2.1 Specification
- License: BSD-3-Clause

### 143. angus-activation
- Group: org.eclipse.angus
- Type: library
- Version: 2.0.2
- PURL: pkg:maven/org.eclipse.angus/angus-activation@2.0.2?type=jar
- Description: Angus Activation Registries Implementation
- License: BSD-3-Clause

### 144. junit
- Group: junit
- Type: library
- Version: 4.13.2
- PURL: pkg:maven/junit/junit@4.13.2?type=jar
- Description: JUnit is a unit testing framework for Java, created by Erich Gamma and Kent Beck.
- License: EPL-1.0

### 145. exp4j
- Group: net.objecthunter
- Type: library
- Version: 0.4.8
- PURL: pkg:maven/net.objecthunter/exp4j@0.4.8?type=jar
- Description: A simple mathematical expression evaluator for java.
- License: Apache-2.0

### 146. commons-jexl3
- Group: org.apache.commons
- Type: library
- Version: 3.2
- PURL: pkg:maven/org.apache.commons/commons-jexl3@3.2?type=jar
- Description: JEXL is a library intended to facilitate the implementation of scripting features in applications and frameworks written in Java.
- License: Apache-2.0

### 147. commons-logging
- Group: commons-logging
- Type: library
- Version: 1.2
- PURL: pkg:maven/commons-logging/commons-logging@1.2?type=jar
- Description: Apache Commons Logging is a thin adapter allowing configurable bridging to other, well known logging systems.
- License: Apache-2.0

### 148. jlama-core
- Group: com.github.tjake
- Type: library
- Version: 0.3.1
- PURL: pkg:maven/com.github.tjake/jlama-core@0.3.1?type=jar
- Description: Jlama: A modern LLM inference engine for Java
- License: Apache-2.0

### 149. jackson-databind
- Group: com.fasterxml.jackson.core
- Type: library
- Version: 2.15.2
- PURL: pkg:maven/com.fasterxml.jackson.core/jackson-databind@2.15.2?type=jar
- Description: General data-binding functionality for Jackson: works on core streaming API
- License: Apache-2.0

### 150. jackson-annotations
- Group: com.fasterxml.jackson.core
- Type: library
- Version: 2.15.2
- PURL: pkg:maven/com.fasterxml.jackson.core/jackson-annotations@2.15.2?type=jar
- Description: Core annotations used for value types, used by Jackson data binding package.
- License: Apache-2.0

### 151. jackson-core
- Group: com.fasterxml.jackson.core
- Type: library
- Version: 2.15.2
- PURL: pkg:maven/com.fasterxml.jackson.core/jackson-core@2.15.2?type=jar
- Description: Core Jackson processing abstractions (aka Streaming API), implementation for JSON
- License: Apache-2.0

### 152. guava
- Group: com.google.guava
- Type: library
- Version: 32.0.1-jre
- PURL: pkg:maven/com.google.guava/guava@32.0.1-jre?type=jar
- Description: Guava is a suite of core and expanded libraries that include utility classes, Google's collections, I/O classes, and much more.
- License: Apache-2.0

### 153. failureaccess
- Group: com.google.guava
- Type: library
- Version: 1.0.1
- PURL: pkg:maven/com.google.guava/failureaccess@1.0.1?type=jar
- Description: Contains com.google.common.util.concurrent.internal.InternalFutureFailureAccess and InternalFutures. Most users will never need to use this artifact. Its classes is conceptually a part of Guava, but they're in this separate artifact so that Android libraries can use them without pulling in all of Guava (just as they can use ListenableFuture by depending on the listenablefuture artifact).
- License: Apache-2.0

### 154. listenablefuture
- Group: com.google.guava
- Type: library
- Version: 9999.0-empty-to-avoid-conflict-with-guava
- PURL: pkg:maven/com.google.guava/listenablefuture@9999.0-empty-to-avoid-conflict-with-guava?type=jar
- Description: An empty artifact that Guava depends on to signal that it is providing ListenableFuture -- but is also available in a second "version" that contains com.google.common.util.concurrent.ListenableFuture class, without any other Guava classes. The idea is: - If users want only ListenableFuture, they depend on listenablefuture-1.0. - If users want all of Guava, they depend on guava, which, as of Guava 27.0, depends on listenablefuture-9999.0-empty-to-avoid-conflict-with-guava. The 9999.0-... version number is enough for some build systems (notably, Gradle) to select that empty artifact over the "real" listenablefuture-1.0 -- avoiding a conflict with the copy of ListenableFuture in guava itself. If users are using an older version of Guava or a build system other than Gradle, they may see class conflicts. If so, they can solve them by manually excluding the listenablefuture artifact or manually forcing their build systems to use 9999.0-....
- License: Apache-2.0

### 155. jsr305
- Group: com.google.code.findbugs
- Type: library
- Version: 3.0.2
- PURL: pkg:maven/com.google.code.findbugs/jsr305@3.0.2?type=jar
- Description: JSR305 Annotations for Findbugs
- License: Apache-2.0

### 156. checker-qual
- Group: org.checkerframework
- Type: library
- Version: 3.33.0
- PURL: pkg:maven/org.checkerframework/checker-qual@3.33.0?type=jar
- Description: checker-qual contains annotations (type qualifiers) that a programmer writes to specify Java code for type-checking by the Checker Framework.
- License: MIT

### 157. error_prone_annotations
- Group: com.google.errorprone
- Type: library
- Version: 2.18.0
- PURL: pkg:maven/com.google.errorprone/error_prone_annotations@2.18.0?type=jar
- Description: Error Prone is a static analysis tool for Java that catches common programming mistakes at compile-time.
- License: Apache-2.0

### 158. j2objc-annotations
- Group: com.google.j2objc
- Type: library
- Version: 2.8
- PURL: pkg:maven/com.google.j2objc/j2objc-annotations@2.8?type=jar
- Description: A set of annotations that provide additional information to the J2ObjC translator to modify the result of translation.
- License: Apache-2.0

### 159. jctools-core
- Group: org.jctools
- Type: library
- Version: 4.0.1
- PURL: pkg:maven/org.jctools/jctools-core@4.0.1?type=jar
- Description: Java Concurrency Tools Core Library
- License: Apache-2.0

### 160. jinjava
- Group: com.hubspot.jinjava
- Type: library
- Version: 2.7.2
- PURL: pkg:maven/com.hubspot.jinjava/jinjava@2.7.2?type=jar
- Description: Jinja templating engine implemented in Java

### 161. javassist
- Group: org.javassist
- Type: library
- Version: 3.24.1-GA
- PURL: pkg:maven/org.javassist/javassist@3.24.1-GA?type=jar
- Description: Javassist (JAVA programming ASSISTant) makes Java bytecode manipulation simple. It is a class library for editing bytecodes in Java.
- License: MPL-1.1, LGPL-2.1-only, Apache-2.0

### 162. re2j
- Group: com.google.re2j
- Type: library
- Version: 1.2
- PURL: pkg:maven/com.google.re2j/re2j@1.2?type=jar
- Description: Linear time regular expressions for Java
- License: Go License

### 163. commons-net
- Group: commons-net
- Type: library
- Version: 3.9.0
- PURL: pkg:maven/commons-net/commons-net@3.9.0?type=jar
- Description: Apache Commons Net library contains a collection of network utilities and protocol implementations. Supported protocols include: Echo, Finger, FTP, NNTP, NTP, POP3(S), SMTP(S), Telnet, Whois
- License: Apache-2.0

### 164. java-ipv6
- Group: com.googlecode.java-ipv6
- Type: library
- Version: 0.17
- PURL: pkg:maven/com.googlecode.java-ipv6/java-ipv6@0.17?type=jar
- Description: Sonatype helps open source projects to set up Maven repositories on https://oss.sonatype.org/
- License: Apache-2.0

### 165. annotations
- Group: com.google.code.findbugs
- Type: library
- Version: 3.0.1
- PURL: pkg:maven/com.google.code.findbugs/annotations@3.0.1?type=jar
- Description: Annotation the FindBugs tool supports
- License: GNU Lesser Public License

### 166. jackson-dataformat-yaml
- Group: com.fasterxml.jackson.dataformat
- Type: library
- Version: 2.14.0
- PURL: pkg:maven/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml@2.14.0?type=jar
- Description: Support for reading and writing YAML-encoded data via Jackson abstractions.
- License: Apache-2.0

### 167. snakeyaml
- Group: org.yaml
- Type: library
- Version: 1.33
- PURL: pkg:maven/org.yaml/snakeyaml@1.33?type=jar
- Description: YAML 1.1 parser and emitter for Java
- License: Apache-2.0

### 168. big-math
- Group: ch.obermuhlner
- Type: library
- Version: 2.0.0
- PURL: pkg:maven/ch.obermuhlner/big-math@2.0.0?type=jar
- Description: Math functions for BigDecimal.
- License: MIT

### 169. jemoji
- Group: net.fellbaum
- Type: library
- Version: 1.4.1
- PURL: pkg:maven/net.fellbaum/jemoji@1.4.1?type=jar
- Description: A Java library for conveniently working with emojis
- License: Apache-2.0

### 170. jspecify
- Group: org.jspecify
- Type: library
- Version: 0.3.0
- PURL: pkg:maven/org.jspecify/jspecify@0.3.0?type=jar
- Description: An artifact of well-named and well-specified annotations to power static analysis checks
- License: Apache-2.0

### 171. mvel2
- Group: org.mvel
- Type: library
- Version: 2.4.7.Final
- PURL: pkg:maven/org.mvel/mvel2@2.4.7.Final?type=jar
- Description: MVEL is a powerful expression language for Java-based applications. It provides a plethora of features and is suited for everything from the smallest property binding and extraction, to full blown scripts.
- License: Apache-2.0

### 172. telegrambots-client
- Group: org.telegram
- Type: library
- Version: 7.10.0
- PURL: pkg:maven/org.telegram/telegrambots-client@7.10.0?type=jar
- Description: Easy to use library to create Telegram Bots
- License: MIT

### 173. telegrambots-meta
- Group: org.telegram
- Type: library
- Version: 7.10.0
- PURL: pkg:maven/org.telegram/telegrambots-meta@7.10.0?type=jar
- Description: Easy to use library to create Telegram Bots
- License: MIT

### 174. jackson-datatype-jsr310
- Group: com.fasterxml.jackson.datatype
- Type: library
- Version: 2.17.2
- PURL: pkg:maven/com.fasterxml.jackson.datatype/jackson-datatype-jsr310@2.17.2?type=jar
- Description: Add-on module to support JSR-310 (Java 8 Date & Time API) data types.
- License: Apache-2.0

### 175. okhttp
- Group: com.squareup.okhttp3
- Type: library
- Version: 4.12.0
- PURL: pkg:maven/com.squareup.okhttp3/okhttp@4.12.0?type=jar
- Description: Square’s meticulous HTTP client for Java and Kotlin.
- License: Apache-2.0

### 176. okio
- Group: com.squareup.okio
- Type: library
- Version: 3.6.0
- PURL: pkg:maven/com.squareup.okio/okio@3.6.0?type=jar
- Description: A modern I/O library for Android, Java, and Kotlin Multiplatform.
- License: Apache-2.0

### 177. okio-jvm
- Group: com.squareup.okio
- Type: library
- Version: 3.6.0
- PURL: pkg:maven/com.squareup.okio/okio-jvm@3.6.0?type=jar
- Description: A modern I/O library for Android, Java, and Kotlin Multiplatform.
- License: Apache-2.0

### 178. kotlin-stdlib-common
- Group: org.jetbrains.kotlin
- Type: library
- Version: 1.9.10
- PURL: pkg:maven/org.jetbrains.kotlin/kotlin-stdlib-common@1.9.10?type=jar
- Description: Kotlin Common Standard Library
- License: Apache-2.0

### 179. kotlin-stdlib-jdk8
- Group: org.jetbrains.kotlin
- Type: library
- Version: 1.8.21
- PURL: pkg:maven/org.jetbrains.kotlin/kotlin-stdlib-jdk8@1.8.21?type=jar
- Description: Kotlin Standard Library JDK 8 extension
- License: Apache-2.0

### 180. kotlin-stdlib
- Group: org.jetbrains.kotlin
- Type: library
- Version: 1.8.21
- PURL: pkg:maven/org.jetbrains.kotlin/kotlin-stdlib@1.8.21?type=jar
- Description: Kotlin Standard Library for JVM
- License: Apache-2.0

### 181. kotlin-stdlib-jdk7
- Group: org.jetbrains.kotlin
- Type: library
- Version: 1.8.21
- PURL: pkg:maven/org.jetbrains.kotlin/kotlin-stdlib-jdk7@1.8.21?type=jar
- Description: Kotlin Standard Library JDK 7 extension
- License: Apache-2.0

### 182. telegrambots-longpolling
- Group: org.telegram
- Type: library
- Version: 7.10.0
- PURL: pkg:maven/org.telegram/telegrambots-longpolling@7.10.0?type=jar
- Description: Easy to use library to create Telegram Bots
- License: MIT

### 183. slf4j-api
- Group: org.slf4j
- Type: library
- Version: 2.0.7
- PURL: pkg:maven/org.slf4j/slf4j-api@2.0.7?type=jar
- Description: The slf4j API
- License: MIT

### 184. minimalftp
- Group: com.guichaguri
- Type: library
- Version: 1.0.6
- PURL: pkg:maven/com.guichaguri/minimalftp@1.0.6?type=jar
- Description: A lightweight, simple FTP server. Pure Java, no libraries.
- License: Apache-2.0

### 185. cyclonedx-maven-plugin
- Group: org.cyclonedx
- Type: library
- Version: 2.7.9
- PURL: pkg:maven/org.cyclonedx/cyclonedx-maven-plugin@2.7.9?type=jar
- Description: The CycloneDX Maven plugin generates CycloneDX Software Bill of Materials (SBOM) containing the aggregate of all direct and transitive dependencies of a project.
- License: Apache-2.0

### 186. cyclonedx-core-java
- Group: org.cyclonedx
- Type: library
- Version: 7.3.2
- PURL: pkg:maven/org.cyclonedx/cyclonedx-core-java@7.3.2?type=jar
- Description: The CycloneDX core module provides a model representation of the BOM along with utilities to assist in creating, parsing, and validating BOMs.
- License: Apache-2.0

### 187. packageurl-java
- Group: com.github.package-url
- Type: library
- Version: 1.4.1
- PURL: pkg:maven/com.github.package-url/packageurl-java@1.4.1?type=jar
- Description: The official Java implementation of the PackageURL specification. PackageURL (purl) is a minimal specification for describing a package via a "mostly universal" URL.
- License: MIT

### 188. jackson-dataformat-xml
- Group: com.fasterxml.jackson.dataformat
- Type: library
- Version: 2.14.2
- PURL: pkg:maven/com.fasterxml.jackson.dataformat/jackson-dataformat-xml@2.14.2?type=jar
- Description: Data format extension for Jackson to offer alternative support for serializing POJOs as XML and deserializing XML as pojos.
- License: Apache-2.0

### 189. stax2-api
- Group: org.codehaus.woodstox
- Type: library
- Version: 4.2.1
- PURL: pkg:maven/org.codehaus.woodstox/stax2-api@4.2.1?type=jar
- Description: tax2 API is an extension to basic Stax 1.0 API that adds significant new functionality, such as full-featured bi-direction validation interface and high-performance Typed Access API.
- License: BSD-4-Clause

### 190. woodstox-core
- Group: com.fasterxml.woodstox
- Type: library
- Version: 6.5.0
- PURL: pkg:maven/com.fasterxml.woodstox/woodstox-core@6.5.0?type=jar
- Description: Woodstox is a high-performance XML processor that implements Stax (JSR-173), SAX2 and Stax2 APIs
- License: Apache-2.0

### 191. json-schema-validator
- Group: com.networknt
- Type: library
- Version: 1.0.77
- PURL: pkg:maven/com.networknt/json-schema-validator@1.0.77?type=jar
- Description: A json schema validator that supports draft v4, v6, v7, v2019-09 and v2020-12
- License: Apache-2.0

### 192. itu
- Group: com.ethlo.time
- Type: library
- Version: 1.7.0
- PURL: pkg:maven/com.ethlo.time/itu@1.7.0?type=jar
- Description: Extremely fast date/time parser and formatter - RFC 3339 (ISO 8601 profile) and W3C format
- License: Apache-2.0

### 193. commons-codec
- Group: commons-codec
- Type: library
- Version: 1.15
- PURL: pkg:maven/commons-codec/commons-codec@1.15?type=jar
- Description: The Apache Commons Codec package contains simple encoder and decoders for various formats such as Base64 and Hexadecimal. In addition to these widely used encoders and decoders, the codec package also maintains a collection of phonetic encoding utilities.
- License: Apache-2.0

### 194. maven-dependency-tree
- Group: org.apache.maven.shared
- Type: library
- Version: 3.2.1
- PURL: pkg:maven/org.apache.maven.shared/maven-dependency-tree@3.2.1?type=jar
- Description: A tree-based API for resolution of Maven project dependencies
- License: Apache-2.0

### 195. aether-util
- Group: org.eclipse.aether
- Type: library
- Version: 1.0.0.v20140518
- PURL: pkg:maven/org.eclipse.aether/aether-util@1.0.0.v20140518?type=jar
- Description: A collection of utility classes to ease usage of the repository system.
- License: EPL-1.0

### 196. aether-api
- Group: org.eclipse.aether
- Type: library
- Version: 1.0.0.v20140518
- PURL: pkg:maven/org.eclipse.aether/aether-api@1.0.0.v20140518?type=jar
- Description: The application programming interface for the repository system.
- License: EPL-1.0

### 197. maven-dependency-analyzer
- Group: org.apache.maven.shared
- Type: library
- Version: 1.13.2
- PURL: pkg:maven/org.apache.maven.shared/maven-dependency-analyzer@1.13.2?type=jar
- Description: Analyzes the dependencies of a project for undeclared or unused artifacts.
- License: Apache-2.0

### 198. maven-core
- Group: org.apache.maven
- Type: library
- Version: 3.2.5
- PURL: pkg:maven/org.apache.maven/maven-core@3.2.5?type=jar
- Description: Maven Core classes.
- License: Apache-2.0

### 199. maven-settings
- Group: org.apache.maven
- Type: library
- Version: 3.2.5
- PURL: pkg:maven/org.apache.maven/maven-settings@3.2.5?type=jar
- Description: Maven Settings model.
- License: Apache-2.0

### 200. maven-settings-builder
- Group: org.apache.maven
- Type: library
- Version: 3.2.5
- PURL: pkg:maven/org.apache.maven/maven-settings-builder@3.2.5?type=jar
- Description: The effective settings builder, with inheritance and password decryption.
- License: Apache-2.0

### 201. maven-repository-metadata
- Group: org.apache.maven
- Type: library
- Version: 3.2.5
- PURL: pkg:maven/org.apache.maven/maven-repository-metadata@3.2.5?type=jar
- Description: Per-directory local and remote repository metadata.
- License: Apache-2.0

### 202. maven-plugin-api
- Group: org.apache.maven
- Type: library
- Version: 3.2.5
- PURL: pkg:maven/org.apache.maven/maven-plugin-api@3.2.5?type=jar
- Description: The API for plugins - Mojos - development.
- License: Apache-2.0

### 203. maven-model-builder
- Group: org.apache.maven
- Type: library
- Version: 3.2.5
- PURL: pkg:maven/org.apache.maven/maven-model-builder@3.2.5?type=jar
- Description: The effective model builder, with inheritance, profile activation, interpolation, ...
- License: Apache-2.0

### 204. maven-aether-provider
- Group: org.apache.maven
- Type: library
- Version: 3.2.5
- PURL: pkg:maven/org.apache.maven/maven-aether-provider@3.2.5?type=jar
- Description: Extensions to Aether for utilizing Maven POM and repository metadata.
- License: Apache-2.0

### 205. aether-spi
- Group: org.eclipse.aether
- Type: library
- Version: 1.0.0.v20140518
- PURL: pkg:maven/org.eclipse.aether/aether-spi@1.0.0.v20140518?type=jar
- Description: The service provider interface for repository system implementations and repository connectors.
- License: EPL-1.0

### 206. aether-impl
- Group: org.eclipse.aether
- Type: library
- Version: 1.0.0.v20140518
- PURL: pkg:maven/org.eclipse.aether/aether-impl@1.0.0.v20140518?type=jar
- Description: An implementation of the repository system.
- License: EPL-1.0

### 207. org.eclipse.sisu.plexus
- Group: org.eclipse.sisu
- Type: library
- Version: 0.3.0.M1
- PURL: pkg:maven/org.eclipse.sisu/org.eclipse.sisu.plexus@0.3.0.M1?type=jar
- Description: Plexus-JSR330 adapter; adds Plexus support to the Sisu-Inject container
- License: EPL-1.0

### 208. cdi-api
- Group: javax.enterprise
- Type: library
- Version: 1.0
- PURL: pkg:maven/javax.enterprise/cdi-api@1.0?type=jar
- Description: APIs for JSR-299: Contexts and Dependency Injection for Java EE
- License: Apache-2.0

### 209. jsr250-api
- Group: javax.annotation
- Type: library
- Version: 1.0
- PURL: pkg:maven/javax.annotation/jsr250-api@1.0?type=jar
- Description: JSR-250 Reference Implementation by Glassfish
- License: CDDL-1.0

### 210. org.eclipse.sisu.inject
- Group: org.eclipse.sisu
- Type: library
- Version: 0.3.0.M1
- PURL: pkg:maven/org.eclipse.sisu/org.eclipse.sisu.inject@0.3.0.M1?type=jar
- Description: JSR330-based container; supports classpath scanning, auto-binding, and dynamic auto-wiring
- License: EPL-1.0

### 211. sisu-guice
- Group: org.sonatype.sisu
- Type: library
- Version: 3.2.3
- PURL: pkg:maven/org.sonatype.sisu/sisu-guice@3.2.3?classifier=no_aop&type=jar
- Description: Patched build of Guice: a lightweight dependency injection framework for Java 6 and above
- License: Apache-2.0

### 212. aopalliance
- Group: aopalliance
- Type: library
- Version: 1.0
- PURL: pkg:maven/aopalliance/aopalliance@1.0?type=jar
- Description: AOP Alliance
- License: Public Domain

### 213. plexus-interpolation
- Group: org.codehaus.plexus
- Type: library
- Version: 1.21
- PURL: pkg:maven/org.codehaus.plexus/plexus-interpolation@1.21?type=jar
- Description: The Plexus project provides a full software stack for creating and executing software projects.
- License: Apache-2.0

### 214. plexus-utils
- Group: org.codehaus.plexus
- Type: library
- Version: 3.0.20
- PURL: pkg:maven/org.codehaus.plexus/plexus-utils@3.0.20?type=jar
- Description: A collection of various utility classes to ease working with strings, files, command lines, XML and more.
- License: Apache-2.0

### 215. plexus-classworlds
- Group: org.codehaus.plexus
- Type: library
- Version: 2.5.2
- PURL: pkg:maven/org.codehaus.plexus/plexus-classworlds@2.5.2?type=jar
- Description: A class loader framework
- License: Apache-2.0

### 216. plexus-component-annotations
- Group: org.codehaus.plexus
- Type: library
- Version: 1.5.5
- PURL: pkg:maven/org.codehaus.plexus/plexus-component-annotations@1.5.5?type=jar
- Description: Plexus Component "Java 5" Annotations, to describe plexus components properties in java sources with standard annotations instead of javadoc annotations.
- License: Apache-2.0

### 217. plexus-sec-dispatcher
- Group: org.sonatype.plexus
- Type: library
- Version: 1.3
- PURL: pkg:maven/org.sonatype.plexus/plexus-sec-dispatcher@1.3?type=jar
- License: Apache-2.0

### 218. plexus-cipher
- Group: org.sonatype.plexus
- Type: library
- Version: 1.4
- PURL: pkg:maven/org.sonatype.plexus/plexus-cipher@1.4?type=jar
- License: Apache-2.0

### 219. javax.inject
- Group: javax.inject
- Type: library
- Version: 1
- PURL: pkg:maven/javax.inject/javax.inject@1?type=jar
- Description: The javax.inject API
- License: Apache-2.0

### 220. asm
- Group: org.ow2.asm
- Type: library
- Version: 9.5
- PURL: pkg:maven/org.ow2.asm/asm@9.5?type=jar
- Description: ASM, a very small and fast Java bytecode manipulation framework
- License: BSD-3-Clause

### 221. tika-core
- Group: org.apache.tika
- Type: library
- Version: 2.9.2
- PURL: pkg:maven/org.apache.tika/tika-core@2.9.2?type=jar
- Description: This is the core Apache Tika™ toolkit library from which all other modules inherit functionality. It also includes the core facades for the Tika API.
- License: Apache-2.0

### 222. mina-core
- Group: org.apache.mina
- Type: library
- Version: 2.1.5
- PURL: pkg:maven/org.apache.mina/mina-core@2.1.5?type=jar
- Description: Apache MINA is a network application framework which helps users develop high performance and highly scalable network applications easily. It provides an abstract event-driven asynchronous API over various transports such as TCP/IP and UDP/IP via Java NIO.
- License: Apache-2.0

### 223. telnetd-x
- Group: net.wimpi
- Type: library
- Version: 2.1.1
- PURL: pkg:maven/net.wimpi/telnetd-x@2.1.1?type=jar
- Description: The Java Telnet Daemon
- License: Modified BSD TelnetD license

### 224. log4j
- Group: log4j
- Type: library
- Version: 1.2.9
- PURL: pkg:maven/log4j/log4j@1.2.9?type=jar

