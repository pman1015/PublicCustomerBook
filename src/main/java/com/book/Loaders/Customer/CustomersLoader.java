package com.book.Loaders.Customer;

import com.book.Controllers.CustomerController;
import com.book.DAOs.CustomerDAO;
import com.book.Main;
import com.book.Models.Customer.Customer;
import com.book.SceneController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CustomersLoader {

  public class Sort {

    private String type;
    private String state;
    Button button;

    public Sort(String type, String state, Button button) {
      this.button = button;
      this.type = type;
      this.state = state;
    }

    public String getType() {
      return type;
    }

    public String getState() {
      return state;
    }

    public void Press(ArrayList<Sort> allSorts) {
      if (state.equals("Higher")) {
        state = "Lower";
        button.setText("\u21E9");
      } else if (state.equals("Lower")) {
        state = "Neutral";
        button.setText("-");
      } else {
        state = "Higher";
        button.setText("\u21E7");
      }
      for (Sort s : allSorts) {
        if (!(s.getType().equals(type))) {
          s.reset();
        }
      }
    }

    public void reset() {
      state = "Neutral";
      button.setText("-");
    }
  }

  //Sorting buttons
  @FXML
  Button NameSort;

  @FXML
  Button AliasSort;

  @FXML
  Button RatingSort;

  ArrayList<Sort> allSorts;

  //Page Elements
  @FXML
  VBox CustomerContainer;

  @FXML
  VBox Content;

  @FXML
  TextField SearchField;

  @FXML
  Button CreateNew;

  @FXML
  Pane Self;

  @FXML
  ScrollPane CustomerScroll;

  ArrayList<Customer> customerControllers;
  BorderPane parent;
  CustomerController cc;
  boolean createStatus = false;
  int pullSize = 5;
  boolean allShown = false;

  public CustomersLoader() {
    cc = new CustomerController();
    customerControllers = new ArrayList<>();
    allSorts = new ArrayList<>();
  }

  @FXML
  public void initialize() {
    updateCustomers();
    allSorts.add(0, new Sort("Name", "Neutral", NameSort));
    allSorts.add(1, new Sort("Alias", "Neutral", AliasSort));
    allSorts.add(2, new Sort("Rating", "Neutral", RatingSort));
  }

  //when you reach the bottom of the list poll for more
  public void scroll() {
    if (CustomerScroll.getVvalue() == 1.0 && !allShown) {
      int before = customerControllers.size();
      System.out.println(pullSize);
      pullSize = pullSize + 5;
      update();
      if (before == customerControllers.size()) {
        allShown = true;
      }
    }
  }

  public void updateCustomers() {
    customerControllers = new ArrayList<>();
    ArrayList<CustomerDAO> daos = cc.getCustomers(pullSize, 0);
    for (CustomerDAO dao : daos) {
      try {
        FXMLLoader loader = new FXMLLoader(
          Main.class.getResource("Scenes/Customers/CustomerCard.fxml")
        );
        Node n = loader.load();
        Customer c = loader.getController();
        c.setParent(this);
        c.setSelf(n);
        c.updateCard(dao.getRating(), dao.getName(), dao.getAlias());
        customerControllers.add(c);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void setParent(BorderPane parent) {
    this.parent = parent;
  }

  public void update() {
    updateCustomers();
    CustomerContainer.getChildren().clear();
    for (Customer c : customerControllers) {
      CustomerContainer.getChildren().add(c.getSelf());
    }
  }

  public void sort(int index, String type) {
    for (Customer c : customerControllers) {
      c.setCompareValue(type);
    }
    if (allSorts.get(index).getState().equals("Higher")) {
      Collections.sort(customerControllers);
    } else {
      if (allSorts.get(index).getState().equals("Lower")) {
        Collections.reverse(customerControllers);
      }
    }
    CustomerContainer.getChildren().clear();
    for (Customer c : customerControllers) {
      CustomerContainer.getChildren().add(c.getSelf());
    }
  }

  public void ToggleCreate() {
    //If create is toggling to false remove it as a child and make the main page visible
    if (createStatus) {
      Content.getChildren().remove(0);
      createStatus = !createStatus;
      return;
    }
    //If toggling create true load the fxml and set it to the be the main
    FXMLLoader create = new FXMLLoader(
      Main.class.getResource("Scenes/Customers/CustomerCreate.fxml")
    );
    try {
      Node createNode = create.load();
      CustomerCreate c = create.getController();
      c.setParent(this);
      Content.getChildren().add(0, createNode);
      createStatus = !createStatus;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void addDAO(CustomerDAO customer) {
    cc.addDAO(customer);
    update();
  }

  public void NameSortPress() {
    allSorts.get(0).Press(allSorts);
    sort(0, "Name");
  }

  public void AliasSortPress() {
    allSorts.get(1).Press(allSorts);
    sort(1, "Alias");
  }

  public void RatingSortPress() {
    allSorts.get(2).Press(allSorts);
    sort(2, "Rating");
  }

  public void Search() {
    System.out.println("Search");
  }

  public void setMoreInfo(String Name) {
    FXMLLoader loader = new FXMLLoader(
      Main.class.getResource("Scenes/Customers/MoreInfo.fxml")
    );
    try {
      Node MoreInfo = loader.load();
      ((MoreInfoLoader) loader.getController()).setParent(this);
      ((MoreInfoLoader) loader.getController()).setName(Name);
      ((MoreInfoLoader) loader.getController()).setSelf(MoreInfo);
      ((MoreInfoLoader) loader.getController()).setBP(parent);

     
      parent.setCenter(MoreInfo);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void setCenter(Node Content) {
    parent.setCenter(Content);
  }

  public void reset() {
    update();
    parent.setCenter(Self);
  }
}
