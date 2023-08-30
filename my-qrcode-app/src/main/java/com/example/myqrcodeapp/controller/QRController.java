
package com.example.myqrcodeapp.controller;

import com.example.myqrcodeapp.model.QrData;
import com.example.myqrcodeapp.service.QRCodeGenerator;
import com.example.myqrcodeapp.util.QRCodeReader;
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
        logger.info("Inside getQRCode ");
        byte[] image2 = new byte[0];
        try {
            image2 = qrCodeGenerator.generateQRCodeImage(qrData.getQrText(), qrData.getWidht(), qrData.getHeight(), qrCodeImagePath+qrData.getQrId());
            image2=qrCodeGenerator.getQRCodeImage(qrData.getQrText(), qrData.getWidht(), qrData.getHeight());
        } catch (WriterException | IOException e) {
            logger.info("Inside catch createQRCode ");

            e.printStackTrace();
        }
        logger.info("Exiting getQRCode....");

        return ResponseEntity.status(HttpStatus.OK).body(image2);
    }

    @GetMapping(value="/qrcode/{qrId}",produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@PathVariable("qrId") String qrId) {
        logger.info("Inside generateQRCode ()....");
        byte[] image2 = new byte[0];
        String preQrCode;
        try {
            String filePath=qrCodeImagePath+qrId;
            File file = new File(filePath+qrFileExtension);
            if(file.exists())
            image2 = QRCodeReader.decodeQRCodeCreated(file);

        } catch (  IOException e) {
            logger.info("Inside generateQRCode ()--> catch block....");

            e.printStackTrace();
        }
        logger.info("Exiting generateQRCode ()....");
        return ResponseEntity.status(HttpStatus.OK).body(image2);
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
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body(image2);
    }

    @GetMapping(value="/readqrcode/{qrId}")
    public ResponseEntity<String> readQRCode(@PathVariable("qrId") String qrId) {
        String result="";
        try {
            File file = new File(qrCodeImagePath+qrId+qrFileExtension);
             result = QRCodeReader.decodeQRCode(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}

