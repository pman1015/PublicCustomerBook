package com.book.Loaders.Equipment;

import com.book.Controllers.CustomerController;
import com.book.Controllers.EquipmentController;
import com.book.Controllers.MachineController;
import com.book.DAOs.CustomerDAO;
import com.book.DAOs.CustomerEquipmentDAO;
import com.book.Loaders.Customer.MoreInfoLoader;
import com.book.Main;
import com.book.Models.Equipment.EquipmentDetailCard;
import com.book.Models.Equipment.EquipmentDetailInfo;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import org.bson.BsonObjectId;
import org.bson.types.ObjectId;

@Getter
@Setter
public class EquipmentDetailLoader {

  @FXML
  Pane Self;

  @FXML
  AnchorPane LeftContent;

  @FXML
  AnchorPane RightContent;

  @FXML
  VBox EquipmentCardHolder;

  @FXML
  HBox MainContentContainer;

  @FXML
  Pane List;

  //Pointer to the parent
  Node parent;
  //Nodes for content
  Node EquipmentInfo;
  Node AddEqipment;

  //Controllers for the Right Content
  EquipmentDetailInfo equipmentInfoController;
  EquipmentDetailsAddEquipment equipmentAddController;

  //String to store what is displayed on the right side
  String rightSideContentString = "None";

  //List of all the equipment a cutomer ownes
  ArrayList<CustomerEquipmentDAO> CustomersEquipment;
  //Machine Database controller
  MachineController mc;
  //EquipmentController Database
  EquipmentController ec;
  //CustomerController database
  CustomerController cc;
  //CustomerDAO that the macines are for
  CustomerDAO refrence;

  @FXML
  public void addEquipment() {
    rightSideContentString = "Add";
    addModels();
    equipmentAddController.setCustomerName(refrence.getName());
    updatePageContent();
  }

  @FXML
  public void initialize() {
    //initalize the database controller;
    mc = new MachineController();
    ec = new EquipmentController();
    cc = new CustomerController();

    CustomersEquipment = new ArrayList<>();

    //Set the Details node
    FXMLLoader infoLoader = new FXMLLoader(
      Main.class.getResource("Scenes/Equipment/EquipmentDetailInfo.fxml")
    );
    //Add loader goes here
    FXMLLoader addMachineLoader = new FXMLLoader(
      Main.class.getResource(
          "Scenes/Equipment/EquipmentDetailAddEquipment.fxml"
        )
    );

    try {
      //Equipment info node and controller set
      EquipmentInfo = infoLoader.load();
      equipmentInfoController = infoLoader.getController();
      equipmentInfoController.setParent(this);

      //Set Add equipment node and controller
      AddEqipment = addMachineLoader.load();
      equipmentAddController = addMachineLoader.getController();
      equipmentAddController.setParent(this);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void updateCards() {
    //Loop thru all equipment and add them to the VBox
    for (CustomerEquipmentDAO dao : CustomersEquipment) {
      FXMLLoader EQ = new FXMLLoader(
        Main.class.getResource("Scenes/Equipment/EquipmentDetailCard.fxml")
      );
      try {
        Node Card = EQ.load();
        EquipmentDetailCard controller = EQ.getController();
        controller.setParent(this);
        controller.setSelf(dao);
        EquipmentCardHolder.getChildren().add(Card);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void updatePageContent() {
    if (RightContent.getChildren().size() > 0) {
      RightContent.getChildren().get(0).setVisible(false);
    }
    RightContent.getChildren().clear();
    switch (rightSideContentString) {
      case ("Info"):
        RightContent.getChildren().add(0, EquipmentInfo);
        break;
      case ("Add"):
        RightContent.getChildren().add(0, AddEqipment);
        break;
      default:
        break;
    }
    if (RightContent.getChildren().size() > 0) {
      RightContent.getChildren().get(0).setVisible(true);
    }
  }

  public void setRefrence(CustomerDAO dao) {
    refrence = dao;
    if (dao.getMachineIDs() != null) {
      CustomersEquipment = mc.findAll(dao.getMachineIDs());
      updateCards();
    }
  }

  public void addModels() {
    equipmentAddController.updateModels(ec.getAllModels());
  }

  public void Back(ActionEvent event) {
    updatePageContent();
    parent.setVisible(true);
    ((BorderPane) ((Button) event.getSource()).getScene().getRoot()).setCenter(
        parent
      );
  }

  public void updateCustomer(ObjectId objectId) {
    if (refrence.getMachineIDs() == null) {
      refrence.setMachineIDs(new ArrayList<ObjectId>());
    }
    refrence.getMachineIDs().add(objectId);
    cc.updateCustomer(refrence);
    updateCards();
  }
}
