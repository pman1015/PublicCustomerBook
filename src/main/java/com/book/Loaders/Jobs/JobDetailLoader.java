package com.book.Loaders.Jobs;

import com.book.Controllers.EquipmentController;
import com.book.Controllers.InventoryController;
import com.book.Controllers.JobController;
import com.book.Controllers.MachineController;
import com.book.Controllers.ReportsController;
import com.book.DAOs.CustomerEquipmentDAO;
import com.book.DAOs.EquipmentDAO;
import com.book.DAOs.JobDAO;
import com.book.DAOs.LaborChargeDAO;
import com.book.DAOs.PartChargeDAO;
import com.book.DAOs.PartDAO;
import com.book.DAOs.PromotionalPriceDAO;
import com.book.Main;
import com.book.Models.Jobs.BillCharge;
import com.book.Models.Status;
import com.book.Models.Warnings;
import com.book.Utilities.WarningSender;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Setter;
import org.bson.types.ObjectId;

@Setter
public class JobDetailLoader {

  //Content containers for left side
  @FXML
  VBox Details;

  @FXML
  Pane LaborChargeEdit;

  @FXML
  Pane PartsChargeEdit;

  //Fields for editing details
  @FXML
  Text JobName;

  @FXML
  TextField JobNameEdit;

  @FXML
  Text StartDate;

  @FXML
  Text EndDate;

  @FXML
  Text Equipment;

  @FXML
  Text Status;

  @FXML
  ChoiceBox<String> StatusSelect;

  @FXML
  TextArea JobNotes;

  @FXML
  HBox EditOptions;

  @FXML
  HBox EditSelect;

  //Fields for LaborChargeEdit
  @FXML
  TextField HourlyRateField;

  @FXML
  TextField HoursField;

  @FXML
  Button AddLaborCharge;

  @FXML
  Button LaborChargeUpdate;

  @FXML
  Label RateError;

  @FXML
  Label HoursError;

  //Fields for PartsChargeEdit
  @FXML
  ComboBox<String> PartNumberComboBox;

  @FXML
  Button PartChargeAdd;

  @FXML
  Button PartChargeUpdate;

  @FXML
  TextField PartChargeQuanity;

  //Bill Fields
  @FXML
  TextField DeliveryCost;

  @FXML
  Label DeliveryError;

  @FXML
  Text BillStatus;

  @FXML
  VBox LaborCharges;

  @FXML
  Text LaborTotal;

  @FXML
  VBox PartCharges;

  @FXML
  Text PartsTotal;

  @FXML
  Text BillTotal;

  @FXML
  Button NewLaborCharge;

  @FXML
  Button NewPartsCharge;

  Node parent;
  BorderPane bp;
  String LeftContent = "Details";
  ArrayList<Node> LeftContentOptions;
  JobDAO selfRefrence;
  EquipmentController ec;
  MachineController mc;
  JobController jc;

  @FXML
  public void initialize() {
    //Initalise DatabaseControllers
    jc = new JobController();
    mc = new MachineController();
    ec = new EquipmentController();

    LeftContentOptions =
      new ArrayList<Node>(
        Arrays.asList(
          (Node) Details,
          (Node) LaborChargeEdit,
          (Node) PartsChargeEdit
        )
      );
    UpdateContent();
    DeliveryError.setVisible(false);

    DeliveryCost
      .focusedProperty()
      .addListener(
        new ChangeListener<Boolean>() {
          @Override
          public void changed(
            ObservableValue<? extends Boolean> observable,
            Boolean oldValue,
            Boolean newValue
          ) {
            editDeliveryCost();
          }
        }
      );
  }

  public void editDeliveryCost() {
    boolean fail = false;
    String enteredAmount = DeliveryCost.getText();
    if (enteredAmount == null || enteredAmount.length() < 1) {
      DeliveryError.setText("DeliveryCost cannot be blank Changes not saved");
      DeliveryError.setVisible(true);
      fail = true;
    } else {
      if (!Pattern.matches("(\\-?\\d*\\.?\\d+)", enteredAmount)) {
        DeliveryError.setText("Delivery cost must be a number");
        DeliveryError.setVisible(true);
        fail = true;
      } else {
        DeliveryError.setVisible(false);
      }
    }
    if (fail) return;

    selfRefrence.getBill().setDeliveryCost(Double.parseDouble(enteredAmount));
    selfRefrence.getBill().updateCost();
    jc.updateJob(selfRefrence);
    UpdateBill();
  }

