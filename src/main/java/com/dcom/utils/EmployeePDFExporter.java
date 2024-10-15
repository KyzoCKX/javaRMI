package com.dcom.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;
import com.dcom.dataModel.Employee;
import java.util.Collections;
import java.util.Comparator;

public class EmployeePDFExporter implements Runnable {
    private List<Employee> employees;  
    private String filePath;           

    private static final Font defaultFont = new Font(Font.FontFamily.COURIER, 12, Font.NORMAL);
    private static final Font boldFont = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);
    private static final Font titleFont = new Font(Font.FontFamily.COURIER, 18, Font.BOLD);

    public EmployeePDFExporter(List<Employee> employees, String filePath) {
        Collections.sort(employees, Comparator.comparingInt(Employee::getUserId));
        this.employees = employees;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            addTitle(document);
            addReportHeader(document);
            addTableHeader(document);
            addEmployeeData(document);

            document.close();

            System.out.println("PDF generated successfully at: " + filePath);
        } catch (Exception e) {
            System.err.println("Error while generating PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph("DHEL Employee Details", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);  
        document.add(title);
    }

    private void addReportHeader(Document document) throws DocumentException {

        Paragraph reportInfo = new Paragraph(
            "Report Generated on: " + LocalDate.now(),
            defaultFont 
        );
        reportInfo.setAlignment(Element.ALIGN_LEFT);
        reportInfo.setSpacingBefore(0);
        reportInfo.setSpacingAfter(20);
        document.add(reportInfo);
    }

    private void addTableHeader(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);  

        float[] columnWidths = {15f, 25f, 20f, 20f, 20f};
        table.setWidths(columnWidths);

        table.addCell(createHeaderCell("Employee ID"));
        table.addCell(createHeaderCell("Name"));
        table.addCell(createHeaderCell("Salary (MYR)"));
        table.addCell(createHeaderCell("Total Days of Work"));
        table.addCell(createHeaderCell("Available Paid Leave"));

        document.add(table);
    }

    private void addEmployeeData(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);  

        float[] columnWidths = {15f, 25f, 20f, 20f, 20f};
        table.setWidths(columnWidths);

        for (Employee employee : employees) {
            table.addCell(createValueCell(String.valueOf(employee.getUserId())));
            table.addCell(createValueCell(employee.getName()));
            table.addCell(createValueCell(String.format("%.2f", employee.getSalary())));
            table.addCell(createValueCell(String.valueOf(employee.getTotalDaysOfWork())));
            table.addCell(createValueCell(String.valueOf(employee.getAvailablePaidLeave())));
        }

        document.add(table);
    }

    private PdfPCell createHeaderCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, boldFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);  
        cell.setPadding(5);
        return cell;
    }

    private PdfPCell createValueCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, defaultFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);  
        cell.setPadding(5);
        return cell;
    }
}
