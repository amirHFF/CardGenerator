package com.db;

import com.Main;

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
                ex.printStackTrace();
            }
        }
        return connection;
    }


}
