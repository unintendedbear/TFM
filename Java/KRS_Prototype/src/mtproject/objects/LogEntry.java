package mtproject.objects;

import java.util.Date;

public class LogEntry {
	private int http_reply_code;
	private String http_method;
	private int duration_milliseconds;
	private String content_type_MCT;
	private String content_type;
	private String server_or_cache_address;
	private String time;
	private String squid_hierarchy;
	private int bytes;
	private String url;
	private String url_core;
	private String url_TLD;
	private String client_address;
	private String label;
	
	public LogEntry (int http_reply_code, String http_method, int duration_milliseconds, String content_type_MCT, String content_type, String server_or_cache_address, String time, String squid_hierarchy, int bytes, String url, String url_core, String url_TLD, String client_address) {
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
		this.url_core = url_core;
		this.url_TLD = url_TLD;
		this.client_address = client_address;
	}
	
	public int getHTTP_reply_code () {
		
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
	
	public String getURL_core () {
		
		return this.url_core;
	}
	
	public String getTLD () {
		
		return this.url_TLD;
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
