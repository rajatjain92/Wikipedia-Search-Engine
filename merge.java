import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;


public class merge {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<FileInputStream> fstream=new ArrayList<FileInputStream>();
		ArrayList<BufferedReader> br=new ArrayList<BufferedReader>();
		HashMap<String,ArrayList<Integer>> index=new HashMap<String,ArrayList<Integer>>();
		char mergedcount='0';
		PrintWriter out=null;
		try {
				out = new PrintWriter(new BufferedWriter(new FileWriter("merged"+mergedcount)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean[] finished=new boolean[3260]; 
		int cnt=3260;
		for(int i=0;i<3260;i++)
		{
			try 
				{
					fstream.add(new FileInputStream("/home/ayush/Desktop/IRE/index/index"+i));
				}
			catch (FileNotFoundException e) 
				{
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		for(FileInputStream f:fstream)
		{
			br.add(new BufferedReader(new InputStreamReader(f)));
		}
		ArrayList<StringBuilder> sb=new ArrayList<StringBuilder>();
		StringBuilder temp=null;
		String Line;
		BufferedReader tempreader=null;
		for(int i=0;i<3260;i++)
		{
			tempreader=br.get(i);
			temp=new StringBuilder();
			try {
				
				Line=tempreader.readLine();
				temp.append(Line);
				sb.add(i,temp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		int indexcnt=0;
	for(StringBuilder t:sb)
	{
		String[] keyword=t.toString().split("-");
		if(index.get(keyword[0])==null)
			{
				
				ArrayList<Integer> value=new ArrayList<Integer>();
				value.add(indexcnt);
				index.put(keyword[0],value);
			}
		else
		{
			ArrayList<Integer> value=new ArrayList<Integer>();
			value=index.get(keyword[0]);
			value.add(indexcnt);
			index.put(keyword[0], value);
		}
			
		indexcnt++;
	}
//	System.out.print(index.size());
	TreeSet<String> ts= new TreeSet<String>(index.keySet());
	StringBuilder output=null;
	//cnt=2;
	while(cnt>0)
	{
		
		//System.out.println(index.size());
		//cnt--;
		ArrayList<Integer> value=new ArrayList<Integer>();
		String term=new String();
		for(String t:ts)
		{
			value=index.get(t);
			//System.out.println(t+value.toString());
			term=t;
			break;
		}	
		ts.remove(term);
		//System.out.println(value.toString());
		output=new StringBuilder();
		if(value.size()==1)
		{
			Integer i=value.get(0);
			//output.append(term+"-");
			output.append(sb.get(i));
			//System.out.println(sb.get(i));
			//sb.remove(i);
			tempreader=br.get(i);
			temp=new StringBuilder();
			if(finished[i])
				continue;
			try {
				Line=tempreader.readLine();
				if(Line!=null)
				{
					temp.append(Line);
					sb.set(i,temp);
					
					String[] keyword=Line.split("-");
					if(index.get(keyword[0])==null)
						{
							
							ArrayList<Integer> val=new ArrayList<Integer>();
							val.add(i);
							index.put(keyword[0],val);
							ts.add(keyword[0]);
						}
					else
					{
						ArrayList<Integer> val=new ArrayList<Integer>();
						val=index.get(keyword[0]);
						val.add(i);
						index.put(keyword[0], val);
						ts.add(keyword[0]);

					}
					
				}
				else
				{
					cnt--;
					System.out.println("cnt : "+cnt);
;					finished[i]=true;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			output.append(term+"-");
			//System.out.print(term+"-");
			for(Integer a:value)
			{
				String[] split=sb.get(a).toString().split("-");
				//sb.remove(a);
				output.append(split[1]);
				//System.out.print(split[1]);
				tempreader=br.get(a);
				temp=new StringBuilder();
				if(finished[a])
					continue;
				try {
					Line=tempreader.readLine();
					if(Line!=null)
					{
						temp.append(Line);
						//sb.remove(a);
						sb.set(a,temp);
						
						String[] keyword=Line.split("-");
						if(index.get(keyword[0])==null)
							{
								
								ArrayList<Integer> val=new ArrayList<Integer>();
								val.add(a);
								index.put(keyword[0],val);
								ts.add(keyword[0]);

							}
						else
						{
							ArrayList<Integer> val=new ArrayList<Integer>();
							val=index.get(keyword[0]);
							val.add(a);
							index.put(keyword[0], val);
							ts.add(keyword[0]);

						}
						
					}
					else
					{
						cnt--;
						System.out.println("cnt : "+cnt);
						finished[a]=true;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		index.remove(term);
		
		if(output.charAt(0)>='a' && mergedcount!=output.charAt(0))
		{
			out.close();
			mergedcount=output.charAt(0);
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter("merged"+mergedcount)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		out.write(output.toString()+"\n");
		output=null;
	}
}
	

}
