package com.example.tudien2;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class JDBCManagement {

    private static Connection connection = JDBCConnection.getJDBCConnection();
    //Lấy danh sách tất cả các từ trên MySQL
    public List<Word> getAllWord () {
        List<Word> words = new ArrayList<>(); // tạo 1 list
        String sql = "SELECT * FROM TBL_EDICT"; //câu lệnh MySQL
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql); //Thực thi câu lệnh
            ResultSet result = preparedStatement.executeQuery(); // lấy kết quả
            while (result.next()) {
                Word word = new Word();
                word.setWord(result.getString("word"));
                word.setMean(result.getString("detail"));
                words.add(word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }

    //thêm từ vào mySQL
    public void addWord(Word word) {
        String sql = "INSERT INTO TBL_EDICT (IDX, WORD, DETAIL) VALUES (null,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, word.get_word());
            preparedStatement.setString(2, word.get_mean());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //sửa từ vào mySQL
    public void updateWord(Word word) {
        String sql = "UPDATE TBL_EDICT SET DETAIL = ? WHERE WORD = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(2, word.get_word());
            preparedStatement.setString(1, word.get_mean());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWord(String word_target) {
        String sql  = "DELETE FROM TBL_EDICT WHERE WORD = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, word_target);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

