package com.book.Models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.book.Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Expenses {
    private String ExpenseCardPath = "Scenes/Home/HomePageExpenseCard.fxml";

    private double weeklyTotal;
    private double ytdTotal;

    ArrayList<String> categories;
    ArrayList<Double> values;

    public Expenses(){
        categories = new ArrayList<>();
        values = new ArrayList<>();

        categories.add("Blades");
        values.add(129.33);

        weeklyTotal = 0.00;
        ytdTotal = 99.99;
    }

    public double getWeekly(){
        return weeklyTotal;
    }

    public double getYTD(){
        return ytdTotal;
    }

    public Node getCards(VBox container) throws IOException{
        int pos = 0;
        for(String cat : categories){
            Pane ExpenseCard = FXMLLoader.load(Main.class.getResource(ExpenseCardPath));
            List<Node> tags =  ((HBox)(ExpenseCard.getChildren().get(0))).getChildren();
            //Set the category name
            ((Text)tags.get(0)).setText(cat);
            //Set the value
            ((Text)tags.get(1)).setText("$"+values.get(pos).toString());
            container.getChildren().add(pos, ExpenseCard);
            pos++;
        }
        return(container);
    }
}
