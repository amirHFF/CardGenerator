package com.service;

import com.Main;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.controller.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.model.CardInfo;
import com.spire.presentation.*;
import com.spire.presentation.drawing.IImageData;
import javafx.scene.control.Alert;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.w3c.dom.Element;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class CardGenerator {

    private static final String TEMPLATE_NAME_KEY = "نام";
    private static final String TEMPLATE_TEL_KEY = "تلفن";
    private static final String TEMPLATE_NATIONAL_ID_KEY = "کد ملی";
    private static final String TEMPLATE_BIRTH_DATE_KEY = "تاریخ تولد";


    public void CardGenerator(BufferedImage checkMarkBufferedImage,CardInfo cardInfo) throws Exception {
//        if (cardInfo.getFirstName() == null || cardInfo.getLastName()==null || cardInfo.getBirthDay()==null
//                || cardInfo.getPhone() == null || cardInfo.getQrCode() == null || cardInfo.getPicture() == null)
        String errorMessage = cardInfo.getFirstName() == null ? "first name" :
                (cardInfo.getLastName() == null ? "last name" : (cardInfo.getBirthDay() == null ? "birth date" :
                        (cardInfo.getPhone() == null ? "phone" : (cardInfo.getQrCode() == null ? "Qr code" :
                                (cardInfo.getPicHash() == null ? "picture hash" : "true")))));
        if (errorMessage.equals("true")) {
            String fullName;
            InputStream templateInputStream = new FileInputStream(new File(Main.CONFIG.getPptpTemplatePath()));
            Presentation presentation = new Presentation();
            presentation.loadFromStream(templateInputStream, FileFormat.PPTX_2013);
            ISlide slide = presentation.getSlides().get(0);
            Map<String, String> map = new HashMap<>();
            fullName = replaceCharacters(cardInfo.getFirstName().concat(" ").concat(cardInfo.getLastName()));

            String farsiTel = this.replaceWithPersian(cardInfo.getPhone());
            String farsiNationalId = this.replaceWithPersian(cardInfo.getNationalCode());
            String farsiBirthDate = this.replaceWithPersian(cardInfo.getBirthDay());


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
            InputStream qrCodeStream = new ByteArrayInputStream(cardInfo.getQrCode());
            BufferedImage qrBufferedImage = ImageIO.read(qrCodeStream);
            IImageData qrImage = presentation.getImages().append(qrBufferedImage);
            ShapeCollection shapes = slide.getShapes();
            if (shapes.get(4) instanceof SlidePicture) {
                ((SlidePicture) shapes.get(4)).getPictureFill().getPicture().setEmbedImage(qrImage);
            }

            ByteArrayInputStream profilePictureStream = new ByteArrayInputStream(cardInfo.getPicture());
            BufferedImage profileBufferedImage = ImageIO.read(profilePictureStream);
            IImageData userImage = presentation.getImages().append(profileBufferedImage);
            shapes.get(5).getFill().getPictureFill().getPicture().setEmbedImage(userImage);

            //set check-mark

            IImageData checkMarkImage = presentation.getImages().append(checkMarkBufferedImage);
            if (cardInfo.isSeizure() && shapes.get(6) instanceof SlidePicture) {
                ((SlidePicture) shapes.get(6)).getPictureFill().getPicture().setEmbedImage(checkMarkImage);

            }

            if (cardInfo.isCommunicationProblem() && shapes.get(7) instanceof SlidePicture)
                ((SlidePicture) shapes.get(7)).getPictureFill().getPicture().setEmbedImage(checkMarkImage);
            if (cardInfo.isADHD() && shapes.get(8) instanceof SlidePicture)
                ((SlidePicture) shapes.get(8)).getPictureFill().getPicture().setEmbedImage(checkMarkImage);


//        presentation.saveToFile(path + nationalId + ".pptx", FileFormat.PPTX_2013);
            //converting pptx to image//
            presentation.setAutoCompressPictures(false);
            BufferedImage image = presentation.getSlides().get(0).saveAsImage(1075, 673);
            String fileName = Main.CONFIG.getPptpDestination().concat("/").concat(cardInfo.getNationalCode() + ".jpg");

            // Image writer
            JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO.getImageWritersBySuffix("jpg").next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(new File(fileName));
            imageWriter.setOutput(ios);

            // Compression
            JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(1);

            // Metadata (dpi)
            IIOMetadata data = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image), jpegParams);
            Element tree = (Element) data.getAsTree("javax_imageio_jpeg_image_1.0");
            Element jfif = (Element) tree.getElementsByTagName("app0JFIF").item(0);
            jfif.setAttribute("Xdensity", Integer.toString(600));
            jfif.setAttribute("Ydensity", Integer.toString(600));
            jfif.setAttribute("resUnits", "1"); // density is dots per inch
            data.setFromTree("javax_imageio_jpeg_image_1.0", tree);

            // Write and clean up
            imageWriter.write(null, new IIOImage(image, null, data), jpegParams);
            ios.close();
            imageWriter.dispose();
        }
        if (!errorMessage.equals("true")){
            throw new RuntimeException(errorMessage.concat(" is fetched null from database"));
        }
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

    public byte[] qrGenerate(String string) {
        QRCode qrCode =QRCode.from(string)
                .to(ImageType.JPG).withSize(246,246);
        ByteArrayOutputStream stream =qrCode.stream();

        return stream.toByteArray();
    }

    public final byte[] downloadFile(String token, String fileHash) {
        byte[] byteArray = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("https://sandbox.podspace.ir:8443/".concat("api/files/")+ fileHash);
        try {
//            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            request.setHeader("Authorization", token);
            request.setHeader("_token_", token);
            request.setHeader("_token_issuer_", "1");

            CloseableHttpResponse httpResponse = httpClient.execute(request);
            if (httpResponse.getCode()==200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    if (httpEntity.getContent() != null)
                        byteArray = httpEntity.getContent().readAllBytes();
                }
            }
            else
                Controller.lunchAlert("http error : " +httpResponse.getCode(), Alert.AlertType.ERROR);

        } catch (Exception exception) {
            Controller.lunchAlert(exception.getMessage(), Alert.AlertType.ERROR);

        }
        return byteArray;
    }
    private String replaceWithPersian(String value) {
        String newValue = value.replace("/", "/").replace("1", "۱").replace("2", "۲")
                .replace("3", "۳").replace("4", "۴").replace("٤", "۴").replace("5", "۵").replace("٥", "۵")
                .replace("٦", "۶").replace("6", "۶").replace("7", "۷")
                .replace("8", "۸").replace("9", "۹").replace("0", "۰").replace("٠", "۰");

        return newValue;
    }

    private String replaceCharacters(String string) {

        string = string.replaceAll("ی", "ي");
        string = string.replaceAll("ک", "ك");
        string = string.replaceAll("آ", "آ\u200C");
        string = string.replaceAll("ژ", "ژ\u200C");
        checkForSpecificCharacter(string, 'گ');
        checkForSpecificCharacter(string, 'چ');
        checkForSpecificCharacter(string, 'پ');
        return string;

    }

    private void checkForSpecificCharacter(String string, char c) {
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == c && i >= 1 && checkPrefix(string.charAt(i - 1))) {
                if (string.charAt(i + 1) != ' ' && string.charAt(i + 1) != '\u200C') {
                    String postfix = string.substring(i + 1);
                    String replacement = "ـ" + c + "'ـ";
                    string = string.substring(0, i).concat(replacement).concat(postfix);
                } else {
                    String postfix = string.substring(i + 1);
                    String replacement = "ـ" + c;
                    string = string.substring(0, i).concat(replacement).concat(postfix);
                }
            }
            if (string.charAt(i) == c && string.charAt(i + 1) != ' ' && string.charAt(i + 1) != '\u200C') {
                String postfix = string.substring(i + 1);
                String replacement = c + "ـ";
                string = string.substring(0, i).concat(replacement).concat(postfix);
            }
        }
    }

    private boolean checkPrefix(char c) {
        return (c != 'ا' && c != 'د' && c != 'ذ' && c != 'ر' && c != 'و' && c != 'ز' && c != ' ' && c != '\u200C');
    }

    public BufferedImage getCheckMarkImage(String path){
        BufferedImage checkMarkBufferedImage=null;
        try {
        InputStream checkMarkInputStream = new FileInputStream(new File(path));
            checkMarkBufferedImage = ImageIO.read(checkMarkInputStream);
        } catch (IOException e) {
            Controller.lunchAlert(e.getMessage(), Alert.AlertType.ERROR);
        }
        return checkMarkBufferedImage;
    }

}



