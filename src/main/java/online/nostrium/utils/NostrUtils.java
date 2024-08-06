/*
 * Internal utils
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils;


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


/**
 * @author Brito
 * @date: 2024-08-06
 * @location: Germany
 */
public class NostrUtils {

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

    /**
     * 
     * @param bytes first value is NSEC, second is NPUB
     * @return 
     */
    private static byte[] trimLeadingZero(byte[] bytes) {
        byte[] trimmedBytes = new byte[bytes.length - 1];
        System.arraycopy(bytes, 1, trimmedBytes, 0, trimmedBytes.length);
        return trimmedBytes;
    }
}
