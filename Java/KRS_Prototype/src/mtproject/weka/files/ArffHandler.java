/**
 * 
 */
package mtproject.weka.files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mtproject.file.handlers.*;
import mtproject.objects.LogEntry;
import mtproject.objects.Rule;
import mtproject.parsers.DataParser;
import mtproject.parsers.RuleParser;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * @author osica
 *
 */
public class ArffHandler {

	/**
	 * 
	 */
	public ArffHandler() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static String[] obtain_arff (String[] CSV_File_name) throws Exception {
		// TODO Auto-generated method stub
			
		try {
			
			String ARFF_File_name = CSV_File_name[0].substring(0, CSV_File_name[0].length()-4);
			File ARFF_File = new File("/home/paloma/workspace/KRS_Prototype/ARFF/"+ARFF_File_name+".arff");
			
			// Obtener las instancias
			DataSource source = new DataSource(CSV_File_name[1]);
			Instances instances = source.getDataSet();
			 
			// Make the last attribute be the class
			instances.setClassIndex(instances.numAttributes() - 1);
			
			ArffSaver saver = new ArffSaver();
			saver.setInstances(instances);
			saver.setFile(ARFF_File);
			saver.writeBatch();
			
			String[] arff_path_data = new String[2];
			arff_path_data[0] = ARFF_File.getName();
			arff_path_data[1] = ARFF_File.getAbsolutePath();
			return arff_path_data;
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}		

	}
	

}
