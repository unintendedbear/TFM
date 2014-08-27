/**
 * 
 */
package mtproject.test;

import mtproject.parsers.*;
import mtproject.objects.*;
import junit.framework.TestCase;
import org.junit.Test;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * @author osica
 *
 */
public class DataParserTest extends TestCase {

	/**
	 * @throws java.lang.Exception
	 */
	
	List<LogEntry> testEntries = new ArrayList<LogEntry>();

	@Test
	public void testLogCreator() {
		
		try {
			testEntries = DataParser.parsing_Log();
			int test_http_reply_code;
			String test_http_method;
			int test_duration_milliseconds;
			String test_content_type_MCT;
			String test_content_type;
			String test_server_or_cache_address;
			String test_time;
			String test_squid_hierarchy;
			int test_bytes;
			String test_url;
			String test_url_core;
			String test_url_TLD;
			String test_client_address;
			
			int i;
			for ( i = 0; i < testEntries.size(); i++) {
				
				test_http_reply_code = testEntries.get(i).getHTTP_reply_code();
				test_http_method = testEntries.get(i).getHTTP_method();
				test_duration_milliseconds = testEntries.get(i).getNum_miliseconds();
				test_content_type_MCT = testEntries.get(i).getMCT();
				test_content_type = testEntries.get(i).getContent_type();
				test_server_or_cache_address = testEntries.get(i).getIP_server();
				test_time = testEntries.get(i).getTime();
				test_squid_hierarchy = testEntries.get(i).getSquid_hierarchy();
				test_bytes = testEntries.get(i).getBytes();
				test_url = testEntries.get(i).getURL();
				test_url_core = testEntries.get(i).getURL_core();
				test_url_TLD = testEntries.get(i).getTLD();
				test_client_address = testEntries.get(i).getIP_client();
				
				System.out.println("Entrada "+i+"º:\n");
				System.out.println("test_http_reply_code: "+test_http_reply_code+"\n");
				System.out.println("test_http_method: "+test_http_method+"\n");
				System.out.println("test_duration_milliseconds: "+test_duration_milliseconds+"\n");
				System.out.println("test_content_type_MCT: "+test_content_type_MCT+"\n");
				System.out.println("test_content_type: "+test_content_type+"\n");
				System.out.println("test_server_or_cache_address: "+test_server_or_cache_address+"\n");
				System.out.println("test_time: "+test_time+"\n");
				System.out.println("test_squid_hierarchy: "+test_squid_hierarchy+"\n");
				System.out.println("test_bytes: "+test_bytes+"\n");
				System.out.println("test_url: "+test_url+"\n");
				System.out.println("test_url_core: "+test_url_core+"\n");
				System.out.println("test_url_TLD: "+test_url_TLD+"\n");
				System.out.println("test_client_address: "+test_client_address+"\n");
				
			}
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}

}
