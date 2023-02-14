package com.kmsoft.lucene.database.demo.dao;

import com.kmsoft.lucene.database.demo.entity.FilmEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 查询Dao
 * </P>
 *
 * @author Major Tom
 * @since 2023/2/14 11:35
 */
public class FilmDao {
    /**
     * 获取所有电影数据
     *
     * @return /
     */
    public List<FilmEntity> getAllFilms() {
        List<FilmEntity> resultList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            //加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //创建数据库连接对象
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/Sakila?useSSL=true",
                    "root",
                    "1234"
            );
            //进行查询
            String sql = "select * from film";
            statement = conn.prepareStatement(sql);
            resultSet = statement.executeQuery();
            //得到并处理结果集
            while (resultSet.next()) {
                FilmEntity film = FilmEntity.builder()
                        .filmId(resultSet.getLong("film_id"))
                        .title(resultSet.getString("title"))
                        .description(resultSet.getString("description"))

                        .releaseYear((resultSet.getString("release_year").split("-"))[0])

                        .build();
                resultList.add(film);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                if (null != conn) {
                    conn.close();
                }
                if (null != statement) {
                    statement.close();
                }
                if (null != resultSet) {
                    resultSet.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }
}