  public void UpdateContent() {
    for (Node n : LeftContentOptions) {
      if (n.getId().equals(LeftContent)) {
        n.setVisible(true);
        n.toFront();
      } else {
        n.setVisible(false);
      }
    }
  }

  public void setDetails() {
    JobName.setText(selfRefrence.getJobName());
    JobName.setVisible(true);
    JobNameEdit.setText(selfRefrence.getJobName());
    JobNameEdit.setVisible(false);
    JobNameEdit.setDisable(true);

    StartDate.setText(selfRefrence.getStartDate());
    EndDate.setText(selfRefrence.getEndDate());

    EquipmentDAO eDAO = ec.findByID(selfRefrence.getEquipment());
    Equipment.setText(eDAO.getModelNumber());
    Status.setText(selfRefrence.getStatus());
    Status.setVisible(true);

    //Set the status options and if its not complete then remove the paid status
    //Also add Cancel status to be used to delete the Job
    String[] types = new Status().getTypes();
    boolean includePaid = selfRefrence.getStatus().equals("CompletedUnpaid");
    int pos = 0;
    ArrayList<String> adjustedTypes = new ArrayList<>();
    while (pos < types.length) {
      if (!types[pos].equals("CompletedPaid") || includePaid) {
        adjustedTypes.add(types[pos]);
      }
      pos++;
    }
    adjustedTypes.add("Canceled");
    StatusSelect.setItems(FXCollections.observableList(adjustedTypes));
    StatusSelect.setValue(selfRefrence.getStatus());
    //StatusSelect hide
    StatusSelect.setVisible(false);

    JobNotes.setText(selfRefrence.getDetails());
    JobNotes.setEditable(false);
  }

  public void Back() {
    parent.setVisible(true);
    bp.setCenter(parent);
  }

  public void Confirm() {
    //Update the text DAO from the fields
    selfRefrence.setJobName(JobNameEdit.getText());

    //check if status has changed
    if (!selfRefrence.getStatus().equals(StatusSelect.getValue())) {
      //Check if status is going from Not complete to complete
      if (
        //Previous status was not complete
        !selfRefrence.getStatus().equals("CompletedPaid") &&
        !selfRefrence.getStatus().equals("CompletedUnpaid")
      ) {
        //if new status is complete set end date
        if (StatusSelect.getValue().equals("CompletedUnpaid")) {
          DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy");
          selfRefrence.setEndDate(df.format(LocalDate.now()));
        }
      } else {
        
        //Status was previously complete and is now not complete
        if (!StatusSelect.getValue().equals("CompleteUnpaid")) {
          selfRefrence.setEndDate("current");
          new ReportsController().removeJob(selfRefrence.getId());
        }
        if(StatusSelect.getValue().equals("CompletedPaid")){
          new ReportsController().addIcome(selfRefrence.getBill().generateProfit(), selfRefrence.getId());
        }
      }
    }

    selfRefrence.setStatus(StatusSelect.getValue());
    selfRefrence.setDetails(JobNotes.getText());

    //Update the DAO
    if (!jc.updateJob(selfRefrence)) {
      System.out.println("Fail");
      return;
    }
    //Update fields
    setDetails();
    //Toggle visibility of edits
    EditSelect.setVisible(true);
    EditOptions.setVisible(false);
    EditSelect.toFront();
    UpdateBill();
  }

  public void Clear() {
    setDetails();
    EditSelect.setVisible(true);
    EditOptions.setVisible(false);
    EditSelect.toFront();
  }

  //Details Update
  public void DetailsUpdate() {
    //Toggle the button options
    EditSelect.setVisible(false);
    EditOptions.setVisible(true);
    EditOptions.toFront();

    //Toggle the edit fields
    JobName.setVisible(false);
    JobNameEdit.setVisible(true);
    JobNameEdit.setDisable(false);
    JobNameEdit.toFront();

    Status.setVisible(false);
    StatusSelect.setVisible(true);
    StatusSelect.toFront();

    JobNotes.setEditable(true);
  }

