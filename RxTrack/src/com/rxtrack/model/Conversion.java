package com.rxtrack.model;

import java.util.ArrayList;
import java.util.List;

import com.rxtrack.util.RxUtil;

public class Conversion {
	
	private int conversionrate = -1;
	private List<ConversionRangeRow> ranges = new ArrayList<ConversionRangeRow>();
	ConversionRangeRow header = null;
	
	public void addRangeRowHeader(String from, String to, String replacement){
		if (RxUtil.isEmpty(from)) from = "";
		if (RxUtil.isEmpty(to)) to = "";
		if (RxUtil.isEmpty(replacement)) replacement = "";
		header = new ConversionRangeRow();
		header.setFrom(from);
		header.setTo(to);
		header.setReplacement(replacement);
	}

	public void addRangeRow(String from, String to, String replacement){
		if (RxUtil.isEmpty(from)) from = "";
		if (RxUtil.isEmpty(to)) to = "";
		if (RxUtil.isEmpty(replacement)) replacement = "";
		ConversionRangeRow crr = new ConversionRangeRow();
		crr.setFrom(from);
		crr.setTo(to);
		crr.setReplacement(replacement);
		ranges.add(crr);
	}
	
	public ConversionRangeRow getRangeHeaderRows(){
		return header;
	}
	
	public List<ConversionRangeRow> getRangeRows(){
		return ranges;
	}
	
	public int getConversionrate() {
		return conversionrate;
	}

	public void setConversionrate(int conversionrate) {
		this.conversionrate = conversionrate;
	}

}
