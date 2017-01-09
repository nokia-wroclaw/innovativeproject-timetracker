package models;

import java.awt.Color;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

import models.Time;
import models.Schedule;

import org.apache.poi.hssf.record.CFRuleBase;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;

public class ExcelGenerator {
    private XSSFWorkbook workbook;
    private List<Schedule> weeklySchedule;
    private List<Time> timeline;

    public ExcelGenerator(List<Schedule> weeklySchedule, List<Time> timeline) {
        this.weeklySchedule = weeklySchedule;
        this.timeline = timeline;
        for (Time storage : timeline)
            System.out.println(storage.getLogin() + " " + storage.getBegin() + " " + storage.getEnd());
        workbook = new XSSFWorkbook();
    }

    //walidacja poprawności danych - TODO serwer + strona
    //tzn. begin < end oraz zakresy na siebie nie nachodzą
    private double countExpectedTime(String weekDay) {
        double expectedTime = 0.0;
        for (Schedule schedule : weeklySchedule)
            if (schedule.getDay().equals(weekDay))
                expectedTime += (schedule.end.getTime() - schedule.begin.getTime()) / (1000.0 * 60 * 60);
        return expectedTime;
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

    private void createHeaderRow(XSSFSheet sheet, XSSFCellStyle headerStyle) {
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
            Date time = new Date((hour - 1) * 60 * 60 * 1000);
            cell.setCellValue(time);
            cell.setCellStyle(headerStyle);

            int regionIndexes[] = {1, 1, firstMergedColumnIndex, lastMergedColumnIndex};
            CellRangeAddress region = borderRegion(BorderStyle.THICK, sheet, regionIndexes);
            sheet.addMergedRegion(region);

            for (int columnIndex = firstMergedColumnIndex; columnIndex <= lastMergedColumnIndex; columnIndex++)
                sheet.setColumnWidth(columnIndex, 600);
        }
    }

    private void createDateColumn(XSSFSheet sheet, XSSFCellStyle dateStyle) {
        for (int rowIndex = 2; rowIndex < 9; rowIndex++) {
            XSSFRow row = sheet.getRow(rowIndex);
            row.createCell(0).setCellStyle(dateStyle);
        }
    }

    private void createDayColumn(XSSFSheet sheet, XSSFCellStyle dayStyle) {
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

    private void createExpectedColumn(XSSFSheet sheet, XSSFCellStyle expectedStyle) {
        //na razie weeklySchedule wszedzie taki sam wiec dodajemy go do templatki
        String weekDays[] = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
        XSSFCell cell;
        int rowExcelOffset = 2;
        for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
            XSSFRow row = sheet.getRow(dayIndex + rowExcelOffset);
            cell = row.createCell(3);
            cell.setCellStyle(expectedStyle);
            cell.setCellValue(countExpectedTime(weekDays[dayIndex]));
        }
        cell = sheet.getRow(9).createCell(3);
        cell.setCellStyle(expectedStyle);
        cell.setCellFormula("SUM(D3:D9)");
    }

