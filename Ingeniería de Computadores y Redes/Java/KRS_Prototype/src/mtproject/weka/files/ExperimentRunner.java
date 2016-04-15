/**
 * 
 */
package mtproject.weka.files;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.DefaultListModel;

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
import weka.experiment.ResultMatrix;
import weka.experiment.ResultMatrixPlainText;
import weka.experiment.SplitEvaluator;

/**
 * @author paloma
 *
 */
public class ExperimentRunner {

	/**
	 * @param args
	 * @return 
	 * @throws Exception 
	 */
	public static Double experimenter(String[] ARFF_File_input, String classifier_type) throws Exception {
		
		System.out.println("Setting up...");
		Experiment exp = new Experiment();
	    exp.setPropertyArray(new Classifier[0]);
	    exp.setUsePropertyIterator(true);
	    
	    SplitEvaluator se  = new ClassifierSplitEvaluator();
	    Classifier sec = ((ClassifierSplitEvaluator) se).getClassifier();
	    
	    CrossValidationResultProducer cvrp = new CrossValidationResultProducer();
	    cvrp.setNumFolds(10);
	    cvrp.setSplitEvaluator(se);
	      
	    PropertyNode[] propertyPath = new PropertyNode[2];
	    try {
	    	propertyPath[0] = new PropertyNode(se,
	    			new PropertyDescriptor("splitEvaluator",
	    					CrossValidationResultProducer.class),
	    					CrossValidationResultProducer.class);
	    	propertyPath[1] = new PropertyNode( sec,
	    			new PropertyDescriptor("classifier",
	    					se.getClass()),
	    					se.getClass());
	    	} catch (IntrospectionException e) {
	    		e.printStackTrace();
	    	}	    
	    exp.setResultProducer(cvrp);
	    exp.setPropertyPath(propertyPath);
	    
	    exp.setRunLower(1);
	    exp.setRunUpper(1);
	    
	    String[] options = Utils.splitOptions(classifier_type);
	    String classname = options[0];
	    options[0]       = "";
	    Classifier c     = (Classifier) Utils.forName(Classifier.class, classname, options);
	    exp.setPropertyArray(new Classifier[]{c});
	    
	    DefaultListModel model = new DefaultListModel();
	    File file = new File(ARFF_File_input[1]);
		model.addElement(file);
		exp.setDatasets(model);
		
		InstancesResultListener irl = new InstancesResultListener();
	    String ARFF_File_name = ARFF_File_input[0].substring(0, ARFF_File_input[0].length()-5);
		File ARFF_File_output = new File("/home/paloma/workspace/KRS_Prototype/ARFF/"+ARFF_File_name+"_experimenter_results.arff");
	    irl.setOutputFile(ARFF_File_output);
	    exp.setResultListener(irl);
	   
	    System.out.println("Initializing...");
	    exp.initialize();
	    System.out.println("Running...");
	    exp.runExperiment();
	    System.out.println("Finishing...");
	    exp.postProcess();
	    
	    PairedTTester tester = new PairedCorrectedTTester();
	    Instances result = new Instances(new BufferedReader(new FileReader(irl.getOutputFile())));
	    tester.setInstances(result);
	    tester.setSortColumn(-1);
	    tester.setRunColumn(result.attribute("Key_Run").index());
	    tester.setFoldColumn(result.attribute("Key_Fold").index());
	    tester.setDatasetKeyColumns(
		new Range(
		    "" 
		    + (result.attribute("Key_Dataset").index() + 1)));
	    tester.setResultsetKeyColumns(
		new Range(
		    "" 
		    + (result.attribute("Key_Scheme").index() + 1)
		    + ","
		    + (result.attribute("Key_Scheme_options").index() + 1)
		    + ","
		    + (result.attribute("Key_Scheme_version_ID").index() + 1)));
	    tester.setResultMatrix(new ResultMatrixPlainText());
	    tester.setDisplayedResultsets(null);       
	    tester.setSignificanceLevel(0.05);
	    tester.setShowStdDevs(true);
	    // fill result matrix (but discarding the output)
	    tester.multiResultsetFull(0, result.attribute("Percent_correct").index());
	    // output results for reach dataset
	    
	    ResultMatrix matrix = tester.getResultMatrix();
	    return matrix.getMean(0, 0);

	}

}
