import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mtproject.parsers.RuleParser;
import mtproject.parsers.DataParser;
import mtproject.weka.files.ArffHandler;
import mtproject.weka.files.CSVHandler;
import mtproject.weka.files.ExperimentRunner;
import mtproject.file.handlers.Labeller;
import mtproject.objects.*;

public class MainClass {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		
		/*
		 * This was the very first line of the project and so it should remain here.
		 */
		//System.out.println("Hola u_u");
		
		long time_start, time_end;
		time_start = System.currentTimeMillis();
		
		
		
		List<LogEntry> unlabelled_Entries = new ArrayList<LogEntry>();
		List<Rule> DRL_Rules = new ArrayList<Rule>();
		
		try {
			
			System.out.println("Parsing log...");
			unlabelled_Entries = DataParser.parsing_Log();
			
			
			
			System.out.println("Parsing rules...");
			DRL_Rules = RuleParser.parsing_DRL();
			
																																																																																																																																																																																																																																											
			
			
			
			System.out.println("Labelling...");
			List<LogEntry> labelled_Entries = Labeller.obtain_labels(unlabelled_Entries, DRL_Rules);
			System.out.println("Obtaining CSV...");
			String[] CSV_File_name = CSVHandler.obtain_csv(labelled_Entries, "data_100k_instances_url_log_w_labels", false);
			
			System.out.println("Creating ARFF...");
			String[] ARFF_File_name = ArffHandler.obtain_arff(CSV_File_name);
			
			time_end = System.currentTimeMillis();
			System.out.println("the task has taken "+ ( time_end - time_start ) +" milliseconds");
			
			/**
			 * Pruebas CSV
			 */
			
			System.out.println("Undersampling...");
			String[] file_undersampled = CSVHandler.undersampling(CSV_File_name);
			String[] ARFF_File_name_und = ArffHandler.obtain_arff(file_undersampled);
			
			System.out.println("Oversampling...");
			String[] file_oversampled = CSVHandler.oversampling(CSV_File_name);
			String[] ARFF_File_name_ov = ArffHandler.obtain_arff(file_oversampled);
			
			System.out.println("Obtaining training and test random...");
			String[] files = CSVHandler.training_test_random(file_undersampled, "", 60);
			String[] tr_files = new String[2];
			String[] ts_files = new String[2];
			tr_files[0] = files[0];
			tr_files[1] = files[1];
			ts_files[0] = files[2];
			ts_files[1] = files[3];
			String[] ARFF_File_name_tr = ArffHandler.obtain_arff(tr_files);
			String[] ARFF_File_name_ts = ArffHandler.obtain_arff(ts_files);
			System.out.println("Obtaining training and test consecutive...");
			String[] files2 = CSVHandler.training_test_consecutive(CSV_File_name, "", 80);
			String[] tr_files2 = new String[2];
			String[] ts_files2 = new String[2];
			tr_files2[0] = files2[0];
			tr_files2[1] = files2[1];
			ts_files2[0] = files2[2];
			ts_files2[1] = files2[3];
			String[] ARFF_File_name_tr2 = ArffHandler.obtain_arff(tr_files2);
			String[] ARFF_File_name_ts2 = ArffHandler.obtain_arff(ts_files2);
			
			System.out.println("Creating ARFF...");
			//String[] ARFF_File_name = ArffHandler.obtain_arff(CSV_File_name);
			System.out.println("ARFF at "+ARFF_File_name[1]);
			
			/*
			String[] experiments = new String[11];
			experiments[0] = "NaiveBayes";
			experiments[1] = "DecisionTable -X 1 -S \"weka.attributeSelection.BestFirst -D 1 -N 5\"";
			experiments[2] = "JRip -F 3 -N 2.0 -O 2 -S 1";
			experiments[3] = "OneR -B 6";
			experiments[4] = "PART -M 2 -C 0.25 -Q 1";
			experiments[5] = "ZeroR";
			experiments[6] = "DecisionStump";
			experiments[7] = "J48 -C 0.25 -M 2";
			experiments[8] = "RandomForest -I 10 -K 0 -S 1 -num-slots 1";
			experiments[9] = "RandomTree -K 0 -M 1.0 -V 0.001 -S 1";
			experiments[10] = "REPTree -M 2 -V 0.001 -N 3 -S 1 -L -1 -I 0.0";
			
			Double[] percentages = new Double[experiments.length];
			
			int i;
			for ( i = 0; i < experiments.length; i++) {
				System.out.println("Launching "+experiments[i]+"...");
				Double temp_percentage = ExperimentRunner.experimenter(ARFF_File_name, experiments[i]);
				if (!temp_percentage.isNaN()) {	
					percentages[i] = temp_percentage;
					System.out.println(percentages[i]);
				}
			}*/
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	

}
