import java.util.List;

// Objeto tipo regla que lleva una lista de condiciones y 'true' si es allow, o 'false' si es deny

public class Rule {
	private List<Condition> conditions;
	private boolean allow;
	
	public Rule (List<Condition> conditions, boolean allow) {
		this.conditions = conditions;
		this.allow = allow;
	}
}
