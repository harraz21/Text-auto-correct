package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Node;
import model.RedBlackTree;
import utility.SourceReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    ArrayList<String> dictionaryWords;
    ArrayList<String> deletedWords;
    ArrayList<String> insertedWords;
    RedBlackTree redBlackTree;

    @FXML
    TextField typedWord;
    @FXML
    Button insertButton;
    @FXML
    Button deleteButton;
    @FXML
    Button searchButton;
    @FXML
    Button saveButton;
    @FXML
    Label systemMessages;
    @FXML
    Label suggestionsLab;
    @FXML
    CheckBox autoSearchMode;

    @FXML
    public void autoSearchOnAction(){
        searchButton.setDisable(!searchButton.isDisabled());
    }
    @FXML
    public void keyPressed(){
        if(!autoSearchMode.isSelected())return;
        if(!searchOnAction()){
             systemMessages.setText("");
             suggestionsLab.setText("");
        }
    }
    @FXML
    public void insertOnAction(){
        String insertedWord=typedWord.getText();
        suggestionsLab.setText("");
        if(insertedWord.equals("")){
            systemMessages.setText("Type any word to be inserted!");
            return;
        }
        if(redBlackTree.search(insertedWord,redBlackTree.getRoot())==null){
            redBlackTree.add(insertedWord);
            systemMessages.setText("Dictionary size: "+redBlackTree.getSize()+" Dictionary BlackHeight: "+redBlackTree.getBlackHeight(redBlackTree.getRoot()));
            insertedWords.add(insertedWord);
        }else{
            systemMessages.setText(insertedWord+" is already in the current dictionary!");
        }
    }
    @FXML
    public void deleteOnAction(){
        String deletedWord=typedWord.getText();
        suggestionsLab.setText("");
        if(deletedWord.equals("")){
            systemMessages.setText("Type any word to be deleted!");
            return;
        }
        Node node=redBlackTree.search(deletedWord,redBlackTree.getRoot());
        if(node!=null){
            redBlackTree.delete(node);
            systemMessages.setText("Dictionary size: "+redBlackTree.getSize()+" Dictionary BlackHeight: "+redBlackTree.getBlackHeight(redBlackTree.getRoot()));
            deletedWords.add(deletedWord);
        }else{
            systemMessages.setText(deletedWord+" is not in the current dictionary!");
        }
    }
    @FXML
    public boolean searchOnAction(){
        String searchedWord=typedWord.getText();
        if(searchedWord.equals("")){
            suggestionsLab.setText("");
            systemMessages.setText("Type any word to be found!");
            return false;
        }
        if(redBlackTree.search(searchedWord,redBlackTree.getRoot())!=null){
            systemMessages.setText("Dictionary size: "+redBlackTree.getSize()+" Dictionary BlackHeight: "+redBlackTree.getBlackHeight(redBlackTree.getRoot()));
            suggestionsLab.setText(searchedWord+" exists in the current dictionary!");
        }else{
           Node n= redBlackTree.getMostlyAlike(searchedWord);
           String mostlyAlike=n.getWord();
           String predWord=n.getRight().getMin().getWord();
           String succWord=n.getLeft().getMax().getWord();
           suggestionsLab.setText(searchedWord+" does not exist , Suggestions: "+predWord+" "+mostlyAlike+" "+succWord);
            systemMessages.setText("Dictionary size: "+redBlackTree.getSize()+" Dictionary BlackHeight: "+redBlackTree.getBlackHeight(redBlackTree.getRoot()));

        }
        return true;
    }
    @FXML
    public void saveOnAction(){
        for(String s : deletedWords){
           int len=dictionaryWords.size();
            for(int i=0;i<len;i++){
                if(s.equals(dictionaryWords.get(i))){
                    dictionaryWords.set(i,"");
                    break;
                }
            }
        }
        for(String s:insertedWords){
            dictionaryWords.add(s);
        }
        deletedWords.clear();
        insertedWords.clear();
        SourceReader.getInstance().writeFile(dictionaryWords,"res/English Dictionary.txt");
        suggestionsLab.setText("");
        systemMessages.setText("Saved current dictionary.");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        deletedWords=new ArrayList<>();
        insertedWords=new ArrayList<>();

        dictionaryWords= SourceReader.getInstance().readFile("res/English Dictionary.txt");
        int len=dictionaryWords.size();
        redBlackTree=new RedBlackTree(dictionaryWords.get(0));
        for(int i=1;i<len;i++){
            redBlackTree.add(dictionaryWords.get(i));
        }
    }


}