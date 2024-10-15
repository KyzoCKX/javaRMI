package com.dcom.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import com.dcom.dataModel.Employee;
import com.dcom.dataModel.LeaveApplication;

public class LeaveApplicationPDFExporter implements Runnable {
    private List<LeaveApplication> leaveApplications;  
    private String filePath;                           

    private static final Font defaultFont = new Font(Font.FontFamily.COURIER, 12, Font.NORMAL);
    private static final Font boldFont = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);
    private static final Font titleFont = new Font(Font.FontFamily.COURIER, 18, Font.BOLD);

    public LeaveApplicationPDFExporter(List<LeaveApplication> leaveApplications, String filePath) {
        Collections.sort(leaveApplications, Comparator.comparingInt(LeaveApplication::getLeaveApplicationId));
        this.leaveApplications = leaveApplications;
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

            addLeaveApplicationData(document);

            document.close();

            System.out.println("PDF generated successfully at: " + filePath);
        } catch (Exception e) {
            System.err.println("Error while generating PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph("DHEL Leave Applications Report", titleFont);
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
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);  

        float[] columnWidths = {10f, 10f, 20f, 10f, 12.5f, 22.5f, 15f};
        table.setWidths(columnWidths);

        table.addCell(createHeaderCell("Leave ID"));
        table.addCell(createHeaderCell("User ID"));
        table.addCell(createHeaderCell("Date"));
        table.addCell(createHeaderCell("Days"));
        table.addCell(createHeaderCell("Type"));
        table.addCell(createHeaderCell("Reason"));
        table.addCell(createHeaderCell("Status"));

        document.add(table);
    }

    private void addLeaveApplicationData(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);  

        float[] columnWidths = {10f, 10f, 20f, 10f, 12.5f, 22.5f, 15f};
        table.setWidths(columnWidths);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (LeaveApplication leave : leaveApplications) {
            table.addCell(createValueCell(String.valueOf(leave.getLeaveApplicationId())));
            table.addCell(createValueCell(String.valueOf(leave.getUserId())));
            table.addCell(createValueCell(dateFormat.format(leave.getDate())));
            table.addCell(createValueCell(String.valueOf(leave.getNumberOfDays())));
            table.addCell(createValueCell(leave.getType()));
            table.addCell(createValueCell(leave.getReason()));
            table.addCell(createValueCell(leave.getStatus()));
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
