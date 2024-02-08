package com.book.Models.Customer;

import com.book.Loaders.Customer.CustomersLoader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Customer implements Comparable<Customer>{
    
    @FXML
    Text Name;

    @FXML
    Text Alias;
    
    @FXML
    Text Progress;

    int currentProgress;
    String Customername;
    String AliasName;
    CustomersLoader parentController;
    String compareType = "Name";
    Node self;
    
    public Customer(){
        currentProgress = 3;
    }
    @FXML
    public void initialize(){
        Name.setText("Test");
    }
    public void setParent(CustomersLoader parent){
        parentController = parent;
    }
    public void updateCard(int currentProgress, String CustomerName , String AliasName ){
        this.currentProgress = currentProgress;
        this.AliasName = AliasName;
        this.Customername = CustomerName;

        Progress.setText(currentProgress+"/5");
        Name.setText(CustomerName);
        Alias.setText(AliasName);
    }

    public void MoreInfo(){
       parentController.setMoreInfo(Name.getText());
    }
    public void setCompareValue(String type){
        compareType = type;
    }
    public Node getSelf(){
        return self;
    }
    public void setSelf(Node n){
        self = n;
    }

    @Override 
    public int compareTo(Customer o) {
        //If setType is not set return 0 for all
        if(compareType == null){
            return 0;
        }else{
            switch(compareType){
                case("Name"):
                    return Customername.compareTo(o.Customername);
                case("Alias"):
                     return AliasName.compareTo(o.AliasName);
                case("Rating"):
                     return ((Integer)currentProgress).compareTo((Integer)o.currentProgress);
                default:
                    return 0;
            }
        }       
    }
}
