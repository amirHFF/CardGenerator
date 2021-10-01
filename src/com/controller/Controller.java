package com.controller;

import com.model.CardInfo;
import com.repository.SqlCodeRunner;
import com.service.CardGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {

    private CardGenerator cardGenerator=new CardGenerator();

    @FXML
    private Button startProcess;

    @FXML
    private TextArea nationalCodeField;

    private SqlCodeRunner sqlCodeRunner=new SqlCodeRunner();
    @FXML
    private void onStartClick(ActionEvent actionEvent){
        List<CardInfo> cardInfoList=new ArrayList<>();
        String nationalCodeList=nationalCodeField.getText();
        String[] nationalCode=nationalCodeList.split(",");
        try {
            cardInfoList=sqlCodeRunner.fetchAutismByNationalCode(Arrays.asList(nationalCode));
            for (CardInfo cardInfo : cardInfoList) {
              cardGenerator.downloadFile("30aa7ede70254a569f6838e13de4bfe5",cardInfo.getPicture());
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
