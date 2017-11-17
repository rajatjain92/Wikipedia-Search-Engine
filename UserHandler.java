import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Comparator;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


class scorecomp implements Comparator<Entry<Integer, HashMap<String, Integer>>>{
	 
    @Override
    public int compare(Entry<Integer, HashMap<String, Integer>> e1,Entry<Integer, HashMap<String, Integer>> e2) {
    	if(e1.getValue().containsKey("score") && e2.getValue().containsKey("score"))
    			{
    				if (e1.getValue().get("score")<e2.getValue().get("score"))
    					return 1;			
    				else
    					return -1;
    			}
    	return -1;
    }
}  



public class UserHandler extends DefaultHandler {

		//static int i = 0;
		boolean btopic=false;
		boolean btext=false;
		//boolean bpage=false;
		boolean infobox=false;
		static int pagecount=0;
		static int writecount=0;
		static int indexcount=0;
  	    Pattern refpat=Pattern.compile("== ?references ?==(.*?)==", Pattern.DOTALL);
  	    Matcher refmatcher=null;
  	    Pattern catpat=Pattern.compile("[\\[][\\[]category:(.*?)[\\]][\\]]", Pattern.DOTALL);
  	    Matcher catmatcher=null;
  	    Pattern linkpat=Pattern.compile("== ?external links ?==(.*?)\n\n", Pattern.DOTALL);
  	    Matcher linkmatcher=null;
  	    Pattern http=Pattern.compile("\\[http(.*?)\\]",Pattern.DOTALL);
  	    Matcher httpmatcher=null;
  	    Pattern infopat=Pattern.compile("[{][{]infobox(.*?)\n\n",Pattern.DOTALL);
  	    Matcher infomatcher=null;
  	    Pattern garbage=Pattern.compile("[{][{](.*?)[}][}]",Pattern.DOTALL);
  	    Matcher garbagematcher=null;
		
		Stemmer stm=null;
		String pagetitle=null;
		//FileWriter out = null;
		PrintWriter out=null;
		PrintWriter out1=null;
		String stopword[]={"a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the","ref","reflist","www"};
		@SuppressWarnings("rawtypes")
		HashMap stopwords=new HashMap();
		StringBuilder sb=null;
		HashMap<String, HashMap<Integer,HashMap<String,Integer>>> index=new HashMap<String, HashMap<Integer,HashMap<String,Integer>>>();
		//HashMap<Integer,HashMap<String,Integer>> val=new HashMap<Integer,HashMap<String,Integer>>();
		//HashMap<String,Integer> valarray=new HashMap<String,Integer>();
		HashMap<Integer,HashMap<String,Integer>> temp;
		HashMap<String,Integer> t;
		HashMap<String,Integer> t1;
	//	TreeSet<String> st= new TreeSet<String>();
		
