import java.io.IOException;
import mtproject.parsers.*;
import mtproject.objects.*;

public class MainClass {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//System.out.println("Hola u_u");
		
		String drlFile = "/home/paloma/workspace/KRS_Prototype/Rules/Initial-rules-squid.drl";
		
		try {
			
			ReadFile file = new ReadFile(drlFile);
			String[] aryLines = file.OpenFile();
			
			int i;
			for ( i=0; i < aryLines.length; i++ ) {
				System.out.println( aryLines[ i ] ) ;
			}
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
	

}
