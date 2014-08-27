package mtproject.objects;

import java.util.List;

public class Rule {
	private List<Condition> conditions;
	private String action;
	
	public Rule (List<Condition> conditions, String action) {
		this.conditions = conditions;
		this.action = action;
	}
	
	public List<Condition> getConditions () {
		
		return this.conditions;
	}
	
	public String getPermission () {
		
		return this.action;
	}
}
