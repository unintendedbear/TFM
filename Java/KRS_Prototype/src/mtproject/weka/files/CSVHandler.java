/**
 * 
 */
package mtproject.weka.files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import mtproject.file.handlers.*;
import mtproject.objects.LogEntry;

/**
 * @author osica
 *
 */
public class CSVHandler {

	/**
	 * 
	 */
	public CSVHandler() {
		
	}

	/**
	 * @param args
	 */
	public static String[] obtain_csv(List<LogEntry> labelled_Entries, String file_name, boolean no_labels, String[] attributes) throws Exception {
		
		File CSV_File = new File("/home/paloma/workspace/KRS_Prototype/CSV/"+file_name+".csv");
		
		try {
			
			FileWriter CSV_Data = new FileWriter(CSV_File);
			
			// Primero metemos los atributos
			CSV_Data = set_attributes(CSV_Data, attributes);
			
			int i;
			for (i = 0; i < labelled_Entries.size(); i++) {
				
				if (labelled_Entries.get(i).getLabel() == null && !no_labels) {
					continue;
				}
				
				int j;
				for (j = 0; j < attributes.length; j++) {
					
					switch (attributes[j]) {
					case "http_reply_code":
						CSV_Data.append(""+labelled_Entries.get(i).getHTTP_reply_code());
						CSV_Data.append(',');
						break;
					case "http_method":
						CSV_Data.append(labelled_Entries.get(i).getHTTP_method());
						CSV_Data.append(',');
						break;
					case "duration_milliseconds":
						CSV_Data.append(""+labelled_Entries.get(i).getNum_miliseconds());
						CSV_Data.append(',');
						break;
					case "content_type_MCT":						
						CSV_Data.append(labelled_Entries.get(i).getMCT());
						CSV_Data.append(',');
						break;
					case "content_type":
						CSV_Data.append(labelled_Entries.get(i).getContent_type());
						CSV_Data.append(',');
						break;
					case "server_or_cache_address":
						CSV_Data.append(labelled_Entries.get(i).getIP_server());
						CSV_Data.append(',');
						break;
					case "time":
						CSV_Data.append(labelled_Entries.get(i).getTime());
						CSV_Data.append(',');
						break;
					case "squid_hierarchy":
						CSV_Data.append(labelled_Entries.get(i).getSquid_hierarchy());
						CSV_Data.append(',');
						break;
					case "bytes":
						CSV_Data.append(""+labelled_Entries.get(i).getBytes());
						CSV_Data.append(',');
						break;
					case "URL_length":
						CSV_Data.append(""+labelled_Entries.get(i).getURL_Length());
						CSV_Data.append(',');
						break;
					case "letters_in_URL":
						CSV_Data.append(""+labelled_Entries.get(i).getNum_Letters());
						CSV_Data.append(',');
						break;
					case "digits_in_URL":
						CSV_Data.append(""+labelled_Entries.get(i).getNum_Digits());
						CSV_Data.append(',');
						break;
					case "nonalphanumeric_chars_in_URL":
						CSV_Data.append(""+labelled_Entries.get(i).getNum_Characters());
						CSV_Data.append(',');
						break;
					case "url_is_IP":
						if (labelled_Entries.get(i).getURL_is_IP()) {
							CSV_Data.append("1");
						} else {
							CSV_Data.append("0");
						}
						CSV_Data.append(',');
						break;
					case "url_has_subdomains":
						if (labelled_Entries.get(i).getURL_has_subdomains()) {
							CSV_Data.append("1");
						} else {
							CSV_Data.append("0");
						}
						CSV_Data.append(',');
						break;
					case "num_subdomains":
						CSV_Data.append(""+labelled_Entries.get(i).getNum_subdomains());
						CSV_Data.append(',');
						break;
					case "subdomain5":
						CSV_Data.append(labelled_Entries.get(i).getSubdomain5());
						CSV_Data.append(',');
						break;
					case "subdomain4":
						CSV_Data.append(labelled_Entries.get(i).getSubdomain4());
						CSV_Data.append(',');
						break;
					case "subdomain3":
						CSV_Data.append(labelled_Entries.get(i).getSubdomain3());
						CSV_Data.append(',');
						break;
					case "subdomain2":
						CSV_Data.append(labelled_Entries.get(i).getSubdomain2());
						CSV_Data.append(',');
						break;
					case "subdomain1":
						CSV_Data.append(labelled_Entries.get(i).getSubdomain1());
						CSV_Data.append(',');
						break;
					case "url_core":
						CSV_Data.append(labelled_Entries.get(i).getURL_core());
						CSV_Data.append(',');
						break;
					case "url_TLD":
						CSV_Data.append(labelled_Entries.get(i).getTLD());
						CSV_Data.append(',');
						break;
					case "url_has_path":
						if (labelled_Entries.get(i).getURL_has_Path()) {
							CSV_Data.append("1");
						} else {
							CSV_Data.append("0");
						}
						CSV_Data.append(',');
						break;
					case "folder1":
						CSV_Data.append(labelled_Entries.get(i).getURL_folder1());
						CSV_Data.append(',');
						break;
					case "folder2":
						CSV_Data.append(labelled_Entries.get(i).getURL_folder2());
						CSV_Data.append(',');
						break;
					case "path_has_parameters":
						if (labelled_Entries.get(i).getPath_has_parameters()) {
							CSV_Data.append("1");
						} else {
							CSV_Data.append("0");
						}
						CSV_Data.append(',');
						break;
					case "num_parameters":
						CSV_Data.append(""+labelled_Entries.get(i).getNum_parameters());
						CSV_Data.append(',');
						break;
					case "url_has_file_extension":
						if (labelled_Entries.get(i).getURL_has_file()) {
							CSV_Data.append("1");
						} else {
							CSV_Data.append("0");
						}
						CSV_Data.append(',');
						break;
					case "filename_length":
						CSV_Data.append(""+labelled_Entries.get(i).getFile_length());
						CSV_Data.append(',');
						break;
					case "letters_in_filename":
						CSV_Data.append(""+labelled_Entries.get(i).getFile_num_letters());
						CSV_Data.append(',');
						break;
					case "digits_in_filename":
						CSV_Data.append(""+labelled_Entries.get(i).getFile_num_digits());
						CSV_Data.append(',');
						break;
					case "other_char_in_filename":
						CSV_Data.append(""+labelled_Entries.get(i).getFile_num_other_char());
						CSV_Data.append(',');
						break;
					case "file_extension":
						CSV_Data.append(labelled_Entries.get(i).getURL_file());
						CSV_Data.append(',');
						break;
					case "url_protocol":
						CSV_Data.append(labelled_Entries.get(i).getURL_protocol());
						CSV_Data.append(',');
						break;
					case "client_address":
						CSV_Data.append(labelled_Entries.get(i).getIP_client());
						CSV_Data.append(',');
						break;
					}
					
				}
				if (labelled_Entries.get(i).getLabel() != null) {
					CSV_Data.append(labelled_Entries.get(i).getLabel());
					CSV_Data.append('\n');
				} else {
					CSV_Data.append("no_label");
					CSV_Data.append('\n');
				}
				
			}
			
			 CSV_Data.flush();
			 CSV_Data.close();
			 
			 String[] csv_path_data = new String[2];
			 csv_path_data[0] = CSV_File.getName();
			 csv_path_data[1] = CSV_File.getAbsolutePath();
			 return csv_path_data;
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}		

	}
	
