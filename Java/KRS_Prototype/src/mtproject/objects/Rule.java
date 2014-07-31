package mtproject.objects;

import java.util.List;

public class Rule {
	private List<Condition> conditions;
	private boolean allow;
	
	public Rule (List<Condition> conditions, boolean allow) {
		this.conditions = conditions;
		this.allow = allow;
	}
}
