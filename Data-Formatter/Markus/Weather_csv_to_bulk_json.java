package weatherPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Weather_csv_to_bulk_json {

	/***
	 * This class is used for putting in Data in the Format: ( DATE , SOMEVALUE ) and sorting it over the attribute date  
	 * @author marku
	 *
	 */
	public static class Entry implements Comparable<Entry>{
		int val_per_day;
		LocalDateTime date;
		public Entry(int vpd, int year, int month, int dayOfMonth){
			val_per_day = vpd;
			date = LocalDateTime.of(year,month,dayOfMonth,0,0);
		}	
		public static Entry fromString(String s){
			String[] helper1 = s.split(",");
			String[] helper2 = helper1[0].split("/");	
			return new Entry(Integer.parseInt(helper1[1]),Integer.parseInt(helper2[0]),Integer.parseInt(helper2[1]),Integer.parseInt(helper2[2]));
		}
		public Entry(){};
		
		@Override
		public String toString(){
			if(this ==  new Entry(1,1,1,1)){return "";}
			return (date.format(fm) + "," + val_per_day);
		}
		
		/***
		 * compareTo - Method used to sort by date ... 
		 */
		@Override
		public int compareTo(Entry entr) {
			LocalDateTime ldt1 = this.date;
			LocalDateTime ldt2 = entr.date;
			if(ldt1.getYear() == ldt2.getYear()){
				if(ldt1.getMonth() == ldt2.getMonth()){
					if(ldt1.getDayOfMonth() == ldt2.getDayOfMonth()){
						return 0;
					}else if(ldt1.getDayOfMonth() > ldt2.getDayOfMonth()){
						return 1;
					}else{
						return -1;
					}
				}else if(ldt1.getMonthValue() > ldt2.getMonthValue()){
					return 1;
				}else{					
					return -1;
				}
			}else if(ldt1.getYear() > ldt2.getYear()){
				return 1;
			}else{
				return -1;
			}
		}
	}
	
	static DateTimeFormatter fm = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	static String[] fields = {"no","no","no","no","no","n","nr","se","h","d","tw"};
	
	/***
	 * this maps the strings for weatherEvents to numbers ... elastic can only aggregate with numbers or atleast not Strings *sadFace*
	 * @param s
	 * @return
	 */
	public static int filenameToIntID(String s){
		if(s.equals("nr")){return 0;}
		if(s.equals("n")){return 1;}
		if(s.equals("d")){return 2;}
		if(s.equals("h")){return 3;}
		if(s.equals("tw")){return 4;}
		if(s.equals("se")){return 5;}
		else return 10;
	}
	
	/***
	 * this method prepares the weatherData for the correlation! It adds all the weather events (represented by o or 1 for every day) of a specific type up and creates a file for every weatherEventType
	 * Problem: we only have the data for every day that there was a weatherEvent of type x ... therefore we have to stuff in all the dates where there was no weatherEvent of type x.
	 * goal is to correlate every day of weatherData with every day of ufoData from 1975 to 2015 :) 
	 * 
	 * @param fieldname - String
	 * @param dir - File
	 */
	public static void getField(String fieldname, File dir){
		try{
		System.out.println(fieldname + " :::::: \n");
		if(fieldname == "No"){return;}
		int pos = 0;
		for(int i = 0; i<fields.length; i++){
			if(fieldname.equals(fields[i])){
				pos = i;
			}
		}
		if(dir.isDirectory()){
			ArrayList<Entry> al = new ArrayList<Entry>();
			File[] csv_weather_files = dir.listFiles();
			String s;
			BufferedReader br;
			String datestring = "";
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Weather_csv_to_bulk_json.class.getResource("Output/prep_for_correl_").getPath() + fieldname +".csv")));
			bw.write("date(year/month/day),occurences\r\n");
			Entry entr = new Entry(1,1,1,1);
			for(File f : csv_weather_files){
				br = new BufferedReader(new FileReader(f));
				while((s = br.readLine()) != null){
					String[] helper = s.split(",", 11);
					if(datestring.equals(helper[1] + helper[2] + helper[3])){
						entr.val_per_day = (entr.val_per_day + Integer.parseInt(helper[pos]));
					}else{
						al.add(entr);
						datestring = (helper[1] + helper[2] + helper[3]);
						entr = new Entry(Integer.parseInt(helper[pos]), Integer.parseInt(helper[1]),Integer.parseInt(helper[2]), Integer.parseInt(helper[3]));
					}
				}
				System.out.println(f.getPath() + "\n");
			}
			Collections.sort(al);
			for(Entry en : al){
				bw.write(en.toString() + "," + filenameToIntID(fieldname) + "\r\n");
			}
			bw.close();
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/***
	 * starts the getField() for every weatherEvent
	 */
	public static void prep_for_correl(){

		File CSV_DIR = new File (Weather_csv_to_bulk_json.class.getResource("Input/csv_dir").getPath());
		
		String[] all_weather_events = {"n","nr","se","h","d","tw"};
		for(String s : all_weather_events){
			getField(s, CSV_DIR);
		}
	}


	
	/***
	 * In the stations csv there are 1000 entries with stationNumber 999999 so i just remove all of those 
	 * after that we still have over 6000 stations. All remaining stations are in the us because of the kaggle sql query.
	 * 
	 * @param stationsPath String
	 * @return retMap HashMap
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	 
	public static HashMap<Integer,String> readStations_csv(String stationsPath) throws NumberFormatException, IOException{
		HashMap<Integer,String> retMap = new HashMap<Integer,String>();
		File stationsFile = new File(stationsPath);
		BufferedReader br = new BufferedReader(new FileReader(stationsFile));
		String s = "";
		br.readLine();
		while((s = br.readLine()) != null){
			String[] helper = s.split(",");
			if(helper[0].equals("999999")){continue;}
			retMap.put(Integer.parseInt(helper[0]), ("\"geop\": { \"lat\": \"" + helper[2] + "\",\"lon\":\"" + helper[3] + "\"}"));
		}
		br.close();
		return retMap;
	}
	/***
	 * This method creates curl commands for every relevant weatherDataFile and creates .cmd file to execute the curl commands 
	 * 
	 * @param indexName String
	 * @param bulk_json_dir String
	 * @param ip String 
	 * @param weatherBulkDestination String
	 * @return cmdFile File
	 * @throws IOException
	 */
	public static File create_cmd_file(String indexName, String bulk_json_dir, String ip) throws IOException{
		File cmdFile = new File("C:\\Users\\marku\\desk\\Programmierung\\weather_curl.cmd");
		BufferedWriter bw = new BufferedWriter(new FileWriter(cmdFile));
		String startString = "";
		bw.write(startString);
		//start :::   cmd_file << "cd elasticsearch-6.2.3\\bin\r\nstart elasticsearch.bat\r\ncd .. \r\ncd .. \r\ncd\r\ncd .. \r\ncd .. \r\nDownloads\\curl \r\nping 127.0.0.1 -n 30 > nul\r\n"
		
		File[] bulk_jsons = new File(bulk_json_dir).listFiles();
		for(File f : bulk_jsons){
			String p = f.getPath();
			bw.write(String.format("curl -H Content-Type:application/x-ndjson -XPUT %s:9200/%s/doc/_bulk?pretty --data-binary @%s \r\n",ip,indexName,p));
		}
		bw.close();
		return cmdFile;
	}
	/***
	 * gets data from csv file and creates for every 300.000 entries one ndjson file that is ready for elastic
	 * 				calls the methods create_cmd_file & readStationscsv
	 * 
	 * 
	 * @param bulkFilePath
	 * @param IndexName
	 * @throws IOException
	 */
	public static void weather_csv_to_elast(String bulkFilePath, String IndexName) throws IOException{
		HashMap<Integer,String> stationMap = readStations_csv(Weather_csv_to_bulk_json.class.getResource("Input/stations/stations.csv").getPath());
		File CSV_DIR = new File(bulkFilePath);
		int id = 0;	//int : -2,147,483,648 to +2,147,483,647 is enough for all index lines -> shouldnt be more than 30 mio
		String nb = "1";
		String l = "";

		if(CSV_DIR.isDirectory()){
			File[] csv_weather_files = CSV_DIR.listFiles();
			ArrayList<String> result = new ArrayList<String>();
			for(File f : csv_weather_files){
				BufferedReader br = new BufferedReader(new FileReader(f));
				ArrayList<String> allLines = new ArrayList<String>();
				while((l = br.readLine()) != null){allLines.add(l);}
				
				for(String line : allLines){
					String[] helper = line.split(",", 11);
					if(stationMap.containsKey(Integer.parseInt(helper[0]))){
						/** ndJson String: **/
						String bulk_json_line = String.format("{\"sn\":\"%s\",\"date\":\"%s/%s/%s\",\"n\":\"%s\",\"nr\":\"%s\",\"se\":\"%s\",\"h\":\"%s\",\"d\":\"%s\",\"tw\":\"%s\",${stationMap.get(Integer.parseInt(helper[0]))}}", helper[0],helper[1],helper[2],helper[3],helper[5],helper[6],helper[7],helper[8],helper[9],helper[10]);
						result.add(String.format("{\"index\":{\"_index\":\"%s\",\"_id\":\"%d\"}}",IndexName,id));  //index line
						result.add(bulk_json_line);
						if((result.size()) >= 600000){
							File file = new File(Weather_csv_to_bulk_json.class.getResource("Output/bulk_json/bulk_json_dir").getPath() + "\\" + nb + ".ndjson");
							nb += "1";
							BufferedWriter bw = new BufferedWriter(new FileWriter(file));
							for(String s : result){
								bw.write( s + '\n');
							}
							bw.close();
							result = new ArrayList<String>();
							System.out.println(String.format("done with a file (to %d)",id));
						}
						id++;
					}
				}
			}
		}
		File curl_cmd = create_cmd_file(IndexName, "C:\\Users\\marku\\Desktop\\weather\\bulk_json_dir", "141.72.191.130");	
	}
	
	
	
	
	
	
	public static void main (String[] args){
		
		System.out.println("hello world");
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		
		String bulkFilePath = "C:\\Users\\marku\\VS\\webdb\\weather\\csv_dir", IndexName = "us_weather_70_to_15";
		try {
			weather_csv_to_elast(bulkFilePath, IndexName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		prep_for_correl();
	}
}































/***
 *PUT /us_weather
{
  "mappings": {
    "doc": {
      "properties": {
        "sn": {
          "type": "integer"
        },
        "date": {
          "type": "date",
          "format": "yyyy/MM/dd"
        },
        "n": {
          "type": "integer"
        },
        "nr": {
          "type": "integer"
        },
        "se": {
          "type": "integer"
        },
        "h": {
          "type": "integer"
        },
        "d": {
          "type": "integer"
        },
        "tw": {
          "type": "integer"
        },
        "geop": {
          "type": "geo_point"
        }
      }
    }
  }
}
*****
{
  "acknowledged": true,
  "shards_acknowledged": true,
  "index": "us_weather"
}
 */
