package com.db;

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
                connection= DriverManager.getConnection("jdbc:oracle:thin:@172.16.110.122:1525:PDB_DEV_EHS"
                        ,"autism_app_develop","autism_app_develop");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return connection;
    }


}
