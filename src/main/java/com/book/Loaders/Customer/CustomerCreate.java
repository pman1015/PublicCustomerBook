package com.book.Loaders.Customer;

import com.book.DAOs.CustomerDAO;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerCreate {

  @FXML
  private Label NameError;

  @FXML
  private Label AliasError;

  @FXML
  private TextField Address;

  @FXML
  private TextField Alias;

  @FXML
  private Label AddressError;

  @FXML
  private TextField PhoneNumber;

  @FXML
  private Label PhoneNumberError;

  @FXML
  private TextField CustomerName;

  @FXML
  private TextArea Notes;

  CustomersLoader parent;
  ArrayList<TextField> textFields;
  ArrayList<Label> errors;

  @FXML
  public void initialize() {
    textFields =
      new ArrayList<>(Arrays.asList(CustomerName, Alias, Address, PhoneNumber));
    errors =
      new ArrayList<>(
        Arrays.asList(NameError, AliasError, AddressError, PhoneNumberError)
      );
    Clear();
  }

  public void Create() {
    if (!validate()) {
      return;
    }
    CustomerDAO newCustomer = new CustomerDAO();
    newCustomer.setName(CustomerName.getText());
    newCustomer.setAlias(Alias.getText());
    newCustomer.setAddress(Address.getText());
    newCustomer.setRating(3);
    newCustomer.setNotes(Notes.getText());
    newCustomer.setPhoneNumber(PhoneNumber.getText());
    newCustomer.setJobIDs(new ArrayList<>());
    newCustomer.setMachineIDs(new ArrayList<>());

    parent.addDAO(newCustomer);
    Clear();
    parent.ToggleCreate();
  }

  public void Cancel() {
    Clear();
    parent.ToggleCreate();
  }

  public void Clear() {
    for (TextField field : textFields) {
      field.setText("");
    }
    for (Label error : errors) {
      error.setVisible(false);
    }
  }

  public boolean validate() {
    boolean fail = false;
    int pos = 0;
    while (pos < textFields.size()) {
      boolean check = nullCheck(textFields.get(pos), errors.get(pos));
      fail = (fail || check);
      pos++;
    }
    return !fail;
  }

  public boolean nullCheck(TextField data, Label error) {
    if (data.getText() == null || data.getText().length() < 1) {
      error.setVisible(true);
      error.setText("Field cannot be blank");
      return true;
    } else {
      error.setVisible(false);
      return false;
    }
  }
}
