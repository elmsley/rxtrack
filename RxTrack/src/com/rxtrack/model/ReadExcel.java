package com.rxtrack.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.rxtrack.util.RxUtil;

public class ReadExcel {

	public static final int C_Dosage	= 0;
	public static final int C_Inventory	= 1;
	public static final int C_Mitte		= 2;
	public static final int C_SIG		= 3;
	public static final int C_Bin		= 4;
	public static final int C_Pictures	= 5;
	public static final int C_conversion= 6; 
	public static final int C_flags		= 7; 


	private static Workbook readFile(String filename) throws InvalidFormatException, FileNotFoundException, IOException {
		Workbook w = WorkbookFactory.create(new FileInputStream(filename));
		return w;
	}


	public List<List<DosageListItem>> loadRxTrackFile(String excelFile) {
		List<DosageListItem> dosageList = new ArrayList<DosageListItem>();
		List<DosageListItem> errorList =  new ArrayList<DosageListItem>();
		Workbook wb = null;
		try {
			wb = ReadExcel.readFile(excelFile);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		try {
			
			Sheet sheet = wb.getSheet("DosageList");
			int i=1;
			Row row = sheet.getRow(i);
			while (row!=null){
			    Cell cell1 = row.getCell(C_Dosage);
			    Cell cell2 = row.getCell(C_Inventory);
			    Cell cell3 = row.getCell(C_Mitte);
			    Cell cell4 = row.getCell(C_SIG);
			    Cell cell5 = row.getCell(C_Bin);
			    Cell cell6 = row.getCell(C_Pictures);
			    Cell cell7 = row.getCell(C_conversion);
			    Cell cell8 = row.getCell(C_flags);
			    
			    String dosage = "";
			    int inventory = 0;
			    int mitte = 0;
			    String sig = "";
			    String bin = "";
			    String pictures = "";
			    String flags = "";
			    Conversion conversion = new Conversion();
			    String errorMessage = "";
			    
			    dosage = 	(cell1 != null) ? cell1.getStringCellValue().trim() : "";
			    inventory = (cell2 != null && cell2.getCellType() == Cell.CELL_TYPE_NUMERIC) ? (int)cell2.getNumericCellValue() : 0;
			    mitte = 	(cell3 != null && cell3.getCellType() == Cell.CELL_TYPE_NUMERIC) ? (int)cell3.getNumericCellValue() : 0;
			    sig = 		(cell4 != null) && cell4.getCellType() == Cell.CELL_TYPE_STRING ? cell4.getStringCellValue().trim() : "";
			    bin = 		(cell5 != null) ? cell5.toString() : "";
			    pictures = 	(cell6 != null) && cell6.getCellType() == Cell.CELL_TYPE_STRING ? cell6.getStringCellValue().trim() : "";
			    flags	= 	(cell8 != null) && cell8.getCellType() == Cell.CELL_TYPE_STRING ? cell8.getStringCellValue().trim() : "";
			    
			    if (RxUtil.isEmpty(pictures)){
			    	pictures = " ";
			    }
			    
			    if (dosage.length()>0 && pictures.length()>0){
			    	boolean hasReplacement = sig.indexOf("_")>=0;
			    	if (hasReplacement){

			    		if (cell7 ==null){
			    			errorMessage = "Lookup sheet not defined";
			    		} else if (cell7.getCellType() == Cell.CELL_TYPE_NUMERIC){
				    		// 1. good, find conversion value
				    		conversion.setConversionrate((int)cell7.getNumericCellValue());
			    		} else if (cell7.getCellType() != Cell.CELL_TYPE_BLANK){
				    		String lookupSheetName = cell7.toString();
				    		if (RxUtil.isEmpty(lookupSheetName)){
				    			errorMessage = "Lookup sheet not defined";
				    		} else {
				    			Sheet lookupSheet = wb.getSheet(lookupSheetName);
				    			if (lookupSheet == null){
				    				errorMessage = "Lookup sheet missing: (" + lookupSheetName + ")";
				    			} else {
					    			Row lookupRowTitle = lookupSheet.getRow(0);
					    			
					    			String fromTitle = lookupRowTitle.getCell(0) != null ? lookupRowTitle.getCell(0).toString() : null;
					    			String toTitle = lookupRowTitle.getCell(1) != null ? lookupRowTitle.getCell(1).toString() : null;
					    			String replacementTitle = lookupRowTitle.getCell(2) != null ? lookupRowTitle.getCell(2).toString() : null;
					    			
					    			if (!RxUtil.isEmpty(fromTitle) && !RxUtil.isEmpty(toTitle) && !RxUtil.isEmpty(replacementTitle)){
					    				// store conversion row titles
					    				conversion.addRangeRowHeader(fromTitle, toTitle, replacementTitle);
					    				
						    			int j=1;
						    			Row lookupRow = lookupSheet.getRow(j);
						    			while (lookupRow!=null){
						    				String from = lookupRow.getCell(0) != null ? lookupRow.getCell(0).toString() : null;
						    				String to = lookupRow.getCell(1) != null ? lookupRow.getCell(1).toString() : null;
						    				String replacement = lookupRow.getCell(2) != null ? lookupRow.getCell(2).toString() : null;
						    				conversion.addRangeRow(from, to, replacement);
						    				
							    			j++;
							    			lookupRow = lookupSheet.getRow(j);
						    			}
						    			
						    		} else {
						    			errorMessage = "lookup table for " + lookupSheetName + " has incomplete header data";
						    		}
				    			}
				    		}
				    	
			    		} else {
				    		errorMessage = "Check conversion column.";
				    	}
			    	
			    	}
			    	if (sig.indexOf("_")!=sig.lastIndexOf("_")){
			    		// too many replacements
			    		errorMessage = "Too many blank (_) characters specified.";
			    	}
			    } else {
			    	// error in this row
				    errorMessage = "Check blank columns.";
			    }
			    if (!RxUtil.isEmpty(errorMessage)){
				    DosageListItem errorDosageListItem = new DosageListItem();
				    int userRow = row.getRowNum() + 1;
				    errorDosageListItem.setDosage("Row " + userRow + ": [" + dosage + "] - [" + errorMessage + "]");
			    	errorList.add(errorDosageListItem);
			    } else {
			    	// store the row
				    DosageListItem dosageListItem = new DosageListItem();
				    dosageListItem.setDosage(dosage);
				    dosageListItem.setInventory(inventory);
				    dosageListItem.setMitte(mitte);
				    dosageListItem.setSig(sig);
				    dosageListItem.setBin(bin);
				    dosageListItem.setPictures(pictures);
				    dosageListItem.setConversion(conversion);
				    dosageListItem.setFlags(flags);
			    	dosageList.add(dosageListItem);
			    }
			    
			    // next
			    i++;
			    row = sheet.getRow(i);
			}
		} catch (Exception e) {
			System.out.println("Error after processing: " + dosageList.size() + " rows.");
			e.printStackTrace();
		}
		List<List<DosageListItem>> results = new ArrayList<List<DosageListItem>>();
		results.add(dosageList);
		Collections.sort(dosageList, new Comparator<DosageListItem>() {

			public int compare(DosageListItem o1, DosageListItem o2) {
				if (o1!=null && o2!=null && o1.getDosage()!=null && o2.getDosage()!=null){
					return o1.getDosage().toLowerCase().compareTo(o2.getDosage().toLowerCase());
				}
				return 0;
			}
		});
		if (errorList.size()>0){
			results.add(errorList);
		}
		
		readOtherLookups(wb);
	    return results;

	}

	private void readOtherLookups(Workbook wb) {
		try {
			
			Sheet sheet = wb.getSheet(XLSConstants.CITY_SHEET);
			if (sheet!=null){
				int i=1;
				Row row = sheet.getRow(i);
				
			    Set<String> set = Lookups.getInstance().getLookup(XLSConstants.CITY_SHEET);
		
			    while (row!=null){
				    Cell cell1 = row.getCell(0);
				    
				    String lookupValue = (cell1 != null) ? cell1.getStringCellValue().trim() : "";
				    if (lookupValue!=null && lookupValue.trim().length()>0){
				    	set.add(lookupValue);
				    }
				 // next
				    i++;
				    row = sheet.getRow(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
