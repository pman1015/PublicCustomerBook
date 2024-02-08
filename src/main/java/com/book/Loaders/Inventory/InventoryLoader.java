package com.book.Loaders.Inventory;

import com.book.Controllers.InventoryController;
import com.book.DAOs.PartDAO;
import com.book.Main;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InventoryLoader {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private VBox PartCardContainer;

  @FXML
  private VBox RightContent;

  @FXML
  private TextField Search;

  @FXML
  void CreateNewModel(ActionEvent event) {
    rightSideContent = "makeNewPart";
    updateContent();
  }

  @FXML
  void initialize() {
    assert PartCardContainer !=
    null : "fx:id=\"PartCardContainer\" was not injected: check your FXML file 'Inventory.fxml'.";
    assert RightContent !=
    null : "fx:id=\"RightContent\" was not injected: check your FXML file 'Inventory.fxml'.";
    //Initalise the conroller for the database and update the page conent
    ic = new InventoryController();
    setContent();

    //initalize the make a part node and the controller
    FXMLLoader makeNewPartLoader = new FXMLLoader(
      Main.class.getResource("Scenes/Inventory/InventoryAddPart.fxml")
    );
    FXMLLoader partDetailLoader = new FXMLLoader(
      Main.class.getResource("Scenes/Inventory/InventoryDetail.fxml")
    );

    try {
      //makeNewPart load
      makeNewPart = makeNewPartLoader.load();
      makeNewPartController = makeNewPartLoader.getController();
      makeNewPartController.setParent(this);

      //partDetail Load
      partDetail = partDetailLoader.load();
      partDetailController = partDetailLoader.getController();
      partDetailController.setInventoryLoader(this);
    } catch (IOException e) {
      e.printStackTrace();
    }
    //update the page content
    updateContent();
  }

  //BorderPane to change the center content
  BorderPane bp;
  //Inventrory database controller
  InventoryController ic;
  //List of all of the parts
  ArrayList<PartDAO> parts;
  //Nodes for right side conent
  Node makeNewPart;
  Node partDetail;
  //Controller for the content
  InventoryAddPart makeNewPartController;
  InventoryPartDetail partDetailController;
  //String to store what is the right side content
  String rightSideContent = "None";

  public void setContent() {
    Search.setText(null);
    parts = ic.getAllPartsList();
    setCards(parts);
  }

  public void setCards(ArrayList<PartDAO> toAdd) {
    //Clear the existing cards
    PartCardContainer.getChildren().clear();
    //Get the list of all the parts then load them into the appropriate part card
    for (PartDAO dao : toAdd) {
      FXMLLoader loader = new FXMLLoader(
        Main.class.getResource("Scenes/Inventory/InventoryCard.fxml")
      );
      try {
        //Load the card
        Node content = loader.load();
        //Access the controller
        InventoryCard controller = loader.getController();
        //Set the part information inside the controller
        controller.setSelfRefrence(dao);
        //Set a refrence back to the controller
        controller.setParent(this);
        //Add the card to the card container
        PartCardContainer.getChildren().add(content);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void updateContent() {
    //Clear the existing right side conent
    RightContent.getChildren().clear();
    //Select the content based on string
    switch (rightSideContent) {
      case ("makeNewPart"):
        RightContent.getChildren().add(makeNewPart);
        break;
      case ("partDetail"):
        RightContent.getChildren().add(partDetail);
        break;
      default:
        break;
    }
  }

  @FXML
  void FindByPart() {
    System.out.println("Search");
    String searchBy = Search.getText();
    if (searchBy != null && searchBy.length() > 1) {
      setCards(ic.findByString(searchBy));
    } else {
      setCards(parts);
    }
  }
}
