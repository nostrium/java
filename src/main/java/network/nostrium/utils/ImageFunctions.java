/*
 *  Identify images based on magic signature
 *   
 * Copyright (c) 2023 contributors
 * License: Apache-2.0
 */
package network.nostrium.utils;

import java.util.Arrays;

/**
 * Date: 2023-02-07
 * Place: Germany
 * @author brito
 */
public class ImageFunctions {

    private static final int NUM_BYTES_TO_READ = 4;
    private static final byte[] PNG_HEADER = {(byte) 0x89, 0x50, 0x4E, 0x47};
    private static final byte[] GIF_HEADER = {0x47, 0x49, 0x46, 0x38};
    private static final byte[] JPG_HEADER = {(byte) 0xff, (byte) 0xd8, (byte) 0xff, (byte) 0xe0};

    public static String getExtension(byte[] source) {
        byte[] headerBytes = new byte[NUM_BYTES_TO_READ];
        System.arraycopy(source, 0, headerBytes, 0,
                Math.min(source.length, NUM_BYTES_TO_READ));
        if (Arrays.equals(headerBytes, PNG_HEADER)) {
            return ".png";
        } else if (Arrays.equals(headerBytes, GIF_HEADER)) {
            return ".gif";
        } else if (Arrays.equals(headerBytes, JPG_HEADER)) {
            return ".jpg";
        } else {
            System.out.println("Error: Unrecognized file type.");
            return null;
        }
    }

}
