package online.nostrium.utils.cybersec;

import org.shredzone.acme4j.*;
import org.shredzone.acme4j.challenge.Http01Challenge;
import org.shredzone.acme4j.exception.AcmeException;
import org.shredzone.acme4j.util.CSRBuilder;
import org.shredzone.acme4j.util.KeyPairUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.KeyPair;
import java.security.cert.X509Certificate;


public class LetsEncryptDomainRegistration {
    private final String domain;
    private final String email;
    private final File accountKeyFile;
    private final File domainKeyFile;
    private final File certFile;
    private final File chainFile;
    private final File challengeFilePath;
    private final Session session;

    public LetsEncryptDomainRegistration(String domain, String email, File baseDir, File challengeDir, boolean useStaging) {
        this.domain = domain;
        this.email = email;
        this.accountKeyFile = new File(baseDir, "account.key");
        this.domainKeyFile = new File(baseDir, "domain.key");
        this.certFile = new File(baseDir, "domain.crt");
        this.chainFile = new File(baseDir, "chain.crt");
        this.challengeFilePath = challengeDir;
        String acmeUrl = useStaging ? "acme://letsencrypt.org/staging" : "acme://letsencrypt.org";
        this.session = new Session(acmeUrl);
    }

    @SuppressWarnings("UseSpecificCatch")
    public boolean registerDomain() {
        try {
            KeyPair accountKeyPair = loadOrCreateKeyPair(accountKeyFile);
            KeyPair domainKeyPair = loadOrCreateKeyPair(domainKeyFile);

            Account account = findOrRegisterAccount(accountKeyPair);
            if (account == null) return false;

            Order order = account.newOrder().domains(domain).create();

            for (Authorization auth : order.getAuthorizations()) {
                Http01Challenge challenge = auth.findChallenge(Http01Challenge.TYPE);
                if (challenge == null) {
                    System.out.println("No HTTP-01 challenge found.");
                    return false;
                }

                if (!serveChallengeFile(challenge)) return false;

                challenge.trigger();

                if (!pollAuthorization(auth)) return false;
            }

            CSRBuilder csrBuilder = new CSRBuilder();
            csrBuilder.addDomain(domain);
            csrBuilder.sign(domainKeyPair);

            order.execute(csrBuilder.getEncoded());

            Certificate certificate = order.getCertificate();

            return saveCertificate(certificate);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private KeyPair loadOrCreateKeyPair(File keyFile) throws IOException {
        if (keyFile.exists()) {
            try (FileReader fr = new FileReader(keyFile)) {
                return KeyPairUtils.readKeyPair(fr);
            }
        } else {
            KeyPair keyPair = KeyPairUtils.createKeyPair(2048);
            try (FileWriter fw = new FileWriter(keyFile)) {
                KeyPairUtils.writeKeyPair(keyPair, fw);
            }
            return keyPair;
        }
    }

    private Account findOrRegisterAccount(KeyPair accountKeyPair) {
        try {
            // Try to find an existing account
            AccountBuilder accountBuilder = new AccountBuilder()
                .useKeyPair(accountKeyPair);
            
            Account account;
            try {
                account = accountBuilder.onlyExisting().create(session);
                System.out.println("Found existing account with Let's Encrypt");
            } catch (AcmeException ex) {
                // Account doesn't exist, create a new one
                account = accountBuilder
                    .addContact("mailto:" + email)
                    .agreeToTermsOfService()
                    .create(session);
                System.out.println("Created a new account with Let's Encrypt");
            }
            
            // Save the account URL for future use
            saveAccountUrl(account.getLocation());
            
            return account;
        } catch (AcmeException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean serveChallengeFile(Http01Challenge challenge) {
        try {
            File challengeFile = new File(challengeFilePath, challenge.getToken());
            try (FileWriter fw = new FileWriter(challengeFile)) {
                fw.write(challenge.getAuthorization());
            }
            System.out.println("Challenge file served at: " + challengeFile.getAbsolutePath());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("SleepWhileInLoop")
    private boolean pollAuthorization(Authorization auth) throws AcmeException {
        int attempts = 10;
        while (auth.getStatus() != Status.VALID && attempts-- > 0) {
            if (auth.getStatus() == Status.INVALID) {
                return false;
            }
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            auth.update();
        }
        return auth.getStatus() == Status.VALID;
    }

    private boolean saveCertificate(Certificate certificate) {
    try {
        // Write the certificate
        try (FileWriter fw = new FileWriter(certFile)) {
            fw.write(certificate.getCertificate().toString());
        }

        // Write the certificate chain
        try (FileWriter fw = new FileWriter(chainFile)) {
            for (X509Certificate cert : certificate.getCertificateChain()) {
                fw.write(cert.toString());
                fw.write("\n");
            }
        }

        System.out.println("Certificate and chain saved to files.");
        return true;
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}

    private void saveAccountUrl(URL accountUrl) {
        try (FileWriter fw = new FileWriter(new File(accountKeyFile.getParentFile(), "account.url"))) {
            fw.write(accountUrl.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String domain = "nostrium.online";
        String email = "brito@" + domain;
        File baseDir = new File("./certs");
        File challengeDir = new File("/var/www/html/.well-known/acme-challenge/");
        boolean useStaging = true;

        LetsEncryptDomainRegistration registrar = 
            new LetsEncryptDomainRegistration(
                    domain, 
                    email, 
                    baseDir, 
                    challengeDir, 
                    useStaging
            );
    boolean success = registrar.registerDomain();
        if (success) {
            System.out.println("Domain registration and certificate issuance successful!");
        } else {
            System.out.println("Domain registration failed.");
        }
    }
}