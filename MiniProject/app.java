//javac --module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib" --add-modules javafx.controls,javafx.fxml app.java
//java --module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib" --add-modules javafx.controls,javafx.fxml app

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*; 
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.fxml.FXMLLoader;
import java.net.URL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class app extends Application{
    public static void main(String args[]){
        launch(args);
    }
    @Override
    public void start(Stage mystage){
        FlowPane flowpane = new FlowPane();
        Scene myscene = new Scene(flowpane,600,330);
        FlowPane flowpane1 = new FlowPane();
        try {
            FXMLLoader loader = new FXMLLoader();
            Path path = Paths.get("./Layouts/LoginPage.fxml");
            loader.setLocation(path.toUri().toURL());
            flowpane1 = loader.<FlowPane>load();
        } catch (MalformedURLException e) {
            System.out.println("Invalid URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error loading FXML: " + e.getMessage());
        }   
        
        flowpane.getChildren().add(flowpane1);
        mystage.setScene(myscene);
        mystage.show();
    }
    public void ButtonTest(){
        System.out.println("Hello World!");
    }
}