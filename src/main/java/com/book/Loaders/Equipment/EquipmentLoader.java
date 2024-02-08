package com.book.Loaders.Equipment;

import com.book.Controllers.EquipmentController;
import com.book.DAOs.EquipmentDAO;
import com.book.Loaders.Inventory.InventoryPartDetail;
import com.book.Main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Setter @Getter
public class EquipmentLoader {

  @FXML
  private VBox RightSideContent;

  @FXML
  private AnchorPane LeftSideContent;

  @FXML
  private VBox List;

  @FXML
  private VBox ModelCards;

  //Strings for what content to load
  String LeftContent = "List";
  String RightContent = "";

  //Controllers for each content option
  EquipmentPageDetail detailsController;
  EquipmentAddPart addPartController;
  EquipmentMakeEquipment makeEquipmentController;
  EquipmentPageMakePart makePartController;
  InventoryPartDetail inventoryPartDetailController;

  //Nodes to store the content
  Node detailsNode;
  Node addPartNode;
  Node makeEquipmentNode;
  Node makePartNode;
  Node equipmentList;
  Node inventoryDetail;

  //Equipment controller
  EquipmentController ec;

  //Equipment DAOs
  ArrayList<EquipmentDAO> daos;
  int limit = 20;
  int skip = 0;

  @FXML
  void initialize() {
    //Initalize the controller and daos
    ec = new EquipmentController();
    daos = new ArrayList<>();
    //Load all the nodes
    FXMLLoader details = new FXMLLoader(
      Main.class.getResource("Scenes/Equipment/EquipmentPageDetail.fxml")
    );
    FXMLLoader addPart = new FXMLLoader(
      Main.class.getResource("Scenes/Equipment/AddPart.fxml")
    );
    FXMLLoader makeModel = new FXMLLoader(
      Main.class.getResource("Scenes/Equipment/MakeModel.fxml")
    );
    FXMLLoader makePart = new FXMLLoader(
      Main.class.getResource("Scenes/Equipment/MakeNewPart.fxml")
    );
    FXMLLoader makeInventoryDetail = new FXMLLoader(
      Main.class.getResource("Scenes/Inventory/InventoryDetail.fxml")
    );

    try {
      detailsNode = details.load();
      detailsController = details.getController();
      detailsController.setParent(this);

      addPartNode = addPart.load();
      addPartController = addPart.getController();
      addPartController.setParent(this);

      makeEquipmentNode = makeModel.load();
      makeEquipmentController = makeModel.getController();
      makeEquipmentController.setParent(this);

      makePartNode = makePart.load();
      makePartController = makePart.getController();
      makePartController.setParent(this);

      inventoryDetail = makeInventoryDetail.load();
      inventoryPartDetailController = makeInventoryDetail.getController();
      inventoryPartDetailController.setEquipmentLoader(this);

      equipmentList = List;
    } catch (IOException e) {
      e.printStackTrace();
    }

    //Update the content
    updateContent();
  }

  public void updateEquipment() {
    //Update the dao's
    daos = (ec.getEquipment(limit, skip));

    //Set the daos
    ModelCards.getChildren().clear();
    for (EquipmentDAO dao : daos) {
      FXMLLoader loader = new FXMLLoader(
        Main.class.getResource("Scenes/Equipment/EquipmentCard.fxml")
      );
      try {
        ModelCards.getChildren().add(loader.load());
        EquipmentCard card = loader.getController();
        card.setParent(this);
        card.setDao(dao);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void updateEquipmentDAO(EquipmentDAO dao) {
    ec.updateEquipment(dao);
  }

  public void updateContent() {
    //Set the proper Content on the right side
    RightSideContent.getChildren().clear();
    switch (RightContent) {
      case ("Details"):
        RightSideContent.getChildren().add(0, detailsNode);
        break;
      case ("AddPart"):
        RightSideContent.getChildren().add(0, addPartNode);
        break;
      case ("MakeModel"):
        RightSideContent.getChildren().add(0, makeEquipmentNode);
        break;
      case ("MakePart"):
        RightSideContent.getChildren().add(0, makePartNode);
        break;
      default:
        break;
    }
    //Set LeftSideContent
    LeftSideContent.getChildren().clear();
    switch (LeftContent) {
      case ("List"):
        LeftSideContent.getChildren().add(0, List);
        break;
      case ("InventoryDetails"):
        LeftSideContent.getChildren().add(0, inventoryDetail);

        break;
      default:
        LeftSideContent.getChildren().add(0, List);
        break;
    }
  }

  public void setDetails(EquipmentDAO dao) {
    RightContent = "Details";
    detailsController.setValues(dao);
    updateContent();
  }

  public void Close() {
    RightContent = "None";
    updateContent();
  }

  @FXML
  void CreateNewModel(ActionEvent event) {
    RightContent = "MakeModel";
    updateContent();
  }

  public void addMadePart(String partNumber, EquipmentDAO eq) {
    eq.addPart(partNumber);
    ec.updateEquipment(eq);
    RightContent = "Details";
    updateEquipment();
    detailsController.updateParts();
    updateContent();
  }

  public void setInventoryDetail(String PartNumber) {
    if (inventoryPartDetailController.setSelfByNumber(PartNumber)) {
      LeftContent = "InventoryDetails";
      updateContent();
    }
  }
}
