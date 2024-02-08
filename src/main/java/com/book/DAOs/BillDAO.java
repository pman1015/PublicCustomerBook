package com.book.DAOs;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BillDAO {

  String status;
  ArrayList<LaborChargeDAO> LaborCharges;
  ArrayList<PartChargeDAO> PartCharges;
  double billTotal;
  double deliveryCost;

  public BillDAO generateBill() {
    BillDAO b = new BillDAO();
    b.setStatus("Incomplete");
    b.setLaborCharges(new ArrayList<LaborChargeDAO>());
    b.setPartCharges(new ArrayList<PartChargeDAO>());
    b.setBillTotal(0);
    b.setDeliveryCost(0.0);
    return b;
  }

  public String[][] makePartData() {
    String[][] toReturn = new String[PartCharges.size()][4];
    for (int count = 0; count < toReturn.length; count++) {
      toReturn[count] = PartCharges.get(count).makeData();
    }
    return toReturn;
  }

  public String[][] makeLaborData() {
    String[][] toReturn = new String[LaborCharges.size()][4];
    for (int count = 0; count < toReturn.length; count++) {
      toReturn[count] = LaborCharges.get(count).makeData();
    }
    return toReturn;
  }

  public void addLaborCharge(LaborChargeDAO c) {
    LaborCharges.add(c);
  }

  public void addPartCharge(PartChargeDAO p) {
    PartCharges.add(p);
  }

  public String getLaborCost() {
    double cost = 0.00;
    if (LaborCharges != null && LaborCharges.size() > 0) {
      for (LaborChargeDAO charge : LaborCharges) {
        cost = cost + charge.cost;
      }
    }
    return String.valueOf(cost);
  }

  public String getPartsCost() {
    double cost = 0.00;
    if (PartCharges != null && PartCharges.size() > 0) {
      for (PartChargeDAO charge : PartCharges) {
        cost += charge.getCharge() * charge.getQuanity();
      }
    }
    return String.valueOf(cost);
  }

  public void updateCost() {
    billTotal =
      Double.valueOf(getLaborCost()) +
      Double.valueOf(getPartsCost()) +
      deliveryCost;
  }

  public void removePartCharge(PartChargeDAO toRemove) {
    PartCharges.remove(toRemove);
  }

  public void removeLaborCharge(LaborChargeDAO toRemove) {
    LaborCharges.remove(toRemove);
  }
  public double generateProfit(){
    double profit = 0;
    for(LaborChargeDAO labor : LaborCharges){
      profit += labor.getCost();
    }
    for(PartChargeDAO part : PartCharges){
      profit += part.generateProfit();
    }
    profit += deliveryCost;

    return profit;
  }
}
