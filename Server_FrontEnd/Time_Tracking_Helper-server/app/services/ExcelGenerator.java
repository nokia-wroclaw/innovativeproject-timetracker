package services;

import java.awt.Color;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
    private long begin;
    private long end;

    public ExcelGenerator(List<Schedule> weeklySchedule, List<Time> timeline, long begin, long end) {
        this.weeklySchedule = weeklySchedule;
        this.timeline = timeline;
        this.workbook = new XSSFWorkbook();
        this.begin = begin;
        this.end = end;
    }

    private double countExpectedTime(String weekDay) {
        double expectedTime = 0.0;
        for (Schedule schedule : this.weeklySchedule)
            if (schedule.getDay().equals(weekDay))
                expectedTime += (schedule.end.getTime() - schedule.begin.getTime()) / (1000.0 * 60 * 60);
        return expectedTime;
    }

    private XSSFCellStyle createStyle(XSSFColor color, String format) {
        XSSFCellStyle style = this.workbook.createCellStyle();

        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setAlignment(HorizontalAlignment.CENTER);

        style.setBorderBottom(BorderStyle.THICK);
        style.setBorderTop(BorderStyle.THICK);
        style.setBorderLeft(BorderStyle.THICK);
        style.setBorderRight(BorderStyle.THICK);

        CreationHelper createHelper = this.workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat(format));

        return style;
    }

    private XSSFCellStyle createSummaryStyle() {
        XSSFCellStyle style = this.workbook.createCellStyle();

        XSSFFont font = this.workbook.createFont();
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
        int lastHour = 5 + 24;

        for (int hour = firstHour; hour <= lastHour; hour++) {
            int firstMergedColumnIndex = 5 + (hour - firstHour) * 4;
            int lastMergedColumnIndex = firstMergedColumnIndex + 3;

            cell = headRow.createCell(firstMergedColumnIndex);
            Date time = new Date((hour - 1) % 24 * 60 * 60 * 1000);
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
            StringBuilder formulaBuilder = new StringBuilder().append("(COUNTIF(F").append(formulaRowIndex).append(":CW")
                    .append(formulaRowIndex).append(", \"=n\")+COUNTIF(F").append(formulaRowIndex).append(":CW")
                    .append(formulaRowIndex).append(", \"=h\")) / 4");

            cell.setCellFormula(formulaBuilder.toString());
            cell.setCellStyle(totalStyle);
        }
        sheet.getRow(9).getCell(4).setCellFormula("SUM(E3:E9)");
    }

    private void createReportingArea(XSSFSheet sheet) {
        int leftBorder = 5;
        int rightBorder = 5 + 24 * 4;
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
        XSSFCellStyle nokiaStyle = this.workbook.createCellStyle();

        XSSFColor nokiaBackgroundColor = new XSSFColor(new Color(112, 48, 160));
        XSSFColor nokiaFontColor = new XSSFColor(new Color(255, 255, 0));

        nokiaStyle.setFillForegroundColor(nokiaBackgroundColor);
        nokiaStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont nokiaFont = this.workbook.createFont();
        nokiaFont.setColor(nokiaFontColor);
        nokiaStyle.setFont(nokiaFont);

        nokiaStyle.setAlignment(HorizontalAlignment.CENTER);

        XSSFCellStyle homeStyle = this.workbook.createCellStyle();

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

        CellRangeAddress[] regions = {new CellRangeAddress(2, 8, 5, 100)};
        sheetCF.addConditionalFormatting(regions, nokiaRule);
        sheetCF.addConditionalFormatting(regions, homeRule);
    }

    private void createTemplateSheet() {
        XSSFSheet sheet = this.workbook.createSheet();

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

    private void insertTimeline(int weeks) {
        Map<Date, XSSFRow> days = new HashMap<Date, XSSFRow>();
        //Date beginDate = this.timeline.get(0).getBegin();
		Date beginDate = new Date(this.begin);

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(beginDate);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        String previousName = "";
        for (int sheetIndex = 0; sheetIndex < weeks; sheetIndex++) {
            if (sheetIndex > 0)
                this.workbook.getSheetAt(sheetIndex).getRow(10).getCell(2).setCellFormula(previousName + "!C12");
            StringBuilder nameBuilder = new StringBuilder().append("CW").append(calendar.get(Calendar.WEEK_OF_YEAR)).
                    append("_").append(calendar.get(Calendar.YEAR));
            String name = nameBuilder.toString();
            this.workbook.setSheetName(sheetIndex, name);
            insertDates(calendar, this.workbook.getSheetAt(sheetIndex), days);
            previousName = name;
        }
		if (!this.timeline.isEmpty())
			this.timeline.forEach((period) -> insertPeriod(period, days, calendar));
    }

    private void insertPeriod(Time period, Map<Date, XSSFRow> days, Calendar calendar) {
        calendar.setTime(period.getEnd());
        int endMinute = calendar.get(Calendar.MINUTE);
        int endHour = calendar.get(Calendar.HOUR_OF_DAY);
        if (endHour < 6)
            endHour += 24;

        calendar.setTime(period.getBegin());
        int beginMinute = calendar.get(Calendar.MINUTE);
        int beginHour = calendar.get(Calendar.HOUR_OF_DAY);
        if (beginHour < 6)
            beginHour += 24;

        int reportingOffset = 5;
        int firstCell = 4 * (beginHour - 6) + (int) Math.round(beginMinute / 15.0) + reportingOffset;
        int lastCell = 4 * (endHour - 6) + (int) Math.round(endMinute / 15.0) + reportingOffset;
        int lastReportingCell = 4 * 24 + reportingOffset;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date day = null;
        try {
            day = sdf.parse(sdf.format(period.getBegin()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        XSSFRow row = days.get(day);
        if (firstCell > lastCell) {
            for (int column = firstCell; column < lastReportingCell; column++)
                row.getCell(column).setCellValue("n");
            firstCell = reportingOffset;
        }
        for (int column = firstCell; column < lastCell; column++)
            row.getCell(column).setCellValue("n");
    }

    private void insertDates(Calendar calendar, XSSFSheet sheet, Map<Date, XSSFRow> days) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateWithoutTime;
        for (int rowIndex = 2; rowIndex < 9; rowIndex++) {
            XSSFRow row = sheet.getRow(rowIndex);
            XSSFCell cell = row.getCell(0);
            try {
                dateWithoutTime = sdf.parse(sdf.format(calendar.getTime()));
                cell.setCellValue(dateWithoutTime);
                days.put(dateWithoutTime, row);
            } catch (Exception e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, 1);
        }
    }

    private int weeksBetween(Date beginDate, Date endDate) {
        int weeks = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        calendar.setTime(endDate);
        int endWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int endYear = calendar.get(Calendar.YEAR);

        calendar.setTime(beginDate);
        int beginWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int beginYear = calendar.get(Calendar.YEAR);

        if (beginYear == endYear) {
            if (beginWeek > endWeek)
                weeks = endWeek;
            else
                weeks = endWeek - beginWeek;
        }
        else {
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            weeks = calendar.get(Calendar.WEEK_OF_YEAR) - beginWeek;

            calendar.add(Calendar.YEAR, 1);
            while (calendar.get(Calendar.YEAR) != endYear) {
                weeks += calendar.get(Calendar.WEEK_OF_YEAR);
                calendar.add(Calendar.YEAR, 1);
            }
            weeks += endWeek;
        }
        return weeks;
    }

    public byte[] generateExcel() {
        createTemplateSheet();

        Date beginDate = new Date(this.begin);
        Date endDate = new Date(this.end);

        int weeks = weeksBetween(beginDate, endDate);

        for (int i = 0; i < weeks; i++)
            this.workbook.cloneSheet(0);

        insertTimeline(weeks + 1);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            this.workbook.write(bos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
    }
}
