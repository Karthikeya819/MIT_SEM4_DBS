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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Movie{
    private int movie_id;
    private String title;
    private String duration;
    private String rating;
    private String imageUrl;

    public Movie(int movie_id,String title, String duration, String rating, String imageUrl){
        this.movie_id = movie_id;
        this.title = title;
        this.duration = duration;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    public String getTitle(){ return title; }
    public String getDuration(){ return duration; }
    public String getRating(){ return rating; }
    public String getImageUrl(){ return imageUrl; }
    public int getId(){return movie_id;}
}

public class UserMainPage{
    private Stage mystage;
    private DBConnection db;
    private int mst_usr_id;
    private String mst_username;
    

    @FXML
    private Label TextFieldUsername;
    @FXML
    private ListView<Object> ListViewScreenings;
    @FXML
    private FlowPane fp1;
    @FXML
    private ListView<Screening> lv1;
    @FXML
    private Button bt1;
    @FXML
    private TextField tf1;
    @FXML
    private FlowPane fp2;
    @FXML
    private HBox hb1;
    @FXML
    private Label lb1;
    @FXML
    private Label lb2;

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
    public void dk_initialize() throws SQLException{
        TextFieldUsername.setText(this.mst_username);
        LoadScreenings();
    }

    public void LoadScreenings() throws SQLException{
        lb1.setText("Book Tickets");
        lb2.setText("Current Screenings:");
        ListViewScreenings.lookupAll(".scroll-bar").forEach(scrollBar -> {
            scrollBar.setOpacity(0);
            scrollBar.setMouseTransparent(true);
        });
        //String query = "SELECT * FROM mst_movies";
        String query = "SELECT * from mst_movies where EXISTS(SELECT * from mst_screenings where mst_screenings.movie_id = mst_movies.movie_id);";
        if(this.db == null)this.db = new DBConnection();
        if(this.db == null){
            // ErrorMsgLabel.setText("Database Connection Failed!");
            return;
        }
        ObservableList<Object> movies = FXCollections.observableArrayList();
        ResultSet rs = this.db.executeQuery(query);
        while(rs.next()){
            int movie_id = rs.getInt("movie_id");
            String title = rs.getString("title");
            int duration = rs.getInt("duration");
            float rating = rs.getFloat("rating");
            String img_url = rs.getString("img_url");
            movies.add(new Movie(movie_id,title, duration + " min", String.valueOf(rating), img_url));
        }
        ListViewScreenings.setItems(movies);
        ListViewScreenings.setOrientation(Orientation.HORIZONTAL);
        ListViewScreenings.setCellFactory(param -> new CustomCellFactory1("Movie", ListViewScreenings));
        ListViewScreenings.getSelectionModel().selectedItemProperty().addListener((obs, oldMovie, newMovie) -> {
            try{
                ObservableList<Screening> screening_list = FXCollections.observableArrayList();
                Movie newMovieobj = (Movie)newMovie;
                int movie_id = newMovieobj.getId();
                String query1 = "Select * from mst_screenings where movie_id = "+movie_id+";";
                if(this.db == null)this.db = new DBConnection();
                try{
                    ResultSet rs1 = this.db.executeQuery(query1);
                    while(rs1.next()){
                        screening_list.add(new Screening(rs1.getInt("screening_id"),rs1.getInt("movie_id"),rs1.getString("screen_number"),rs1.getString("show_time"),rs1.getInt("available_seats")));
                    }
                }catch(SQLException e){e.printStackTrace();}
                lv1.setItems(screening_list);
                lv1.setCellFactory(param -> new ScreeningCell());
                fp1.setVisible(true);
            }catch(NullPointerException e){}
        });
        bt1.setOnAction(event->{
            BookTickect();
        });
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

    public void BookTickect(){
        LocalDate today = LocalDate.now();
        int seats_booked = Integer.valueOf(tf1.getText());
        int screening_id = lv1.getSelectionModel().getSelectedItem().getScreeningId();
        String booking_date = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if(seats_booked > lv1.getSelectionModel().getSelectedItem().getAvailableSeats()){
            System.out.println("Insufficient Seats!");
            return;
        }
        if(this.db == null)this.db = new DBConnection();
        String query = String.format("INSERT into mst_bookings(user_id,screening_id,seats_booked,booking_date) VALUES(%d,%d,%d,'%s');",this.mst_usr_id,screening_id,seats_booked,booking_date);
        String query1 = String.format("update mst_screenings set available_seats = available_seats - %d where screening_id = %d",seats_booked,screening_id);
        int rowsAffected = db.executeUpdate(query1);
        if(rowsAffected < 1)return;
        rowsAffected = db.executeUpdate(query);
        if(rowsAffected < 1){
            query1 = String.format("update mst_screenings set available_seats = available_seats + %d where screening_id = %d",seats_booked,screening_id);
            rowsAffected = db.executeUpdate(query1);
            return;
        }
        tf1.setText("");
        // go to my Bookings
    }
    public void showPastBookings() throws SQLException{
        lb1.setText("Past Bookings");
        lb2.setText("");
        fp1.setVisible(false);

        String query = "SELECT * FROM mst_bookings join mst_screenings on mst_bookings.screening_id = mst_screenings.screening_id join mst_movies on mst_movies.movie_id = mst_screenings.movie_id WHERE user_id = "+this.mst_usr_id+";";
        System.out.println(query);
        if(this.db == null)this.db = new DBConnection();
        if(this.db == null){
            // ErrorMsgLabel.setText("Database Connection Failed!");
            return;
        }
        ObservableList<Object> bookings = FXCollections.observableArrayList();
        ResultSet rs = this.db.executeQuery(query);
        while(rs.next()){
            int booking_id = rs.getInt("booking_id");
            String booking_date = rs.getString("booking_date");
            int seats_booked = rs.getInt("seats_booked");
            String screen_number = rs.getString("screen_number");
            String show_time = rs.getString("show_time");
            String title = rs.getString("title");
            bookings.add(new Bookings(booking_id, booking_date, seats_booked, screen_number, show_time, title));
        }
        ListViewScreenings.getSelectionModel().clearSelection();
        ListViewScreenings.setItems(bookings);
        ListViewScreenings.setOrientation(Orientation.VERTICAL);
    }
}

class ScreeningCell extends ListCell<Screening> {
    @Override
    protected void updateItem(Screening screening, boolean empty) {
        super.updateItem(screening, empty);
        
        if (empty || screening == null) {
            setText(null);
            setGraphic(null);
        } else {
            Label screenLabel = new Label(screening.getScreenNumber());
            Label timeLabel = new Label(screening.getShowTime());
            Label seatsLabel = new Label(String.valueOf(screening.getAvailableSeats()));
            seatsLabel.setStyle("-fx-text-fill: green;");
            VBox vbox = new VBox(timeLabel, new HBox(5,screenLabel,seatsLabel));
            setGraphic(vbox);
        }
    }
}
class Bookings{
    int booking_id;
    String booking_date;
    int seats_booked;
    String screen_number;
    String show_time;
    String title;
    public Bookings(int booking_id, String booking_date, int seats_booked, String screen_number, String show_time, String title){
        this.booking_id = booking_id;
        this.booking_date = booking_date;
        this.seats_booked = seats_booked;
        this.screen_number = screen_number;
        this.show_time = show_time;
        this.title = title;
    }
}

class CustomCellFactory1 extends ListCell<Object>{
    private String type;
    private ListView<Object> parentListView;
    public CustomCellFactory1(String type, ListView<Object> listView){
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
        if(item instanceof Movie){
            Movie movie = (Movie)item;
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
        }else if(item instanceof Bookings){
            Bookings booking = (Bookings)item;
            Label booking_id = new Label(String.valueOf(booking.booking_id));
            Label booking_date = new Label(booking.booking_date);
            Label seats_booked = new Label(String.valueOf(booking.seats_booked));
            Label screen_number = new Label(booking.screen_number);
            Label show_time = new Label(booking.show_time);
            Label title = new Label(booking.title);
            HBox hbox = new HBox(25,booking_id,booking_date,seats_booked,screen_number,show_time,title);
            setGraphic(hbox);
        }
    }
}
