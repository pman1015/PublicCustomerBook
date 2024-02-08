package com.book.DAOs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Getter
@Setter
@ToString
public class LaborChargeDAO {

  double hours;
  double rate;
  double cost;
  

  public void setCost() {
    cost = hours * rate;
  }
 
  public String[] makeData(){
    return new String[]{"$"+String.valueOf(rate)+"/Hr", String.valueOf(hours), "$"+String.valueOf(cost)};
  }
}
