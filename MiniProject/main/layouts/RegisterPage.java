package layouts;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.*;
import javafx.scene.control.*;

import utils.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterPage{
    private Stage mystage;
    private DBConnection db;

    @FXML
    private Label ErrorMsgLabel;
    @FXML
    private TextField TextFieldUserName;
    @FXML
    private PasswordField PasswordFieldPassword;
    @FXML
    private PasswordField PasswordFieldRePassword;

    public void setStage(Stage stage) {
        this.mystage = stage;
    }
    public void setDB(DBConnection db){
        this.db = db;
    }
    public static void main(String args[]){

    }
    public void Register() throws SQLException,IOException{
        String st_Username = TextFieldUserName.getText();
        String Password = PasswordFieldPassword.getText();
        String RePassword = PasswordFieldRePassword.getText();
        if(st_Username == "" || Password == ""){
            ErrorMsgLabel.setText("Username/Password can't be Empty!");return;
        }
        if(Password.compareTo(RePassword) != 0){
            ErrorMsgLabel.setText("Passwords don't match");
            return;
        }
        if(this.db == null)this.db = new DBConnection();
        String query = "SELECT * FROM mst_user where username='"+st_Username+"';";
        ResultSet rs = this.db.executeQuery(query);
        try{
            if(rs.next()){
                ErrorMsgLabel.setText("Username already exists!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        query = "insert into mst_user(username,password,role) values('"+st_Username+"','"+Password+"',1)";
        int rowsAffected = db.executeUpdate(query);
        if(rowsAffected > 0){
            this.goToLoginPage();
        }else{
            ErrorMsgLabel.setText("User Registration Failed!");
        }
    }
    
    public void goToLoginPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        Parent root = loader.load();
        LoginPage loginController = loader.getController();
        loginController.setDB(this.db);
        Scene scene = new Scene(root);
        mystage.setScene(scene);
        mystage.show();
    }
}