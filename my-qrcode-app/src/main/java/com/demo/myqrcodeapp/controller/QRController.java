
package com.demo.myqrcodeapp.controller;

import com.demo.myqrcodeapp.model.QrData;
import com.demo.myqrcodeapp.util.QRCodeReader;
import com.demo.myqrcodeapp.service.QRCodeGenerator;
import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
public class QRController {

    @Value("${qr.filepath}")
    String qrCodeImagePath;
    @Value("${qr.file.extension}")
    String qrFileExtension;

    @Autowired
    QRCodeGenerator qrCodeGenerator;

    Logger logger = LoggerFactory.getLogger(QRController.class);

    @PostMapping(value="/qrcode/",produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> createQRCode(@RequestBody QrData qrData) {

        logger.info("Inside getQRCode with qrData:{}",qrData);
        byte[] imageBytes = new byte[0];

        try {
            logger.info("calling qrCodeGenerator.generateQRCodeImage....");
            imageBytes = qrCodeGenerator.generateQRCodeImage(
                    qrData.getQrText(),
                    qrData.getWidht(),
                    qrData.getHeight(),
                    qrCodeImagePath + qrData.getQrId()
            );

        } catch (WriterException | IOException e) {
            logger.error("Error in createQRCode:", e);
        }

        return ResponseEntity.ok(imageBytes);
    }


    @GetMapping(value="/qrcode/{qrId}",produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@PathVariable("qrId") String qrId) {
        logger.info("Inside generateQRCode().... for qrId: {}", qrId);
        byte[] image2 = new byte[0];

        try {
            File file = new File(qrCodeImagePath + qrId + qrFileExtension);

            if (file.exists()) {
                logger.info("File is present....");
                image2 = QRCodeReader.decodeQRCodeCreated(file);
            }

        } catch (IOException e) {
            logger.error("Exception in generateQRCode:", e);
        }

        return ResponseEntity.ok(image2);
    }

    @GetMapping(value="/qrcode1/{text}/{width}/{height}",produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(
            @PathVariable("text") String text,
            @PathVariable("width") int width,
            @PathVariable("height") int height) {

        byte[] image2 = new byte[0];

        try {
            image2 = qrCodeGenerator.getQRCodeImage(text, width, height);
        } catch (WriterException | IOException e) {
            logger.error("Error in generateQRCode:", e);
        }

        return ResponseEntity.ok(image2);
    }

    @GetMapping(value="/readqrcode/{qrId}")
    public ResponseEntity<String> readQRCode(@PathVariable("qrId") String qrId) {
        String result = "";

        try {
            File file = new File(qrCodeImagePath + qrId + qrFileExtension);
            result = QRCodeReader.decodeQRCode(file);

        } catch (IOException e) {
            logger.error("Error in readQRCode:", e);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/")
    public ResponseEntity<String> getHello() {
        return ResponseEntity.ok("Welcome to My QrCode App....");
    }

}


