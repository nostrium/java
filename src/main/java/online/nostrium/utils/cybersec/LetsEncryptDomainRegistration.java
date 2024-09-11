package online.nostrium.utils.cybersec;

import org.shredzone.acme4j.*;
import org.shredzone.acme4j.challenge.Http01Challenge;
import org.shredzone.acme4j.exception.AcmeException;
import org.shredzone.acme4j.util.CSRBuilder;
import org.shredzone.acme4j.util.KeyPairUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Base64;
import online.nostrium.utils.screens.Screen;
import org.shredzone.acme4j.exception.AcmeServerException;

public class LetsEncryptDomainRegistration {

    private final String domain;
    private final String email;
    private final File accountKeyFile;
    private final File domainKeyFile;
    private final File certFile;
    private final File chainFile;
    private final File challengeFilePath;
    private final Session session;
    private final Screen screen;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public LetsEncryptDomainRegistration(
            String domain, 
            String email, 
            File baseDir, 
            File challengeDir, 
            boolean useStaging,
            Screen screen
    ) {
        this.domain = domain;
        this.email = email;
        this.accountKeyFile = new File(baseDir, "account.key");
        this.domainKeyFile = new File(baseDir, "domain.key");
        this.certFile = new File(baseDir, "domain.crt");
        this.chainFile = new File(baseDir, "chain.crt");
        this.challengeFilePath = challengeDir;
        String acmeUrl = useStaging ? "acme://letsencrypt.org/staging" : "acme://letsencrypt.org";
        this.session = new Session(acmeUrl);
        this.screen = screen;
    }

    private Account findOrRegisterAccount(Session session) throws Exception {
        KeyPair accountKey = loadOrCreateKeyPair(accountKeyFile);
        AccountBuilder accountBuilder = new AccountBuilder()
                .addContact("mailto:" + email)  // Ensure correct mailto format
                .agreeToTermsOfService()
                .useKeyPair(accountKey);

        Account account;
        try {
            account = accountBuilder.create(session);
            log("Created a new ACME account.");
        } catch (AcmeServerException ex) {
            if (ex.getProblem() != null && ex.getProblem().getType().equals("urn:ietf:params:acme:error:accountDoesNotExist")) {
                account = accountBuilder.onlyExisting().create(session);
                log("Using existing ACME account.");
            } else {
                throw ex;
            }
        }

        saveAccountUrl(account.getLocation());
        return account;
    }

    private KeyPair createAndSaveKeyPair(File keyFile) throws IOException {
        KeyPair keyPair = KeyPairUtils.createKeyPair(2048);
        try (FileWriter fw = new FileWriter(keyFile)) {
            KeyPairUtils.writeKeyPair(keyPair, fw);
        }
        log("Created and saved new key pair to: " + keyFile.getAbsolutePath());
        return keyPair;
    }

