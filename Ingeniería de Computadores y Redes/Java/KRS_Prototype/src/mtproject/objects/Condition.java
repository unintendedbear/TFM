package mtproject.objects;

public class Condition {
	private String dataType;
	private String relationship;
	private String value;

	public Condition(String dataType, String relationship, String value) {
		// TODO Auto-generated constructor stub
		
		this.dataType = dataType;
		this.relationship = relationship;
		this.value = value;
	}
	
	public String getDataType () {
		
		return this.dataType;
	}
	
	public String getRelationship () {
		
		return this.relationship;
	}
	
	public String getValue () {
		
		return this.value;
	}

}
