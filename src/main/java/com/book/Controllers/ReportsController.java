package com.book.Controllers;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;

import com.book.DAOs.CompletedJobDAO;
import com.book.DAOs.IncomeDAO;
import com.book.DAOs.ReportDAO;
import com.mongodb.client.MongoCollection;
import java.time.LocalDate;
import java.util.ArrayList;
import org.bson.types.ObjectId;

public class ReportsController extends DatabaseConnection {

  MongoCollection<ReportDAO> collection;

  public ReportsController() {
    collection = database.getCollection("Reports", ReportDAO.class);
    inititalReport();
  }

  public void printAll() {
    collection
      .find()
      .forEach(e -> {
        System.out.println(e.toString());
      });
  }

  public ReportDAO getExpenses() {
    return collection.find().first();
  }

  public ReportDAO getReport() {
    inititalReport();
    ReportDAO dao = collection.find().first();
    dao = updateIncome(dao);
    dao = updateJobs(dao);
    return dao;
  }

  public void updateExpenses(ReportDAO update) {
    collection.findOneAndReplace(eq("_id", update.getId()), update);
  }

  private void inititalReport() {
    if (collection.find().iterator().available() < 1) {
      //Create a blank Report
      collection.insertOne(new ReportDAO().makeBlank());
    }
  }

  public void addIcome(double amount, ObjectId jobId) {
    ReportDAO dao = collection.find().first();
    dao.getIncomeEntries().add(new IncomeDAO(LocalDate.now(), amount, jobId));
    updateIncome(dao);
  }

  public void newJob(ObjectId jobId) {
    ReportDAO dao = collection.find().first();
    if (dao.getOpenJobsList() == null) {
      dao.setOpenJobsList(new ArrayList<>());
    }
    dao.getOpenJobsList().add(new CompletedJobDAO(LocalDate.now(), jobId));
    updateExpenses(dao);
  }
  public void removeJob(ObjectId id){
    ReportDAO report = collection.find().first();
    report.removeJob(id);
    updateIncome(report);
  }

  private ReportDAO updateIncome(ReportDAO dao) {
    dao.updateIncome(LocalDate.now());
    updateExpenses(dao);
    return dao;
  }
  

  private ReportDAO updateJobs(ReportDAO dao) {
    //set New Jobs
    if (dao.getOpenJobsList() == null) {
      dao.setOpenJobsList(new ArrayList<>());
    }

    ArrayList<CompletedJobDAO> prunedList = (ArrayList<CompletedJobDAO>) dao
      .getOpenJobsList()
      .clone();
    for (CompletedJobDAO job : dao.getOpenJobsList()) {
      if (job.getDateCompleted().isBefore(LocalDate.now().minusDays(7))) {
        prunedList.remove(job);
        System.out.println(job.toString());
      }
    }
    dao.setNewJobs(prunedList.size());
    dao.setOpenJobsList(prunedList);

    //update the pending jobs
    dao.setOpenJobs(new JobController().getOpen());
    updateExpenses(dao);
    return dao;
  }
}