    private KeyPair loadOrCreateKeyPair(File keyFile) throws IOException {
        if (keyFile.exists()) {
            try (FileReader fr = new FileReader(keyFile)) {
                return KeyPairUtils.readKeyPair(fr);
            } catch (IOException e) {
                log("Error reading existing key file: " + keyFile.getAbsolutePath() + ". Deleting invalid key file and creating a new one.");
                if (!keyFile.delete()) {
                    log("Failed to delete invalid key file: " + keyFile.getAbsolutePath());
                }
                return createAndSaveKeyPair(keyFile);
            }
        } else {
            return createAndSaveKeyPair(keyFile);
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    public boolean registerDomain() {
        try {
            KeyPair accountKeyPair = loadOrCreateKeyPair(accountKeyFile);
            KeyPair domainKeyPair = loadOrCreateKeyPair(domainKeyFile);

            Account account = findOrRegisterAccount(session);
            if (account == null) return false;

            Order order = account.newOrder().domains(domain).create();

            for (Authorization auth : order.getAuthorizations()) {
                Http01Challenge challenge = auth.findChallenge(Http01Challenge.TYPE);
                if (challenge == null) {
                    log("No HTTP-01 challenge found.");
                    return false;
                }

                if (!serveChallengeFile(challenge)) return false;

                challenge.trigger();

                if (!pollAuthorization(auth)) return false;
            }

            // Create CSR
            CSRBuilder csrBuilder = new CSRBuilder();
            csrBuilder.addDomain(domain);
            csrBuilder.sign(domainKeyPair);
            byte[] csr = csrBuilder.getEncoded();

            log("CSR generated successfully.");

            // Finalize the order
            order.execute(csr);
            log("Order finalized.");

            // Fetch the certificate
            Certificate certificate = order.getCertificate();
            if (certificate == null) {
                log("Certificate order failed: Certificate is null.");
                return false;
            }

            return saveCertificate(certificate);

        } catch (Exception e) {
            log("Error during domain registration: " + e.getMessage());
            return false;
        }
    }

    private boolean serveChallengeFile(Http01Challenge challenge) {
        try {
            File challengeFile = new File(challengeFilePath, challenge.getToken());
            try (FileWriter fw = new FileWriter(challengeFile)) {
                fw.write(challenge.getAuthorization());
            }
            log("Challenge file served at: " + challengeFile.getAbsolutePath());
            log("Challenge should be accessible at: http://" + domain + "/.well-known/acme-challenge/" + challenge.getToken());
            return true;
        } catch (IOException e) {
            log("Error serving challenge file: " + e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("SleepWhileInLoop")
    private boolean pollAuthorization(Authorization auth) throws AcmeException {
        int attempts = 10;
        while (auth.getStatus() != Status.VALID && attempts-- > 0) {
            if (auth.getStatus() == Status.INVALID) {
                log("Authorization became invalid.");
                return false;
            }
            try {
                log("Waiting 30 seconds... (attempts remaining: " + attempts + ")");
                Thread.sleep(30000L);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            auth.update();
        }

        boolean isValid = auth.getStatus() == Status.VALID;
        if (isValid) {
            log("Authorization valid.");
        } else {
            log("Authorization failed after multiple attempts.");
        }
        return isValid;
    }

    private boolean saveCertificate(Certificate certificate) {
        try {
            try (FileWriter fw = new FileWriter(certFile)) {
                fw.write("-----BEGIN CERTIFICATE-----\n");
                fw.write(Base64.getMimeEncoder(64, new byte[]{'\n'})
                        .encodeToString(certificate.getCertificate().getEncoded()));
                fw.write("\n-----END CERTIFICATE-----\n");
            }

            try (FileWriter fw = new FileWriter(chainFile)) {
                for (X509Certificate cert : certificate.getCertificateChain()) {
                    fw.write("-----BEGIN CERTIFICATE-----\n");
                    fw.write(Base64.getMimeEncoder(64, new byte[]{'\n'})
                            .encodeToString(cert.getEncoded()));
                    fw.write("\n-----END CERTIFICATE-----\n");
                    fw.write("\n");
                }
            }

            log("Certificate and chain saved to files.");
            return true;
        } catch (IOException | java.security.cert.CertificateEncodingException e) {
            log("Error saving certificate: " + e.getMessage());
            return false;
        }
    }

    private void saveAccountUrl(URL accountUrl) {
        try (FileWriter fw = new FileWriter(new File(accountKeyFile.getParentFile(), "account.url"))) {
            fw.write(accountUrl.toString());
        } catch (IOException e) {
            log("Error saving account URL: " + e.getMessage());
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
                    useStaging,
                    null
            );

        if (Security.getProvider("BC") == null) {
            registrar.log("BouncyCastle provider is not installed");
        } else {
            registrar.log("BouncyCastle provider is installed");
        }

        boolean success = registrar.registerDomain();
        if (success) {
            registrar.log("Domain registration and certificate issuance successful!");
        } else {
            registrar.log("Domain registration failed.");
        }
    }

    public void log(String text){
        if(screen == null){
            System.out.println(text);
            return;
        }
        screen.writeln(text);
    }
}
