package com.book.DAOs;

import com.book.Models.Jobs.Job;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

@Getter
@Setter
@ToString
public class JobDAO implements Cloneable {

  ObjectId id;
  String jobName;
  String startDate;
  String endDate;
  String status;
  String details;
  ObjectId machineIds;
  ObjectId equipment;
  BillDAO bill;
  LocalDate created;
  String customerName;

  public void toJob(Job j) {
    if (j.getType().equals("JobPage")) {
      j.setUp(
        jobName,
        customerName,
        status,
        startDate + "-" + endDate
      );
    } else {
      j.setUp(jobName, customerName, status);
    }
  }

  public JobDAO clone() throws CloneNotSupportedException {
    return (JobDAO) super.clone();
  }
  public void copy(JobDAO j){
    id =j.getId();
    jobName = j.getJobName();
    startDate = j.getStartDate();
    endDate = j.getEndDate();
    status = j.getStatus();
    details = j.getDetails();
    machineIds = j.getMachineIds();
    equipment = j.getEquipment();
    bill = j.getBill();
    created = j.getCreated();
    customerName = j.getCustomerName();
  }
}
