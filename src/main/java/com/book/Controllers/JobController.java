package com.book.Controllers;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;

import com.book.DAOs.BillDAO;
import com.book.DAOs.JobDAO;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.internal.Iterables;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.text.html.HTMLDocument.Iterator;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class JobController extends DatabaseConnection {

  MongoCollection<JobDAO> collection;
  DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy");

  public JobController() {
    collection = database.getCollection("Jobs", JobDAO.class);
  }

  public void printAll() {
    collection
      .find()
      .forEach(d -> {
        System.out.println(d.toString());
      });
  }

  public void makeNew(
    String JobName,
    ObjectId machineId,
    ObjectId equipment,
    String customerName
  ) {
    JobDAO job = new JobDAO();
    job.setJobName(JobName);
    job.setMachineIds(machineId);
    job.setEquipment(equipment);
    job.setEndDate("Current");
    job.setStartDate(df.format(LocalDate.now()));
    job.setStatus("Waiting");
    job.setDetails("");
    job.setCreated(LocalDate.now());
    job.setCustomerName(customerName);

    job.setBill(new BillDAO().generateBill());
    collection.insertOne(job);
  }

  public ArrayList<JobDAO> getList(int size, int skip) {
    ArrayList<JobDAO> output = new ArrayList<>();
    FindIterable<JobDAO> result = collection
      .find()
      .sort(descending("created"))
      .limit(size)
      .skip(skip);
    result.forEach(d -> {
      output.add(d);
    });
    return output;
  }

  public ArrayList<JobDAO> getRecent() {
    ArrayList<JobDAO> recent = new ArrayList<>();
    //If there are 10 or more jobs with an endDate of current add them

    collection
      .find(eq("endDate", "Current"))
      .forEach(j -> {
        recent.add(j);
      });
    if (recent.size() < 10) {
      Iterable<JobDAO> sorted = collection
        .find(ne("endDate", "Current"))
        .sort(descending("created"));
      java.util.Iterator<JobDAO> results = sorted.iterator();
      while (recent.size() <= 10 && results.hasNext()) {
        recent.add(results.next());
      }
    }
    return recent;
  }

  public ArrayList<JobDAO> getJobHistory(
    String customerName,
    int size,
    int skip
  ) {
    ArrayList<JobDAO> history = new ArrayList<>();
    collection
      .find(eq("customerName", customerName))
      .sort(descending("created"))
      .limit(size)
      .skip(skip)
      .forEach(c -> {
        history.add(c);
      });
    return history;
  }

  public ObjectId createAndAdd(
    String jobName,
    ObjectId machineId,
    ObjectId equipmentId,
    String customerName,
    String details
  ) {
    JobDAO dao = new JobDAO();
    dao.setCustomerName(customerName);
    dao.setJobName(jobName);
    dao.setMachineIds(machineId);
    dao.setEquipment(equipmentId);
    dao.setDetails(details);
    dao.setStartDate(df.format(LocalDate.now()));
    dao.setCreated(LocalDate.now());
    dao.setEndDate("current");
    dao.setStatus("waiting");
    dao.setBill(new BillDAO().generateBill());

    InsertOneResult returnValue = collection.insertOne(dao);
    return returnValue.getInsertedId().asObjectId().getValue();
  }

  public boolean updateJob(JobDAO update) {
    JobDAO isUpdated = collection.findOneAndReplace(
      eq("_id", update.getId()),
      update
    );
    if (isUpdated == null) {
      return false;
    }
    return true;
  }

  public JobDAO getMostRecent(ArrayList<ObjectId> jobs) {
    JobDAO result = new JobDAO();

    collection
      .find(in("_id", jobs))
      .forEach(e -> {
        if (
          result.getCreated() == null ||
          result.getCreated().isBefore(e.getCreated())
        ) {
          result.copy(e);
        }
      });
    System.out.println(result.toString());
    return result;
  }

  public ArrayList<JobDAO> getCompleted() {
    ArrayList<JobDAO> toReturn = new ArrayList<>();
    collection
      .find(or(eq("status", "CompletedUnpaid"), eq("status", "CompletedPaid")))
      .forEach(e -> {
        toReturn.add(e);
      });
    return toReturn;
  }

  public int getOpen() {
    return collection
      .find(nor(eq("status", "CompletedUnpaid"), eq("status", "CompletedPaid")))
      .iterator()
      .available();
  }
}
