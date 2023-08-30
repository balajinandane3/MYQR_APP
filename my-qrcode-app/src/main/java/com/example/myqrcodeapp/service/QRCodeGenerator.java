package com.example.myqrcodeapp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.example.myqrcodeapp.controller.QRController;
import com.example.myqrcodeapp.util.Colors;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class QRCodeGenerator {
    Logger logger = LoggerFactory.getLogger(QRCodeGenerator.class);

    @Value("${qr.file.extension}")
     String qrFileExtension;
    @Value("${qr.on.color}")
    String qrOnColor;
    @Value("${qr.off.color}")
    String qrOffColor;

    public  byte[] generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        logger.info("Inside generateQRCodeImage () .... text: {},filePath :{}",text,filePath);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath+qrFileExtension);
            MatrixToImageConfig con = new MatrixToImageConfig(Colors.WHITE.getArgb(), Colors.ORANGE.getArgb()) ;

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path,con);
        logger.info("Exiting generateQRCodeImage ()");
        return null;
    }


    public  byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        logger.info("Inside getQRCodeImage () .... text: {},filePath :{}",text);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageConfig con = new MatrixToImageConfig(Colors.WHITE.getArgb(), Colors.ORANGE.getArgb()) ;
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream,con);
        byte[] pngData = pngOutputStream.toByteArray();
        logger.info("Exitign  getQRCodeImage () ",text);

        return pngData;
    }
}