Wikipedia Search Engine  

For indexing:  
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;run parcerc.java with 2 arguements - wikidump and indexfilename  
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;multiple index files are created in chunks of 5000 pages  
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;run merge.java to merge to multiple index files into 36 files(0-9a-z)  
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;run secondindex.java to create 36 secondary index files   
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;run secondary.java and tertiary.java to create index of title file  
  
for running queries:  
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;run query.java  
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for free text query just enter the text. top 10 relevant documents are retrieved based on tf-idf score  
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;example - "sachin tendulkar"  (without the quotes)  
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for field query:-  
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;t:title  
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;b:body  
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;c:category  
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;i:infobox  
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;l:external links  
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;r:references  
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;example - "t:sachin b:ecommerc"  
				 
