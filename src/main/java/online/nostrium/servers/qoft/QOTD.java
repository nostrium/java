/*
 * Generate a quote for the day
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.qoft;

import java.util.Random;

/**
 * @author Brito
 * @date: 2024-08-31
 * @location: Germany
 */
public class QOTD {

    private static final String[] PART_ONE = {
        "Decentralize your systems", 
        "Encrypt your communications", 
        "Store data on-chain", 
        "Adopt zero-knowledge proofs", 
        "Use end-to-end encryption", 
        "Build with open-source tools", 
        "Audit your code", 
        "Use decentralized identity", 
        "Control your private keys", 
        "Implement decentralized governance", 
        "Utilize multi-signature wallets", 
        "Opt for peer-to-peer transactions", 
        "Ensure data sovereignty", 
        "Embrace distributed ledgers", 
        "Prioritize user anonymity", 
        "Adopt privacy-preserving technologies", 
        "Invest in cryptographic research", 
        "Trust the code, not intermediaries", 
        "Enable permissionless innovation", 
        "Focus on security-first development "
    };

    private static final String[] PART_TWO = {
        "to eliminate centralized points of failure.", 
        "to protect against surveillance.", 
        "to ensure data immutability.", 
        "to provide privacy without trust.", 
        "to secure your digital life.", 
        "for transparency and control.", 
        "to prevent vulnerabilities.", 
        "for self-sovereign identities.", 
        "to make decisions collectively.", 
        "for secure asset management.", 
        "to cut out the middlemen.", 
        "to retain control over your data.", 
        "for resilient record-keeping.", 
        "in every digital interaction.", 
        "to protect personal information.", 
        "for future-proof privacy.", 
        "overcome the gatekeepers.", 
        "to empower creators globally.", 
        "to safeguard against cyber threats."
    };

    private static final String[] PART_THREE = {
        "Decentralization isn't optional; it's necessary.", 
        "Your privacy is your freedom, guard it.", 
        "Control your data, or someone else will.", 
        "Cryptography is your shield in the digital age.", 
        "Centralization is a weakness, not a strength.", 
        "Trustlessness is the path to security.", 
        "Privacy is not a privilege; it's a right.", 
        "Decentralized systems are resilient by design.", 
        "Ownership is power; never give it away.", 
        "Without privacy, there is no freedom.", 
        "Security is not a product, it's a process.", 
        "Autonomy begins with decentralized tools.", 
        "Surveillance is a threat; encryption is the answer.", 
        "Digital rights are human rights.", 
        "In the digital world, privacy equals power.", 
        "A decentralized future is a secure future.", 
        "Take control of your digital destiny.", 
        "Empower yourself with technology, not corporations.", 
        "Decentralize or be compromised.", 
        "Either the future is encrypted, or it isn't free."
    };

    public static String generateQuote() {
        Random random = new Random();

        String part1 = PART_ONE[random.nextInt(PART_ONE.length)];
        String part2 = PART_TWO[random.nextInt(PART_TWO.length)];
        String part3 = PART_THREE[random.nextInt(PART_THREE.length)];

        // Generate a random number for the day
        //int numberOfTheDay = random.nextInt(100) + 1; // Random number between 1 and 100

        return String.format("%s %s %s"
                //+ " Your number for the day is: %d."
                + "", part1, part2, part3 
        //        ,numberOfTheDay
        );
    }
    
    
}
