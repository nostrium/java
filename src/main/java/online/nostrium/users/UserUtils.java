/*
 * Internal utils for users
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.users;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.util.encoders.Hex;

import java.security.SecureRandom;
import java.security.Security;
import online.nostrium.utils.TextFunctions;


/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class UserUtils {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String[] generateNostrKeys() {
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        var curveSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
        ECDomainParameters domainParameters = new ECDomainParameters(
            curveSpec.getCurve(), curveSpec.getG(), curveSpec.getN(), curveSpec.getH(), curveSpec.getSeed());

        ECKeyGenerationParameters keyGenParams = new ECKeyGenerationParameters(domainParameters, new SecureRandom());
        generator.init(keyGenParams);
        AsymmetricCipherKeyPair keyPair = generator.generateKeyPair();
        ECPrivateKeyParameters privateKey = (ECPrivateKeyParameters) keyPair.getPrivate();
        ECPublicKeyParameters publicKey = (ECPublicKeyParameters) keyPair.getPublic();

        if (publicKey == null) {
            System.out.println("Public key generation failed.");
            return null;
        }

        byte[] publicKeyBytes = publicKey.getQ().getEncoded(true);
        if (publicKeyBytes == null) {
            System.out.println("Public key encoding failed.");
            return null;
        }

        byte[] privateKeyBytes = privateKey.getD().toByteArray();
        String nsec = Hex.toHexString(privateKeyBytes[0] == 0 ? trimLeadingZero(privateKeyBytes) : privateKeyBytes);
        String npub = Hex.toHexString(publicKeyBytes);

        return new String[]{nsec, npub};
    }

    private static byte[] trimLeadingZero(byte[] bytes) {
        byte[] trimmedBytes = new byte[bytes.length - 1];
        System.arraycopy(bytes, 1, trimmedBytes, 0, trimmedBytes.length);
        return trimmedBytes;
    }

    public static void main(String[] args) {
        String[] keys = generateNostrKeys();
        System.out.println("NSEC (Private Key): " + keys[0]);
        System.out.println("NPUB (Public Key): " + keys[1]);
    }
    
    /**
     * Create an anonymous user
     *
     * @return user that was automatically generated
     */
    public static User createUserAnonymous() {
        User user = new User();
        // generate random nostr keys
        String[] keys = generateNostrKeys();
        user.setNsec(keys[0]);
        user.setNpub(keys[1]);
        user.setDisplayName("Anonymouse#" + user.getNpub().substring(0, 4));
        String timestamp = TextFunctions.getDate();
        user.setRegisteredTime(timestamp);
        user.setLastLoginTime(timestamp);
        return user;
    }

}
