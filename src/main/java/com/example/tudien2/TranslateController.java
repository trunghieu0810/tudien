package com.example.tudien2;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.ResourceBundle;

public class TranslateController implements Initializable {
    @FXML
    public TextArea word_translated;
    @FXML
    public TextField word_target;


    public void translate() {
            String text = word_target.getText();
            if (text.equals("")) {
                showAlert();
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String urlStr = null;
                    try {
                        urlStr = "https://script.google.com/macros/s/AKfycbwBBo3bAEC5aDzqDq1gYehfcUJShDDYU6hkpB4DJWl3kXBeURuq/exec" + "?q=" + URLEncoder.encode(text, "UTF-8") + "&target=" + "vi" + "&source=" + "en";
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    URL url = null;
                    try {
                        assert urlStr != null;
                        url = new URL(urlStr);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    StringBuilder response = new StringBuilder();
                    HttpURLConnection con = null;
                    try {
                        assert url != null;
                        con = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert con != null;
                    con.setRequestProperty("User-Agent", "Mozilla/5.0");
                    BufferedReader in = null;
                    try {
                        in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String inputLine = "";
                    while (true) {
                        try {
                            assert in != null;
                            if ((inputLine = in.readLine()) == null) break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        response.append(inputLine);
                    }
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    word_translated.setText(response.toString());
                }
            }).start();
        }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Alert");
        alert.setContentText("Bạn chưa nhập gì mà :<");
        alert.showAndWait();
    }

    public void speak() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us" + ".cmu_us_kal.KevinVoiceDirectory");
        final String voicename = "kevin16";
        Voice voice;
        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice(voicename);
        voice.allocate();
        voice.speak(word_target.getText());
    }

    public void back() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        Scene scene = new Scene(root, 512, 424);
        Stage stage = (Stage) word_target.getScene().getWindow();
        stage.setTitle("Dictionary");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}


