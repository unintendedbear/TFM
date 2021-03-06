import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mtproject.parsers.RuleParser;
import mtproject.parsers.DataParser;
import mtproject.weka.files.ArffHandler;
import mtproject.weka.files.CSVHandler;
import mtproject.weka.files.ExperimentRunner;
import mtproject.file.handlers.Labeller;
import mtproject.objects.Rule;
import mtproject.objects.LogEntry;

public class MainClass {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		
		/*
		 * This was the very first line of the project and so it should remain here.
		 */
		//System.out.println("Hola u_u");
		
		//long time_start, time_end;
		//time_start = System.currentTimeMillis();
		
		/*String[] csv_paths = {"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated.csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Training_80_random(1).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Training_80_random(2).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Training_80_random(3).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Training_80_random(4).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Training_80_random(5).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Training_80_random(6).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Training_80_random(7).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Training_80_random(8).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Training_80_random(9).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Training_80_random(10).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Test_20_random(1).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Test_20_random(2).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Test_20_random(3).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Test_20_random(4).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Test_20_random(5).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Test_20_random(6).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Test_20_random(7).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Test_20_random(8).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Test_20_random(9).csv",
				"/home/paloma/workspace/KRS_Prototype/CSV/Paper/data_100k_33features_without_duplicated_Test_20_random(10).csv"};
		
		for (int ind = 0; ind < csv_paths.length; ind++) {
			String name = csv_paths[ind].substring(47, csv_paths[ind].length());
			String[] input = {name, csv_paths[ind]};
			System.out.println("Creating ARFF...");
			try {
				String[] ARFF_File_name = ArffHandler.obtain_arff(input);
				System.out.println("ARFF at "+ARFF_File_name[1]);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}*/
		
		List<LogEntry> unlabelled_Entries = new ArrayList<LogEntry>();
		List<Rule> DRL_Rules = new ArrayList<Rule>();
		
		/*String[] attributes = {"http_reply_code", "http_method", "duration_milliseconds", "content_type_MCT", "content_type",
				"server_or_cache_address", "time", "squid_hierarchy", "bytes", "URL_length", "letters_in_URL", "digits_in_URL",
				"nonalphanumeric_chars_in_URL", "url_is_IP", "url_has_subdomains", "num_subdomains", "subdomain5", "subdomain4",
				"subdomain3", "subdomain2", "subdomain1", "url_core", "url_TLD", "url_has_path", "folder1", "folder2",
				"path_has_parameters", "num_parameters", "url_has_file_extension", "filename_length", "letters_in_filename",
				"digits_in_filename", "other_char_in_filename", "file_extension", "url_protocol", "client_address"};*/
		
		/*String[] attributes = {"http_reply_code", "http_method", "duration_milliseconds", "content_type_MCT", "content_type",
				"server_or_cache_address", "squid_hierarchy", "bytes", "URL_length", "letters_in_URL", "digits_in_URL",
				"nonalphanumeric_chars_in_URL", "url_is_IP", "url_has_subdomains", "num_subdomains", "subdomain4",
				"subdomain3", "subdomain2", "subdomain1", "url_core", "url_TLD", "url_has_path", "folder1", "folder2",
				"path_has_parameters", "num_parameters", "url_has_file_extension", "filename_length", "letters_in_filename",
				"digits_in_filename", "other_char_in_filename", "file_extension", "url_protocol", "label"};*/
		
		/*String[] attributes = {"http_method", "duration_milliseconds", "content_type_MCT", "content_type",
				"server_or_cache_address", "bytes", "URL_length", "letters_in_URL", "digits_in_URL",
				"nonalphanumeric_chars_in_URL", "subdomain2", "subdomain1", "url_TLD", "folder2",
				"num_parameters", "url_has_file_extension", "filename_length", "letters_in_filename",
				"other_char_in_filename", "label"};*/
		
		/*String[] attributes = {"http_reply_code", "http_method", "content_type",
				"server_or_cache_address", "URL_length", "letters_in_URL", "digits_in_URL",
				"nonalphanumeric_chars_in_URL", "num_subdomains", "subdomain2", "subdomain1", "url_TLD", "folder1", "folder2",
				"path_has_parameters", "num_parameters", "digits_in_filename", "other_char_in_filename", "file_extension", "label"};*/
		
		/*String[] attributes = {"http_reply_code", "http_method", "duration_milliseconds", "content_type_MCT", "content_type",
				"server_or_cache_address", "time", "squid_hierarchy", "bytes", "url_core", "client_address", "label"};*/
		
		/*String[] attributes = {"http_reply_code", "duration_milliseconds", "content_type",
				"server_or_cache_address", "time", "bytes", "url_core", "client_address", "label"};*/
		
		String[] attributes = {"http_reply_code", "http_method", "content_type_MCT", "content_type",
				"server_or_cache_address", "time", "squid_hierarchy", "url_core", "client_address", "label"};
		
		try {
		
			System.out.println("Parsing log...");
			unlabelled_Entries = DataParser.parsing_Log();			
			
			System.out.println("Parsing rules...");
			DRL_Rules = RuleParser.parsing_DRL();
			
			System.out.println("Labelling...");
			List<LogEntry> labelled_Entries = Labeller.obtain_labels(unlabelled_Entries, DRL_Rules);
			
			//time_end = System.currentTimeMillis();
			//System.out.println("the task has taken "+ ( time_end - time_start ) +" milliseconds");
			
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
			
			//Double[] percentages = new Double[attributes.length];
			
			//int i;
			//for ( i = 0; i < attributes.length; i++) {
				
				//String[] subattributes = obtain_subarray(attributes, i+2);				
				//subattributes[i+1] = "label";
				
				/*for (int k=0; k<subattributes.length; k++) {
					System.out.println(k+"-"+subattributes[k]);
				}*/				
				
				System.out.println("Obtaining CSV...");
				String[] CSV_File_name = CSVHandler.obtain_csv(labelled_Entries, "data_100k_instances_url_log_w_labels", false, attributes);
				System.out.println("CSV at "+CSV_File_name[1]);
				
				System.out.println("Creating ARFF...");
				String[] ARFF_File_name = ArffHandler.obtain_arff(CSV_File_name);
				System.out.println("ARFF at "+ARFF_File_name[1]);
				
				System.out.println("Launching "+experiments[0]+"...");
				Double temp_percentage = ExperimentRunner.experimenter(ARFF_File_name, experiments[0]);
				if (!temp_percentage.isNaN()) {	
					//percentages[i] = temp_percentage;
					//System.out.println(i+","+percentages[i]);
					System.out.println(temp_percentage);
				}
			//}
			
			//for (int j=0; j<percentages.length; j++) {
			//	System.out.println(j+","+percentages[j]);
			//}
			
			/**
			 * Pruebas CSV
			 
			
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
			String[] ARFF_File_name_ts2 = ArffHandler.obtain_arff(ts_files2);*/
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	public static String[] obtain_subarray(String[] initial_array, int length) {
		
		String[] final_array = new String[length];
		
		int array_index;
		for (array_index = 0; array_index < length-1; array_index++) {
			
			final_array[array_index] = initial_array[array_index];
			
		}
		
		return final_array;
		
	}
	

}
