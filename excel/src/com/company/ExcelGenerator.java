package com.company;

import java.awt.Color;
import java.io.*;
import java.sql.Time;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;

public class ExcelGenerator {
    private XSSFWorkbook workbook;
    private ArrayList<XSSFSheet> sheets;
    private XSSFCellStyle headerStyle;
    private XSSFCellStyle dateStyle;
    private XSSFCellStyle dayStyle;
    private XSSFCellStyle expectedStyle;
    private XSSFCellStyle totalStyle;
    private XSSFCellStyle summaryStyle;

    public ExcelGenerator() {
        workbook = new XSSFWorkbook();
        sheets = new ArrayList<>();
    }

    private XSSFCellStyle createStyle(XSSFColor color, String format) {
        XSSFCellStyle style = workbook.createCellStyle();

        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setAlignment(HorizontalAlignment.CENTER);

        style.setBorderBottom(BorderStyle.THICK);
        style.setBorderTop(BorderStyle.THICK);
        style.setBorderLeft(BorderStyle.THICK);
        style.setBorderRight(BorderStyle.THICK);

        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat(format));

        return style;
    }

    private XSSFCellStyle createSummaryStyle() {
        XSSFCellStyle style = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();
        font.setColor(new XSSFColor(new Color(127, 127, 127)));

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.RIGHT);

        return style;
    }

    private CellRangeAddress borderRegion(BorderStyle borderStyle, XSSFSheet sheet, int[] regionIndexes) {
        CellRangeAddress region = new CellRangeAddress(regionIndexes[0], regionIndexes[1],
                regionIndexes[2], regionIndexes[3]);
        RegionUtil.setBorderBottom(borderStyle, region, sheet);
        RegionUtil.setBorderTop(borderStyle, region, sheet);
        RegionUtil.setBorderRight(borderStyle, region, sheet);
        return region;
    }

    private void createHeaderRow(XSSFSheet sheet) {
        XSSFRow headRow = sheet.getRow(1);

        XSSFCell cell = headRow.createCell(3);
        cell.setCellValue("Expected [h]");
        cell.setCellStyle(headerStyle);
        sheet.autoSizeColumn(3);

        cell = headRow.createCell(4);
        cell.setCellValue("Total [h]");
        cell.setCellStyle(headerStyle);
        sheet.autoSizeColumn(4);

        int firstHour = 6;
        int lastHour = 21;

        for (int hour = firstHour; hour <= lastHour; hour++) {
            int firstMergedColumnIndex = 5 + (hour - firstHour) * 4;
            int lastMergedColumnIndex = 8 + (hour - firstHour) * 4;

            cell = headRow.createCell(firstMergedColumnIndex);
            Time time = new Time((hour - 1) * 60 * 60 * 1000);
            cell.setCellValue(time);
            cell.setCellStyle(headerStyle);

            int regionIndexes[] = {1, 1, firstMergedColumnIndex, lastMergedColumnIndex};
            CellRangeAddress region = borderRegion(BorderStyle.THICK, sheet, regionIndexes);
            sheet.addMergedRegion(region);

            for (int columnIndex = firstMergedColumnIndex; columnIndex <= lastMergedColumnIndex; columnIndex++)
                sheet.setColumnWidth(columnIndex, 600);
        }
    }

    private void createDateColumn(XSSFSheet sheet) {
        for (int rowIndex = 2; rowIndex < 9; rowIndex++) {
            XSSFRow row = sheet.getRow(rowIndex);
            row.createCell(0).setCellStyle(dateStyle);
            //XSSFCell cell = row.createCell(0);
//            cell.setCellStyle(dateStyle);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            try {
//                Date dateWithoutTime = sdf.parse(sdf.format(new Date()));
//                System.out.println(dateWithoutTime);
//                cell.setCellValue(dateWithoutTime);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void createDayColumn(XSSFSheet sheet) {
        String weekDays[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (int rowIndex = 2; rowIndex < 9; rowIndex++) {
            XSSFRow row = sheet.getRow(rowIndex);

            XSSFCell cell = row.createCell(1);
            cell.setCellValue(weekDays[rowIndex - 2]);
            cell.setCellStyle(dayStyle);

            int regionIndexes[] = {rowIndex, rowIndex, 1, 2};
            CellRangeAddress region = borderRegion(BorderStyle.THICK, sheet, regionIndexes);
            sheet.addMergedRegion(region);
        }
    }

    private void createExpectedColumn(XSSFSheet sheet) {
        for (int rowIndex = 2; rowIndex < 10; rowIndex++) {
            XSSFRow row = sheet.getRow(rowIndex);
            row.createCell(3).setCellStyle(expectedStyle);
        }
        sheet.getRow(9).getCell(3).setCellFormula("SUM(D3:D9)");
    }

    private void createTotalColumn(XSSFSheet sheet) {
        for (int rowIndex = 2; rowIndex < 10; rowIndex++) {
            XSSFRow row = sheet.getRow(rowIndex);
            XSSFCell cell = row.createCell(4);

            int formulaRowIndex = rowIndex + 1;
            StringBuilder formulaBuilder = new StringBuilder().append("(COUNTIF(F").append(formulaRowIndex).append(":CS")
                    .append(formulaRowIndex).append(", \"=n\")+COUNTIF(F").append(formulaRowIndex).append(":CG")
                    .append(formulaRowIndex).append(", \"=h\")) / 4");

            cell.setCellFormula(formulaBuilder.toString());
            cell.setCellStyle(totalStyle);
        }
        sheet.getRow(9).getCell(4).setCellFormula("SUM(E3:E9)");
    }

    private void createReportingArea(XSSFSheet sheet) {
        int leftBorder = 5;
        int rightBorder = 68;
        int topBorder = 2;
        int bottomBorder = 8;
        for (int row = topBorder; row <= bottomBorder; row++) {
            for (int column = leftBorder; column < rightBorder; column += 4) {
                int mergeIndexes[] = {row, row, column, column + 3};
                borderRegion(BorderStyle.THICK, sheet, mergeIndexes);
            }
        }
    }

    private void createSummary(XSSFSheet sheet) {
        String names[] = {"Extra", "Previous", "New"};
        String formulas[] = {"E10-D10", "0", "SUM(C10:C11)"}; //todo change 'previous' formula
        for (int i = 0; i < names.length; i++) {
            XSSFRow row = sheet.getRow(9 + i);

            XSSFCell cell = row.createCell(1);
            cell.setCellValue(names[i]);
            cell.setCellStyle(summaryStyle);

            cell = row.createCell(2);
            cell.setCellFormula(formulas[i]);
            cell.setCellStyle(summaryStyle);
        }

        XSSFCellStyle summaryStyleWithTopBorder = createSummaryStyle();
        summaryStyleWithTopBorder.setBorderTop(BorderStyle.THICK);

        XSSFRow row = sheet.getRow(11);
        row.getCell(1).setCellStyle(summaryStyleWithTopBorder);
        row.getCell(2).setCellStyle(summaryStyleWithTopBorder);
    }

    private void createLegend(XSSFSheet sheet) {
        XSSFCellStyle nokiaStyle = workbook.createCellStyle();

        XSSFColor nokiaBackgroundColor = new XSSFColor(new Color(112, 48, 160));
        XSSFColor nokiaFontColor = new XSSFColor(new Color(255, 255, 0));

        nokiaStyle.setFillForegroundColor(nokiaBackgroundColor);
        nokiaStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont nokiaFont = workbook.createFont();
        nokiaFont.setColor(nokiaFontColor);
        nokiaStyle.setFont(nokiaFont);

        nokiaStyle.setAlignment(HorizontalAlignment.CENTER);

        XSSFCellStyle homeStyle = workbook.createCellStyle();

        XSSFColor homeBackgroundColor = new XSSFColor(new Color(255, 173, 48));

        homeStyle.setFillForegroundColor(homeBackgroundColor);
        homeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        homeStyle.setAlignment(HorizontalAlignment.CENTER);

        XSSFRow row = sheet.getRow(29);
        row.createCell(1).setCellValue("Legend:");

        XSSFCell cell = row.createCell(2);
        cell.setCellValue("Nokia office");
        cell.setCellStyle(nokiaStyle);

        row = sheet.getRow(30);
        cell = row.createCell(2);
        cell.setCellValue("Home office");
        cell.setCellStyle(homeStyle);

        sheet.addMergedRegion(new CellRangeAddress(29, 29, 2, 4));
        sheet.addMergedRegion(new CellRangeAddress(30, 30, 2, 4));
    }

    private void createTemplateSheet(String sheetName) {
        XSSFSheet sheet = workbook.createSheet(sheetName);
        sheets.add(sheet);

        for (int rowIndex = 1; rowIndex < 12; rowIndex++)
            sheet.createRow(rowIndex);

        sheet.createRow(29);
        sheet.createRow(30);

        XSSFColor headerColor = new XSSFColor(new Color(255, 234, 102));
        String headerFormat = "hh:mm";
        headerStyle = createStyle(headerColor, headerFormat);
        createHeaderRow(sheet);

        XSSFColor dateColor = new XSSFColor(new Color(252, 213, 181));
        String dateFormat = "d.mm;@";
        dateStyle = createStyle(dateColor, dateFormat);
        createDateColumn(sheet);

        XSSFColor dayColor = new XSSFColor(new Color(255, 193, 102));
        String dayFormat = "";
        dayStyle = createStyle(dayColor, dayFormat);
        createDayColumn(sheet);

        expectedStyle = dayStyle;
        createExpectedColumn(sheet);

        XSSFColor totalColor = dayColor;
        String totalFormat = "0.00";
        totalStyle = createStyle(totalColor, totalFormat);
        createTotalColumn(sheet);

        createReportingArea(sheet);

        summaryStyle = createSummaryStyle();
        createSummary(sheet);

        createLegend(sheet);

        //todo create conditional formatting rule
    }

    public void generateExcel() {
        createTemplateSheet("FirstSheet");
        saveExcel();
    }

    private void saveExcel() {
        try {
            String filename = "NewExcelFile.xlsx";
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            System.out.println("Your excel file has been generated!");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