  public void NoEdit() {
    NewLaborCharge.setVisible(false);
    NewLaborCharge.setDisable(true);
    NewPartsCharge.setVisible(false);
    NewPartsCharge.setDisable(true);
  }

  public void UpdateBill() {
    //Set Status
    switch (selfRefrence.getStatus()) {
      case "CompletedUnpaid":
        BillStatus.setText("UNPAID");
        NoEdit();
        break;
      case "CompletedPaid":
        BillStatus.setText("PAID");
        NoEdit();
        break;
      default:
        BillStatus.setText("INCOMPLETE");
        break;
    }
    //Add Labor Charges
    if (selfRefrence.getBill().getLaborCharges() != null) {
      //Clear the list
      LaborCharges.getChildren().clear();
      for (LaborChargeDAO charge : selfRefrence.getBill().getLaborCharges()) {
        FXMLLoader loader = new FXMLLoader(
          Main.class.getResource("Scenes/Jobs/BillCharge.fxml")
        );
        try {
          Node content = loader.load();
          BillCharge controller = (BillCharge) loader.getController();
          controller
            .getFirstValue()
            .setText(String.valueOf(charge.getHours()) + "Hrs ");
          controller
            .getSecondValue()
            .setText("$" + String.valueOf(charge.getRate()) + "/Hr");
          controller.setParent(this);
          controller.setLc(charge);
          LaborCharges.getChildren().add(content);
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    LaborTotal.setText(selfRefrence.getBill().getLaborCost());
    //Add Parts Charge
    if (selfRefrence.getBill().getPartCharges() != null) {
      //Clear the list
      PartCharges.getChildren().clear();
      for (PartChargeDAO charge : selfRefrence.getBill().getPartCharges()) {
        FXMLLoader loader = new FXMLLoader(
          Main.class.getResource("Scenes/Jobs/BillCharge.fxml")
        );
        try {
          Node content = loader.load();
          BillCharge controller = (BillCharge) loader.getController();
          controller.getFirstValue().setText(charge.getPartNumber());
          controller
            .getSecondValue()
            .setText(
              String.valueOf(charge.getQuanity()) +
              "x " +
              String.valueOf(charge.getCharge())
            );
          controller.setParent(this);
          controller.setPc(charge);
          PartCharges.getChildren().add(content);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    PartsTotal.setText(selfRefrence.getBill().getPartsCost());

    //Set deliveryCost
    DeliveryCost.setText(
      (String.valueOf(selfRefrence.getBill().getDeliveryCost()))
    );
    //Set Total
    BillTotal.setText(String.valueOf(selfRefrence.getBill().getBillTotal()));
  }

  public boolean Add() {
    //Labor charge Add
    if (LeftContent.equals("LaborChargeEdit")) {
      //Validate the fields
      boolean fail = false;
      if (HoursField.getText() == null || HoursField.getText().length() < 1) {
        HoursError.setText("Hours cannot be blank");
        HoursError.setVisible(true);
        fail = true;
      } else {
        if (!Pattern.matches("(\\-?\\d*\\.?\\d+)", HoursField.getText())) {
          HoursError.setText("Hours must be a number");
          HoursError.setVisible(true);
          fail = true;
        } else {
          HoursError.setVisible(false);
        }
      }
      if (
        HourlyRateField.getText() == null ||
        HourlyRateField.getText().length() < 1
      ) {
        RateError.setText("Hourly rate cannot be blank");
        RateError.setVisible(true);
        fail = true;
      } else {
        if (!Pattern.matches("(\\-?\\d*\\.?\\d+)", HourlyRateField.getText())) {
          RateError.setText("Rate must be a number");
          RateError.setVisible(true);
          fail = true;
        } else {
          RateError.setVisible(false);
        }
      }
      if (fail) {
        return false;
      }
      LaborChargeDAO charge = new LaborChargeDAO();
      charge.setRate(Double.parseDouble(HourlyRateField.getText()));
      charge.setHours(Double.parseDouble(HoursField.getText()));
      charge.setCost();
      if (charge.getHours() <= 0) {
        if (
          new WarningSender()
            .sendWarning(
              "Number of hours set is 0 if you are trying to update a charge this will delete the charge if you are trying to make a new charge none will be generated",
              ""
            )
        ) {
          return true;
        }
        return false;
      }
      addLaborCharge(charge);
      return true;
    }
    //Parts charge add

    //Check if the part selection is valid
    if (
      !PartNumberComboBox.getItems().contains(PartNumberComboBox.getValue())
    ) {
      new WarningSender()
        .sendWarning(
          "PartNumber you entered is not listed please select a part number from the drop down",
          "acknowledgement"
        );
      return false;
    }

    String partnumber = (PartNumberComboBox.getValue());

    //Check if the quanity is an int
    if (
      PartChargeQuanity.getText() == null ||
      PartChargeQuanity.getText().length() < 1 ||
      !Pattern.matches("(\\-?\\d*)", PartChargeQuanity.getText())
    ) {
      new WarningSender()
        .sendWarning("Part quanity is not a whole number", "acknowledgment");
      return false;
    }
    int numbertoAdd = Integer.parseInt(PartChargeQuanity.getText());
    //If updating subtract the existing number of parts in the charge
    //from the amount selected
    if (isUpdate) {
      numbertoAdd = numbertoAdd - partChargeToUpdate.getQuanity();
      if (numbertoAdd <= 0) return true;
    }

    //Inventory database call to get the price and update the charge
    InventoryController ic = new InventoryController();
    PartDAO part = ic.findByPartNumber(partnumber);
    //Check the instock on the part
    if (part.getInStock() < 1) {
      //If the part has no stock send a warning
      new WarningSender()
        .sendWarning(
          "Cannot add part: No of this part in stock ",
          "acknowledgement"
        );
      return false;
    }

    if (part.getInStock() < numbertoAdd) {
      new WarningSender()
        .sendWarning(
          "Quantity selected is greater than the amount in stock. You have " +
          part.getInStock() +
          " in stock;",
          "acknowledgement"
        );
      return false;
    }
    part.setInStock(part.getInStock() - numbertoAdd);

    //Update the inventory count
    //Check if there are any promotional price
    ArrayList<PartChargeDAO> charges = new ArrayList<>();
    if (part.getPromotions() != null && part.getPromotions().size() >= 1) {
      //Add the promotional price to the charge and subtract from the promotional instock
      ArrayList<PromotionalPriceDAO> allPromotions = (ArrayList<PromotionalPriceDAO>) part
        .getPromotions()
        .clone();
      checkpromotions:for (PromotionalPriceDAO promo : allPromotions) {
        if (numbertoAdd < 1) {
          break checkpromotions;
        }
        //For each promotion add as many as you can
        PartChargeDAO currentCharge = new PartChargeDAO();
        currentCharge.setPartNumber(partnumber);
        //If to many to for just this promo
        if (numbertoAdd >= promo.getQuanity()) {
          currentCharge.setQuanity(promo.getQuanity());
          numbertoAdd = numbertoAdd - promo.getQuanity();
          part.getPromotions().remove(promo);
        } else {
          currentCharge.setQuanity(numbertoAdd);
          part.getPromotions().remove(promo);
          promo.setQuanity(promo.getQuanity() - numbertoAdd);
          part.getPromotions().add(promo);
          numbertoAdd = 0;
        }
        currentCharge.setCharge(promo.getCharge());
        currentCharge.setCost(promo.getPurchasePrice());
        charges.add(currentCharge);
      }
    }
    if (!(numbertoAdd < 1)) {
      PartChargeDAO toAdd = new PartChargeDAO();
      toAdd.setQuanity(numbertoAdd);
      toAdd.setCost(part.getPrice());
      toAdd.setCharge(part.getCharge());
      toAdd.setPartNumber(partnumber);
      charges.add(toAdd);
    }

    for (PartChargeDAO dao : charges) {
      selfRefrence.getBill().addPartCharge(dao);
    }
    selfRefrence.getBill().updateCost();
    jc.updateJob(selfRefrence);
    ic.updatePart(part);
    UpdateBill();
    Cancel();
    return true;
  }

  public void addLaborCharge(LaborChargeDAO dao) {
    selfRefrence.getBill().addLaborCharge(dao);
    jc.updateJob(selfRefrence);
    selfRefrence.getBill().updateCost();
    UpdateBill();
    Cancel();
  }

  public void setPartsBox(ObjectId machineID) {
    EquipmentDAO equipment = ec.findByID(selfRefrence.getEquipment());
    ObservableList parts = FXCollections.observableList(equipment.getParts());
    PartNumberComboBox.setItems(parts);
  }

  boolean isUpdate = false;
  LaborChargeDAO laborChargeToUpdate;
  PartChargeDAO partChargeToUpdate;

  public void setIsUpdate(boolean isUpdate) {
    this.isUpdate = isUpdate;
  }

  public void LaborChargeUpdate() {
    //if the update goes thru remove the old charge and update again
    if (Add()) {
      selfRefrence.getBill().removeLaborCharge(laborChargeToUpdate);
      selfRefrence.getBill().updateCost();
      jc.updateJob(selfRefrence);
      UpdateBill();
      Cancel();
    }
  }

  public void PartChargeUpdate() {
    if (Add()) {
      //Determine if the number of the part used is less than the origional amount
      if (
        Integer.parseInt(PartChargeQuanity.getText()) <
        partChargeToUpdate.getQuanity()
      ) {
        //Create a new charge for the lower quanity and add the remainder back into inventory
        //Determine how many to remove
        int toInventory =
          partChargeToUpdate.getQuanity() -
          Integer.parseInt(PartChargeQuanity.getText());
        InventoryController ic = new InventoryController();
        PartDAO toUpdate = ic.findByPartNumber(
          partChargeToUpdate.getPartNumber()
        );
        toUpdate.AddNewPromotion(
          new PromotionalPriceDAO(
            partChargeToUpdate.getCost(),
            toInventory,
            partChargeToUpdate.getCharge()
          )
        );
        ic.updatePart(toUpdate);
        selfRefrence.getBill().removePartCharge(partChargeToUpdate);
        partChargeToUpdate.setQuanity(
          partChargeToUpdate.getQuanity() - toInventory
        );
        if (partChargeToUpdate.getQuanity() > 0) {
          selfRefrence.getBill().addPartCharge(partChargeToUpdate);
        }
        selfRefrence.getBill().updateCost();
        jc.updateJob(selfRefrence);
        UpdateBill();
        Cancel();
      }
    }
  }

  public void setUpdate(PartChargeDAO pc) {
    partChargeToUpdate = pc;
    PartNumberComboBox.setValue(pc.getPartNumber());
    PartNumberComboBox.setDisable(true);

    PartChargeQuanity.setText(Integer.toString(pc.getQuanity()));
  }

  public void setUpdate(LaborChargeDAO lc) {
    laborChargeToUpdate = lc;
    HourlyRateField.setText(Double.toString(lc.getRate()));
    HoursField.setText(Double.toString(lc.getHours()));
  }

  public void NewPartsCharge() {
    LeftContent = "PartsChargeEdit";
    //Set StackPanePosition

    PartChargeAdd.setVisible(!isUpdate);
    PartChargeUpdate.setVisible(isUpdate);
    if (isUpdate) {
      PartChargeUpdate.toFront();
    } else {
      PartChargeAdd.toFront();
    }

    setPartsBox(selfRefrence.getMachineIds());
    UpdateContent();
  }

  public void NewLaborCharge() {
    LeftContent = "LaborChargeEdit";
    HoursError.setVisible(false);
    RateError.setVisible(false);
    //Set StackPane Position
    AddLaborCharge.setVisible(!isUpdate);
    LaborChargeUpdate.setVisible(isUpdate);
    if (isUpdate) {
      LaborChargeUpdate.toFront();
    } else {
      AddLaborCharge.toFront();
    }
    UpdateContent();
  }

  public void Cancel() {
    LeftContent = "Details";
    PartNumberComboBox.setDisable(false);
    isUpdate = false;
    HourlyRateField.clear();
    HoursField.clear();
    HoursError.setVisible(false);
    RateError.setVisible(false);

    UpdateContent();
  }

  public void setSelfRefrence(JobDAO refrence) {
    this.selfRefrence = refrence;
    setDetails();
    UpdateBill();
  }
}
