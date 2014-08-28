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
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		List<LogEntry> unlabelled_Entries = new ArrayList<LogEntry>();
		List<Rule> DRL_Rules = new ArrayList<Rule>();
		String arffFile = "/home/osica/workspace/KRS_Prototype/ARFFs/.csv";
		
		try {
			
			unlabelled_Entries = DataParser.parsing_Log();
			DRL_Rules = RuleParser.parsing_DRL();
			
			List<LogEntry> labelled_Entries = Labeller.obtain_labels(unlabelled_Entries, DRL_Rules);
			String[] CSV_File_name = CSVHandler.obtain_csv(labelled_Entries, "data_100k_instances_url_log_w_labels");
			String ARFF_File_name = CSV_File_name[0].substring(0, CSV_File_name[0].length()-4);
			File ARFF_File = new File("/home/osica/workspace/KRS_Prototype/ARFFs/"+ARFF_File_name+".arff");
			
			// Obtener las instancias
			DataSource source = new DataSource(CSV_File_name[1]);
			Instances instances = source.getDataSet();
			 
			// Make the last attribute be the class
			instances.setClassIndex(instances.numAttributes() - 1);
			
			ArffSaver saver = new ArffSaver();
			saver.setInstances(instances);
			saver.setFile(ARFF_File);
			saver.writeBatch();
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		

	}

}
