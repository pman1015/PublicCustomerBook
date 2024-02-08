package com.book.DAOs;

import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PartDAO {

  String partNumber;
  int inStock;
  double charge;
  double price;
  ArrayList<ObjectId> equipment;
  ObjectId id;
  String url;
  String expenseCategory;
  ArrayList<PromotionalPriceDAO> promotions;

  public PartDAO(String partNumber, String price, String charge) {
    this.partNumber = partNumber;
    this.price = Double.parseDouble(price);
    this.charge = Double.parseDouble(charge);
    inStock = 0;
    equipment = new ArrayList<>();
  }

  public void addEquipment(ObjectId e) {
    if (equipment != null) {
      equipment.add(e);
    } else {
      equipment = new ArrayList<>();
      equipment.add(e);
    }
  }

  public void AddNewPromotion(PromotionalPriceDAO dao) {
    if (promotions == null) {
      promotions = new ArrayList<>();
    }
    promotions.add(dao);
    inStock = dao.quanity + inStock;
  }

  public PromotionalPriceDAO updatePromotion(
    PromotionalPriceDAO dao,
    String newPrice,
    String newCharge,
    String newQuanity
  ) {
    promotions.remove(dao);
    PromotionalPriceDAO newDAO = new PromotionalPriceDAO(
      Double.valueOf(newPrice),
      Integer.valueOf(newQuanity),
      Double.valueOf(newCharge)
    );
    promotions.add(newDAO);
    return newDAO;
  }

  public void priceChange() {
    //Calculate the amount of stock at the old price
    int totalOnPromo = 0;
    if (promotions != null) {
      for (PromotionalPriceDAO promo : promotions) {
        totalOnPromo = totalOnPromo + promo.getQuanity();
      }
    } else {
      promotions = new ArrayList<>();
    }
    //Add the new promo
    promotions.add(
      new PromotionalPriceDAO(price, inStock - totalOnPromo, charge)
    );
  }
  public void removePromo(PromotionalPriceDAO dao){
    promotions.remove(dao);
    inStock = inStock - dao.getQuanity();
  }
}
