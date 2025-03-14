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
import java.util.*;
import javafx.geometry.*;

import utils.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AdminMainPage{
    private Stage mystage;
    private DBConnection db;
    private int mst_usr_id;
    private String mst_username;

    @FXML
    private Label LabelUsername;
    @FXML
    private ListView<Object> ListView1;
    @FXML
    private TextField tf1;
    @FXML
    private Button bt1;
    @FXML
    private Label lb1;
    @FXML
    private Label lb2;
    @FXML
    private Label lb3;
    @FXML
    private FlowPane fp1;

    private TextField mv_tf1 = new TextField();
    private TextField mv_tf2 = new TextField();
    private TextField mv_tf3 = new TextField();
    private TextField mv_tf4 = new TextField();
    private TextField mv_tf5 = new TextField();
    private TextField mv_tf6 = new TextField();
    
    public void setStage(Stage stage){
        this.mystage = stage;
    }
    public void setDB(DBConnection db){
        this.db = db;
    }
    public void passInfo(int mst_usr_id, String mst_username){
        this.mst_usr_id = mst_usr_id;
        this.mst_username = mst_username;
    }
    public void dk_initialize() throws SQLException{
        mv_tf1.setPromptText("Title");
        mv_tf2.setPromptText("Genre Id");
        mv_tf3.setPromptText("Release Date");
        mv_tf4.setPromptText("Duration");
        mv_tf5.setPromptText("Rating");
        mv_tf6.setPromptText("Image Url");
        LabelUsername.setText(this.mst_username);
        //this.onGenresClicked();
        this.onMoviesClicked();
    }

    public static void main(String args[]){
        System.out.println("jfndnjd");
    }

    public void goToLoginPage() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        Parent root = loader.load();
        LoginPage loginController = loader.getController();
        loginController.setDB(this.db);
        Scene scene = new Scene(root);
        mystage.setScene(scene);
        mystage.show();
    }
    public void updateListView1(String type){
        ObservableList<Object> items = FXCollections.observableArrayList();

        if(type.equals("Genres")){
            items.addAll(fetchGenresFromDB());
        } 
        else if(type.equals("Movies")){
            items.addAll(fetchMoviesFromDB());
        } 
        else if(type.equals("Screenings")){
            items.addAll(fetchScreeningsFromDB());
        }

        ListView1.setItems(items);
        ListView1.setCellFactory(param -> new CustomCellFactory(type, ListView1)); 
    }


    public ObservableList<Genre> fetchGenresFromDB(){
        ObservableList<Genre> genres = FXCollections.observableArrayList();
        String query = "SELECT genre_id, genre_name FROM mst_genre";
        try{
            ResultSet rs = db.executeQuery(query);
            while(rs.next()){
                int id = rs.getInt("genre_id");
                String name = rs.getString("genre_name");
                genres.add(new Genre(id, name));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return genres;
    }


    private List<Movie> fetchMoviesFromDB(){
        List<Movie> movies = new ArrayList<>();
        try{
            if(this.db == null)this.db = new DBConnection();
            ResultSet rs = db.executeQuery("SELECT * FROM mst_movies;");
            while(rs.next()){
                movies.add(new Movie(rs.getInt("movie_id"),rs.getString("title"), rs.getInt("duration") + " min",String.valueOf(rs.getFloat("rating")), rs.getString("img_url")));
            }
        }catch(SQLException e){ e.printStackTrace(); }
        return movies;
    }

    private List<Screening> fetchScreeningsFromDB(){
        List<Screening> screenings = new ArrayList<>();
        try{
            if(this.db == null)this.db = new DBConnection();
            ResultSet rs = db.executeQuery("SELECT screen_number, show_time, available_seats FROM Screenings");
            while(rs.next()){
                screenings.add(new Screening(rs.getInt("screen_number"), rs.getTimestamp("show_time"),rs.getInt("available_seats")));
            }
        } catch(SQLException e){ e.printStackTrace(); }
        return screenings;
    }
    class CustomCellFactory extends ListCell<Object>{
        private String type;
        private ListView<Object> parentListView;

        public CustomCellFactory(String type, ListView<Object> listView){
            this.type = type;
            this.parentListView = listView;
        }

        @Override
        protected void updateItem(Object item, boolean empty){
            super.updateItem(item, empty);

            if(empty || item == null){
                setGraphic(null);
                return;
            }

            HBox cellContainer = new HBox(10);
            cellContainer.setAlignment(Pos.CENTER_LEFT);
            Button deleteButton = new Button("❌ Delete");
            deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            deleteButton.setOnAction(event ->{
                deleteItem(item);
            });

            if(item instanceof Genre){
                Genre genre =(Genre) item;
                Label genreLabel = new Label(genre.getGenreName());
                Label genreidLabel = new Label(String.valueOf(genre.getGenreId()));
                cellContainer.getChildren().addAll(genreidLabel,genreLabel, deleteButton);
            } 
            else if(item instanceof Movie){
                Movie movie =(Movie) item;
                Label movieid = new Label(String.valueOf(movie.getId()));
                ImageView imageView = new ImageView(new Image(movie.getImageUrl(), 100, 150, true, true));
                Label movieduration = new Label(movie.getDuration());
                Label movierating = new Label(movie.getRating());
                Label movieLabel = new Label(movie.getTitle());
                cellContainer.getChildren().addAll(movieid, movieLabel, imageView,movieduration,movierating,deleteButton);
            } 
            else if(item instanceof Screening){
                Screening screening =(Screening) item;
                Label screeningLabel = new Label("Screen: " + screening.getScreenNumber());
                cellContainer.getChildren().addAll(screeningLabel, deleteButton);
            }

            setGraphic(cellContainer);
        }


        private void deleteItem(Object item){
            parentListView.getItems().remove(item);

            if(item instanceof Genre){
                int genreId =((Genre) item).getGenreId();
                db.executeUpdate("DELETE FROM mst_genre WHERE genre_id = '" +genreId+"';");
            } 
            else if(item instanceof Movie){
                int movieId =((Movie) item).getId();
                db.executeUpdate("DELETE FROM mst_movies WHERE movie_id = " + movieId + ";");
            } 
            else if(item instanceof Screening){
                int screenNumber =((Screening) item).getScreenNumber();
                db.executeUpdate("DELETE FROM Screenings WHERE screen_number = " + screenNumber);
            }
        }
    }
    @FXML
    private void onGenresClicked(){
        lb1.setText("Manage Genre");
        lb2.setText("Genre List:");
        lb3.setText("New Genre:");
        bt1.setText("Add Genre");
        updateListView1("Genres");
        bt1.setOnAction(event->{
            String newElem = tf1.getText();
            if(newElem == "")return;
            try{
                if(this.db == null)this.db = new DBConnection();
                String query = "SELECT * FROM mst_genre where genre_name='"+newElem+"';";
                ResultSet rs = this.db.executeQuery(query);
                if(rs.next())return;
                query = "insert into mst_genre(genre_name) values('"+newElem+"')";
                int rowsAffected = db.executeUpdate(query);
                if(rowsAffected > 1)tf1.setText("");
                updateListView1("Genres");
            }catch(SQLException e){e.printStackTrace();}
        });
    }

    @FXML
    private void onMoviesClicked(){
        lb1.setText("Manage Movies");
        lb2.setText("Movie List:");
        lb3.setText("New Movie:");
        bt1.setText("Add Movie");
        fp1.getChildren().clear();
        bt1.setOnAction(event->{
            String title = mv_tf1.getText();
            int genre_id = Integer.valueOf(mv_tf2.getText());
            String date = mv_tf3.getText();
            int duration = Integer.valueOf(mv_tf4.getText());
            float rating = Float.valueOf(mv_tf5.getText());
            String img_url = mv_tf6.getText();
            if(this.db == null)this.db = new DBConnection();
            String query = String.format("INSERT into mst_movies(title,genre_id,release_date,duration,rating,img_url) VALUES('%s',%d,'%s',%d,%f,'%s');",title,genre_id,date,duration,rating,img_url);
            int rowsAffected = db.executeUpdate(query);
            if(rowsAffected >= 1){
                mv_tf1.setText("");
                mv_tf2.setText("");
                mv_tf3.setText("");
                mv_tf4.setText("");
                mv_tf5.setText("");
                mv_tf6.setText("");
            }
            updateListView1("Movies");
        });
        fp1.setAlignment(Pos.CENTER);
        fp1.getChildren().addAll(lb3,new VBox(5,new HBox(20,mv_tf1,mv_tf2),new HBox(20,mv_tf3,mv_tf4),new HBox(20,mv_tf5,mv_tf6,bt1)));
        updateListView1("Movies");
    }

    @FXML
    private void onScreeningsClicked(){
        lb1.setText("Manage Screenings");
        lb2.setText("Screenings List:");
        lb3.setText("New Screenings:");
        bt1.setText("Add Screenings");
        //updateListView1("Screenings");
    }

}
class Genre{
    private int genreId;
    private String genreName;

    public Genre(int genreId, String genreName){
        this.genreId = genreId;
        this.genreName = genreName;
    }

    public int getGenreId(){ return genreId; }
    public String getGenreName(){ return genreName; }

    @Override
    public String toString(){ return genreName; }
}

class Screening{
    private int screenNumber;
    private Timestamp showTime;
    private int availableSeats;

    public Screening(int screenNumber, Timestamp showTime, int availableSeats){
        this.screenNumber = screenNumber;
        this.showTime = showTime;
        this.availableSeats = availableSeats;
    }

    public int getScreenNumber(){ return screenNumber; }
    public Timestamp getShowTime(){ return showTime; }
    public int getAvailableSeats(){ return availableSeats; }

    @Override
    public String toString(){  
        return "Screen: " + screenNumber + ", Time: " + showTime + ", Seats: " + availableSeats;
    }
}

