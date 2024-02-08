package com.book.Loaders.Inventory;

import com.book.Controllers.InventoryController;
import com.book.Controllers.ReportsController;
import com.book.DAOs.PartDAO;
import com.book.DAOs.PromotionalPriceDAO;
import com.book.DAOs.ReportDAO;
import com.book.Loaders.Equipment.EquipmentLoader;
import com.book.Main;
import com.book.Models.Warnings;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryPartDetail {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Label RetailPrice;

  @FXML
  private Label PartNumber;

  @FXML
  private Button Cancel;

  @FXML
  private Button Confirm;

  @FXML
  private Label PurchasePrice;

  @FXML
  private Button Edit;

  @FXML
  private Text PartNumberError;

  @FXML
  private Text URLError;

  @FXML
  private Text PromoPurchasePriceError;

  @FXML
  private Label URL;

  @FXML
  private TextField PromoPurchasePriceEdit;

  @FXML
  private Button AddPromotion;

  @FXML
  private TextField URLEdit;

  @FXML
  private Text PromoQuanityError;

  @FXML
  private TextField StockEdit;

  @FXML
  private Text PromoChargeError;

  @FXML
  private TextField PurchasePriceEdit;

  @FXML
  private TextField RetailPriceEdit;

  @FXML
  private Text StockError;

  @FXML
  private Pane PromotionalPricePane;

  @FXML
  private TextField PromoChargeEdit;

  @FXML
  private Button UpdatePromotion;

  @FXML
  private Text RetailPriceError;

  @FXML
  private TextField PromoQuanityEdit;

  @FXML
  private AnchorPane InventoryDetails;

  @FXML
  private VBox PromotionalPriceContainer;

  @FXML
  private Text PurchasePriceError;

  @FXML
  private TextField PartNumberEdit;

  @FXML
  private Label Stock;

  @FXML
  private HBox EditOptions;

  @FXML
  private HBox CategoryToggleContainer;

  @FXML
  private ToggleButton CategoryToggleFalse;

  @FXML
  private ToggleButton CategoryToggleTrue;

  @FXML
  private Label CategoryLabel;

  @FXML
  private ComboBox<String> ExpenseComboBox;

  @FXML
  private Text ExpenseComboBoxError;

  @FXML
  private TextField ExpenseTextField;

  @FXML
  private Text ExpenseTextFieldError;

  @FXML
  private HBox ToggleContainer;

  @FXML
  private AnchorPane ExpenseComboBoxPane;

  @FXML
  private AnchorPane ExpenseTextFieldPane;

  @FXML
  void ExpenseCategoryToggle() {
    //Toggle the expense category
    expenseToggle = !expenseToggle;
    //Set the toggle button states
    CategoryToggleTrue.setSelected(expenseToggle);
    CategoryToggleFalse.setSelected(!expenseToggle);
    //Set the style of the container
    CategoryToggleContainer.getStyleClass().clear();
    if (expenseToggle) {
      CategoryToggleContainer.getStyleClass().add("toggleSwitchContainerTrue");
    } else {
      CategoryToggleContainer.getStyleClass().add("toggleSwitchContainerFalse");
    }
    //Toggle the visible edit
    toggleCategoryEdit();
  }

  @FXML
  void CloseWindow(ActionEvent event) {
    //If opened from an inventory controller change the right content
    if (inventoryLoader != null) {
      inventoryLoader.setRightSideContent("None");
      inventoryLoader.updateContent();
      clear();
    }
    if (equipmentLoader != null) {
      equipmentLoader.setLeftContent("List");
      equipmentLoader.updateContent();
      clear();
    }
  }

  @FXML
  void CreatePromotionalPrice(ActionEvent event) {
    clearPromo();
    togglePromo();
  }

  @FXML
  void EditPartDetails(ActionEvent event) {
    toggleEdit();
  }

  boolean warningCleared = false;

  public boolean sendWarning(String message) {
    warningCleared = false;
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("Warning.fxml"));
    try {
      AnchorPane warning = loader.load();
      Warnings controller = loader.getController();
      controller.getMessage().setText(message);

      Scene scene = new Scene(warning);
      scene.setFill(Color.web("#ffffff00"));
      Stage stage = new Stage();
      stage.setScene(scene);
      stage.sizeToScene();
      stage.setResizable(false);
      stage.initStyle(StageStyle.TRANSPARENT);
      stage.showAndWait();
      
      return controller.getReturnValue();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return false;
    }
  }

  @FXML
  void UpdateExistingPromotion(ActionEvent event) {
    //Check the validity of promofields
    if (!validatePromo()) {
      //If you set the stock to 0 detete the promo and adjust the instock
      if (Integer.valueOf(PromoQuanityEdit.getText()) == 0) {
        if (
          !sendWarning(
            "You are about to delete this promo are you sure you want to continue?"
          )
        ) {
          return;
        }
        self.removePromo(activePromoDAO);
        ic.updatePart(self);
        togglePromo();
        clear();
        return;
      }
      //if valid update the refrence to the price in the dao and the card
      activePromoDAO =
        self.updatePromotion(
          activePromoDAO,
          PromoPurchasePriceEdit.getText(),
          PromoChargeEdit.getText(),
          PromoQuanityEdit.getText()
        );
      activeCard.setSelf(activePromoDAO);
      //Update the database
      ic.updatePart(self);
      //reload the inventory cards
      if (inventoryLoader != null) {
        inventoryLoader.setContent();
      }
      togglePromo();
      clear();
    }
  }

  @FXML
  void ConfirmPartEdit(ActionEvent event) {
    //Check if the values are valid
    if (!validateDetailsEdit()) {
      //If new price is different from the older make a promotional price for the old stock
      if (self.getPrice() != Double.valueOf(PurchasePriceEdit.getText())) {
        if (
          !sendWarning(
            "By changing the purchase price all old stock will be added as a promotional price"
          )
        ) {
          return;
        }
        self.priceChange();
      }
      //Update the self dao
      self.setPartNumber(PartNumberEdit.getText());
      self.setPrice(Double.valueOf(PurchasePriceEdit.getText()));
      self.setCharge(Double.valueOf(RetailPriceEdit.getText()));
      self.setInStock(Integer.valueOf(StockEdit.getText()));
      if (URLEdit.getText() != null) {
        self.setUrl(URLEdit.getText());
      }
      if (expenseToggle) {
        self.setExpenseCategory(ExpenseTextField.getText());
        expenseReport.getExpenseCategories().add(ExpenseTextField.getText());
        rc.updateExpenses(expenseReport);
      } else {
        self.setExpenseCategory(ExpenseComboBox.getValue());
      }
      //If update was suscessfull
      if (ic.updatePart(self)) {
        clear();
        toggleEdit();
        if (inventoryLoader != null) {
          inventoryLoader.setContent();
        }
        if (equipmentLoader != null) {
          equipmentLoader.getDetailsController().updateParts();
        }
      }
    }
  }

  @FXML
  void CancelPartEdit(ActionEvent event) {
    clear();
    toggleEdit();
  }

  @FXML
  void ClosePromo(ActionEvent event) {
    clearPromo();
    togglePromo();
  }

  @FXML
  void AddPromotion(ActionEvent event) {
    if (!validatePromo()) {
      self.AddNewPromotion(
        new PromotionalPriceDAO(
          Double.valueOf(PromoPurchasePriceEdit.getText()),
          Integer.valueOf(PromoQuanityEdit.getText()),
          Double.valueOf(PromoChargeEdit.getText())
        )
      );
      if (ic.updatePart(self)) {
        clearPromo();
        togglePromo();
        if (inventoryLoader != null) {
          inventoryLoader.setContent();
        }
      }
    }
  }

  @FXML
  void initialize() {
    ic = new InventoryController();
    rc = new ReportsController();
    clear();
    togglePromo();
    toggleEdit();
  }

  boolean showPromo = true;
  boolean showEdit = true;
  boolean expenseToggle = false;
  PartDAO self;
  InventoryLoader inventoryLoader;
  EquipmentLoader equipmentLoader;
  InventoryController ic;
  ReportsController rc;
  ReportDAO expenseReport;

  public void setSelf(PartDAO dao) {
    this.self = dao;
    clear();
  }

  public boolean setSelfByNumber(String partNumber) {
    self = ic.findByPartNumber(partNumber);
    if (self != null) {
      clear();
      return true;
    }
    return false;
  }

  public void clear() {
    clearDetails();
    clearPromo();
  }

  public void clearDetails() {
    //If self is set reset all of the labels
    //If not clear all edits
    if (self != null) {
      setValues();
    } else {
      PartNumberEdit.setText("");
      PurchasePriceEdit.setText("");
      RetailPriceEdit.setText("");
      StockEdit.setText("");
      URLEdit.setText("");
      ExpenseComboBox.setValue("");
      ExpenseTextField.setText("");
    }
    //Hide all Errors
    PartNumberError.setVisible(false);
    PurchasePriceError.setVisible(false);
    RetailPriceError.setVisible(false);
    StockError.setVisible(false);
    URLError.setVisible(false);
    ExpenseComboBoxError.setVisible(false);
    ExpenseTextFieldError.setVisible(false);
    //Reset the edit Buttons
    EditOptions.setVisible(false);
    EditOptions.toBack();
    Edit.setVisible(true);
    Edit.toFront();
    //Hide the expense toggle
    ToggleContainer.setVisible(false);
  }

  public void setValues() {
    //Update the fields with the values from the DAO
    PartNumber.setText(self.getPartNumber());
    PartNumberEdit.setText(self.getPartNumber());

    PurchasePrice.setText(Double.toString(self.getPrice()));
    PurchasePriceEdit.setText(Double.toString(self.getPrice()));

    RetailPrice.setText(Double.toString(self.getCharge()));
    RetailPriceEdit.setText(Double.toString(self.getCharge()));

    Stock.setText(Integer.toString(self.getInStock()));
    StockEdit.setText(Integer.toString(self.getInStock()));

    URL.setText(self.getUrl());
    URLEdit.setText(self.getUrl());

    if (self.getExpenseCategory() != null) {
      CategoryLabel.setText(self.getExpenseCategory());
      ExpenseComboBox.setValue(self.getExpenseCategory());
      ExpenseTextField.setText(self.getExpenseCategory());
    } else {
      CategoryLabel.setText("Uncategorised");
      ExpenseTextField.setText("Uncategorised");
      ExpenseComboBox.setValue("Uncategorised");
    }
    //Update the combobox
    setComboBox();
    //Set the promocards
    setPromoCards();
  }

  public void setComboBox() {
    //Initalise the combobox for categories
    expenseReport = rc.getExpenses();
    ObservableList<String> expenses = FXCollections.observableArrayList();
    for (String s : expenseReport.getExpenseCategories()) {
      expenses.add(s);
    }
    ExpenseComboBox.setItems(expenses);
  }

  public void setPromoCards() {
    PromotionalPriceContainer.getChildren().clear();
    if (self.getPromotions() != null) {
      for (PromotionalPriceDAO dao : self.getPromotions()) {
        FXMLLoader loader = new FXMLLoader(
          Main.class.getResource(
              "Scenes/Inventory/InventoryDetailPromotionCard.fxml"
            )
        );
        try {
          PromotionalPriceContainer.getChildren().add(loader.load());
          InventoryPartDetailPromoCard controller = loader.getController();
          controller.setParent(this);
          controller.setSelf(dao);
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }

  public void clearPromo() {
    PromoPurchasePriceEdit.setText("");
    PromoPurchasePriceError.setVisible(false);

    PromoQuanityEdit.setText("");
    PromoQuanityError.setVisible(false);

    PromoChargeEdit.setText("");
    PromoChargeError.setVisible(false);
  }

  public void togglePromo() {
    //If toggling without a promoDAO toggle the visibility
    showPromo = !showPromo;
    PromotionalPricePane.setVisible(showPromo);
    //If toggling to true then set the pane to front and show the Add button
    if (showPromo) {
      PromotionalPricePane.toFront();
      AddPromotion.setVisible(true);
      UpdatePromotion.setVisible(false);
      AddPromotion.toFront();
    } else {
      PromotionalPricePane.toBack();
    }
  }

  public void togglePromo(PromotionalPriceDAO dao) {
    //If toggling with a dao then toggle to update
    showPromo = !showPromo;
    PromotionalPricePane.setVisible(showPromo);
    //If toggling to true then toggle the pane to front update fields and show Update button
    if (showPromo) {
      PromotionalPricePane.toFront();
      setPromoFields(dao);
      UpdatePromotion.setVisible(true);
      AddPromotion.setVisible(false);
      UpdatePromotion.toFront();
    } else {
      PromotionalPricePane.toBack();
    }
  }

  public void setPromoFields(PromotionalPriceDAO dao) {
    //Set the fields of the promotional pane to that of the dao
    PromoPurchasePriceEdit.setText(Double.toString(dao.getPurchasePrice()));
    PromoQuanityEdit.setText(Integer.toString(dao.getQuanity()));
    PromoChargeEdit.setText(Double.toString(dao.getCharge()));
  }

  public void toggleCategoryEdit() {
    ExpenseComboBoxPane.setVisible(showEdit && !expenseToggle);
    ExpenseTextFieldPane.setVisible(showEdit && (expenseToggle));
    if (expenseToggle) {
      ExpenseTextFieldPane.toFront();
    } else {
      ExpenseComboBoxPane.toFront();
    }
  }

  public void toggleEdit() {
    showEdit = !showEdit;
    //If show edit then hide the labels and show the textfields

    PartNumber.setVisible(!showEdit);
    PartNumberEdit.toFront();
    PartNumberEdit.setVisible(showEdit);

    PurchasePrice.setVisible(!showEdit);
    PurchasePriceEdit.toFront();
    PurchasePriceEdit.setVisible(showEdit);

    RetailPrice.setVisible(!showEdit);
    RetailPriceEdit.toFront();
    RetailPriceEdit.setVisible(showEdit);

    Stock.setVisible(!showEdit);
    StockEdit.toFront();
    StockEdit.setVisible(showEdit);

    URL.setVisible(!showEdit);
    URLEdit.toFront();
    URLEdit.setVisible(showEdit);

    CategoryLabel.setVisible(!showEdit);
    toggleCategoryEdit();
    ToggleContainer.setVisible(showEdit);

    //Toggle the edit options
    Edit.setVisible(!showEdit);
    EditOptions.setVisible(showEdit);
    if (showEdit) {
      EditOptions.toFront();
    } else {
      Edit.toFront();
    }
  }

  public boolean validateDetailsEdit() {
    boolean fail = false;
    //Check the part number
    if (
      PartNumberEdit.getText() == null || PartNumberEdit.getText().length() < 1
    ) {
      PartNumberError.setText("Part Number cannot be empty");
      PartNumberError.setVisible(true);
      fail = true;
    } else {
      if (
        !PartNumberEdit.getText().equals(self.getPartNumber()) &&
        !ic.checkPartNumber(PartNumberEdit.getText())
      ) {
        PartNumberError.setText("Part Number Taken");
        PartNumberError.setVisible(true);
        fail = true;
      } else {
        PartNumberError.setVisible(false);
      }
    }
    //Check the Puchase price
    if (
      PurchasePriceEdit.getText() == null ||
      PurchasePriceEdit.getText().length() < 1
    ) {
      PurchasePriceError.setText("Purchase Price cannot be empty");
      PurchasePriceError.setVisible(true);
      fail = true;
    } else {
      if (!Pattern.matches("(\\-?\\d*\\.?\\d+)", PurchasePriceEdit.getText())) {
        PurchasePriceError.setText(
          "Purchse Price must be a number without a $"
        );
        PurchasePriceError.setVisible(true);
        fail = true;
      } else {
        PurchasePriceError.setVisible(false);
      }
    }
    //Check the Retil Price
    if (RetailPrice.getText() == null || RetailPrice.getText().length() < 1) {
      RetailPriceError.setText("Retail Price cannot be empty");
      RetailPriceError.setVisible(true);
      fail = true;
    } else {
      if (!Pattern.matches("(\\-?\\d*\\.?\\d+)", RetailPriceEdit.getText())) {
        RetailPriceError.setText("Retail Price must be a number without a $");
        RetailPriceError.setVisible(true);
        fail = true;
      } else {
        RetailPriceError.setVisible(false);
      }
    }
    //Check the Stock
    if (StockEdit.getText() == null || StockEdit.getText().length() < 1) {
      StockError.setText("Stock cannot be blank");
      StockError.setVisible(true);
      fail = true;
    } else {
      if (!Pattern.matches("(\\-?\\d*)", StockEdit.getText())) {
        StockError.setText("In stock must be a whole number");
        StockError.setVisible(true);
        fail = true;
      } else {
        StockError.setVisible(false);
      }
    }
    //Check the Expense Category
    //Determine if new or existing is checked
    //If existing then no need to check
    if (expenseToggle) {
      if (
        ExpenseTextField.getText() == null ||
        ExpenseTextField.getText().length() < 1
      ) {
        ExpenseTextFieldError.setText("Expense category cannot be blank");
        ExpenseTextFieldError.setVisible(true);
        fail = true;
      } else {
        expenseReport = rc.getExpenses();
        if (expenseReport.inUse(ExpenseTextField.getText())) {
          ExpenseTextFieldError.setText("Expense Category name in use");
          ExpenseTextFieldError.setVisible(true);
          fail = true;
        } else {
          ExpenseTextFieldError.setVisible(false);
        }
      }
    }
    return fail;
  }

  public boolean validatePromo() {
    boolean fail = false;
    //Check the puchase price
    if (
      PromoPurchasePriceEdit.getText() == null ||
      PromoPurchasePriceEdit.getText().length() < 1
    ) {
      PromoPurchasePriceError.setText("Purchase price cannot be blank");
      PromoChargeError.setVisible(true);
      fail = true;
    } else {
      if (
        !Pattern.matches("(\\-?\\d*\\.?\\d+)", PromoPurchasePriceEdit.getText())
      ) {
        PromoPurchasePriceError.setText(
          "Puchase price must be a number without a $"
        );
        PromoPurchasePriceError.setVisible(true);
        fail = true;
      } else {
        PromoPurchasePriceError.setVisible(false);
      }
    }
    //Check the quanity
    if (
      PromoQuanityEdit.getText() == null ||
      PromoQuanityEdit.getText().length() < 1
    ) {
      PromoQuanityError.setText("Quantity cannont be blank");
      PromoQuanityError.setVisible(true);
      fail = true;
    } else {
      if (!Pattern.matches("(\\-?\\d*)", PromoQuanityEdit.getText())) {
        PromoQuanityError.setText("Quantity must be a whole number");
        PromoQuanityError.setVisible(true);
        fail = true;
      } else {
        PromoQuanityError.setVisible(false);
      }
    }
    //Check the altered charge
    if (
      PromoChargeEdit.getText() == null ||
      PromoChargeEdit.getText().length() < 1
    ) {
      PromoChargeError.setText("Altered Charge cannot be empty");
      PromoChargeError.setVisible(true);
      fail = true;
    } else {
      if (!Pattern.matches("(\\-?\\d*\\.?\\d+)", PromoChargeEdit.getText())) {
        PromoChargeError.setText("Charge must be a number without a $");
        PromoChargeError.setVisible(true);
        fail = true;
      } else {
        PromoChargeError.setVisible(false);
      }
    }
    return fail;
  }

  InventoryPartDetailPromoCard activeCard;
  PromotionalPriceDAO activePromoDAO;

  public void updatePromo(
    InventoryPartDetailPromoCard card,
    PromotionalPriceDAO dao
  ) {
    //If promo is already up ignore the press
    if (showPromo) {
      return;
    }
    //if not set the promofields and toggle the promo
    activeCard = card;
    activePromoDAO = dao;
    togglePromo(dao);
  }
}
