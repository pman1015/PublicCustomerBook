package com.book.Utilities.Invoice;

import com.book.Controllers.CustomerController;
import com.book.DAOs.BillDAO;
import com.book.DAOs.CustomerDAO;
import com.book.DAOs.JobDAO;
import com.book.DAOs.PartChargeDAO;
import com.book.Main;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Table;
import com.spire.doc.collections.CellCollection;
import com.spire.doc.fields.Field;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class InvoiceGenerator {

  Document doc;

  public InvoiceGenerator(JobDAO job) {
    BillDAO dao = job.getBill();
    DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    //Generate the document
    doc = new Document();

    //load the template
    try {
      doc.loadFromStream(
        Main.class.getResource("Files/InvoiceTemplate.docx").openStream(),
        FileFormat.Docx
      );
    } catch (IOException e) {
      System.out.println(e);
      e.printStackTrace();
      return;
    }

    CustomerDAO customer = new CustomerController()
      .getDAO(job.getCustomerName());
    //Set the top fields
    doc.replace("#CustomerName", customer.getName(), true, true);
    doc.replace("#CustomerAddress", customer.getAddress(), true, true);
    doc.replace(
      "#CustomerPhoneNumber",
      customer.getPhoneNumber() == null ? "" : customer.getPhoneNumber(),
      true,
      true
    );
    doc.replace("#InvoiceNO", job.getId().toString(), true, true);
    doc.replace("#Date", df.format(LocalDate.now()), true, true);
    doc.replace("#JobName", job.getJobName(), true, true);
    doc.replace("#BillStatus", job.getStatus(), true, true);

    //Add Totals
    doc.replace("#PartsTotal", "$" + dao.getPartsCost(), true, true);
    doc.replace("#LaborTotal", "$" + dao.getLaborCost(), true, true);
    doc.replace("#OtherFees", "$" + dao.getDeliveryCost(), true, true);
    doc.replace("#BillTotal", "$" + dao.getBillTotal(), true, true);
    //Get the tables
    Table partsTable = doc.getSections().get(0).getTables().get(1);
    Table laborTable = doc.getSections().get(0).getTables().get(2);
    Table feesTable = doc.getSections().get(0).getTables().get(3);

    //Write the data to the table
    writeData(partsTable, dao.makePartData());
    writeData(laborTable, dao.makeLaborData());

    String[][] fees = {
      new String[] {
        "Pickup / Delivery",
        "$" + dao.getDeliveryCost(),
        "$" + dao.getDeliveryCost(),
      },
    };
    writeData(feesTable, fees);

    //Save the file
    doc.saveToFile(
      "C:\\Users\\" +
      System.getProperty("user.name") +
      "\\Downloads\\Invoice.pdf",
      FileFormat.PDF
    );
  }

  private static void writeData(Table table, String[][] data) {
    if (data.length > table.getRows().getCount() - 2) {
      addRows(table, data.length - 1);
    }
    writeDataToTable(table, data);
  }

  private static void writeDataToTable(Table table, String[][] data) {
    for (int row = 0; row < data.length; row++) {
      for (int col = 0; col < data[row].length; col++) {
        table
          .getRows()
          .get(row + 1)
          .getCells()
          .get(col)
          .getParagraphs()
          .get(0)
          .setText(data[row][col]);
      }
    }
  }

  private static void addRows(Table table, int rowNum) {
    for (int i = 0; i < rowNum; i++) {
      //insert specific number of rows by cloning the second row
      table.getRows().insert(2 + i, table.getRows().get(1).deepClone());
      //update formulas for Total
      for (Object object : table
        .getRows()
        .get(2 + i)
        .getCells()
        .get(3)
        .getParagraphs()
        .get(0)
        .getChildObjects()) {
        if (object instanceof Field) {
          Field field = (Field) object;
          field.setCode(String.format("=B%d*C%d\\# \"0.00\"", 3 + i, 3 + i));
        }
        break;
      }
    }
  }
  
}
