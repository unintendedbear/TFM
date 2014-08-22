package mtproject.test;

import mtproject.parsers.*;
import mtproject.objects.*;
import junit.framework.TestCase;
import org.junit.Test;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class RuleParserTest extends TestCase {

	List<Rule> testRules = new ArrayList<Rule>();
	
	@Test
	public void testRuleCreator() throws IOException {
		
		try {
			testRules = RuleParser.parsing_DRL();
			List<Condition> testConditions = new ArrayList<Condition>();
			boolean testAllow;
			String testDataType;
			String testRelationship;
			String testValue;
			
			int i;
			for ( i = 0; i < testRules.size(); i++) {
				testConditions.clear();
				testConditions = testRules.get(i).getConditions();
				testAllow = testRules.get(i).getPermission();
				System.out.println("Hay una regla con permiso:\n"+testAllow+"\nY con las siguientes condiciones:\n");
				
				int j;
				for ( j = 1; j <= testConditions.size(); j++) {
					testDataType = testConditions.get(j).getDataType();
					testRelationship = testConditions.get(j).getRelationship();
					testValue = testConditions.get(j).getValue();
					
					System.out.println("Condición "+j+"º: "+testDataType+" "+testRelationship+" "+testValue+"\n");
				}
			}
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
