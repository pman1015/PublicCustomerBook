package com.book.DAOs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PartChargeDAO {

  double cost;
  String partNumber;
  int quanity;
  double charge;

  public double getTotal(){
    return charge*quanity;
  }
  public String[] makeData(){

   return  new String[]{partNumber,String.valueOf(quanity),"$"+String.valueOf(charge),"$"+String.valueOf(getTotal())};
  }
  public double generateProfit(){
    return (charge-cost)*quanity;
  }
}