    private void createTotalColumn(XSSFSheet sheet, XSSFCellStyle totalStyle) {
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

    private void createSummary(XSSFSheet sheet, XSSFCellStyle summaryStyle) {
        String names[] = {"Extra", "Previous", "New"};
        String formulas[] = {"E10-D10", "0", "SUM(C10:C11)"};
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

    private void createConditionalFormattingRules(XSSFSheet sheet) {
        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();

        ConditionalFormattingRule nokiaRule =
                sheetCF.createConditionalFormattingRule(CFRuleBase.ComparisonOperator.EQUAL, "\"n\"");

        XSSFColor nokiaColor = new XSSFColor(new Color(112, 48, 160));

        FontFormatting nokiaFont = nokiaRule.createFontFormatting();
        nokiaFont.setFontColorIndex(IndexedColors.BLUE.index);
        nokiaFont.setFontColor(nokiaColor);

        PatternFormatting nokiaPattern = nokiaRule.createPatternFormatting();
        nokiaPattern.setFillBackgroundColor(nokiaColor);

        ConditionalFormattingRule homeRule =
                sheetCF.createConditionalFormattingRule(CFRuleBase.ComparisonOperator.EQUAL, "\"h\"");

        XSSFColor homeColor = new XSSFColor(new Color(255, 173, 48));

        FontFormatting homeFont = homeRule.createFontFormatting();
        homeFont.setFontColorIndex(IndexedColors.ORANGE.index);
        homeFont.setFontColor(homeColor);

        PatternFormatting homePattern = homeRule.createPatternFormatting();
        homePattern.setFillBackgroundColor(homeColor);

        CellRangeAddress[] regions = {new CellRangeAddress(2, 8, 5, 109)};
        sheetCF.addConditionalFormatting(regions, nokiaRule);
        sheetCF.addConditionalFormatting(regions, homeRule);
    }

    private void createTemplateSheet(String sheetName) {
        XSSFSheet sheet = workbook.createSheet(sheetName);

        for (int rowIndex = 1; rowIndex < 12; rowIndex++)
            sheet.createRow(rowIndex);

        sheet.createRow(29);
        sheet.createRow(30);

        XSSFColor headerColor = new XSSFColor(new Color(255, 234, 102));
        String headerFormat = "hh:mm";
        XSSFCellStyle headerStyle = createStyle(headerColor, headerFormat);
        createHeaderRow(sheet, headerStyle);

        XSSFColor dateColor = new XSSFColor(new Color(252, 213, 181));
        String dateFormat = "dd.mm;@";
        XSSFCellStyle dateStyle = createStyle(dateColor, dateFormat);
        createDateColumn(sheet, dateStyle);

        XSSFColor dayColor = new XSSFColor(new Color(255, 193, 102));
        String dayFormat = "";
        XSSFCellStyle dayStyle = createStyle(dayColor, dayFormat);
        createDayColumn(sheet, dayStyle);

        XSSFColor expectedColor = dayColor;
        String expectedFormat = "0.00";
        XSSFCellStyle expectedStyle = createStyle(expectedColor, expectedFormat);
        createExpectedColumn(sheet, expectedStyle);

        XSSFCellStyle totalStyle = expectedStyle;
        createTotalColumn(sheet, totalStyle);

        createReportingArea(sheet);

        XSSFCellStyle summaryStyle = createSummaryStyle();
        createSummary(sheet, summaryStyle);

        createLegend(sheet);

        createConditionalFormattingRules(sheet);
    }

    /*
    todo:
    - przerobić to porządnie
    - rozdzielić na mniejsze metody (?)
    - ogarnąć zaokrąglanie
    - co jeśli ktoś pracował z niedzieli na poniedziałek w nocy (granica przedziału)
    - co jeśli ktoś pracować z jakiegokolwiek dnia na jakikolwiek dzień
    - czy w ogóle zakładać że ktoś mógł pracować poza 6 - 22?
    - zmienić granice periodEnda na poniedziałek północ
    - ustawić formułę previous dla kolejnych sheetów index > 1
     */
    private void insertTimeline() {
        int timePeriodIndex = 0;
        int sheetIndex = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        Calendar periodCalendar = Calendar.getInstance();
        XSSFSheet sheet;

        while (timePeriodIndex < timeline.size()) {
            sheet = workbook.getSheetAt(sheetIndex);
            Date beginDate = timeline.get(timePeriodIndex).getBegin();

            calendar.setTime(beginDate);
            periodCalendar.setTime(beginDate);
            StringBuilder nameBuilder = new StringBuilder().append("CW").append(calendar.get(Calendar.WEEK_OF_YEAR)).
                    append("_").append(calendar.get(Calendar.YEAR));
            workbook.setSheetName(sheetIndex, nameBuilder.toString());

            insertDates(beginDate, sheet, calendar);

            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);

            /*
            Sunday to wg Amerykanów pierwszy dzień tygodnia
            calendar.setFirstDayOfWeek nie zmienia tego <- to działa tylko na WEEK_OF_YEAR i coś jeszcze, ale
            na pewno nie na DAY_OF_WEEK */
            System.out.println(calendar.getTime());
            periodCalendar.setTimeInMillis(calendar.getTimeInMillis() - 7 * 24 * 60 * 60 * 1000);
            System.out.println(periodCalendar.getTime());
            periodCalendar.set(periodCalendar.get(Calendar.YEAR), periodCalendar.get(Calendar.MONTH),
                    periodCalendar.get(Calendar.DAY_OF_MONTH), 6, 15);
            System.out.println(periodCalendar.getTime());

            long periodEnd = calendar.getTimeInMillis();
            int rowIndex = 2;
            while (beginDate.getTime() < periodEnd && timePeriodIndex < timeline.size()) {
                System.out.println("petla while");
                long beginPeriod = timeline.get(timePeriodIndex).getBegin().getTime();
                long endPeriod = timeline.get(timePeriodIndex).getEnd().getTime();
                long cellPeriod = periodCalendar.getTimeInMillis();
                int cellColumn = 5;
                System.out.println(timeline.get(timePeriodIndex).getBegin() + " " + periodCalendar.getTime());
                System.out.println("begin " + beginPeriod + " cellPeriod " + cellPeriod);
                while (cellPeriod < beginPeriod) {
                    System.out.println("czekanie" + periodCalendar.getTime());
                    if (cellColumn > 68)
                        break;
                    periodCalendar.add(Calendar.MINUTE, 15);
                    cellPeriod = periodCalendar.getTimeInMillis();
                    cellColumn++;
                }
                XSSFRow row = sheet.getRow(rowIndex);
                XSSFCell cell;
                while (cellPeriod < endPeriod) {
                    System.out.println("wpisywanie" + periodCalendar.getTime());
                    if (cellColumn > 68)
                        break;
                    periodCalendar.add(Calendar.MINUTE, 15);
                    cellPeriod = periodCalendar.getTimeInMillis();
                    cell = row.getCell(cellColumn);
                    cell.setCellValue("n");
                    cellColumn++;
                }
                timePeriodIndex++;
                rowIndex++;
                cellColumn = 5;
            }
            sheetIndex++;
        }
    }

    private void insertDates(Date beginDate, XSSFSheet sheet, Calendar calendar) {
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        if (weekDay == 1)
            calendar.setTimeInMillis(calendar.getTimeInMillis() - 6 * 24 * 60 * 60 * 1000);
        else
            calendar.setTimeInMillis(calendar.getTimeInMillis() - (weekDay - 2) * 24 * 60 * 60 * 1000);

        for (int rowIndex = 2; rowIndex < 9; rowIndex++) {
            XSSFRow row = sheet.getRow(rowIndex);
            XSSFCell cell = row.getCell(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date dateWithoutTime = sdf.parse(sdf.format(calendar.getTime()));
                cell.setCellValue(dateWithoutTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, 1);
        }
    }

    public void generateExcel() {
        createTemplateSheet("TemplateSheet");

        //todo obliczyć ile tygodni, to poniżej nie działa dla np. sob-wt (jeden tydzień wychodzi)
//        Date firstDate = timeline.get(0).getBegin();
//        Date lastDate = timeline.get(timeline.size() - 1).getEnd();

        //long weeks = (lastDate.getTime() - firstDate.getTime()) / (7 * 24 * 60 * 60 * 1000);
        //System.out.println(weeks);
        //for (int i = 0; i < weeks; i++)
        //workbook.cloneSheet(0);

        insertTimeline();

        saveExcel();
    }

    private void saveExcel() {
        try {
            String filename = "app//NewExcelFile.xlsx";
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            System.out.println("Your excel file has been generated!");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
