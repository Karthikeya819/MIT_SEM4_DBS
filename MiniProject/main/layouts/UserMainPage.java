package layouts;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.scene.image.*;

import utils.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;

class Movie {
    private String title;
    private String duration;
    private String rating;
    private String imageUrl;

    public Movie(String title, String duration, String rating, String imageUrl) {
        this.title = title;
        this.duration = duration;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    public String getTitle() { return title; }
    public String getDuration() { return duration; }
    public String getRating() { return rating; }
    public String getImageUrl() { return imageUrl; }
}

public class UserMainPage{
    private Stage mystage;
    private DBConnection db;
    private int mst_usr_id;
    private String mst_username;
    

    @FXML
    private Label TextFieldUsername;
    @FXML
    private ListView<Movie> ListViewScreenings;

    public void setStage(Stage stage) {
        this.mystage = stage;
    }
    public void setDB(DBConnection db){
        this.db = db;
    }
    public void passInfo(int mst_usr_id, String mst_username){
        this.mst_usr_id = mst_usr_id;
        this.mst_username = mst_username;
        //TextFieldUsername.setText(mst_username);
    }
    public void initialize(){
        TextFieldUsername.setText(this.mst_username);
        ListViewScreenings.lookupAll(".scroll-bar").forEach(scrollBar -> {
            scrollBar.setOpacity(0);
            scrollBar.setMouseTransparent(true);
        });
        ObservableList<Movie> movies = FXCollections.observableArrayList(
                new Movie("Mirchi", "2h 28m", "8.8", "https://csv2contact.groupspend.in/img/test.jpg"),
                new Movie("Maya Bazar", "2h 49m", "8.6", "https://csv2contact.groupspend.in/img/test.jpg"),
                new Movie("Aagadu", "2h 32m", "9.0", "https://csv2contact.groupspend.in/img/test.jpg"),
                new Movie("Jersey", "2h 42m", "7.9", "https://csv2contact.groupspend.in/img/test.jpg")
        );
        ListViewScreenings.setItems(movies);
        ListViewScreenings.setOrientation(Orientation.HORIZONTAL);
        ListViewScreenings.setCellFactory(lv -> new MovieCell());
    }

    public static void main(String args[]){
        System.out.println("jfndnjd");
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

class MovieCell extends ListCell<Movie> {
    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setGraphic(null);
        } else {
            ImageView imageView = new ImageView(new Image(movie.getImageUrl(), 100, 150, true, true));
            Text title = new Text(movie.getTitle());
            Text duration = new Text("⏳ " + movie.getDuration());
            Text rating = new Text("⭐ " + movie.getRating());
            VBox textContainer = new VBox(title, duration, rating);
            textContainer.setSpacing(5);
            VBox movieBox = new VBox(imageView, textContainer);
            movieBox.setSpacing(10);
            movieBox.setStyle("-fx-alignment: center;");
            setGraphic(movieBox);
            // this.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            //     System.out.println("Cell Size -> Width: " + newBounds.getWidth() + ", Height: " + newBounds.getHeight());
            // });
        }
    }
}
