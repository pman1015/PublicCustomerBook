package com.book.Models.Equipment;

import com.book.DAOs.PartDAO;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class EquipmentDetailPartCard {

    @FXML
    Text PartNumber;
    @FXML
    Text NumberInStock;

   PartDAO self;
    @FXML
    public void initialize(){

    }
    public void setPartDAO(PartDAO self){
        this.self = self;
        PartNumber.setText(self.getPartNumber());
        NumberInStock.setText(Integer.toString(self.getInStock()));
    }

    
}
