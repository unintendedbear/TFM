/**
 * 
 */
package mtproject.objects;

/**
 * @author paloma
 *
 */
public class Session {
	
	private String client_session_IP;
	private int num_allows;
	private int num_denies;
	private int session_bytes;
	private int session_ms;
	private int session_latency;
	private int num_servers;
	private int num_core_domains;
	private boolean if_error;

	/**
	 * 
	 */
	public Session(String client_session_IP, int num_allows, int num_denies, int session_bytes, int session_ms, int session_latency, int num_servers, int num_core_domains, boolean if_error) {
		// TODO Auto-generated constructor stub
		
		this.client_session_IP = client_session_IP;
		this.num_allows = num_allows;
		this.num_denies = num_denies;
		this.session_bytes = session_bytes;
		this.session_ms = session_ms;
		this.session_latency = session_latency;
		this.num_servers = num_servers;
		this.num_core_domains = num_core_domains;
		this.if_error = if_error;
	}

	/**
	 * @param args
	 */
	public int getClient_IP() {
		
		return this.getClient_IP();

	}
	
	public int getNum_allows() {
		
		return this.num_allows;
		
	}
	
	public int getNum_denies() {
		
		return this.num_denies;
		
	}
	
	public int getSession_bytes() {
		
		return this.session_bytes;
		
	}
	
	public int getSession_ms() {
		
		return this.session_ms;
		
	}
	
	public int getSession_latency() {
		
		return this.session_latency;
		
	}
	
	public int getNum_servers() {
		
		return this.num_servers;
		
	}
	
	public int getNum_core_domains() {
		
		return this.num_core_domains;
		
	}
	
	public boolean getIf_error() {
		
		return this.if_error;
		
	}

}
