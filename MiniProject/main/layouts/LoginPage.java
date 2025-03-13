package layouts;
// javac -cp .;mysql-connector-j-8.0.31.jar --module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib" --add-modules javafx.controls,javafx.fxml layouts/LoginPage.java
// java -cp .;mysql-connector-j-8.0.31.jar --module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib" --add-modules javafx.controls,javafx.fxml layouts.LoginPage

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.Parent;
import javafx.scene.control.*; 
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.fxml.FXMLLoader;
import java.net.URL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import utils.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginPage extends Application{

    private DBConnection db;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label ErrorMsgLabel;
    @FXML
    private TextField TextFieldUserName;
    @FXML
    private PasswordField PasswordFieldPassword;

    public void setDB(DBConnection db){
        this.db = db;
    }

    public static void main(String args[]){
        launch(args);
    }
    @Override
    public void start(Stage mystage){
        FlowPane flowpane = new FlowPane();
        Scene myscene = new Scene(flowpane, 600, 330);
        try{
            FXMLLoader loader = new FXMLLoader(Paths.get("./layouts/LoginPage.fxml").toUri().toURL());
            FlowPane flowpane1 = loader.load();
            flowpane.getChildren().add(flowpane1);
        }catch(MalformedURLException e){
            System.out.println("Invalid URL: " + e.getMessage());
        }catch (IOException e){
            System.out.println("Error loading FXML: " + e.getMessage());
        }

        mystage.setScene(myscene);
        mystage.show();
    }
    public void Login() throws SQLException{
        String username = TextFieldUserName.getText();
        String password = PasswordFieldPassword.getText();
        ErrorMsgLabel.setText("");

        if(username == "" || password == ""){
            ErrorMsgLabel.setText("Incorrect Username/Password");
            return;
        }

        if(this.db == null)this.db = new DBConnection();
        if(this.db == null){
            ErrorMsgLabel.setText("Database Connection Failed!");
            return;
        }

        String query = "SELECT * FROM mst_user where username='"+username+"' and password='"+password+"';";
        ResultSet rs = this.db.executeQuery(query);
        
        if(rs.next()){
            System.out.println("Login Sucsessfull!");
            System.out.println("ID: " + rs.getInt("user_id") +", UserName: " + rs.getString("username") +", Role: " + rs.getInt("role"));
        }else{
            ErrorMsgLabel.setText("Incorrect Username/Password");
            TextFieldUserName.setText("");
            PasswordFieldPassword.setText("");
        }
    }
    public void Register(ActionEvent event) throws IOException{
        // Parent root = FXMLLoader.load(getClass().getResource("RegisterPage.fxml"));
        
        // stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        // scene = new Scene(root);
        // stage.setScene(scene);
        // stage.show();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterPage.fxml"));
        Parent root = loader.load();
        RegisterPage registerController = loader.getController();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        registerController.setDB(this.db);
        registerController.setStage(stage);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}