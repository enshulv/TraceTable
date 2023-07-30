package org.EMAC.listener.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.EMAC.listener.CommonPlugin;

public class GetForm extends CommonPlugin{
    private HikariDataSource hikari;

    public HikariDataSource getForm(){
        
        hikari = getDataSource();
        
        try (Connection connection = hikari.getConnection()) {
            String[] create = {
                "CREATE TABLE IF NOT EXISTS `user_table` (" +
                "  `UUID` CHAR(36) NOT NULL PRIMARY KEY," +
                "  `player_name` VARCHAR(50) NOT NULL" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';",
            
                "CREATE TABLE IF NOT EXISTS `production_operation_table` (" +
                "  `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "  `UUID` CHAR(36) NOT NULL," +
                "  `timestamp` BIGINT(20) NOT NULL," +
                "  `operation` VARCHAR(30) NOT NULL," +
                "  `consumables` TEXT," +
                "  `outputs` TEXT," +
                "  FOREIGN KEY (`UUID`) REFERENCES `user_table`(`UUID`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='生产操作表';"
            };
            for (String s : create) {
                PreparedStatement preparedStatement = connection.prepareStatement(s);
                preparedStatement.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hikari;
    }
}
