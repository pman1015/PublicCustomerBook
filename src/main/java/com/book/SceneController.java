package com.book;

import com.book.Loaders.Bills.BillsLoader;
import com.book.Loaders.Customer.CustomersLoader;
import com.book.Loaders.Equipment.EquipmentLoader;
import com.book.Loaders.Home.HomeLoader;
import com.book.Loaders.Inventory.InventoryLoader;
import com.book.Loaders.Jobs.JobLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class SceneController {

  JobLoader jobloader;
  HomeLoader homeloader;
  CustomersLoader customesLoader;
  EquipmentLoader equipmentLoader;
  InventoryLoader inventoryLoader;
  BillsLoader billsLoader;

  FXMLLoader HomePageLoader;
  FXMLLoader EquipmentPageLoader;
  FXMLLoader InventoryPageLoader;
  FXMLLoader CustomerPageLoader;
  FXMLLoader JobPageLoader;
  FXMLLoader BillsLoader;

  Node HomePage;
  Node EquipmentPage;
  Node InventoryPage;
  Node CustomerPage;
  Node JobPage;
  Node BillsPage;

  ArrayList<Node> Pages;

  @FXML
  VBox NavMenu;

  public SceneController() throws IOException {
    System.out.println(
      getClass().getResource("Scenes/Customers/Customers.fxml")
    );
    //Load all FXML
    HomePageLoader =
      new FXMLLoader(getClass().getResource("Scenes/Home/Home.fxml"));
    HomePage = HomePageLoader.load();
    homeloader = HomePageLoader.getController();

    EquipmentPageLoader =
      new FXMLLoader(getClass().getResource("Scenes/Equipment/Equipment.fxml"));
    EquipmentPage = EquipmentPageLoader.load();
    equipmentLoader = EquipmentPageLoader.getController();

    InventoryPageLoader =
      new FXMLLoader(getClass().getResource("Scenes/Inventory/Inventory.fxml"));
    InventoryPage = InventoryPageLoader.load();
    inventoryLoader = InventoryPageLoader.getController();

    CustomerPageLoader =
      new FXMLLoader(getClass().getResource("Scenes/Customers/Customers.fxml"));
    CustomerPage = CustomerPageLoader.load();
    customesLoader = CustomerPageLoader.getController();

    JobPageLoader =
      new FXMLLoader(getClass().getResource("Scenes/Jobs/Jobs.fxml"));
    JobPage = JobPageLoader.load();
    jobloader = JobPageLoader.getController();
    jobloader.setSelf(JobPage);

    BillsLoader =
      new FXMLLoader(getClass().getResource("Scenes/Bills/BillsPage.fxml"));
    BillsPage = BillsLoader.load();
    billsLoader = BillsLoader.getController();

    Pages =
      new ArrayList<>(
        Arrays.asList(
          HomePage,
          EquipmentPage,
          InventoryPage,
          CustomerPage,
          JobPage,
          BillsPage
        )
      );
  }

  BorderPane parent;

  @FXML
  public void initilize() {}

  public Node getHome() {
    HomePage = homeloader.updatePage(HomePage);
    return HomePage;
  }

  public void setParent(BorderPane parent) {
    this.parent = parent;
    customesLoader.setParent(parent);
    homeloader.setBp(parent);
    jobloader.setBp(parent);
    inventoryLoader.setBp(parent);
  }

  private void traverse() {
    for (Node page : Pages) {
      page.setVisible(false);
    }
  }

  public void goToHome(ActionEvent e) {
    traverse();
    HomePage.setVisible(true);
    HomePage = homeloader.updatePage(HomePage);
    parent.setCenter(HomePage);
  }

  public void goToJobs(ActionEvent e) {
    traverse();
    JobPage.setVisible(true);
    JobPage = jobloader.SetJobs(JobPage);
    parent.setCenter(JobPage);
  }

  public void goToCustomers(ActionEvent e) {
    traverse();
    CustomerPage.setVisible(true);
    customesLoader.update();
    parent.setCenter(CustomerPage);
  }

  public void goToEquipment(ActionEvent e) {
    traverse();
    EquipmentPage.setVisible(true);
    equipmentLoader.updateEquipment();
    parent.setCenter(EquipmentPage);
  }

  public void goToInventory(ActionEvent e) {
    traverse();
    InventoryPage.setVisible(true);
    inventoryLoader.setContent();
    parent.setCenter(InventoryPage);
  }

  public void goToBills(ActionEvent event) {
    traverse();
    BillsPage.setVisible(true);
    parent.setCenter(BillsPage);
  }
}
