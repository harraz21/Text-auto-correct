

import utility.SourceReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.RedBlackTree;

import java.io.File;
import java.util.ArrayList;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(new File("res/FXML/scene.fxml").toURI().toURL());
        primaryStage.setTitle("Dictionary");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
