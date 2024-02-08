package com.book.Utilities;

import com.book.Main;
import com.book.Models.Warnings;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WarningSender {

  public boolean sendWarning(String message, String type) {
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("Warning.fxml"));

    AnchorPane warning;
    try {
      warning = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    Warnings controller = loader.getController();
    controller.getMessage().setText(message);

    //Set the warning type
    setType(type, controller);

    Scene scene = new Scene(warning);
    scene.setFill(Color.web("#ffffff00"));
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.sizeToScene();
    stage.setResizable(false);
    stage.initStyle(StageStyle.TRANSPARENT);
    stage.showAndWait();
    return controller.getReturnValue();
  }

  void setType(String type, Warnings controller) {
    if (type.equals("acknowledgement")) {
      controller.singleButton();
    } else {
      controller.twoButton();
    }
  }
}
