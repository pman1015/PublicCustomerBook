package com.book;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Header {

  public void CloseWindow(ActionEvent e) {
    Stage s = (Stage) ((Button) e.getSource()).getScene().getWindow();
    s.close();
  }
}
