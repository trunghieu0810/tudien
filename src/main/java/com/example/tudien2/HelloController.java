package com.example.tudien2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.ResourceBundle;

import com.sun.speech.freetts.*;

import java.io.*;
import java.net.URL;
import java.util.*;

public class HelloController implements Initializable {
    @FXML
    private TextField word;
    @FXML
    private ListView<String> listWords;
    @FXML
    private TextArea word_target;
    @FXML
    private TextArea word_mean;
    @FXML
    private ImageView speak;
    @FXML
    private ImageView search;
    @FXML
    private Button translate;
    @FXML
    private MenuItem exit;


    private Dictionary dictionary = new Dictionary();
    private DictionaryManegement dictionarymanegement = new DictionaryManegement();
    private JDBCManagement jdbcManagement = new JDBCManagement();
    ObservableList<String> listword ;

    private void showAlert(String txt) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Alert");
        alert.setContentText(txt);
        alert.showAndWait();
    }

    private int vitri(String text) {
        List<Word>  words = jdbcManagement.getAllWord();
        for(int i=0;i<words.size();i++){
            if(words.get(i).get_word().equals(text)){
                return i;
            }
        }
        return -1;
    }

    public void loadData() {
        List<Word>  words = jdbcManagement.getAllWord();
        List<String> wordlist = new ArrayList<>();
        for (Word value : words) {
            wordlist.add(value.get_word());
        }
        listword = FXCollections.observableList(wordlist);
        listWords.getItems().addAll(listword);
    }
    @FXML
    public void select(MouseEvent event) {
        List<Word>  words = jdbcManagement.getAllWord();
        word.setText(listWords.getSelectionModel().getSelectedItem());
        String worde = word.getText();
        for(int i=0;i<words.size();i++){
            if(words.get(i).get_word().equals(worde)){
                word_target.setText(words.get(i).get_word());
                word_mean.setText(words.get(i).get_mean());
            }
        }
    }

    public void Findword(KeyEvent keyEvent){
        List<Word>  words = jdbcManagement.getAllWord();
        listword = FXCollections.observableArrayList();
        String worde = word.getText();
        for (Word value : words) {
            if (value.get_word().startsWith(worde)) {
                int temp = 0;
                for (int i = 0; i < listword.size(); i++) {
                    if (listword.get(i).equals(value.get_word())) {
                        temp+=1;
                    }
                }
                if (temp == 0) {
                    listword.add(value.get_word());
                }
            }
            if(value.get_word().equals(worde)){
                word_target.setText(value.get_word());
                word_mean.setText(value.get_mean());
            }
            else{
                word_target.setText("");
                word_mean.setText("");
            }
        }
        listWords.setItems(listword);
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
    @FXML
    void edit(ActionEvent event) {
        List<Word>  words = jdbcManagement.getAllWord();
        String worde = word.getText();
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("edit dialog");
        dialog.setHeaderText("Edit word");

        ButtonType editButtonType = new ButtonType("EDIT", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField eng = new TextField();
        eng.setText(word_target.getText());
        TextField vnm = new TextField();
        vnm.setText(word_mean.getText());

        grid.add(new Label("English:"), 0, 0);
        grid.add(eng, 1, 0);
        grid.add(new Label("Vietnamese:"), 0, 1);
        grid.add(vnm, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(editButtonType);
        loginButton.setDisable(true);

        eng.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        vnm.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editButtonType){
                return new Pair<>(eng.getText(), vnm.getText());
            }
            return null;
        } );

        int i = vitri(listWords.getSelectionModel().getSelectedItem());
        int x = listWords.getItems().indexOf(eng.getText());
            if (words.get(i).get_word().equals(worde)) {
                Optional<Pair<String, String>> result = dialog.showAndWait();
                result.ifPresent(editword -> {
                    Word wordss = new Word();

                    wordss.setWord(eng.getText());
                    wordss.setMean(vnm.getText());
                    jdbcManagement.updateWord(wordss);
                    loadData();
                    word.setText("");
                    word_target.setText("");
                    word_mean.setText("");
                    showAlert("da chinh sua");
                });
            }
        }

    @FXML
    void delete(ActionEvent event) {
        List<Word>  words = jdbcManagement.getAllWord();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Alert");
        alert.setContentText("Ban chac chan muon xoa?");
        int i = vitri(word.getText());
        int x = listWords.getItems().indexOf(word_target.getText());
            if (words.get(i).get_word().equals(word_target.getText())) {
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    jdbcManagement.deleteWord(words.get(i).get_word());
                    loadData();
                    //words.remove(i);
                    word.setText("");
                    word_mean.setText("");
                    word_target.setText("");
                    listWords.getItems().remove(x);
                    showAlert("xoa thanh cong!");
                }
        }
    }

    public void search ()  {
        List<Word>  words = jdbcManagement.getAllWord();
        String worde = word.getText();
        int temp=0;
        for (Word value : words) {
            if (value.get_word().equals(worde)) {
                word_target.setText(value.get_word());
                word_mean.setText(value.get_mean());
                temp++;
            }
        }
        if(temp == 0){
            showAlert("chua co trong tu dien!!!");
        }
    }

    @FXML
    void changTranslateStage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("translate.fxml")));
        Scene scene = new Scene(root, 512, 424);
        translate.getScene();
        Stage stage = (Stage) translate.getScene().getWindow();
        stage.setTitle("Translate");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void add(ActionEvent event) {
        List<Word>  words = jdbcManagement.getAllWord();
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("addnew dialog");
        dialog.setHeaderText("Add new word");

        ButtonType addnewButtonType = new ButtonType("ADD", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addnewButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField eng = new TextField();
        eng.setPromptText("ENGLISH");
        TextField vnm = new TextField();
        vnm.setPromptText("VIETNAMESE");

        grid.add(new Label("English:"), 0, 0);
        grid.add(eng, 1, 0);
        grid.add(new Label("Vietnamese:"), 0, 1);
        grid.add(vnm, 1, 1);

        Node addButton = dialog.getDialogPane().lookupButton(addnewButtonType);
        addButton.setDisable(true);

        eng.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addnewButtonType){
                return new Pair<>(eng.getText(), vnm.getText());
            }
            return null;
        } );
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(addnew-> {
            Word wordss = new Word();
            wordss.setWord(eng.getText());
            wordss.setMean(vnm.getText());
            int temp =0;
            for (Word value : words) {
                if (value.get_word().equals(wordss.get_word())) {
                    showAlert("tu nay da co trong tu dien!");
                    temp++;
                    break;
                }
            }
            if(temp == 0){
                jdbcManagement.addWord(wordss);
                loadData();
                showAlert("them tu thanh cong!");
            }
        });
    }

    @FXML
    void exit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Alert");
        alert.setContentText("Ban chac chan muon thoat?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) search.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        loadData();
    }
}