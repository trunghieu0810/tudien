package com.example.tudien2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DictionaryManegement {
    private Dictionary dictionary = new Dictionary();

  /*  public void dictionary_manegement() {
        ArrayList<Word> words = dictionary.getWords();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nhập số từ cần thêm: ");
        int sotu = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Nhập từ mới: ");
        String tu = scanner.nextLine();
        for(int j=0;j< words.size();j++){
            if(words.get(j).get_word().equals(tu)){
                Sy System.out.println("Vui lòng nhập lại từ mới:");
                tu = scanner.nextLine();
            }
        }
        String nghia = scanner.nextLine();
        Word word = new Word();
        word.setWord(tu);
        word.setMean(nghia);
        words.add(word);

    }
*/
/*    public void show() {
        ArrayList<Word> words = dictionary.getWords();
        for (int i = 0; i < words.size(); i++) {
            System.out.println(words.get(i).get_word() +"\t"+ words.get(i).get_mean());
        }stem.out.println("Từ này đã có trong từ điển!!!");

    }
*/
    public List<Word> ReadFile() throws FileNotFoundException {
        File file = new File("data.txt");
        Scanner scanner = new Scanner(file);
        ArrayList<Word> words = dictionary.getWords();
        Word word = new Word();
        int i = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(i%2==0){
                word.setWord(line);
            }
            else{
                word.setMean(line);
                words.add(word);
                word = new Word();
            }
            i++;
        }
        dictionary.setWords(words);
        return words;
    }
/*
    public void lookup(){
        Scanner scanner = new Scanner(System.in);
        ArrayList<Word> words = dictionary.getWords();
        String nhap = scanner.nextLine();
        int temp =0;
        for(int i=0;i< words.size();i++){
            if(words.get(i).get_word().equals(nhap)) {
                System.out.println(words.get(i).get_word() + "\t" + words.get(i).get_mean());
                temp++;
            }
        }
        if(temp == 0){
            System.out.println("ko co trong tu dien");
        }
    }
*/
   /* public void search(){
        Scanner scanner = new Scanner(System.in);
        ArrayList<Word> words = dictionary.getWords();
        String nhap = scanner.nextLine();
        int temp =0;
        for (int i=0;i< words.size();i++){
            if(words.get(i).get_word().startsWith(nhap)) {
                System.out.println(words.get(i).get_word() + "\t" + words.get(i).get_mean());
                temp++;
            }
        }
        if(temp == 0){
            System.out.println("ko co trong tu dien");
        }
    }
*/
    public void uptofile() throws IOException {
        File file = new File("data.txt");
        ArrayList<Word> words = dictionary.getWords();
        OutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        for(int i=0;i< words.size();i++){
            outputStreamWriter.write(words.get(i).get_word());
            outputStreamWriter.write("\n");
            outputStreamWriter.write(words.get(i).get_mean());
            outputStreamWriter.write("\n");
        }
        outputStreamWriter.flush();
    }


}