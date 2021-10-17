package com.controller;

import com.Main;
import com.model.CardInfo;
import com.repository.SqlCodeRunner;
import com.service.CardGenerator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    private static Alert alert;
    @FXML
    private MenuItem settingMenu, sessionSetting;

    @FXML
    private Button startProcess;

    @FXML
    private TextArea nationalCodeField;

    @FXML
    private Label countingLabel;

    @FXML
    private RadioButton comma, lineBreak;

    private String splitter = ",";

    @FXML
    private void onStartClick(ActionEvent actionEvent) {
        String pureCode = nationalCodeField.getText().replaceAll(" ", "");
        HashSet<String> nationalCodeSet;
        if (pureCode.contains("--") && pureCode.contains("==")) {
            nationalCodeSet = checkDuplicate(new HashSet<>(Set.of(pureCode.substring(0, pureCode.indexOf("--")).split(splitter))),
                    new HashSet<>(Set.of(pureCode.substring(pureCode.indexOf("--") + 2, pureCode.indexOf("==")).split(splitter))));
            nationalCodeField.setText(nationalCodeSet.toString());
        } else {
            nationalCodeSet = new HashSet<>(Set.of(pureCode.split(splitter)));
        }
        SqlCodeRunner sqlCodeRunner = new SqlCodeRunner();
        if (nationalCodeValidation(pureCode)) {
            CardGenerator cardGenerator = new CardGenerator();
            HashSet<CardInfo> cardInfoList = new HashSet<>();
            BufferedImage checkMarkImage = cardGenerator.getCheckMarkImage(Main.CONFIG.getCheckMarkImagePath());

            try {
                cardInfoList = sqlCodeRunner.fetchAutismByNationalCode(nationalCodeSet);
                for (CardInfo cardInfo : cardInfoList) {
                    cardInfo.setPicture(cardGenerator.downloadFile(Main.CONFIG.getToken(), cardInfo));
                    cardInfo.setQrCode(cardGenerator.qrGenerate(Main.CONFIG.getQrCodeUrl() + cardInfo.getNationalCode()));
                    cardGenerator.CardGenerator(checkMarkImage, cardInfo);
                }
            } catch (Exception throwables) {
                throwables.printStackTrace();
                lunchAlert(throwables.getMessage(), Alert.AlertType.ERROR);
            }

        }
    }

    @FXML
    private void onRadioButtonClick(ActionEvent actionEvent) {
        if (lineBreak.isSelected()) {
            splitter = "\n";
        }
        if (!lineBreak.isSelected())
            splitter=",";
    }

    @FXML
    private void onSettingClick(ActionEvent actionEvent) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/controller/setting.fxml"));
            System.out.println(loader.getLocation());
            Scene scene = new Scene(loader.load(), 600, 425);
            Stage stage = new Stage();
            stage.setTitle("Setting");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            nationalCodeField.setText(Arrays.toString(e.getStackTrace()));
            lunchAlert(e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void onSessionSetting(ActionEvent actionEvent) {
        SqlCodeRunner sqlCodeRunner = new SqlCodeRunner();
        TextInputDialog sessionInput = new TextInputDialog();
        sessionInput.setContentText("Set new session");
        sessionInput.showAndWait();
        if (sessionInput.getEditor().getText()!=null) {
            try {
                boolean state = sqlCodeRunner.changeSession(sessionInput.getEditor().getText());
                if (state)
                    lunchAlert("successful alter session", Alert.AlertType.INFORMATION);
                else
                    lunchAlert("failure alter session", Alert.AlertType.ERROR);
            } catch (SQLException ex) {
                lunchAlert(ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public static void lunchAlert(String text, Alert.AlertType alertType) {
        alert = new Alert(alertType);
        alert.setAlertType(alertType);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public boolean nationalCodeValidation(String nationalCodeList) {
        for (String nationalCode : nationalCodeList.split(splitter)) {
            if (!nationalCode.matches("\\d{10}") && nationalCode.length() != 10) {
                lunchAlert(nationalCode + " " + "wrong nationalCode syntax", Alert.AlertType.WARNING);
                return false;
            } else
                return true;
        }
        return true;
    }

    public boolean nationalCodeValidationNoErrorLunch(String nationalCodeList) {
        for (String nationalCode : nationalCodeList.split(splitter)) {
            if (!nationalCode.matches("\\d{10}") && nationalCode.length() != 10) {
                return true;
            } else
                return false;
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comma.setSelected(true);
        nationalCodeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                String[] codes = nationalCodeField.getText().split(splitter);
                Set<String> codesSet = new HashSet<>(Set.of(codes));
                codesSet.removeIf(new Controller()::nationalCodeValidationNoErrorLunch);

                countingLabel.setText(String.format("تعداد کد ملی های ولید : %d", codesSet.size()));
                countingLabel.setVisible(nationalCodeField.getText().length() > 0);
            }
        });
    }

    public HashSet<String> checkDuplicate(HashSet<String> nationalCode1, HashSet<String> nationalCode2) {
        HashSet<String> result = new HashSet<>();
        //            result.add(nationalCode2.stream().filter(s1::equals).collect(Collectors.toSet()));
        nationalCode1.removeIf(nationalCode2::contains);
        return nationalCode1;
    }
}
