package com.rxtrack.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.jface.preference.IPreferenceStore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.rxtrack.Activator;
import com.rxtrack.ui.preferences.PreferenceConstants;


public class ScriptPersist {
	private static ScriptPersist _instance = null;
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 
	
	public static ScriptPersist getInstance(){
		if (_instance==null){
			_instance = new ScriptPersist();
		}
		return _instance;
	}
	
	public void save(Script s){
		saveCSV(s);
	    saveXML(s);
	}
	
	public void saveOldXmlFileIfNecessary(){
		try {
			IPreferenceStore store = Activator.getDefault().getPreferenceStore();
			File logFile = new File(store.getString(PreferenceConstants.P_PWD) + "\\" + store.getString(PreferenceConstants.P_LOGFILE) + ".xml");
			if (logFile.exists()){
				// renaming it
				int i=0;
				boolean fileNameExists = true;
				File newLogFile = null;
				while (fileNameExists){
					i++;
					String newFileName = store.getString(PreferenceConstants.P_PWD) + "\\" + store.getString(PreferenceConstants.P_LOGFILE) + i + ".xml";
					newLogFile = new File(newFileName);
					if (!newLogFile.exists()){
						fileNameExists = false;
					}
				}
				logFile.renameTo(newLogFile);
				LOGGER.setLevel(Level.INFO);
				LOGGER.info("Renaming it due to error as " + newLogFile.getAbsolutePath());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private Document createNewXMLFile(){
		
		saveOldXmlFileIfNecessary();
		
		// create new document
		try {
		    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document newdoc = docBuilder.newDocument();
			
			newdoc.appendChild(newdoc.createElement("scripts"));
			
			// save it
			IPreferenceStore store = Activator.getDefault().getPreferenceStore();
			File logFile = new File(store.getString(PreferenceConstants.P_PWD) + "\\" + store.getString(PreferenceConstants.P_LOGFILE) + ".xml");
			StreamResult newResult = new StreamResult(logFile);
			DOMSource newSource = new DOMSource(newdoc);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(newSource, newResult);
			return newdoc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Document loadXML(){
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		File logFile = new File(store.getString(PreferenceConstants.P_PWD) + "\\" + store.getString(PreferenceConstants.P_LOGFILE) + ".xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    Document doc = null;
	    DocumentBuilder db = null;
	    try {
		    db = dbf.newDocumentBuilder();
		    doc = db.parse(logFile);
	    } catch (IOException e) {
			doc = createNewXMLFile();
		} catch (SAXException e) {
			doc = createNewXMLFile();
		} catch (ParserConfigurationException pce){
			pce.printStackTrace();
		}
		return doc;

	}
	
	private void saveXML(Script s) {
		try {
			/**
			 * Load the file append the element, and persist it.
			 */
		    Document doc = loadXML();
			XMLHelper xmlHelper = XMLHelper.getInstance();
		    Element rootElement = doc.getDocumentElement();
				 
		    // Patient Element
			Element patient = doc.createElement("patient");
			rootElement.appendChild(patient);
			
			xmlHelper.addAttribute(doc, patient, XMLConstants.ID, s.getPatient().getId());
			xmlHelper.addAttribute(doc, patient, XMLConstants.NAME, s.getPatient().getName());
			xmlHelper.addAttribute(doc, patient, XMLConstants.CITY, s.getPatient().getCity());

		    // script elements
			Element script = doc.createElement("script");
			rootElement.appendChild(script);
			
			xmlHelper.addAttribute(doc, script, XMLConstants.DATE, s.getDate());
			xmlHelper.addAttribute(doc, script, XMLConstants.TIME, s.getTime());
			xmlHelper.addAttribute(doc, script, XMLConstants.RX, s.getRx());
			xmlHelper.addAttribute(doc, script, XMLConstants.MITTE, s.getMitte());
			xmlHelper.addAttribute(doc, script, XMLConstants.PICS, s.getPics());
			xmlHelper.addSubElement(doc, script, XMLConstants.SIG, s.getSig());
			xmlHelper.addSubElement(doc, script, XMLConstants.PATIENT_REF, s.getPatient().getId());

			// inventory elements, child of script
			Element inventoryItem = doc.createElement("inventoryitem");
			script.appendChild(inventoryItem);
			
			xmlHelper.addAttribute(doc, inventoryItem, XMLConstants.BIN, s.getInventoryItem().getBin());
			xmlHelper.addAttribute(doc, inventoryItem, XMLConstants.PICS, s.getInventoryItem().getPictures());
			xmlHelper.addAttribute(doc, inventoryItem, XMLConstants.DISPENSED, s.getInventoryItem().getDispensed().toString());
			xmlHelper.addAttribute(doc, inventoryItem, XMLConstants.INVENTORY, s.getInventoryItem().getInventory().toString());

			xmlHelper.addSubElement(doc, inventoryItem, XMLConstants.DOSAGE, s.getInventoryItem().getDosage());
			xmlHelper.addSubElement(doc, inventoryItem, XMLConstants.SIG, s.getInventoryItem().getSig());
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(doc);
			IPreferenceStore store = Activator.getDefault().getPreferenceStore();
			File logFile = new File(store.getString(PreferenceConstants.P_PWD) + "\\" + store.getString(PreferenceConstants.P_LOGFILE) + ".xml");
			StreamResult result = new StreamResult(logFile);
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
	 
			LOGGER.log(Level.INFO, "XML saved! Script #: "+s.getRx());
		
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
		
	}

	private void saveCSV(Script s) {
		try{
	    	// add it to the model
			MasterModel.getInstance().getScriptList().add(0, s);

			IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		    // Create file 
			File logFile = new File(store.getString(PreferenceConstants.P_PWD) + "\\" + store.getString(PreferenceConstants.P_LOGFILE) + ".txt");
			FileWriter fstream = new FileWriter(logFile,true);
	        BufferedWriter out = new BufferedWriter(fstream);
	        StringBuffer sb = new StringBuffer();
	        
	        String pics = s.getPics();	  
	        
	        sb.append(s.getPatient().getId()).append("|").append(s.getPatient().getName()).append("|").append(s.getRx()).append("|")
	        .append(s.getSig()).append("|").append(s.getMitte()).append("|").append(s.getInventoryItem().getDosage()).append("|")
	        .append(s.getDate()).append("|").append(s.getTime()).append("|")
	        .append(s.getInventoryItem().getInventory())
	        .append("|").append(pics).append("|").append(s.getInventoryItem().getBin()).append("\n");
	        
	        out.write(sb.toString());
	        
	        //Close the output stream
	        out.close();
			LOGGER.log(Level.INFO, "CSV saved! Script #: "+s.getRx());
	    }catch (Exception e){//Catch exception if any
	      System.err.println("Error: " + e.getMessage());
	    }	
	}

	public List<Script> loadXMLForReading(){
		XMLHelper xmlHelper = XMLHelper.getInstance();
		List<Script> list = new ArrayList<Script>();
		MasterModel.getInstance().getPatientList().clear();
		
		Document doc = loadXML();
		NodeList nodeListPatient = doc.getElementsByTagName("patient");
		for (int i=0;i<nodeListPatient.getLength();i++){
			Node node = nodeListPatient.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element patientElement = (Element) node;
				Patient patient = new Patient();
				patient.setId(xmlHelper.getAttributeValue(patientElement, XMLConstants.ID));
				patient.setName(xmlHelper.getAttributeValue(patientElement, XMLConstants.NAME));
				patient.setCity(xmlHelper.getAttributeValue(patientElement, XMLConstants.CITY));
				MasterModel.getInstance().getPatientList().add(patient);
			}
		}

		NodeList nodeList = doc.getElementsByTagName("script");

		for (int i=0;i<nodeList.getLength();i++){
			Script s = new Script();
			Node node = nodeList.item(i);
			
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				s.setRx(xmlHelper.getAttributeValue(element, XMLConstants.RX));
				s.setDate(xmlHelper.getAttributeValue(element, XMLConstants.DATE));
				s.setTime(xmlHelper.getAttributeValue(element, XMLConstants.TIME));
				s.setMitte(xmlHelper.getAttributeValue(element, XMLConstants.MITTE));
				s.setPics(xmlHelper.getAttributeValue(element, XMLConstants.PICS));

				s.setSig(xmlHelper.getElementValue(element, XMLConstants.SIG));
				String patientRef = xmlHelper.getElementValue(element, XMLConstants.PATIENT_REF);
				s.setPatient(PatientModelProvider.getInstance().findById(patientRef));
				
				Element inventoryitem = (Element)element.getElementsByTagName("inventoryitem").item(0);
				InventoryItem ii = new InventoryItem();
				s.setInventoryItem(ii);
				ii.setBin(xmlHelper.getAttributeValue(inventoryitem, XMLConstants.BIN));
				ii.setPictures(xmlHelper.getAttributeValue(inventoryitem, XMLConstants.PICS));
				ii.setInventory(xmlHelper.getAttributeIntValue(inventoryitem, XMLConstants.INVENTORY));
				ii.setDispensed(xmlHelper.getAttributeIntValue(inventoryitem, XMLConstants.DISPENSED));

				ii.setDosage(xmlHelper.getElementValue(inventoryitem, XMLConstants.DOSAGE));
				ii.setSig(xmlHelper.getElementValue(inventoryitem, XMLConstants.SIG));
			}
				
			list.add(0, s);
		}
		return list;
		
	}
	
	public List<Script> load(){
		return loadXMLForReading();
	}
	
	public List<Script> loadCSV(){
		List<Script> list = new ArrayList<Script>();
		
	    try{
	    	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		    // Create file 
				File logFile = new File(store.getString(PreferenceConstants.P_PWD) + "\\" + store.getString(PreferenceConstants.P_LOGFILE) + ".txt");
				if (!logFile.exists()){
					logFile.createNewFile();
				}
				FileReader fstream = new FileReader(logFile);
				BufferedReader in = new BufferedReader(fstream);
				String sb = in.readLine();
		        while (sb!=null){
					String arr[] = sb.split("\\|");
					if (arr.length==11){
						Patient p = new Patient();
						Script s = new Script();
						p.setId(arr[0]);
						p.setName(arr[1]);
						s.setPatient(p);
						
						s.setRx(arr[2]);
						s.setSig(arr[3]);
						s.setMitte(arr[4]);
						InventoryItem i = new InventoryItem();
						i.setDosage(arr[5]);
						s.setDate(arr[6]);
						s.setTime(arr[7]);
						i.setInventory(Integer.parseInt(arr[8]));
						s.setPics(arr[9]);
						i.setBin(arr[10]);
						s.setInventoryItem(i);
						list.add(0, s);
					} else {
						LOGGER.log(Level.INFO, "Skipping"+sb);
					}
		        	
		        	sb = in.readLine();
		        }
		        //Close the input stream
		        in.close();
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
		
		return list;
	}
}
