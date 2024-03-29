package com.demo.myqrcodeapp.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class QRCodeReader {
    private static Logger logger = LoggerFactory.getLogger(QRCodeReader.class);

    @Value("${qr.filepath}")
    static String qrCodeImagePath;

    public static String decodeQRCode(File qrCodeimage) throws IOException {
        logger.info("Inside decodeQRCode ....");
        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            logger.info("Exiting  decodeQRCode .... try");

            return result.getText();
        } catch (NotFoundException e) {
            logger.info("Exiting  decodeQRCode .... catch ErrorCause: {},ErrorMessage: {}",e.getCause(),e.getMessage());
            return null;
        }

    }
    public static byte[] decodeQRCodeCreated(File qrCodeimage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos );
        byte[] imageInByte=baos.toByteArray();
        return imageInByte ;
    }
   }