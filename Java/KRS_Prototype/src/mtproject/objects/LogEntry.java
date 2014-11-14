package mtproject.objects;

public class LogEntry {
	private String http_reply_code;
	private String http_method;
	private int duration_milliseconds;
	private String content_type_MCT;
	private String content_type;
	private String server_or_cache_address;
	private String time;
	private String squid_hierarchy;
	private int bytes;
	/*
	 * URL features
	 */
	private String url;
	private Boolean url_is_IP;
	private Boolean url_has_subdomains;
	private int num_subdomains;
	private String subdomain5;
	private String subdomain4;
	private String subdomain3;
	private String subdomain2;
	private String subdomain1;
	private String url_core;
	private String url_TLD;
	private Boolean url_has_path;
	private String folder1;
	private String folder2;
	private Boolean path_has_parameters;
	private int num_parameters;
	private Boolean url_has_file_extension;
	private String file_extension;
	private String url_protocol;
	/**/
	private String client_address;
	private String label;
	
	public LogEntry (String http_reply_code,
			String http_method,
			int duration_milliseconds,
			String content_type_MCT,
			String content_type,
			String server_or_cache_address,
			String time,
			String squid_hierarchy,
			int bytes,
			String url,
			Boolean url_is_IP,
			Boolean url_has_subdomains,
			int num_subdomains,
			String subdomain5,
			String subdomain4,
			String subdomain3,
			String subdomain2,
			String subdomain1,
			String url_core,
			String url_TLD,
			Boolean url_has_path,
			String folder1,
			String folder2,
			Boolean path_has_parameters,
			int num_parameters,
			Boolean url_has_file_extension,
			String file_extension,
			String url_protocol,
			String client_address) {
		
		this.http_reply_code = http_reply_code;
		this.http_method = http_method;
		this.duration_milliseconds = duration_milliseconds;
		this.content_type_MCT = content_type_MCT;
		this.content_type = content_type;
		this.server_or_cache_address = server_or_cache_address;
		this.time = time;
		this.squid_hierarchy = squid_hierarchy;
		this.bytes = bytes;
		this.url = url;
		this.url_is_IP = url_is_IP;
		this.url_has_subdomains = url_has_subdomains;
		this.num_subdomains = num_subdomains;
		this.subdomain5 = subdomain5;
		this.subdomain4 = subdomain4;
		this.subdomain3 = subdomain3;
		this.subdomain2 = subdomain2;
		this.subdomain1 = subdomain1;
		this.url_core = url_core;
		this.url_TLD = url_TLD;
		this.url_has_path = url_has_path;
		this.folder1 = folder1;
		this.folder2 = folder2;
		this.path_has_parameters = path_has_parameters;
		this.num_parameters = num_parameters;
		this.url_has_file_extension = url_has_file_extension;
		this.file_extension = file_extension;
		this.url_protocol = url_protocol;
		this.client_address = client_address;
	}
	
	public LogEntry (String http_reply_code,
			String http_method,
			int duration_milliseconds,
			String content_type_MCT,
			String content_type,
			String server_or_cache_address,
			String time,
			String squid_hierarchy,
			int bytes,
			String url,
			Boolean url_is_IP,
			Boolean url_has_subdomains,
			int num_subdomains,
			String subdomain5,
			String subdomain4,
			String subdomain3,
			String subdomain2,
			String subdomain1,
			String url_core,
			String url_TLD,
			Boolean url_has_path,
			String folder1,
			String folder2,
			Boolean path_has_parameters,
			int num_parameters,
			Boolean url_has_file_extension,
			String file_extension,
			String url_protocol,
			String client_address,
			String label) {
		this.http_reply_code = http_reply_code;
		this.http_method = http_method;
		this.duration_milliseconds = duration_milliseconds;
		this.content_type_MCT = content_type_MCT;
		this.content_type = content_type;
		this.server_or_cache_address = server_or_cache_address;
		this.time = time;
		this.squid_hierarchy = squid_hierarchy;
		this.bytes = bytes;
		this.url = url;
		this.url_is_IP = url_is_IP;
		this.url_has_subdomains = url_has_subdomains;
		this.num_subdomains = num_subdomains;
		this.subdomain5 = subdomain5;
		this.subdomain4 = subdomain4;
		this.subdomain3 = subdomain3;
		this.subdomain2 = subdomain2;
		this.subdomain1 = subdomain1;
		this.url_core = url_core;
		this.url_TLD = url_TLD;
		this.url_has_path = url_has_path;
		this.folder1 = folder1;
		this.folder2 = folder2;
		this.path_has_parameters = path_has_parameters;
		this.num_parameters = num_parameters;
		this.url_has_file_extension = url_has_file_extension;
		this.file_extension = file_extension;
		this.url_protocol = url_protocol;
		this.client_address = client_address;
		this.label = label;
	}
	
	public String getHTTP_reply_code () {
		
		return this.http_reply_code;
	}
	
	public String getHTTP_method () {
		
		return this.http_method;
	}
	
	public int getNum_miliseconds () {
		
		return this.duration_milliseconds;
	}
	
	public String getMCT () {
		
		return this.content_type_MCT;
	}
	
	public String getContent_type () {
		
		return this.content_type;
	}
	
	public String getIP_server () {
		
		return this.server_or_cache_address;
	}
	
	public String getTime () {
		
		return this.time;
	}
	
	public String getSquid_hierarchy () {
		
		return this.squid_hierarchy;
	}
	
	public int getBytes () {
		
		return this.bytes;
	}
	
	public String getURL () {
		
		return this.url;
	}
	
	public Boolean getURL_is_IP () {
		
		return this.url_is_IP;
	}
	
	public Boolean getURL_has_subdomains () {
		
		return this.url_has_subdomains;
	}
	
	public int getNum_subdomains () {
		
		return this.num_subdomains;
	}
	
	public String getSubdomain5 () {
		
		return this.subdomain5;
	}
	
	public String getSubdomain4 () {
		
		return this.subdomain4;
	}
	
	public String getSubdomain3 () {
		
		return this.subdomain3;
	}
	
	public String getSubdomain2 () {
		
		return this.subdomain2;
	}
	
	public String getSubdomain1 () {
		
		return this.subdomain1;
	}
	
	public String getURL_core () {
		
		return this.url_core;
	}
	
	public String getTLD () {
		
		return this.url_TLD;
	}
	
	public Boolean getURL_has_Path () {
		
		return this.url_has_path;
	}
	
	public String getURL_folder1 () {
		
		return this.folder1;
	}
	
	public String getURL_folder2 () {
		
		return this.folder2;
	}
	
	public Boolean getPath_has_parameters() {
		
		return this.path_has_parameters;
	}
	
	public int getNum_parameters () {
		
		return this.num_parameters;
	}
	
	public Boolean getURL_has_file () {
		
		return this.url_has_file_extension;
	}
	
	public String getURL_file () {
		
		return this.file_extension;
	}
	
	public String getURL_protocol () {
		
		return this.url_protocol;
	}
	
	public String getIP_client () {
		
		return this.client_address;
	}
	
	public String getLabel () {
		
		return this.label;
	}
	
	public void setLabel (String permission) {
		
		this.label = permission;
	}
}
