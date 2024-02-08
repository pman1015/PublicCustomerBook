package com.book.DAOs;

import java.time.LocalDate;
import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReportDAO {

  ObjectId id;
  ArrayList<String> expenseCategories;
  ArrayList<IncomeDAO> incomeEntries;
  double weeklyIncome;
  double monthlyIncome;
  double yearlyIncome;

  int newJobs;
  int openJobs;
  int completedJobs;

  double expenseTotals;
  double expenseYTD;

  ArrayList<ExpenseDAO> expenses;
  ArrayList<CompletedJobDAO>openJobsList;
  ArrayList<CompletedJobDAO> workCompleted;

  public boolean inUse(String name) {
    if (expenseCategories != null && !expenseCategories.contains(name)) {
      return false;
    }
    return true;
  }
  public void removeJob(ObjectId id){
    ArrayList<IncomeDAO> prunnedList =(ArrayList<IncomeDAO>) incomeEntries.clone();
    for(IncomeDAO dao : incomeEntries){
      if(dao.getRefrence().equals(id)){
        prunnedList.remove(dao);
        break;
      }
    }
    incomeEntries = prunnedList;
  }

  public ReportDAO makeBlank() {
    expenseCategories = new ArrayList<>();
    incomeEntries = new ArrayList<>();
    workCompleted = new ArrayList<>();
    expenses = new ArrayList<>();
    expenseCategories = new ArrayList<>();
    return this;
  }

  public void updateIncome(LocalDate currentTime) {
    if (incomeEntries == null || incomeEntries.size() < 1) return;
    LocalDate weekOld = currentTime.minusDays(7);
    weeklyIncome = 0;
    monthlyIncome = 0;
    yearlyIncome = 0;
    completedJobs = 0;
    ArrayList<IncomeDAO> pruned = (ArrayList<IncomeDAO>) incomeEntries.clone();
    for (IncomeDAO dao : incomeEntries) {
      //if the entry is from a week or less ago add it to the new weeklyIncome
      if (dao.getCompleted().isAfter(weekOld)) {
        weeklyIncome = weeklyIncome + dao.getAmount();
        completedJobs++;
      }
      //If the income is from the month then add it to monthly
      if (dao.getCompleted().getMonth().equals(currentTime.getMonth())) {
        monthlyIncome = monthlyIncome + dao.getAmount();
      }
      //If the income is from this year add it to yearly if not remove it from the list
      if (dao.getCompleted().getYear() == (currentTime.getYear())) {
        yearlyIncome = yearlyIncome + dao.getAmount();
      } else {
        pruned.remove(dao);
      }
    }
    incomeEntries = pruned;
  }
}