	/**
	 * @param args
	 */
	public static String[] undersampling (String[] CSV_File_input, String[] attributes) throws Exception {
		
		String File_name = CSV_File_input[0].substring(0, CSV_File_input[0].length()-4);
		File CSV_File = new File("/home/paloma/workspace/KRS_Prototype/CSV/"+File_name+"_undersampled.csv");
			
		try {
						
			ReadFile in_file = new ReadFile(CSV_File_input[1]);
			FileWriter CSV_File_output = new FileWriter(CSV_File);
			
			String[] arrayLines = in_file.OpenFile();
			
			int i;
			for ( i = 0; i < arrayLines.length; i++ ) {
				
				float rand = (float) Math.random();
				
				if (arrayLines[i].contains("allow")) {
					if (rand < 0.5){
						CSV_File_output.append(arrayLines[i]);
					} else {
						continue;
					}
				} else {
					CSV_File_output.append(arrayLines[i]);
				}
				
				CSV_File_output.append('\n');
			}
			
			CSV_File_output.flush();
			CSV_File_output.close();
			
			String[] csv_path_data = new String[2];
			csv_path_data[0] = CSV_File.getName();
			csv_path_data[1] = CSV_File.getAbsolutePath();
			return csv_path_data;
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}		

	}
	
	/**
	 * @param args
	 */
	public static String[] oversampling (String[] CSV_File_input, String[] attributes) throws Exception {
		
		String File_name = CSV_File_input[0].substring(0, CSV_File_input[0].length()-4);
		File CSV_File = new File("/home/paloma/workspace/KRS_Prototype/CSV/"+File_name+"_oversampled.csv");
					
		try {
			
			ReadFile in_file = new ReadFile(CSV_File_input[1]);
			FileWriter CSV_File_output = new FileWriter(CSV_File);
			
			String[] arrayLines = in_file.OpenFile();
			
			int i;
			for ( i = 0; i < arrayLines.length; i++ ) {
				
				if (arrayLines[i].contains("deny")){
					CSV_File_output.append(arrayLines[i]);
					CSV_File_output.append('\n');
					CSV_File_output.append(arrayLines[i]);
				} else {
					CSV_File_output.append(arrayLines[i]);
				}
				
				CSV_File_output.append('\n');
			}
			
			CSV_File_output.flush();
			CSV_File_output.close();
			
			String[] csv_path_data = new String[2];
			csv_path_data[0] = CSV_File.getName();
			csv_path_data[1] = CSV_File.getAbsolutePath();
			return csv_path_data;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}		

	}
	
