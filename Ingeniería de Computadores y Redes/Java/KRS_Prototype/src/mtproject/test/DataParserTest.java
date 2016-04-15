/**
 * 
 */
package mtproject.test;

import mtproject.parsers.*;
import mtproject.objects.*;
import junit.framework.TestCase;
import org.junit.Test;
import java.io.IOException;
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
			String test_http_reply_code;
			String test_http_method;
			int test_duration_milliseconds;
			String test_content_type_MCT;
			String test_content_type;
			String test_server_or_cache_address;
			String test_time;
			String test_squid_hierarchy;
			int test_bytes;
			String test_url;
			Boolean test_url_is_IP;
			Boolean test_url_has_subdomains;
			int test_num_subdomains;
			String test_subdomain5;
			String test_subdomain4;
			String test_subdomain3;
			String test_subdomain2;
			String test_subdomain1;
			String test_url_core;
			String test_url_TLD;
			Boolean test_url_has_path;
			String test_folder1;
			String test_folder2;
			Boolean test_url_has_file_extension;
			String test_file_extension;
			String test_url_protocol;
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
				test_url_is_IP = testEntries.get(i).getURL_is_IP();
				test_url_has_subdomains = testEntries.get(i).getURL_has_subdomains();
				test_num_subdomains = testEntries.get(i).getNum_subdomains();
				test_subdomain5 = testEntries.get(i).getSubdomain5();
				test_subdomain4 = testEntries.get(i).getSubdomain4();
				test_subdomain3 = testEntries.get(i).getSubdomain3();
				test_subdomain2 = testEntries.get(i).getSubdomain2();
				test_subdomain1 = testEntries.get(i).getSubdomain1();
				test_url_core = testEntries.get(i).getURL_core();
				test_url_TLD = testEntries.get(i).getTLD();
				test_url_has_path = testEntries.get(i).getURL_has_Path();
				test_folder1 = testEntries.get(i).getURL_folder1();
				test_folder2 = testEntries.get(i).getURL_folder2();
				test_url_has_file_extension = testEntries.get(i).getURL_has_file();
				test_file_extension = testEntries.get(i).getURL_file();
				test_url_protocol = testEntries.get(i).getURL_protocol();
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
				System.out.println("test_url_is_IP: "+test_url_is_IP+"\n");
				System.out.println("test_url_has_subdomains: "+test_url_has_subdomains+"\n");
				System.out.println("test_num_subdomains: "+test_num_subdomains+"\n");
				System.out.println("test_subdomain5: "+test_subdomain5+"\n");
				System.out.println("test_subdomain4: "+test_subdomain4+"\n");
				System.out.println("test_subdomain3: "+test_subdomain3+"\n");
				System.out.println("test_subdomain2: "+test_subdomain2+"\n");
				System.out.println("test_subdomain1: "+test_subdomain1+"\n");
				System.out.println("test_url_core: "+test_url_core+"\n");
				System.out.println("test_url_TLD: "+test_url_TLD+"\n");
				System.out.println("test_url_has_path: "+test_url_has_path+"\n");
				System.out.println("test_folder1: "+test_folder1+"\n");
				System.out.println("test_folder2: "+test_folder2+"\n");
				System.out.println("test_url_has_file_extension: "+test_url_has_file_extension+"\n");
				System.out.println("test_file_extension: "+test_file_extension+"\n");
				System.out.println("test_url_protocol: "+test_url_protocol+"\n");
				System.out.println("test_client_address: "+test_client_address+"\n");
				
			}
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}

}
