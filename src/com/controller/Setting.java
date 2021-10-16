package com.controller;

import com.Main;
import com.config.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Setting implements Initializable {

    @FXML
    private TextField pptpDestination;
    @FXML
    private TextField pptpTemplatePath;
    @FXML
    private TextField datasourceUrl;
    @FXML
    private TextField datasourceUN;
    @FXML
    private TextField datasourcePW;
    @FXML
    private TextField qrCodeUrl;
    @FXML
    private TextField token;
    @FXML
    private TextField downloadService;
    @FXML
    private TextField checkMarkImagePath;

    @FXML
    private Alert alert;
    @FXML
    public void onApplyAction(ActionEvent actionEvent){
        alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("آیا تغییرات بوجود آمده مورد تایید است ؟");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)) {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            try {
                Main.CONFIG=settingSetter();
                objectMapper.writeValue(Main.getConfigFile(), Main.CONFIG);
                ((Stage)downloadService.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace();
                Controller.lunchAlert(e.getMessage(), Alert.AlertType.ERROR);
        }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pptpDestination.setText(Main.CONFIG.getPptpDestination());
        pptpTemplatePath.setText(Main.CONFIG.getPptpTemplatePath());
        datasourceUrl.setText(Main.CONFIG.getDatasourceUrl());
        datasourceUN.setText(Main.CONFIG.getDatasourceUN());
        datasourcePW.setText(Main.CONFIG.getDatasourcePW());
        qrCodeUrl.setText(Main.CONFIG.getQrCodeUrl());
        token.setText(Main.CONFIG.getToken());
        downloadService.setText(Main.CONFIG.getDownloadService());
        checkMarkImagePath.setText(Main.CONFIG.getCheckMarkImagePath());
    }

    private Configuration settingSetter(){
        Configuration config=new Configuration();
        config.setDatasourcePW(datasourcePW.getText());
        config.setDatasourceUrl(datasourceUrl.getText());
        config.setDatasourceUN(datasourceUN.getText());
        config.setCheckMarkImagePath(checkMarkImagePath.getText());
        config.setDownloadService(downloadService.getText());
        config.setPptpTemplatePath(pptpTemplatePath.getText());
        config.setPptpDestination(pptpDestination.getText());
        config.setQrCodeUrl(qrCodeUrl.getText());
        config.setToken(token.getText());

        return config;
    }
}
