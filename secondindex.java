import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;


public class secondindex {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileInputStream instream = null;
		PrintWriter outstream = null;

		try {
			//outstream = new FileInputStream("secondtitle.txt");
			outstream = new PrintWriter(new BufferedWriter(new FileWriter("sindex9")));

			instream = new FileInputStream("/home/ayush/Desktop/IRE/merged/merged9");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(instream,Charset.forName("iso-8859-1")));
		Integer offset=0;
		//outstream.write(offset+"\n");
		String line;
		try {
			while((line=in.readLine())!=null)
			{
				String[] word=line.split("-");
				outstream.write(word[0]+"-"+offset+"\n");
				offset=offset+1+line.codePointCount(0,line.length());

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		outstream.close();
		try {
			instream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
