package com.book.DAOs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@NoArgsConstructor
@Setter
@Getter
public class CompletedJobDAO {

  LocalDate dateCompleted;
  ObjectId refrence;

  public CompletedJobDAO(LocalDate dateCompleted, ObjectId refrence){
    this.dateCompleted = dateCompleted;
    this.refrence = refrence;
  }
}
