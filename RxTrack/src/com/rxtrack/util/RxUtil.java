package com.rxtrack.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

import com.rxtrack.ui.preferences.PreferenceConstants;

public class RxUtil {

	public static boolean isEmpty(String str){
		if (str==null || str.trim().length()==0){
			return true;
		}
		return false;
	}
	
	/**
	 * Copy files into <temp>\rxtrack because bpac cannot see the files in the jar files
	 */
	public void createTempImages() {
		List<String>imageList = new ArrayList<String>();
		String picType = "CDEGHILMNPSTUVW";
		for (int i=0;i<picType.length();i++){
			imageList.add("once"+picType.charAt(i)+".bmp");
			imageList.add("twice"+picType.charAt(i)+".bmp");
			imageList.add("thrice"+picType.charAt(i)+".bmp");
		}
		imageList.add("food.bmp");
		imageList.add("alcohol.bmp");
		imageList.add("morning.bmp");
		imageList.add("lunch.bmp");
		imageList.add("evening.bmp");
		imageList.add("night.bmp");
		imageList.add("blank100x100.bmp");
		imageList.add("baby100.bmp");
		imageList.add("kid100.bmp");
		imageList.add("pregnant.bmp");
		imageList.add("jar.bmp");
		String temp  = com.rxtrack.Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_PWD);
		File dir = new File(temp + "\\rxtrack");
		if (!dir.exists()){
			dir.mkdir();
		}
		for (int i=0;i<imageList.size();i++){
			String fileName = temp + "\\rxtrack\\" + imageList.get(i);
			if (!new File(fileName).exists()){
				try {
					Image image = (Image)ImageDescriptor.createFromURL(getClass().getResource("/images/"+imageList.get(i))).createImage();
					ImageData id = image.getImageData();
					ImageLoader il = new ImageLoader();
					il.data = new ImageData[1];
					il.data[0] = id;
					il.data[0].x = 100;
					il.data[0].y = 100;
					il.save(fileName, SWT.IMAGE_BMP);
				} catch (Exception e) {
					System.err.println("Could not process: "+imageList.get(i));
					e.printStackTrace();
				}
			}
		}

		String vbscriptfile = temp + "\\rxtrack\\CreateLabel.vbs";
		if (!new File(vbscriptfile).exists()){
			URL url = getClass().getResource("/vbs/CreateLabel.vbs");
			try {
				URLConnection conn = url.openConnection();
				BufferedReader br = new BufferedReader (new InputStreamReader( conn.getInputStream()));
				String line = br.readLine();
				BufferedWriter out = new BufferedWriter(new FileWriter(vbscriptfile));

				do {
					out.write(line);
					out.write('\n');
					line = br.readLine();
				} while (line!=null);
				out.close();
			} catch (Exception e) {
				System.err.println("Can not copy vbs script.");
				e.printStackTrace();
			}
		}

	}
}
