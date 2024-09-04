/*
 *  Generate QR codes
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils.ascii;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

/**
 * @author Brito
 * @date: 2024-08-23
 * @location: Germany
 */
public class QRCodeGenerator {
    public static String generateQRCode(String url) throws WriterException {
        int size = 40; // Smaller size to make it more compact
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, size, size);

        StringBuilder qrText = new StringBuilder();

        for (int y = 0; y < bitMatrix.getHeight(); y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                line.append(bitMatrix.get(x, y) ? '#' : ' ');
            }
            qrText.append(line).append("\n");
        }

        return qrText.toString();
    }

    public static void main(String[] args) {
        try {
            String url = "https://example.com";
            String qrCodeText = generateQRCode(url);
            System.out.println(qrCodeText);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
