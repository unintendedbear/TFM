/**
 * 
 */
package mtproject.test;

import mtproject.parsers.*;
import mtproject.file.handlers.Labeller;
import mtproject.objects.*;
import junit.framework.TestCase;
import org.junit.Test;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * @author paloma
 *
 */
public class SessionParserTest extends TestCase {

	/**
	 * @throws java.lang.Exception
	 */
	
	List<Session> testSessions = new ArrayList<Session>();

	@Test
	public void testSessionCreator() {
		try {
			
			List<LogEntry> testUnlabelledEntries = DataParser.parsing_Log();
			List<Rule> testRules = RuleParser.parsing_DRL();
			List<LogEntry> testLabelledEntries = Labeller.obtain_labels(testUnlabelledEntries, testRules);			
			testSessions = SessionParser.log_by_sessions(testLabelledEntries);
			
			String client_session_IP;
			int num_allows;
			int num_denies;
			int session_bytes;
			int session_ms;
			int session_latency;
			int num_servers;
			int num_core_domains;
			boolean if_error;
			
			int i;
			for ( i = 0; i < testSessions.size(); i++) {
				
				client_session_IP = testSessions.get(i).getClient_IP();
				num_allows = testSessions.get(i).getNum_allows();
				num_denies = testSessions.get(i).getNum_denies();
				session_bytes = testSessions.get(i).getSession_bytes();
				session_ms = testSessions.get(i).getSession_ms();
				session_latency = testSessions.get(i).getSession_latency();
				num_servers = testSessions.get(i).getNum_servers();
				num_core_domains = testSessions.get(i).getNum_core_domains();
				if_error = testSessions.get(i).getIf_error();
				
				System.out.println("Sesión "+i+"º:\n");
				System.out.println("client_session_IP: "+client_session_IP+"\n");
				System.out.println("num_allows: "+num_allows+"\n");
				System.out.println("num_denies: "+num_denies+"\n");
				System.out.println("session_bytes: "+session_bytes+"\n");
				System.out.println("session_ms: "+session_ms+"\n");
				System.out.println("session_latency: "+session_latency+"\n");
				System.out.println("num_servers: "+num_servers+"\n");
				System.out.println("num_core_domains: "+num_core_domains+"\n");
				System.out.println("if_error: "+if_error+"\n");
				
			}
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
