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
import com.dcom.dataModel.Payroll;

public class AllPayrollPDFExporter implements Runnable {
    private List<Payroll> payrolls;
    private String filePath;

    private static final Font defaultFont = new Font(Font.FontFamily.COURIER, 12, Font.NORMAL);
    private static final Font boldFont = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);
    private static final Font titleFont = new Font(Font.FontFamily.COURIER, 18, Font.BOLD);

    public AllPayrollPDFExporter(List<Payroll> payrolls, String filePath) {
        Collections.sort(payrolls, Comparator.comparingInt(Payroll::getPayrollId));
        this.payrolls = payrolls;
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
            addPayrollData(document);

            document.close();

            System.out.println("PDF generated successfully at: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph("DHEL Payroll Report", titleFont);
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

        float[] columnWidths = {15f, 10f, 20f, 15f, 10f, 15f, 15f};
        table.setWidths(columnWidths);

        table.addCell(createHeaderCell("Payroll ID"));
        table.addCell(createHeaderCell("User ID"));
        table.addCell(createHeaderCell("Date"));
        table.addCell(createHeaderCell("Total Paid"));
        table.addCell(createHeaderCell("Paid"));
        table.addCell(createHeaderCell("Tax"));
        table.addCell(createHeaderCell("EPF"));
        // table.addCell(createHeaderCell("Salary Class"));

        document.add(table);
    }

    private void addPayrollData(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);

        float[] columnWidths = {15f, 10f, 20f, 15f, 10f, 15f, 15f};
        table.setWidths(columnWidths);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (Payroll payroll : payrolls) {
            table.addCell(createValueCell(String.valueOf(payroll.getPayrollId())));
            table.addCell(createValueCell(String.valueOf(payroll.getUserId())));
            table.addCell(createValueCell(dateFormat.format(payroll.getDate())));
            table.addCell(createValueCell(String.valueOf(payroll.getTotalPaid())));
            table.addCell(createValueCell(payroll.isPaid() ? "Yes" : "No"));
            table.addCell(createValueCell(String.valueOf(payroll.getTax())));
            table.addCell(createValueCell(String.valueOf(payroll.getEpf())));
            // table.addCell(createValueCell(payroll.getSalaryClass()));
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
