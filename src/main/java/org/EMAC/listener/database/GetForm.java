package org.EMAC.listener.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetForm{

    public HikariDataSource getForm(HikariDataSource hikari){
        
        try (Connection connection = hikari.getConnection()) {
            String[] create = {
                "CREATE TABLE IF NOT EXISTS `user_table` (" +
                "  `UUID` CHAR(36) NOT NULL PRIMARY KEY," +
                "  `player_name` VARCHAR(50) NOT NULL" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';",
                "CREATE TABLE IF NOT EXISTS `production_operation_table` (" +
                "  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "  `UUID` CHAR(36) NOT NULL," +
                "  `item_name` VARCHAR(100) NOT NULL," +
                "  `operation` VARCHAR(100) NOT NULL," +
                "  `enchant` VARCHAR(200)," +
                "  `amount` INT NOT NULL," +
                "  `timestamps` TEXT NOT NULL," +
                "  FOREIGN KEY (`UUID`) REFERENCES `user_table`(`UUID`)," +
                "UNIQUE KEY unique_values (item_name, operation, enchant)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作表';",
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
