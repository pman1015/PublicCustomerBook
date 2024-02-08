package com.book.Loaders.Inventory;

import com.book.Controllers.InventoryController;
import com.book.DAOs.PartDAO;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class InventoryAddPart {

  @FXML
  private Text RetailPriceError;

  @FXML
  private TextField RetailPrice;

  @FXML
  private TextField PartNumber;

  @FXML
  private TextField InStock;

  @FXML
  private Text InStockError;

  @FXML
  private TextField PurchasePrice;

  @FXML
  private Text PartNumberError;

  @FXML
  private Text PurchasePriceError;

  @FXML
  private TextField PurchaseURL;

  @FXML
  void CloseWindow(ActionEvent event) {
    if (parent != null) {
      parent.setRightSideContent("None");
      parent.updateContent();
    }
  }

  @FXML
  void initialize() {
    //initalize the inventory controller
    ic = new InventoryController();
    //Clear all of the errors
    clear();
  }

  //Refrence to parent
  InventoryLoader parent;
  //Inventory database controller
  InventoryController ic;

  public void clear() {
    PartNumber.setText(null);
    PartNumberError.setVisible(false);

    PurchasePrice.setText(null);
    PurchasePriceError.setVisible(false);

    RetailPrice.setText(null);
    RetailPriceError.setVisible(false);

    InStock.setText(null);
    InStockError.setVisible(false);

    PurchaseURL.setText(null);
  }

  //Add part button press
  @FXML
  public void AddPart() {
    //Check the entered information if its invalid exit early
    if (validateInformation()) {
      System.out.println("validate error");
      return;
    }
    //Create a new Part
    PartDAO dao = new PartDAO();
    dao.setPartNumber(PartNumber.getText());
    dao.setPrice(Double.parseDouble(PurchasePrice.getText()));
    dao.setCharge(Double.parseDouble(RetailPrice.getText()));
    dao.setInStock(Integer.parseInt(InStock.getText()));
    if (PurchaseURL.getText() == null) {
      dao.setUrl("");
    } else {
      dao.setUrl(PurchaseURL.getText());
    }
    dao.setEquipment(new ArrayList<ObjectId>());
    //Insert the part
    ic.addPart(dao);
    //clear the node
    clear();
    //return to the main screen and update the part list
    parent.setRightSideContent("None");
    parent.updateContent();
    parent.setContent();
  }

  //Validate the entered information
  boolean validateInformation() {
    //boolean to store if it failed any condition
    boolean fail = false;

    //Check if the part number is null or if the part number is in use
    if (PartNumber.getText() == null || PartNumber.getText().length() < 1) {
      PartNumberError.setText("Part Number cannot be blank");
      PartNumberError.setVisible(true);
      fail = true;
    } else {
      if (!ic.checkPartNumber(PartNumber.getText())) {
        PartNumberError.setText("Part Number already in use");
        PartNumberError.setVisible(true);
        fail = true;
      } else {
        //Clear an error
        PartNumberError.setVisible(false);
      }
    }
    //Check if the purchase price is empty and if its a double
    if (
      PurchasePrice.getText() == null || PurchasePrice.getText().length() < 1
    ) {
      PurchasePriceError.setText("Purchase price cannot be blank");
      PurchasePriceError.setVisible(true);
      fail = true;
    } else {
      if (!Pattern.matches("(\\-?\\d*\\.?\\d+)", PurchasePrice.getText())) {
        PurchasePriceError.setText(
          "Purchase price needs to be a number without a $"
        );
        PurchasePriceError.setVisible(true);
        fail = true;
      } else {
        //Clear the purchase price error
        PurchasePriceError.setVisible(false);
      }
    }
    //Check if the retail price is empty and if its a double
    if (RetailPrice.getText() == null || RetailPrice.getText().length() < 1) {
      RetailPriceError.setText("Retail price cannot be blank");
      RetailPriceError.setVisible(true);
      fail = true;
    } else {
      if (!Pattern.matches("(\\-?\\d*\\.?\\d+)", RetailPrice.getText())) {
        RetailPriceError.setText(
          "Retail price needs to be a number without a $"
        );
        RetailPriceError.setVisible(true);
        fail = true;
      } else {
        //Clear the retail price error
        RetailPriceError.setVisible(false);
      }
    }
    //Check if stock is empty and if its an integer
    if (InStock.getText() == null || InStock.getText().length() < 1) {
      InStockError.setText("Stock cannot be blank");
      InStockError.setVisible(true);
      fail = true;
    } else {
      if (!Pattern.matches("(\\-?\\d*)", InStock.getText())) {
        InStockError.setText("In Stock must be a whole number");
        InStockError.setVisible(true);
        fail = true;
      } else {
        //Clear the in stocke error
        InStockError.setVisible(false);
      }
    }
    //Return if there were any error with the entered information
    return fail;
  }
}
