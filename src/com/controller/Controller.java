package com.controller;

import com.Main;
import com.model.CardInfo;
import com.repository.SqlCodeRunner;
import com.service.CardGenerator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller  {

    private static Alert alert;

    @FXML
    private MenuItem settingMenu;

    @FXML
    private Button startProcess;

    @FXML
    private TextArea nationalCodeField;


    @FXML
    private void onStartClick(ActionEvent actionEvent) {
        SqlCodeRunner sqlCodeRunner = new SqlCodeRunner();
        if (nationalCodeValidation(nationalCodeField.getText())) {
            CardGenerator cardGenerator = new CardGenerator();
            List<CardInfo> cardInfoList = new ArrayList<>();
            BufferedImage checkMarkImage = cardGenerator.getCheckMarkImage(Main.CONFIG.getCheckMarkImagePath());
            String nationalCodeList = nationalCodeField.getText();
            String[] nationalCode = nationalCodeList.split(",");
            try {
                cardInfoList = sqlCodeRunner.fetchAutismByNationalCode(Arrays.asList(nationalCode));
                for (CardInfo cardInfo : cardInfoList) {
                    cardInfo.setPicture(cardGenerator.downloadFile(Main.CONFIG.getToken(), cardInfo.getPicHash()));
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
    private void onSettingClick(ActionEvent actionEvent) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/controller/setting.fxml"));
            System.out.println(loader.getLocation());
            Scene scene=new Scene(loader.load(),600,425);
            Stage stage = new Stage();
            stage.setTitle("Setting");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            nationalCodeField.setText(Arrays.toString(e.getStackTrace()));
            lunchAlert(e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    public static void lunchAlert(String text, Alert.AlertType alertType) {
        alert = new Alert(alertType);
        alert.setAlertType(alertType);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public boolean nationalCodeValidation(String nationalCodeList) {
        for (String nationalCode : nationalCodeList.split(",")) {
            if (!nationalCode.matches("\\d{10}") && nationalCode.length() != 10) {
                lunchAlert(nationalCode + " " + "wrong nationalCode syntax", Alert.AlertType.WARNING);
                return false;
            } else
                return true;
        }
        return true;
    }

//    [javafx.fxml.FXMLLoader.constructLoadException(FXMLLoader.java:2707),javafx.fxml.FXMLLoader.loadImpl(FXMLLoader.java:2685),javafx.fxml.FXMLLoader.loadImpl(FXMLLoader.java:2548),javafx.fxml.FXMLLoader.loadImpl(FXMLLoader.java:3331),javafx.fxml.FXMLLoader.loadImpl(FXMLLoader.java:3287),javafx.fxml.FXMLLoader.loadImpl(FXMLLoader.java:3255),javafx.fxml.FXMLLoader.loadImpl(FXMLLoader.java:3227),javafx.fxml.FXMLLoader.loadImpl(FXMLLoader.java:3203),javafx.fxml.FXMLLoader.load(FXMLLoader.java:3196),com.controller.Controller.onSettingClick(Controller.java:67),java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(
//    Native Method),java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62),java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43),java.base/java.lang.reflect.Method.invoke(Method.java:566),com.sun.javafx.reflect.Trampoline.invoke(MethodUtil.java:77),java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(
//    Native Method),java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62),java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43),java.base/java.lang.reflect.Method.invoke(Method.java:566),com.sun.javafx.reflect.MethodUtil.invoke(MethodUtil.java:275),com.sun.javafx.fxml.MethodHelper.invoke(MethodHelper.java:84),javafx.fxml.FXMLLoader$MethodHandler.invoke(FXMLLoader.java:1852),javafx.fxml.FXMLLoader$ControllerMethodEventHandler.handle(FXMLLoader.java:1724),com.sun.javafx.event.CompositeEventHandler.dispatchBubblingEvent(CompositeEventHandler.java:86),com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:234),com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:191),com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:58),com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114),com.sun.javafx.event.EventUtil.fireEventImpl(EventUtil.java:74),com.sun.javafx.event.EventUtil.fireEvent(EventUtil.java:49),javafx.event.Event.fireEvent(Event.java:198),javafx.scene.control.MenuItem.fire(MenuItem.java:459),com.sun.javafx.scene.control.ContextMenuContent$MenuItemContainer.doSelect(ContextMenuContent.java:1385),com.sun.javafx.scene.control.ContextMenuContent$MenuItemContainer.lambda$createChildren$12(ContextMenuContent.java:1338),com.sun.javafx.event.CompositeEventHandler$NormalEventHandlerRecord.handleBubblingEvent(CompositeEventHandler.java:247),com.sun.javafx.event.CompositeEventHandler.dispatchBubblingEvent(CompositeEventHandler.java:80),com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:234),com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:191),com.sun.javafx.event.CompositeEventDispatcher.dispatchBubblingEvent(CompositeEventDispatcher.java:59),com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:58),com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114),com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:56),com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114),com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:56),com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114),com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:56),com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114),com.sun.javafx.event.EventUtil.fireEventImpl(EventUtil.java:74),com.sun.javafx.event.EventUtil.fireEvent(EventUtil.java:54),javafx.event.Event.fireEvent(Event.java:198),javafx.scene.Scene$MouseHandler.process(Scene.java:3897),javafx.scene.Scene.processMouseEvent(Scene.java:1878),javafx.scene.Scene$ScenePeerListener.mouseEvent(Scene.java:2623),com.sun.javafx.tk.quantum.GlassViewEventHandler$MouseEventNotification.run(GlassViewEventHandler.java:411),com.sun.javafx.tk.quantum.GlassViewEventHandler$MouseEventNotification.run(GlassViewEventHandler.java:301),java.base/java.security.AccessController.doPrivileged(
//    Native Method),com.sun.javafx.tk.quantum.GlassViewEventHandler.lambda$handleMouseEvent$2(GlassViewEventHandler.java:450),com.sun.javafx.tk.quantum.QuantumToolkit.runWithoutRenderLock(QuantumToolkit.java:424),com.sun.javafx.tk.quantum.GlassViewEventHandler.handleMouseEvent(GlassViewEventHandler.java:449),com.sun.glass.ui.View.handleMouseEvent(View.java:557),com.sun.glass.ui.View.notifyMouse(View.java:943),com.sun.glass.ui.win.WinApplication._runLoop(
//    Native Method),com.sun.glass.ui.win.WinApplication.lambda$runLoop$3(WinApplication.java:184),java.base/java.lang.Thread.run(Thread.java:834)]
}
