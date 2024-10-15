package com.dcom.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import com.dcom.dataModel.PayrollTemplate;

public class PayrollPDFExporter implements Runnable {
    private PayrollTemplate invoice;  
    private String filePath;  
    private String logoPath;  

    private static final Font defaultFont = new Font(Font.FontFamily.COURIER, 12, Font.NORMAL);
    // private static final Font boldFont = new Font(Font.FontFamily.COURIER, 12, Font.NORMAL);
    // private static final Font titleFont = new Font(Font.FontFamily.COURIER, 20, Font.BOLDITALIC);
    // private static final Font italicFont = new Font(Font.FontFamily.COURIER, 12, Font.ITALIC);
    private static final Font footerFont = new Font(Font.FontFamily.COURIER, 8, Font.ITALIC);

    public PayrollPDFExporter(PayrollTemplate invoice, String filePath, String logoPath) {
        this.invoice = invoice;
        this.filePath = filePath;
        this.logoPath = logoPath;
    }

    @Override
    public void run() {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            addLogo(document);

            addInvoiceHeader(document);
            addEmployeeDetails(document);
            addEarningsAndDeductions(document);
            addNetSalarySection(document);
            addFooter(document);

            document.close();

            System.out.println("PDF invoice generated successfully at: " + filePath);
        } catch (Exception e) {
            System.err.println("Error while generating PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addLogo(Document document) throws DocumentException, IOException {
        Image logo = Image.getInstance(logoPath);
        logo.scaleToFit(150, 150);  
        logo.setAlignment(Element.ALIGN_CENTER); 
        document.add(logo);
    }

    private void addInvoiceHeader(Document document) throws DocumentException {
        // Paragraph title = new Paragraph("DHEL PAYSLIP", titleFont);
        // title.setAlignment(Element.ALIGN_CENTER);
        // title.setSpacingAfter(20);
        // document.add(title);

        Paragraph payrollInfo = new Paragraph(
            "Payroll Generated on: " + LocalDate.now(),
            defaultFont 
        );
        payrollInfo.setAlignment(Element.ALIGN_LEFT);
        payrollInfo.setSpacingBefore(40);
        payrollInfo.setSpacingAfter(0);
        document.add(payrollInfo);
    }

    private void addEmployeeDetails(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(1);  
        table.setWidthPercentage(100);  
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
    
        PdfPCell employeeCell = new PdfPCell();
        employeeCell.setBorder(Rectangle.NO_BORDER);
        employeeCell.setPhrase(new Phrase("Employee Name: " + invoice.getEmployeeName(), defaultFont)); 
        employeeCell.setPaddingBottom(10);
        table.addCell(employeeCell);

        PdfPCell payPeriodCell = new PdfPCell();
        payPeriodCell.setBorder(Rectangle.NO_BORDER);
        payPeriodCell.setPhrase(new Phrase("Pay Period: " + invoice.getPayPeriod(), defaultFont));  
        table.addCell(payPeriodCell);
    
        document.add(table);
    }
    
    
    

    private void addEarningsAndDeductions(Document document) throws DocumentException {
        PdfPTable outerTable = new PdfPTable(2);  
        outerTable.setWidthPercentage(100);  
        outerTable.setSpacingBefore(10);
        outerTable.setSpacingAfter(10);

        PdfPTable earningsTable = new PdfPTable(2);  
        earningsTable.setWidthPercentage(100);

        PdfPCell earningsTitle = new PdfPCell(new Phrase("Earnings", defaultFont));  
        earningsTitle.setColspan(2);
        earningsTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
        earningsTable.addCell(earningsTitle);

        earningsTable.addCell(new Phrase("Base Salary", defaultFont));  
        earningsTable.addCell(new Phrase(formatCurrency(invoice.getBaseSalary()), defaultFont));  

        earningsTable.addCell(new Phrase("Total Earnings", defaultFont));  
        earningsTable.addCell(new Phrase(formatCurrency(invoice.getTotalEarned()), defaultFont));  

        PdfPTable deductionsTable = new PdfPTable(2); 
        deductionsTable.setWidthPercentage(100);

        PdfPCell deductionsTitle = new PdfPCell(new Phrase("Deductions", defaultFont));  
        deductionsTitle.setColspan(2);
        deductionsTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
        deductionsTable.addCell(deductionsTitle);

        deductionsTable.addCell(new Phrase("Tax", defaultFont));  
        deductionsTable.addCell(new Phrase(formatCurrency(invoice.getTax()), defaultFont)); 

        deductionsTable.addCell(new Phrase("EPF", defaultFont));  
        deductionsTable.addCell(new Phrase(formatCurrency(invoice.getEpf()), defaultFont));  

        deductionsTable.addCell(new Phrase("Leave Deduction", defaultFont));  
        deductionsTable.addCell(new Phrase(formatCurrency(invoice.getLeaveWithoutPayDeduction()), defaultFont));  

        deductionsTable.addCell(new Phrase("Total Deductions", defaultFont));  
        deductionsTable.addCell(new Phrase(formatCurrency(invoice.getTotalDeductions()), defaultFont));  

        PdfPCell earningsCell = new PdfPCell(earningsTable);
        earningsCell.setBorder(Rectangle.NO_BORDER);
        outerTable.addCell(earningsCell);

        PdfPCell deductionsCell = new PdfPCell(deductionsTable);
        deductionsCell.setBorder(Rectangle.NO_BORDER);
        outerTable.addCell(deductionsCell);

        document.add(outerTable);
    }

    private void addNetSalarySection(Document document) throws DocumentException {
        Paragraph netSalary = new Paragraph("Net Salary: " + formatCurrency(invoice.getNetPay()), defaultFont); 
        netSalary.setAlignment(Element.ALIGN_RIGHT);
        netSalary.setSpacingBefore(20);
        document.add(netSalary);

        String paidStatus = invoice.isPaid() ? "Paid" : "Unpaid";
        Paragraph paymentStatus = new Paragraph("Payment Status: " + paidStatus, defaultFont);  
        paymentStatus.setAlignment(Element.ALIGN_RIGHT);
        paymentStatus.setSpacingBefore(5);
        document.add(paymentStatus);
    }

    private void addFooter(Document document) throws DocumentException {
        Paragraph footer = new Paragraph("Note: This is a computer-generated document. No signature is required.",
                footerFont);  
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(30);
        document.add(footer);
    }

    // Format currency as $X,XXX.XX
    private String formatCurrency(double amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "MY"));
        return currencyFormatter.format(amount);
    }
}
