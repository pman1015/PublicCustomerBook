package com.book.DAOs;

import java.time.LocalDate;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor @AllArgsConstructor
public class IncomeDAO {
    LocalDate completed;
    double amount;
    ObjectId refrence;
    
}
