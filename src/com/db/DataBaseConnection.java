package com.db;

import com.Main;
import com.controller.Controller;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static Connection connection;

    private DataBaseConnection() {
    }

    public static Connection getInstance(){
        if (connection ==null){
            try {
                connection= DriverManager.getConnection(Main.CONFIG.getDatasourceUrl()
                        ,Main.CONFIG.getDatasourceUN(),Main.CONFIG.getDatasourcePW());
            } catch (SQLException ex) {
                Controller.lunchAlert(ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
        return connection;
    }


}
