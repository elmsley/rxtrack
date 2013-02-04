package com.rxtrack.model;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelWriter {
	private static ExcelWriter _instance = null;
	public static ExcelWriter getInstance(){
		if (_instance==null){
			_instance = new ExcelWriter();
		}
		return _instance;
	}
	
	public String exportScripts(String directory){
		try {
			Workbook wb = new HSSFWorkbook();

			String[] monthName = {"January", "February",
					            "March", "April", "May", "June", "July",
					           "August", "September", "October", "November",
					          "December"};
					
			Calendar cal = Calendar.getInstance();
			String month = monthName[cal.get(Calendar.MONTH)];
			int num = cal.get(Calendar.DAY_OF_MONTH);
			int year = cal.get(Calendar.YEAR);
			String filename = directory + "\\RxTrackScriptsSummary_" + month + "_"+ num + "_" + year + ".xls";
			String tempfilename = directory + "\\RxTrackScriptsSummary_" + month + "_"+ num + "_" + year + "q" + ".xls";
			
			String runningFileName = filename;
			int q = 0;
			while (new File(runningFileName).exists()){
				runningFileName = tempfilename.replace("q", "_" + q++);
			}
		    FileOutputStream fileOut = new FileOutputStream(runningFileName);
			
		    Sheet sheet1 = wb.createSheet("Scripts");
		    
		    Row rowh = sheet1.createRow((short)0);
		    // Create a cell and put a value in it.
		    rowh.createCell(0).setCellValue("Date");
		    rowh.createCell(1).setCellValue("Time");
		    rowh.createCell(2).setCellValue("ID");
		    rowh.createCell(3).setCellValue("Name");
		    rowh.createCell(4).setCellValue("Drug");
		    rowh.createCell(5).setCellValue("SIG");
		    rowh.createCell(6).setCellValue("Mitte");
		    rowh.createCell(7).setCellValue("Rx");
		    rowh.createCell(8).setCellValue("Pics");
		    rowh.createCell(9).setCellValue("Bin");
		    
			List<Script> scripts = MasterModel.getInstance().getScriptList();
			for (int i=0;i<scripts.size();i++){
				Script s = scripts.get(i);
				int k=i+1; // offset
				Row row = sheet1.createRow((short)k);
				row.createCell(0).setCellValue(s.getDate());
				row.createCell(1).setCellValue(s.getTime());
				row.createCell(2).setCellValue(s.getPatient().getId());
				row.createCell(3).setCellValue(s.getPatient().getName());
				row.createCell(4).setCellValue(s.getInventoryItem().getDosage());
				row.createCell(5).setCellValue(s.getSig());
				row.createCell(6).setCellValue(s.getMitte());
				row.createCell(7).setCellValue(s.getRx());
				row.createCell(8).setCellValue(s.getPics());
				row.createCell(9).setCellValue(s.getInventoryItem().getBin());

				try {
//					row.createCell(2).setCellValue(Integer.parseInt(s.getPatient().getId()));
					row.createCell(6).setCellValue(Integer.parseInt(s.getMitte()));
					row.createCell(7).setCellValue(Integer.parseInt(s.getRx()));
				} catch (Exception e) {
					// TODO: handle exception
				}
				sheet1.autoSizeColumn(0);
				sheet1.autoSizeColumn(1);
				sheet1.autoSizeColumn(2);
				sheet1.autoSizeColumn(3);
				sheet1.autoSizeColumn(4);
				sheet1.autoSizeColumn(5);
				sheet1.autoSizeColumn(6);
				sheet1.autoSizeColumn(7);
				sheet1.autoSizeColumn(8);
				sheet1.autoSizeColumn(9);
			}

			wb.write(fileOut);
		    fileOut.close();
			return runningFileName;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String exportInventory(String directory){
		try {
			Workbook wb = new HSSFWorkbook();

			String[] monthName = {"January", "February",
					            "March", "April", "May", "June", "July",
					           "August", "September", "October", "November",
					          "December"};
					
			Calendar cal = Calendar.getInstance();
			String month = monthName[cal.get(Calendar.MONTH)];
			int num = cal.get(Calendar.DAY_OF_MONTH);
			int year = cal.get(Calendar.YEAR);
			String filename = directory + "\\RxTrackInventorySummary_" + month + "_"+ num + "_" + year + ".xls";
			String tempfilename = directory + "\\RxTrackInventorySummary_" + month + "_"+ num + "_" + year + "q" + ".xls";
			
			String runningFileName = filename;
			int q = 0;
			while (new File(runningFileName).exists()){
				runningFileName = tempfilename.replace("q", "_" + q++);
			}
		    FileOutputStream fileOut = new FileOutputStream(runningFileName);
			
		    Sheet sheet1 = wb.createSheet("Inventory");
		    
		    Row rowh = sheet1.createRow((short)0);
		    // Create a cell and put a value in it.
		    rowh.createCell(0).setCellValue("Dosage");
		    rowh.createCell(1).setCellValue("Dispensed");
		    rowh.createCell(1).setCellValue("Inventory");
		    rowh.createCell(2).setCellValue("Mitte");
		    rowh.createCell(3).setCellValue("SIG");
		    rowh.createCell(4).setCellValue("Bin");
		    rowh.createCell(5).setCellValue("Pictures");
		    
			List<InventoryItem> inventory = InventoryModelProvider.getInstance().getInventoryItems();
			for (int i=0;i<inventory.size();i++){
				InventoryItem s = inventory.get(i);
				int k=i+1; // offset
				Row row = sheet1.createRow((short)k);
				row.createCell(0).setCellValue(s.getDosage());
				row.createCell(1).setCellValue(s.getDispensed());
				row.createCell(2).setCellValue(s.getInventory());
				row.createCell(3).setCellValue(s.getMitte());
				row.createCell(4).setCellValue(s.getSig());
				row.createCell(5).setCellValue(s.getBin());
				row.createCell(6).setCellValue(s.getPictures());

			}
			sheet1.autoSizeColumn(0);
			sheet1.autoSizeColumn(1);
			sheet1.autoSizeColumn(2);
			sheet1.autoSizeColumn(3);
			sheet1.autoSizeColumn(4);
			sheet1.autoSizeColumn(5);
			sheet1.autoSizeColumn(6);
			sheet1.autoSizeColumn(7);
			sheet1.autoSizeColumn(8);
			sheet1.autoSizeColumn(9);

			wb.write(fileOut);
		    fileOut.close();
			return runningFileName;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
