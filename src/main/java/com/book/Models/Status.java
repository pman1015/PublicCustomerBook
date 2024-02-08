package com.book.Models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Status {

  Circle Indicator;
  String State;
  String[] StatusTypes = {
    "Waiting",
    "InProgress",
    "AwatingParts",
    "CompletedUnpaid",
    "CompletedPaid",
  };

  public Status(String State, Circle Indicator) {
    this.Indicator = Indicator;
    this.State = State;
  }

  public String[] getTypes() {
    return StatusTypes;
  }

  public Circle getIndicator() {
    if (State == null) {
      Indicator.setFill(Paint.valueOf("#ffffff00"));
    }
    switch (State) {
      case "Waiting":
        Indicator.setFill(Paint.valueOf("#0043ce"));
        break;
      case "InProgress":
        Indicator.setFill(Paint.valueOf("#f1c21b"));
        break;
      case "AwatingParts":
        Indicator.setFill(Paint.valueOf("#ff832b"));
        break;
      case "CompletedUnpaid":
        Indicator.setFill(Paint.valueOf("#da1e28"));
        break;
      case "CompletedPaid":
        Indicator.setFill(Paint.valueOf("#46ff1f"));
        break;
      default:
        Indicator.setFill(Paint.valueOf("#ffffff00"));
        break;
    }

    return Indicator;
  }
}
