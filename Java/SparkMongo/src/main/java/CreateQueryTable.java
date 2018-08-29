import java.io.IOException;
import java.io.StringReader;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import org.bson.types.ObjectId;


public class CreateQueryTable {
	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName("Query Table").setMaster(
				"local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);

		Configuration config = new Configuration();
		config.set("xmlinput.start", "<ODM");
		config.set("xmlinput.end", "</ODM>");

		JavaRDD<Text> xmlRDD = sc
				.newAPIHadoopFile(
						"hdfs://172.16.1.64:9000//gileadSandBox/SampleDataset/*",
						XMLInputFormat.class, LongWritable.class, Text.class,
						config).values();
		//System.out.println(xmlRDD.count());
		
		 final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); 
        JavaRDD<List<BasicDBObject>> queryRDD=xmlRDD.map(new Function<Text, List<BasicDBObject>>(){
             		public List<BasicDBObject> call(Text xml) throws ParserConfigurationException, SAXException, IOException, DOMException, ParseException{
             			
             			List<BasicDBObject> bsonList = new ArrayList<BasicDBObject>();

             			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    	factory.setValidating(false);
                   	 	DocumentBuilder builder = factory.newDocumentBuilder();
                   	 	InputSource is = new InputSource();
                   	 	String str=xml.toString();
                        is.setCharacterStream(new StringReader(str));
                        Document doc = builder.parse(is);
                        
                        NodeList node = doc.getElementsByTagName("ClinicalData");
                        System.out.println(node.getLength());
                        for(int i=0;i<node.getLength();i++)
                        {
                        	Element clinical = (Element) node.item(i);

            

                        	if (clinical.getElementsByTagName("mdsol:Query").getLength()==1)
                        	{
                        		NodeList node1=clinical.getElementsByTagName("mdsol:Query");
                        		Element element1 = (Element) node1.item(0);
                        		BasicDBObject bson = new BasicDBObject();

                        		String studyoid = clinical.getAttribute("StudyOID");
                        		String locationoid = ((Element)clinical.getElementsByTagName("SiteRef").item(0)).getAttribute("LocationOID");
                        		String subjectkey = ((Element)clinical.getElementsByTagName("SubjectData").item(0)).getAttribute("SubjectKey");
                        		String  subjectname = ((Element)clinical.getElementsByTagName("SubjectData").item(0)).getAttribute("mdsol:SubjectName");
                        		String studyeventoid = ((Element)clinical.getElementsByTagName("StudyEventData").item(0)).getAttribute("StudyEventOID");
                        		String studyeventoidrepeatkey = ((Element)clinical.getElementsByTagName("StudyEventData").item(0)).getAttribute("StudyEventRepeatKey");
                        		String formoid =  ((Element)clinical.getElementsByTagName("FormData").item(0)).getAttribute("FormOID");
                        		String formrepeatkey = ((Element)clinical.getElementsByTagName("FormData").item(0)).getAttribute("FormRepeatKey");
                        		
                        		bson.put("QueryRepeatKey",element1.getAttribute("QueryRepeatKey"));
                        		bson.put("StudyOID",clinical.getAttribute("StudyOID"));
                        		bson.put("MetaDataVersionOID",clinical.getAttribute("MetaDataVersionOID"));
                        		bson.put("LocationOID",((Element)clinical.getElementsByTagName("SiteRef").item(0)).getAttribute("LocationOID"));
                        		bson.put("SubjectKey",((Element)clinical.getElementsByTagName("SubjectData").item(0)).getAttribute("SubjectKey"));
                        		bson.put("SubjectName", ((Element)clinical.getElementsByTagName("SubjectData").item(0)).getAttribute("mdsol:SubjectName"));
                        		bson.put("StudyEventOID", ((Element)clinical.getElementsByTagName("StudyEventData").item(0)).getAttribute("StudyEventOID"));
                        		bson.put("StudyEventOIDRepeatKey", ((Element)clinical.getElementsByTagName("StudyEventData").item(0)).getAttribute("StudyEventRepeatKey"));
                        		bson.put("FormOID", ((Element)clinical.getElementsByTagName("FormData").item(0)).getAttribute("FormOID"));
                        		bson.put("FormRepeatKey",((Element)clinical.getElementsByTagName("FormData").item(0)).getAttribute("FormRepeatKey"));
                        		
                                // if a field is not present, the value returned is ""
                        		
                        		
                        		String itemgroupoid = "";
                        		String itemgrouprepeatkey = "";
                        	
                        		if(!((Element)clinical.getElementsByTagName("ItemGroupData").item(0)).getAttribute("ItemGroupOID").equals("")){
                        			bson.put("ItemGroupOID", ((Element)clinical.getElementsByTagName("ItemGroupData").item(0)).getAttribute("ItemGroupOID"));
                        			itemgroupoid=((Element)clinical.getElementsByTagName("ItemGroupData").item(0)).getAttribute("ItemGroupOID");
                        		}
                                
                        			
                        		if(!(((Element)clinical.getElementsByTagName("ItemGroupData").item(0)).getAttribute("ItemGroupRepeatKey").equals(""))){
                        				bson.put("ItemGroupRepeatKey", ((Element)clinical.getElementsByTagName("ItemGroupData").item(0)).getAttribute("ItemGroupRepeatKey"));
                            			itemgrouprepeatkey=((Element)clinical.getElementsByTagName("ItemGroupData").item(0)).getAttribute("ItemGroupRepeatKey");
                        			}
                        		
                        		else  bson.put("ItemGroupRepeatKey", null);
                        
                        		String itemoid = "";
	                       		itemoid =  ((Element)clinical.getElementsByTagName("ItemData").item(0)).getAttribute("ItemOID");
	                       		
	                       		bson.put("ItemOID", ((Element)clinical.getElementsByTagName("ItemData").item(0)).getAttribute("ItemOID"));
	                       		
	                       		bson.put("DataPageID",studyoid +"|"+  locationoid+"|"+subjectkey+"|"+subjectname+"|"+studyeventoid+"|"+studyeventoidrepeatkey+ "|"+formoid+"|"+formrepeatkey);
	                       		bson.put("InstanceID",studyoid +"|"+  locationoid+"|"+subjectkey+"|"+subjectname+"|"+studyeventoid+"|"+studyeventoidrepeatkey);
	                       		bson.put("RecordID",studyoid +"|"+  locationoid+"|"+subjectkey+"|"+subjectname+"|"+studyeventoid+"|"+studyeventoidrepeatkey+ "|"+formoid+"|"+formrepeatkey+"|"+itemgroupoid+"|"+itemgrouprepeatkey);	
	                       		bson.put("DataPointID",studyoid +"|"+  locationoid+"|"+subjectkey+"|"+subjectname+"|"+studyeventoid+"|"+studyeventoidrepeatkey+ "|"+formoid+"|"+formrepeatkey+"|"+itemgroupoid+"|"+itemgrouprepeatkey+"|"+itemoid);
	                       		
	                       		bson.put("UserOID",((Element)clinical.getElementsByTagName("UserRef").item(0)).getAttribute("UserOID"));
                        		bson.put("LastUpdatedTimeStamp", formatter.parse(((Element)clinical.getElementsByTagName("DateTimeStamp").item(0)).getTextContent()));
                        		
	                       		if(!element1.getAttribute("Value").equals(""))
	                       			bson.put("QueryValue", element1.getAttribute("Value"));
	                       		else  bson.put("QueryValue", null);
	                       		 
	                       		
	                    		if(!element1.getAttribute("Response").equals(""))
	                    			bson.put("QueryResponse", element1.getAttribute("Response"));
	                    		else  
	                    		    bson.put("QueryResponse", null);
	                    		
	                    		
	                    		bson.put("QueryStatus",element1.getAttribute("Status"));
                        		bson.put("QueryRecepient", element1.getAttribute("Recipient"));
	                    		
                        		
	    						if (element1.getAttribute("Status").equals("Closed"))
	    							bson.put("QueryResolvedTimeStamp",  formatter.parse(((Element)clinical.getElementsByTagName("DateTimeStamp").item(0)).getTextContent()));
	    						else bson.put("QueryResolvedTimeStamp", null);
	    						
	    						
	    						if (element1.getAttribute("Status").equals("Answered"))
	    							bson.put("QueryAnsweredTimeStamp", formatter.parse(((Element)clinical.getElementsByTagName("DateTimeStamp").item(0)).getTextContent()));
	    						else bson.put("QueryAnsweredTimeStamp", null);
	    						
	    						
	    						if(element1.getAttribute("Status").equals("Open"))
	    							bson.put("QueryCreatedTimeStamp",formatter.parse(((Element)clinical.getElementsByTagName("DateTimeStamp").item(0)).getTextContent()) );
	    						else bson.put("QueryCreatedTimeStamp", null);
	    						
	    						bson.put("Flag", 1);
	    						bsonList.add(bson);
                        	}
                        	
                        }
                        
                        return  bsonList;
             		}
             	});
       
        
        JavaRDD<BasicDBObject> queryRDD2=queryRDD.flatMap(new FlatMapFunction<List<BasicDBObject>,BasicDBObject>(){
        	public List<BasicDBObject> call(List<BasicDBObject> q){
        		return q;
        	}
        });  
        JavaRDD<BasicDBObject> queryRDD3=queryRDD2.map(new Function<BasicDBObject,BasicDBObject>(){
			public BasicDBObject call(BasicDBObject doc){
			
				try {
					MongoClient mongoClient = new MongoClient(
							new ServerAddress("203.122.26.109", 27017));

				
					DB db = mongoClient.getDB("SparkBatch");

			
					
					 DBCollection coll = db.getCollection("QueriesUpdated");

		              
	                   
	                    //to find if the updated table already has an entry for the same query
	                     
	                     BasicDBObject searchQuery = new BasicDBObject().append("QueryRepeatKey" , doc.getString("QueryRepeatKey"));
	                     DBCursor cursor = coll.find(searchQuery).addOption(16);
	                    
	                  
	                    
	                    //code to update if entry for same query is present
	                    if(cursor.hasNext())
	                    {
	                    	
	                        //result is the doc of the query that has to be updated
	                    	DBObject result = cursor.next();
	                        ObjectId _id =  (ObjectId) result.get("_id");
	                        doc.append("_id", _id);
	             
	                        
	                        String queryValue = (String) result.get("QueryValue");
	                    	String queryStatusID = (String) result.get("QueryStatus");
	                    	String queryResponse = (String) result.get("QueryResponse");
	                    	Date queryResolvedTimeStamp = (Date) result.get("QueryResolvedTimeStamp");
	                    	Date queryAnsweredTimeStamp = (Date) result.get("QueryAnsweredTimeStamp");
	                    	Date queryCreatedTimeStamp = (Date) result.get("QueryCreatedTimeStamp");
	                    	Date queryLastUpdatedTimeStamp = (Date) result.get("LastUpdatedTimeStamp");
	                    	Date queryUpdatedTimeStamp = (Date) doc.get("LastUpdatedTimeStamp");
	                    
	                    	
	                    	
	                    	// if the following fields don't exist in the present tuple, append the already existing value if it is not null
	                    	if(doc.get("QueryValue") == (null)) { if(!(queryValue == null)) doc.append("QueryValue", queryValue); }
	                        if(doc.get("QueryResponse") == (null)) { if(!(queryResponse == null))  doc.append("QueryResponse", queryResponse); }
	                        if(doc.get("QueryResolvedTimeStamp") == (null)) { if(!(queryResolvedTimeStamp == null))  doc.append("QueryResolvedTimeStamp", queryResolvedTimeStamp); }
	                        if(doc.get("QueryAnsweredTimeStamp") == (null)) { if(!(queryAnsweredTimeStamp == null))  doc.append("QueryAnsweredTimeStamp", queryAnsweredTimeStamp); }
	                        if(doc.get("QueryCreatedTimeStamp") == (null)) {  if(!(queryCreatedTimeStamp == null))  doc.append("QueryCreatedTimeStamp", queryCreatedTimeStamp); }
	                      
	                        //check the timestamps of the present tuple, and the already existing value
	                        if(queryUpdatedTimeStamp.before(queryLastUpdatedTimeStamp)){
	                          
	                      	  doc.append("QueryStatus", (String) result.get("QueryStatus"));
	                      	  doc.append("LastUpdatedTimeStamp", queryLastUpdatedTimeStamp);
	                        
	                       }
	                         
	                        //if query was answered and closed at same time, update the status as closed
	                         else if(queryUpdatedTimeStamp.equals(queryLastUpdatedTimeStamp)){
	                      	  if (queryStatusID.equals("Closed") && ((String) doc.get("QueryStatus")).equals("Answered") ) 
	                      		  doc.append("QueryStatus","Closed");
	                         }
	                        
	                         else
	                        	 doc.append("UserOID", result.get("UserOID")); //so that UserOID at the time of QueryOpen is maintained in table
	                       
	                    }
	                    
	                    
	                    cursor.close();

	                 	System.out.println(doc.getString("QueryRepeatKey"));
	                 	
                     coll.save(doc);

					
					mongoClient.close();
				}
				catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				return doc;
			}
		});
		
		System.out.println(queryRDD3.count());
 
	}
}