		@SuppressWarnings("unchecked")
		UserHandler()
		{
			try
			{
				out = new PrintWriter(new BufferedWriter(new FileWriter("index"+indexcount)));
				out1=new PrintWriter(new BufferedWriter(new FileWriter("title.txt", true)));
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(String st:stopword)
			{
				stopwords.put(st,"1");
			}
			stm=new Stemmer();
		}
		
		public void process(String data,String pos)
		{
			for (String retval: data.split("\\P{Alnum}")){
				   if (stopwords.containsKey(retval.toLowerCase()) && pos!="t")
				   {

					   continue;
				   }	
			//	   val.clear();
				//  valarray.clear();
				   char tt[]=retval.toLowerCase().toCharArray();
				   stm=new Stemmer();
				   stm.add(tt,tt.length);
				   stm.stem();
				   retval=stm.toString();
				   if(retval.length()<2)
					   continue;
				   if(index.get(retval)==null)
				   {
					   HashMap<Integer,HashMap<String,Integer>> val=new HashMap<Integer,HashMap<String,Integer>>();
					   HashMap<String,Integer> valarray=new HashMap<String,Integer>();
					  /* valarray.add(0, 0);
					   valarray.add(1,0);
					   valarray.add(2,0);
					   valarray.add(3,1);*/
					   valarray.put(pos, 1);
					   val.put(pagecount, valarray);
					   index.put(retval, val);
					   //val.clear();
					   //valarray.clear();
				   }
				   else
				   {
					   HashMap<String,Integer> tempind=index.get(retval).get(pagecount);
					   if(tempind==null)
					   {
						   HashMap<String,Integer> valarray=new HashMap<String,Integer>();
						  /* valarray.add(0, 0);
						   valarray.add(1,0);
						   valarray.add(2,0);
						   valarray.add(3,1);*/
						   valarray.put(pos, 1);
						   index.get(retval).put(pagecount, valarray);
						  // valarray.clear();
					   }
					   else
					   {
						   Integer tempval=tempind.get(pos);
						   if(tempval==null)
							   index.get(retval).get(pagecount).put(pos, 1);
						   else
							   {
							   	tempval+=1;
							   	index.get(retval).get(pagecount).put(pos, tempval);
							   }
						  
						//tempind.clear();   
					   }
					   
				   }
				 
				  
				 
	      }
			
	    	  }
		
		
	   @Override
	   public void startElement(String uri, 
	      String localName, String qName, Attributes attributes)
	         throws SAXException {
		   if(qName.equalsIgnoreCase("title"))
		   {
			   btopic=true;
			 //  sb.setLength(0);
			   sb=new StringBuilder();
			   pagecount++;
			  // System.out.println(index.size());
		   }
		   else if(qName.equalsIgnoreCase("text"))
		   {
			   btext=true;
			   sb=new StringBuilder();
		   }
	   }

	   @Override
	   public void endElement(String uri, 
	      String localName, String qName) throws SAXException {
	      if (qName.equalsIgnoreCase("title")) {
//	         System.out.println("End Element :" + qName);
	    	  if(sb!=null)
	    		  {
	    		  pagetitle=sb.toString();
	    		  out1.write(pagetitle+"\n");
	    		  process(pagetitle,"t");
	    		  }
			   btopic=false;
			   
			   if(pagecount%5000==0)
			   {
				  // System.out.print(index.toString());
	//			   st.clear();
				   TreeSet<String> st= new TreeSet<String>(index.keySet());
				   //st= new TreeSet<String>(index.keySet());
				   for(String s:st)
			    	  {	  
			    		  out.write(s+"-");
			    		  temp=index.get(s);
			    		  int score=0;
			    		  
			    		  for(Integer i:temp.keySet())
			    		  {
			    			  t=temp.get(i);
			    			  score=0;
			    			  for(String ss:t.keySet())
			    			  {
			    				  HashMap<String,Integer> t1=new HashMap<String,Integer>(t);
			    				  switch(ss)
			    				  {
			    				  	case "t":score=score+100*(Integer.parseInt(t.get(ss).toString()));
			    				  			break;
			    				  	case "b":score=score+15*(Integer.parseInt(t.get(ss).toString()));
	    				  					break;
			    				  	case "c":score=score+30*(Integer.parseInt(t.get(ss).toString()));
				  							break;
			    				  	case "i":score=score+30*(Integer.parseInt(t.get(ss).toString()));
		  									break;		
			    				  	case "r":score=score+30*(Integer.parseInt(t.get(ss).toString()));
		  									break;
			    				  	case "l":score=score+30*(Integer.parseInt(t.get(ss).toString()));
		  									break;		
			    				  }
			    				  t1.put("score", score);
			    				//  index.put(key, value)
			    				  temp.put(i, t1);
			    		//		  t1.clear();
			    			  }
			    			  
			    		  }
			    	  //index.put(s,temp);
			    		  TreeSet<Entry<Integer, HashMap<String, Integer>>> ts=new TreeSet<Entry<Integer, HashMap<String, Integer>>>(new scorecomp()); 
			    		  //ts=(TreeSet<Entry<Integer, HashMap<String, Integer>>>) temp.entrySet();
			    		  ts.addAll(temp.entrySet());
			  //  		  System.out.println(ts.toString()+"hi");
			    		  int count=0;
			    	for(Entry<Integer,HashMap<String,Integer>> e:ts)
			    		
					  //for (Integer i: temp.keySet())
					   {
						  //System.out.print(i);
			    		count++;
			    		if(count>10)
			    			break;
			    		 Integer i=e.getKey();	
						  out.write(i+" ");
						  //t=temp.get(i);
						   //out.write(t.toString());
						  t=e.getValue();
						  for(String ss:t.keySet())
						  {
							  if(ss=="score")
								  continue;
							  out.write(ss);
							  out.write(t.get(ss).toString());
							  
						  }
						   //System.out.print("\n");
						  out.write("|");
						  t.clear();
					   }
			    		  out.write("\n");
			    		  //temp.clear();
			    		  //ts.clear();
			    	}
				     index.clear();
				     
				     out.close();
				     indexcount++;
				     try
				     	{
				    	 	out = new PrintWriter(new BufferedWriter(new FileWriter("index"+indexcount)));
				     	} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				     finally
				     {
				    	 
				     }
				     
				     
			   }
			   sb=null;
	      }
	      else if (qName.equalsIgnoreCase("mediawiki"))
	      {
	    	
	    	  TreeSet<String> st= new TreeSet<String>(index.keySet());
	    	  //st= new TreeSet<String>(index.keySet());
			   
			   for(String s:st)
		    	  {	  
		    		  out.write(s+"-");
		    		  temp=index.get(s);
		    		  int score=0;
		    		  for(Integer i:temp.keySet())
		    		  {
		    			  t=temp.get(i);
		    			  score=0;
		    			  
		    			  for(String ss:t.keySet())
		    			  {
		    				  HashMap<String,Integer> t1=new HashMap<String,Integer>(t);
		    				  switch(ss)
		    				  {
		    				  	case "t":score=score+100*(Integer.parseInt(t.get(ss).toString()));
		    				  			break;
		    				  	case "b":score=score+15*(Integer.parseInt(t.get(ss).toString()));
   				  					break;
		    				  	case "c":score=score+30*(Integer.parseInt(t.get(ss).toString()));
			  							break;
		    				  	case "i":score=score+30*(Integer.parseInt(t.get(ss).toString()));
	  									break;		
		    				  	case "r":score=score+30*(Integer.parseInt(t.get(ss).toString()));
	  									break;
		    				  	case "l":score=score+30*(Integer.parseInt(t.get(ss).toString()));
	  									break;		
		    				  }
		    				  t1.put("score", score);
		    				//  index.put(key, value)
		    				  temp.put(i, t1);
		    				//  t1.clear();
		    			  }
		    			  
		    		  }
		    		  //index.put(s,temp);
		    		  TreeSet<Entry<Integer, HashMap<String, Integer>>> ts=new TreeSet<Entry<Integer, HashMap<String, Integer>>>(new scorecomp()); 
		    		 // ts=(TreeSet<Entry<Integer, HashMap<String, Integer>>>) temp.entrySet();
		    		  ts.addAll(temp.entrySet());
		    		//  System.out.println(ts.toString()+"hi");
		    		  int count=0;
		    		  for(Entry<Integer,HashMap<String,Integer>> e:ts)
				    		
						  //for (Integer i: temp.keySet())
						   {
							  //System.out.print(i);
		    			  count++;
				    		if(count>10)
				    			break;
				    		 Integer i=e.getKey();	
							  out.write(i+" ");
							  //t=temp.get(i);
							   //out.write(t.toString());
							  t=e.getValue();
							  for(String ss:t.keySet())
							  {
								  if(ss=="score")
									  continue;
								  out.write(ss);
								  out.write(t.get(ss).toString());
								  
							  }
							   //System.out.print("\n");
							  out.write("|");
							  //t.clear();
						   }
				    		  out.write("\n");
				    		  //temp.clear();
				    		  //ts.clear();
				    	}
					     index.clear();
					     
					     out.close();
					     indexcount++;
			     try
			     	{
			    	 	out = new PrintWriter(new BufferedWriter(new FileWriter("index"+indexcount)));
			     	} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			     finally
			     {
			    	 
			     } 	  
	    	  
	    	  
	      }
	      else if (qName.equalsIgnoreCase("text"))
	      {
	    	  btext=false;
	    	  //String data=sb.toString().replaceAll("[{]{2}([^}]*?)[}]{2}", " ");
	    	  if(sb!=null)
	    	  {
	    	  String data=sb.toString();
//	    	  System.out.println(data + "\n\n");
		      //System.out.println(sb.length());
	    	  refmatcher=refpat.matcher(data);
	    	  if (refmatcher.find())
	    		  {
	    		  	//System.out.println(matcher.group(0));
	    		  	String refs=refmatcher.group(0);
	    		  	refs=refs.replaceFirst("== ?references ?==", " ");
	    		  	int i=0;
	    		  	for (String ref:refs.split("="))
	    		  	{
	    		  		if(i%2==0)
	    		  		{	
	    		  			i++;	  		
	    		  			continue;
	    		  		}
	    		  		i++;
	    		  		process(ref,"r");
	    		  	}
	    		  	
	    		  }
	    	  	data=refmatcher.replaceAll(" ");
	    	  	catmatcher=catpat.matcher(data);
		    	  if (catmatcher.find())
		    		  {
		    		  	//System.out.println(matcher.group(0));
		    		  	String cats=catmatcher.group(0);
		    		  	int i=0;
		    		  	for (String cat:cats.split(":"))
		    		  	{
		    		  		if(i%2==0)
		    		  		{	
		    		  			i++;	  		
		    		  			continue;
		    		  		}
		    		  		i++;
		    		  		process(cat,"c");
		    		  	}
		    		  }	
		    	  data=catmatcher.replaceAll(" ");
		    	  linkmatcher=linkpat.matcher(data);
		    	  if (linkmatcher.find())
		    		  {
		    		  	//System.out.println(linkmatcher.group(0));
		    		  	String links=linkmatcher.group(0);
		    		  	httpmatcher=http.matcher(links);
		    		  	while(httpmatcher.find())
		    		  	{
		    		  		//System.out.println(httpmatcher.group(0));
		    		  		String tlink=httpmatcher.group(0);
		    		  		int i=0;
			    		  	for (String llink:tlink.split(" "))
			    		  	{
			    		  		if(i==0)
			    		  		{	
			    		  			i++;	  		
			    		  			continue;
			    		  		}
			    		  		i++;
			    		  		process(llink,"l");
			    		  	}
		    		  	}
		    		  }	
		    	  data=linkmatcher.replaceAll(" ");
		    	  infomatcher=infopat.matcher(data);
		    	  if (infomatcher.find())
		    	  {
	    		  		//System.out.println(infomatcher.group(0));
		    		  String infos=infomatcher.group(0);
		    		  	int i=0;
		    		  	for (String info:infos.split("="))
		    		  	{
		    		  		if(i%2==0)
		    		  		{	
		    		  			i++;	  		
		    		  			continue;
		    		  		}
		    		  		i++;
		    		  		process(info,"i");
		    		  	}
		    	  }
	    	//  else
	    		//  System.out.print("fail");
	    	  //System.out.println(data);
		    	  data=infomatcher.replaceAll(" ");
		    	  garbagematcher=garbage.matcher(data);
		    	  data=garbagematcher.replaceAll(" ");
		    	 
		    	  
	    	  process(data,"b");
	    	  
	    	  sb=null;
	    	  
	      }
	    	
	   }
	   }

	   @Override
	   public void characters(char ch[], 
	      int start, int length) throws SAXException {
		   if(btopic)
		   {
			   
			   //System.out.println(new String(ch,start,length));
			   try
			   {
				   				   
				   sb.append(new String(ch,start,length).toLowerCase());
				   
			   } 
			   finally
			   {
				 
			   }
			   
		   }
		   else if(btext)
		   {
			   try
			   {
				
				  /* String temp=new String(ch,start,length);
				   //temp=temp.replaceAll("\\b\\{\\{[^}]*\\}\\}\\b", " ");
				   for (String retval: temp.split("\\P{Alnum}")){
					   if (stopwords.containsKey(retval.toLowerCase()))
						   continue;
					   out1.write(retval.toLowerCase());
					   out1.write(" ");*/
				  /* if(length>5 && infobox==false)
				   {
					   if(ch[start]=='{' && ch[start+1]=='{' && ch[start+2]=='I' && ch[start+3]=='n' && ch[start+4]=='f')
					   {
						   infobox=true;
						   start=start+2;
						 //  System.out.println("infobox start");
					   }
				  }*/
				  String temp=new String(ch,start,length);
				 
				  sb.append(temp.toLowerCase()); 
				 
				   
			   } 
			   finally
			   {

			   }
			   
			 
		   }
	   }
	 
}  

