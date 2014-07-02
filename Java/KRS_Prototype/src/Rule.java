import java.util.List;
import java.util.ArrayList;

public class Rule {
	private int numConditions;
	private boolean allow;
	private List<Condition> Conditions;
	
	public Rule (List<Condition> Conditions, boolean allow) {
		this.Conditions = Conditions;
		this.allow = allow;
	}
	
	public List getConditions() {
		return this.Conditions;
	}
}
