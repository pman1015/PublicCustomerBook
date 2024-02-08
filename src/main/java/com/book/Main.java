package com.book;

import com.book.Controllers.JobController;
import com.book.Utilities.Invoice.InvoiceGenerator;
import com.mongodb.ConnectionString;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.bson.types.ObjectId;
import org.scenicview.ScenicView;

public class Main extends Application {
  private SceneController SC;
  public static void main(String[] args) {
    launch(args);
  }
  @Override
  public void start(Stage primaryStage) throws Exception {
    //Load the scene from homepage
    //generate the parent pane
    BorderPane page = new BorderPane();

    //add the NavBar
    FXMLLoader loader = new FXMLLoader(
      Main.class.getResource("Scenes/Nav/NavBar.fxml")
    );

    Node Navbar = loader.load();
    SC = loader.getController();
    SC.setParent(page);

    page.setLeft(Navbar);
    //add the header
    Node Header = FXMLLoader.load(
      Main.class.getResource("Scenes/Nav/Header.fxml")
    );
    page.setTop(Header);

    page.setCenter(SC.getHome());
    //set the scene
    Scene scene = new Scene(page, 1200, 700);
    //Load the css
    String css =
      Main.class.getResource("Styles/Application.css").toExternalForm();
    scene.getStylesheets().add(css);

    //Add title and display the scene

    primaryStage.setTitle("CustomerBook");
    primaryStage.setScene(scene);

    primaryStage.sizeToScene();
    primaryStage.setResizable(false);
    primaryStage.initStyle(StageStyle.UNDECORATED);

    primaryStage.show();
    //ScenicView.show(scene);
  }
}
