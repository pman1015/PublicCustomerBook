package com.book.DAOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class PromotionalPriceDAO {
    double purchasePrice;
    int quanity;
    double charge;
}
