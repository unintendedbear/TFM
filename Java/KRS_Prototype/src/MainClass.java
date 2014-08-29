import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mtproject.parsers.*;
import mtproject.weka.files.ArffHandler;
import mtproject.weka.files.CSVHandler;
import mtproject.file.handlers.Labeller;
import mtproject.objects.*;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.Range;
import weka.core.Utils;
import weka.experiment.ClassifierSplitEvaluator;
import weka.experiment.CrossValidationResultProducer;
import weka.experiment.Experiment;
import weka.experiment.InstancesResultListener;
import weka.experiment.PairedCorrectedTTester;
import weka.experiment.PairedTTester;
import weka.experiment.PropertyNode;
import weka.experiment.RandomSplitResultProducer;
import weka.experiment.RegressionSplitEvaluator;
import weka.experiment.ResultMatrix;
import weka.experiment.ResultMatrixPlainText;
import weka.experiment.SplitEvaluator;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.DefaultListModel;

public class MainClass {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		
		/*
		 * This was the very first line of the project and so it should remain here.
		 */
		//System.out.println("Hola u_u");
		
		List<LogEntry> unlabelled_Entries = new ArrayList<LogEntry>();
		List<Rule> DRL_Rules = new ArrayList<Rule>();
		
		try {
			
			unlabelled_Entries = DataParser.parsing_Log();
			DRL_Rules = RuleParser.parsing_DRL();
			
			List<LogEntry> labelled_Entries = Labeller.obtain_labels(unlabelled_Entries, DRL_Rules);
			String[] CSV_File_name = CSVHandler.obtain_csv(labelled_Entries, "data_100k_instances_url_log_w_labels", false);
			String[] ARFF_File_name = ArffHandler.obtain_arff(CSV_File_name);
			
			System.out.print(ARFF_File_name[1]);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	

}