	/**
	 * @param args
	 */
	public static String[] training_test_random (String[] CSV_File_input, String type, int training_percentage, String[] attributes) throws IOException {
		
		String File_name = CSV_File_input[0].substring(0, CSV_File_input[0].length()-4);
			
		try {
			
			ReadFile in_file = new ReadFile(CSV_File_input[1]);
			
			String[] arrayLines = in_file.OpenFile();
			File CSV_training_File = null;
			File CSV_test_File = null;
			FileWriter training_file = null;
			FileWriter test_file = null;
			
			float test_percentage = 100-training_percentage;
								
			CSV_training_File = new File("/home/paloma/workspace/KRS_Prototype/CSV/"+File_name+"_training_"+training_percentage+"_random.csv");
			CSV_test_File = new File("/home/paloma/workspace/KRS_Prototype/CSV/"+File_name+"_test_"+(int)test_percentage+"_random.csv");
			training_file = new FileWriter(CSV_training_File);
			test_file = new FileWriter(CSV_test_File);
			
			training_file = set_attributes(training_file, attributes);
			test_file = set_attributes(test_file, attributes);
			
			int j;
			int count = 0;
			for ( j = 0; j < arrayLines.length-1; j++ ) {
				double temp = Math.random();
				double rand = Math.rint(temp*10)/10;
				if (arrayLines[j].contentEquals(arrayLines[j+1]) && type.compareTo("oversampling") == 0) {
					if (rand < test_percentage/100){
						test_file.append(arrayLines[j]);
						test_file.append('\n');
						test_file.append(arrayLines[j+1]);
						test_file.append('\n');
						j++;
					} else {
						training_file.append(arrayLines[j]);
						training_file.append('\n');
						training_file.append(arrayLines[j+1]);
						training_file.append('\n');
						j++;
					}
				} else {
					
					if (rand < test_percentage/100){
						test_file.append(arrayLines[j]);
						test_file.append('\n');
						count++;
					} else {
						training_file.append(arrayLines[j]);
						training_file.append('\n');
					}
					
				} // else
			} // for j
			System.out.println(count);
			
			training_file.flush();
			training_file.close();
			test_file.flush();
			test_file.close();
			
			String[] csv_path_data = new String[4];
			csv_path_data[0] = CSV_training_File.getName();
			csv_path_data[1] = CSV_training_File.getAbsolutePath();
			csv_path_data[2] = CSV_test_File.getName();
			csv_path_data[3] = CSV_test_File.getAbsolutePath();
			return csv_path_data;
			
		} catch (IOException e) {
			System.out.println("Error: "+e.getMessage());
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		

	}
	
	/**
	 * @param args
	 */
	public static String[] training_test_consecutive (String[] CSV_File_input, String type, int training_percentage, String[] attributes) throws Exception {
		
		String File_name = CSV_File_input[0].substring(0, CSV_File_input[0].length()-4);
			
		try {
			
			ReadFile in_file = new ReadFile(CSV_File_input[1]);
			
			String[] arrayLines = in_file.OpenFile();
			File CSV_training_File = null;
			File CSV_test_File = null;
			FileWriter training_file = null;
			FileWriter test_file = null;
				
			float test_percentage = 100-training_percentage;
				
			CSV_training_File = new File("/home/paloma/workspace/KRS_Prototype/CSV/"+File_name+"_training_"+training_percentage+"_consecutive.csv");
			CSV_test_File = new File("/home/paloma/workspace/KRS_Prototype/CSV/"+File_name+"_test_"+(int)test_percentage+"_consecutive.csv");
			training_file = new FileWriter(CSV_training_File);
			test_file = new FileWriter(CSV_test_File);
			
			training_file = set_attributes(training_file, attributes);
			test_file = set_attributes(test_file, attributes);
			
			int training_length = arrayLines.length*training_percentage/100;
			int allow_training, deny_training;
			allow_training = training_length/2;
			deny_training = training_length - allow_training;
			
			int j;
			for ( j = 1; j < arrayLines.length-1; j++ ) {
				if (!arrayLines[j].contentEquals(arrayLines[j+1])) {
					if (allow_training > 0 && arrayLines[j].contains("allow")){
						training_file.append(arrayLines[j]);
						training_file.append('\n');
						allow_training--;
					} else if (deny_training > 0 && arrayLines[j].contains("deny")) {
						training_file.append(arrayLines[j]);
						training_file.append('\n');
						deny_training--;
					} else {
						test_file.append(arrayLines[j]);
						test_file.append('\n');
					}
				} else {
					if (allow_training > 0 && arrayLines[j].contains("allow")){
						training_file.append(arrayLines[j]);
						training_file.append('\n');
						allow_training--;
					} else if (deny_training > 0 && arrayLines[j].contains("deny")) {
						training_file.append(arrayLines[j]);
						training_file.append('\n');
						training_file.append(arrayLines[j+1]);
						training_file.append('\n');
						deny_training -= 2;
						j++;
					} else {
						test_file.append(arrayLines[j]);
						test_file.append('\n');
						if (arrayLines[j].contains("deny")) {
							test_file.append(arrayLines[j+1]);
							test_file.append('\n');
							j++;
						}
					}
				} // else
				
			} // for j 
			
			training_file.flush();
			training_file.close();
			test_file.flush();
			test_file.close();
			
			String[] csv_path_data = new String[4];
			csv_path_data[0] = CSV_training_File.getName();
			csv_path_data[1] = CSV_training_File.getAbsolutePath();
			csv_path_data[2] = CSV_test_File.getName();
			csv_path_data[3] = CSV_test_File.getAbsolutePath();
			return csv_path_data;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}		

	}
	
	/**
	 * @param args
	 * @return 
	 */
	public static FileWriter set_attributes (FileWriter file, String[] attrib) throws Exception {
		
		try{
			file.append(StringUtils.join(attrib, ","));
			file.append('\n');
			
			return file;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

	}

}