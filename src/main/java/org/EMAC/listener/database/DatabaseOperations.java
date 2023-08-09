package org.EMAC.listener.database;

import com.zaxxer.hikari.HikariDataSource;

import org.EMAC.listener.info.ItemInfo;
import org.EMAC.listener.util.JsonUtil;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DatabaseOperations {

    private HikariDataSource dataSource;

    public DatabaseOperations(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveAll(Map<UUID, List<ItemInfo>> map) {
        if (map.isEmpty())
            return;

        String sql = "INSERT INTO production_operation_table (UUID, item_name, operation, enchant, amount, timestamps) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE amount = amount + ?;";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Map.Entry<UUID, List<ItemInfo>> entry : map.entrySet()) {
                    UUID playerId = entry.getKey();
                    List<ItemInfo> items = entry.getValue();

                    for (ItemInfo item : items) {
                        statement.setString(1, playerId.toString());
                        statement.setString(2, item.getItemName());
                        statement.setString(3, item.getOperation());
                        statement.setString(4, item.getEnchant());
                        statement.setInt(5, item.getAmount());
                        statement.setString(6, JsonUtil.convertToJson(item.getTimestamps()));
                        statement.setInt(7, item.getAmount());
                        statement.executeUpdate();
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        map.clear();
    }

    public boolean isPlayerInDatabase(String uuid) {
        String query = "SELECT UUID FROM user_table WHERE UUID = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // 如果有结果则返回true，表示玩家已在数据库中
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertPlayerIntoDatabase(String uuid, String name) {
        String sql = "INSERT INTO user_table (UUID, player_name) VALUES (?, ?)";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, uuid);
                preparedStatement.setString(2, name);
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
