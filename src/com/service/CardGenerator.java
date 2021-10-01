package com.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.spire.presentation.*;
import com.spire.presentation.drawing.IImageData;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class CardGenerator {

        private static final String TEMPLATE_NAME_KEY = "نام";
        private static final String TEMPLATE_TEL_KEY = "تلفن";
        private static final String TEMPLATE_NATIONAL_ID_KEY = "کد ملی";
        private static final String TEMPLATE_BIRTH_DATE_KEY = "تاریخ تولد";


        public void CardGenerator(BufferedImage checkMarkBufferedImage, String fullName, String birthDate, String tel, String nationalId, boolean seizures, boolean hyperactivity, boolean cognitiveProblems, byte[] qrCode, byte[] userPicture) throws Exception {
            if (fullName == null || birthDate == null || tel == null || qrCode == null || userPicture == null)
                throw new RuntimeException("NULL_VALUES");
            InputStream templateInputStream = this.getClass().getResourceAsStream("/card/template (3).pptx");
            Presentation presentation = new Presentation();
            presentation.loadFromStream(templateInputStream, FileFormat.PPTX_2013);
            ISlide slide = presentation.getSlides().get(0);
            Map<String, String> map = new HashMap<>();
            String farsiTel;
            String farsiBirthDate;
            String farsiNationalId;
            char[] resultChars = tel.toCharArray();
            for (int i = 0; i < resultChars.length; ++i) {
                resultChars[i] = this.replaceWithPersian(resultChars[i]);
            }

            farsiTel = String.valueOf(resultChars);
            resultChars = nationalId.toCharArray();
            for (int i = 0; i < resultChars.length; ++i) {
                resultChars[i] = this.replaceWithPersian(resultChars[i]);
            }
            farsiNationalId = String.valueOf(resultChars);
            resultChars = birthDate.toCharArray();
            for (int i = 0; i < resultChars.length; ++i) {
                resultChars[i] = this.replaceWithPersian(resultChars[i]);
            }
            farsiBirthDate = String.valueOf(resultChars);

            map.put(TEMPLATE_NAME_KEY, fullName);
            map.put(TEMPLATE_TEL_KEY, farsiTel);
            map.put(TEMPLATE_BIRTH_DATE_KEY, farsiBirthDate);
            map.put(TEMPLATE_NATIONAL_ID_KEY, farsiNationalId);


            for (Object shape : slide.getShapes()) {
                if (shape instanceof IAutoShape) {
                    ParagraphEx paragraphEx = ((IAutoShape) shape).getTextFrame().getParagraphs().get(0);
                    for (Object key : map.keySet()) {
                        if (paragraphEx.getText().contains((String) key)) {
                            String key1 = (String) key;
                            paragraphEx.setText(paragraphEx.getText().replace(key1, map.get(key1)));

                        }
                    }
                }
            }

            //set QR code picture
            InputStream qrCodeStream = new ByteArrayInputStream(qrCode);
            BufferedImage qrBufferedImage = ImageIO.read(qrCodeStream);
            IImageData qrImage = presentation.getImages().append(qrBufferedImage);
            ShapeCollection shapes = slide.getShapes();
            if (shapes.get(4) instanceof SlidePicture) {
                ((SlidePicture) shapes.get(4)).getPictureFill().getPicture().setEmbedImage(qrImage);
            }

            ByteArrayInputStream profilePictureStream = new ByteArrayInputStream(userPicture);
            BufferedImage profileBufferedImage = ImageIO.read(profilePictureStream);
            IImageData userImage = presentation.getImages().append(profileBufferedImage);
            shapes.get(5).getFill().getPictureFill().getPicture().setEmbedImage(userImage);

            //set check-mark

            IImageData checkMarkImage = presentation.getImages().append(checkMarkBufferedImage);
            if (seizures && shapes.get(6) instanceof SlidePicture) {
                ((SlidePicture) shapes.get(6)).getPictureFill().getPicture().setEmbedImage(checkMarkImage);

            }

            if (hyperactivity && shapes.get(7) instanceof SlidePicture)
                ((SlidePicture) shapes.get(7)).getPictureFill().getPicture().setEmbedImage(checkMarkImage);
            if (cognitiveProblems && shapes.get(8) instanceof SlidePicture)
                ((SlidePicture) shapes.get(8)).getPictureFill().getPicture().setEmbedImage(checkMarkImage);


            String path = "src/main/resources/card/cardPictures/";
            presentation.saveToFile(path + nationalId + ".pptx", FileFormat.PPTX_2013);
        }

        private char replaceWithPersian(char resultChar) {
            if (resultChar == '0' || resultChar == '٠') {
                return '۰';
            } else if (resultChar == '/') {
                return '/';
            } else if (resultChar == '1' || resultChar == '١') {
                return '۱';
            } else if (resultChar == '2' || resultChar == '٢') {
                return '۲';
            } else if (resultChar == '3' || resultChar == '٣') {
                return '۳';
            } else if (resultChar == '4' || resultChar == '٤') {
                return '۴';
            } else if (resultChar == '5' || resultChar == '٥') {
                return '۵';
            } else if (resultChar == '6' || resultChar == '٦') {
                return '۶';
            } else if (resultChar == '7' || resultChar == '٧') {
                return '۷';
            } else if (resultChar == '8' || resultChar == '٨') {
                return '۸';
            } else {
                return resultChar == '9' || resultChar == '٩' ? '۹' : resultChar;
            }
        }

        public final byte[] downloadFile(String token, String fileHash) {
            ByteArrayInputStream bais = null;
            CloseableHttpClient httpClient= HttpClients.createDefault();
            HttpGet request=new HttpGet("https://podspace.pod.ir/".concat("api/files/").concat("{0}")+fileHash);
            try {
                request.setHeader(HttpHeaders.CONTENT_TYPE,"application/x-www-form-urlencoded");
                request.setHeader("Authorization","Bearer ".concat(token));
                request.setHeader("_token_",token);
                request.setHeader("_token_issuer_","1");

                CloseableHttpResponse httpResponse=httpClient.execute(request);
                HttpEntity httpEntity=httpResponse.getEntity();
                if (httpEntity!=null) {
                    bais= (ByteArrayInputStream) httpEntity.getContent();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            if (bais ==null)
                throw new RuntimeException("null file downloaded");
            return bais.readAllBytes();
        }

}



