package weatherPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Weather_csv_to_bulk_json {
	
    static File jarPath=new File(Weather_csv_to_bulk_json.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    public static String ROOT = jarPath.getParentFile().getPath();

	public static DateTimeFormatter fm = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	public static String[] fields = {"no","no","no","no","no","n","nr","se","h","d","tw"};
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
	/***
	 * this maps the strings for weatherEvents to numbers ... elastic can only aggregate with numbers or atleast not Strings --> found out it works with keywords aswell :D
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
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void getField(String fieldname, File dir) throws NumberFormatException, IOException{
			System.out.println("INFO\t" + fieldname + "\n");
			if(fieldname == "No"){return;} //just in case
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
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File(ROOT + "\\docs\\wetterZwischenergebnisse\\prep_for_correl\\prep_for_correl" + fieldname +".csv")));
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
				}
				Collections.sort(al);
				for(Entry en : al){
					bw.write(en.toString() + "," + filenameToIntID(fieldname) + "\r\n");
				}
				bw.close();
			}
	}
	/***
	 * starts the getField() for every weatherEvent
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void prep_for_correl() throws NumberFormatException, IOException{
		File CSV_DIR = new File (ROOT + "\\src\\csv_dir");
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
		File cmdFile = new File(ROOT + "\\docs\\wetterZwischenergebnisse" + "\\" + "cmdFileForW.sh");
		BufferedWriter bw = new BufferedWriter(new FileWriter(cmdFile));	//TODO: the get ressource stuff makes not the right syntax to cd to file or dir
		String startString = "cd curl"  /**new File(ROOT + "\\curl").getPath()**/ + "\r\n";
		bw.write(startString);

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
	public static void weather_csv_to_elast(String csvPlace, String IndexName) throws IOException{
		HashMap<Integer,String> stationMap = readStations_csv(ROOT + "\\src\\stations\\stations.csv");
		File CSV_DIR = new File(csvPlace);
		int id = 0;	//int : -2,147,483,648 to +2,147,483,647 is enough for all index lines -> shouldnt be more than 30 mio
		String nb = "1";
		String l = "";

		if(CSV_DIR.isDirectory()){
			File[] csv_weather_files = CSV_DIR.listFiles();
			ArrayList<String> result = new ArrayList<String>();
			BufferedReader br = null;
			for(File f : csv_weather_files){
				br = new BufferedReader(new FileReader(f));
				ArrayList<String> allLines = new ArrayList<String>();
				while((l = br.readLine()) != null){allLines.add(l);}

				for(String line : allLines){
					String[] helper = line.split(",", 11);
					if(stationMap.containsKey(Integer.parseInt(helper[0]))){
						/** ndJson String: **/
						String bulk_json_line = String.format("{\"sn\":\"%s\",\"date\":\"%s/%s/%s\",\"n\":\"%s\",\"nr\":\"%s\",\"se\":\"%s\",\"h\":\"%s\",\"d\":\"%s\",\"tw\":\"%s\",${stationMap.get(Integer.parseInt(helper[0]))}}", helper[0],helper[1],helper[2],helper[3],helper[5],helper[6],helper[7],helper[8],helper[9],helper[10]);	//ugly but does the job
						result.add(String.format("{\"index\":{\"_index\":\"%s\",\"_id\":\"%d\"}}",IndexName,id));  //index line
						result.add(bulk_json_line);
						if((result.size()) >= 600000){
							File file = new File(ROOT + "\\docs\\wetterZwischenergebnisse\\bulk_json\\bulk_json_dir\\" + nb + ".ndjson");
							nb += "1";
							BufferedWriter bw = new BufferedWriter(new FileWriter(file));
							for(String s : result){
								bw.write( s + '\n');
							}
							bw.close();
							result = new ArrayList<String>();
							System.out.println(String.format("INFO\t.csv to .ndJSON: File %d",id + 1));
						}
						id++;
					}
				}
			}
			br.close();
		}
		File curl_cmd = create_cmd_file(IndexName, ROOT  + "\\docs\\wetterZwischenergebnisse\\bulk_json\\bulk_json_dir", "141.72.191.130");
		//Runtime.getRuntime().exec("cmd /c start \"\" " + curl_cmd.getPath());
	}
	/***
	 * This method is changing the correl_prep files to ndjson format 
	 * @param filePath
	 * @param indexName
	 * @param filename
	 * @throws IOException 
	 */
	public static void change_correl_prep_to_bulk(String filePath, String indexName, String filename, int id) throws IOException{
		ArrayList<String> fileLines = new ArrayList<String>();

			System.out.println("INFO\t changed corelprep to .ndJSON ");
			BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
			String s = "";
			br.readLine(); // header
			while((s = br.readLine()) != null){
				fileLines.add(s);
			}
			br.close();

			BufferedReader brr = new BufferedReader(new FileReader(new File(ROOT + "\\docs\\wetterZwischenergebnisse\\ufo_prep_for_c\\daily_ufos.csv")));
			ArrayList<String> brrList = new ArrayList<String>();
			String brrs = "";

			while((brrs = brr.readLine()) != null){
				brrList.add(brrs);
			}

			brr.close();
			Collections.sort(brrList);
			int in = 0;

			ArrayList<String> result = new ArrayList<String>();
			for(String ss : fileLines){
				result.add(String.format("{\"index\":{\"_index\":\"%s\",\"_id\":\"%d\"}}", indexName,id));
				String[] helper = ss.split(",");
				result.add(String.format("{\"Date\": \"%s\", \"occurrences\": %s, \"type\": \"%s\",\"ufo_amount\":\"%s\"}", helper[0], helper[1], helper[2],brrList.get(in).split(",")[1]));
				id++;in++;
				if(in >= brrList.size()){break;}
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename + ".json")));
			for(String sss : result){
				bw.write(sss + "\r\n");
			}
			bw.close();
	}
	/***
	 * calls the method change_correl_prep_to_bulk for every File
	 * @throws IOException 
	 */
	public static void change_correl_preps_to_bulks(String prep_for_correl_path, String IndexName) throws IOException{
		int id = 0;
			File cmdFile = new File(ROOT + "\\docs\\wetterZwischenergebnisse\\correl.sh");
			File[] files = new File(prep_for_correl_path).listFiles();
			BufferedWriter bw = new BufferedWriter(new FileWriter(cmdFile));

			bw.write("cd curl" +  "\r\n");
			for(File f : files){
				change_correl_prep_to_bulk ( f.getPath(), IndexName, (ROOT + "\\docs\\wetterZwischenergebnisse\\bulk_json\\BULK_correl\\" + f.getName().split("_")[2]) , id);
			}

			File[] filesbulk = new File(ROOT + "\\docs\\wetterZwischenergebnisse\\bulk_json\\BULK_correl").listFiles();
			for (File bf : filesbulk){
				bw.write(String.format("curl -H Content-Type:application/x-ndjson -XPUT localhost:9200/correltest/doc/_bulk?pretty --data-binary @%s\r\n",bf.getPath()));
			}
			bw.close();
			//Runtime.getRuntime().exec("cmd /c start \"\" " + cmdFile.getPath());

	}
	/***
	 * 
	 * @return ArrayList that represents a timeline from 1975 - to like today
	 */
	public static ArrayList<String> createtimeline(){
		Entry firstentry = new Entry(0, 1975, 1, 1);
		Entry entr = firstentry;
		ArrayList<String> arl = new ArrayList<String>();
		arl.add(new Entry(0,1975,1,1).toString());
		for(int i = 0; i < 15800; i++){
			entr = new Entry();
			entr.date = (firstentry.date = firstentry.date.plus(1, ChronoUnit.DAYS));
			entr.val_per_day = 0;
			arl.add(entr.toString());
		}
		return arl;
	}
	/***
	 * this method is taking the ufo file and creates a complete timeline so it fills all the missing dates and puts the sightings amount to 0 ... first tried to do it with java 8 with plus(temporalUnit) but that seems to not work for times longer than one year .. probably increases the dayOfYear attribute to over 365/366 and then it doesnt work anymore so i made it with comparing the Strings and wrote 2 files 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@SuppressWarnings("rawtypes")
	public static void ufo_prep_for_corellate() throws IOException, ParseException{
			File ufo_file = new File(ROOT + "\\src\\ufo_sightings\\ufo_data.json");

			JSONParser parser = new JSONParser();
			JSONArray JsAr = (JSONArray) parser.parse(new FileReader(ufo_file));
			Iterator iterator = JsAr.iterator();

			ArrayList<Entry>entriesAL = new ArrayList<Entry>();
			while(iterator.hasNext()){
				JSONObject outer = ((JSONObject) iterator.next());
				String[] helper = ((String) outer.get("datetime")).split(" ",2)[0].split("/",3);
				Entry entt = new Entry(1,Integer.parseInt(helper[2]),Integer.parseInt(helper[0]),Integer.parseInt(helper[1]));

				entriesAL.add(entt);
			}
			Collections.sort(entriesAL);
			ArrayList<Entry>entriesRes = new ArrayList<Entry>();

			Entry lastdate = new Entry(1,1,1,1);
			for(int ii = 0; ii < entriesAL.size(); ii++){
				Entry a = entriesAL.get(ii);
				if(a.date.getYear() < 1975){continue;}
				if(a.date.equals(lastdate.date)){
					lastdate.val_per_day += 1;
				}
				else{
					entriesRes.add(lastdate);
					lastdate = a;
				}
			}
			Collections.sort(entriesRes);

			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(ROOT + "\\docs\\wetterZwischenergebnisse\\ufo_prep_for_c\\daily_ufo_sightings.csv")));
			bw.write("date(Year,Month,Day),val_per_day\r\n");
			for(Entry en : entriesRes){
				bw.write(en.toString() + "\r\n");
			}
			bw.close();

			ArrayList<String> timeline = createtimeline();
			ArrayList<String> to_be_corrected = new ArrayList<String>();
			ArrayList<String> result = new ArrayList<String>();

			BufferedReader br = new BufferedReader(new FileReader(new File(ROOT + "\\docs\\wetterZwischenergebnisse\\ufo_prep_for_c\\daily_ufo_sightings.csv")));
			BufferedWriter resultBW = new BufferedWriter(new FileWriter(new File(ROOT + "\\docs\\wetterZwischenergebnisse\\ufo_prep_for_c\\daily_ufos.csv")));

			String ss ;
			br.readLine();
			br.readLine();
			while ((ss = br.readLine()) != null){
				to_be_corrected.add(ss);
			}
			br.close();

			for(int index = 0, j = 0; index < timeline.size() && j < to_be_corrected.size(); index++){
				if(timeline.get(index).split(",")[0].equals(to_be_corrected.get(j).split(",")[0])){
					result.add(to_be_corrected.get(j));
					j++;
				}else{
					result.add(timeline.get(index));
				}
			}
			resultBW.write("date(YYYY/MM/DD),sightings_per_day\r\n");
			for(int iii = 0; iii < result.size(); iii++){
				resultBW.write(result.get(iii) + "\r\n");
			}
			resultBW.close();
	}
	/***
	 * This method is grouping all the weatherData and all the ufo sightings together per quarter for corellation and elasticSearch. It creates an ndjsonFile 
	 * @param dirPath
	 * @param indexName
	 * @param ufopath
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@SuppressWarnings("rawtypes")
	public static void getWeatherData(String dirPath, String indexName, String ufopath) throws IOException, InterruptedException, ParseException{
		System.out.println("INFO\tstarting with the grouping of weather and ufo data per quarter ...");
		File[] dir = new File(dirPath).listFiles();

		HashMap<LocalDateTime, String> weather_per_month = new HashMap<LocalDateTime, String>();
		ArrayList<String> file = new ArrayList<String>();

		for(File f : dir){
			BufferedReader br = new BufferedReader(new FileReader(f));
			ArrayList<String> fileLine = new ArrayList<String>();
			String ss = "";
			while((ss = br.readLine()) != null){
				fileLine.add(ss);
			}
			br.close();
			for(String s : fileLine){
				String[] helper = s.split(",");

				LocalDateTime ldt = LocalDateTime.of(Integer.parseInt(helper[1]), Integer.parseInt(helper[2]), Integer.parseInt(helper[3]),0,0);
				int dayOfYear = ldt.getDayOfYear();
				int month = 0;

				if(dayOfYear <= 91){
					month = 3;
				}
				else if(dayOfYear <= 182){
					month = 6;
				}
				else if(dayOfYear <= 273){
					month = 9;
				}
				else if(dayOfYear <= 366){
					month = 12;
				}

				LocalDateTime hashMapKey = LocalDateTime.of(ldt.getYear(),month,1,0,0);
				if(weather_per_month.containsKey(hashMapKey)){

					String oldVal = weather_per_month.get(hashMapKey);
					String[] helper2 = oldVal.split(",");
					weather_per_month.replace(hashMapKey, 
							((long) Long.parseLong(helper[5]) + (long) Long.parseLong(helper2[0])) + "," +
									((long) Long.parseLong(helper[6]) + (long) Long.parseLong(helper2[1])) + "," + 
									((long) Long.parseLong(helper[7]) + (long) Long.parseLong(helper2[2])) + "," +
									((long) Long.parseLong(helper[8]) + (long) Long.parseLong(helper2[3])) + "," +
									((long) Long.parseLong(helper[9]) + (long) Long.parseLong(helper2[4])) + "," +
									((long) Long.parseLong(helper[10]) + (long) Long.parseLong(helper2[5])));
				}
				else{
					weather_per_month.put(hashMapKey, helper[5] + "," 
							+ helper[6] + "," + helper[7] + "," + helper[8] + "," 
							+ helper[9] + "," + helper[10]);
				}
			}
		}
		Iterator it = weather_per_month.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry entr = (Map.Entry) it.next();
			file.add(((LocalDateTime) entr.getKey()).format(fm) + "," + entr.getValue());
		}

		Collections.sort(file);
		int id = 0;

		JSONParser parser = new JSONParser();
		JSONArray JsAr = (JSONArray) parser.parse(new FileReader(new File(ufopath)));
		Iterator i = JsAr.iterator();

		HashMap<LocalDateTime, Integer> grouping = new HashMap<LocalDateTime,Integer>();

		while(i.hasNext()){
			JSONObject obj = ((JSONObject) i.next());
			String dateString = (String) obj.get("datetime");
			String[] helper = dateString.split(" ")[0].split("/");
			//System.out.println("INFO\t" + dateString);
			LocalDateTime ld = LocalDateTime.of(Integer.parseInt(helper[2]), Integer.parseInt(helper[0]), Integer.parseInt(helper[1]),0,0);
			int month = 0;
			int year = Integer.parseInt(helper[2]);
			if(year < 1975){continue;}

			if(ld.getDayOfYear() <= 91){
				month = 3;
			}
			else if(ld.getDayOfYear() <= 182){
				month = 6;
			}
			else if(ld.getDayOfYear() <= 273){
				month = 9;
			}
			else if(ld.getDayOfYear() <= 366){
				month = 12;
			}
			LocalDateTime hashMapKey = LocalDateTime.of(year,month,1,0,0);

			if(grouping.containsKey(hashMapKey)){
				int oldVal = grouping.get(hashMapKey);
				grouping.replace(hashMapKey, oldVal + 1);
			}
			else{
				grouping.put(hashMapKey, 1);
			}
		}

		ArrayList<String> finalFile = new ArrayList<String>();
		Iterator it2 = grouping.entrySet().iterator();
		while(it2.hasNext()){
			Map.Entry entr = (Map.Entry) it2.next();
			finalFile.add(((LocalDateTime) entr.getKey()).format(fm) + "," + entr.getValue());
		}
		Collections.sort(finalFile);

		BufferedWriter bwJS = new BufferedWriter(new FileWriter(new File("docs\\wetterZwischenergebnisse\\iHateMyLife.json")));
		for(int doubleindex = 0; doubleindex < finalFile.size()-1 ; doubleindex++){
			String s = file.get(doubleindex);
			String ufo_s = finalFile.get(doubleindex);

			bwJS.write(String.format("{\"index\":{\"_index\":\"%s\",\"_id\":\"%d\"}}\r\n", indexName, id));
			String[] helper3 = s.split(",");
			String[] helper4 =  ufo_s.split(",");
			bwJS.write(String.format("{\"quarter\": \"%s\",\"n\": %s,\"nr\": %s,\"se\": %s,\"h\": %s,\"d\": %s,\"tw\": %s,\"ufo_amount\": %s}\r\n", helper3[0], helper3[1], helper3[2],helper3[3],helper3[4],helper3[5],helper3[6],helper4[1]));

			id++;
		}
		bwJS.close();
	}


	public static void main (String[] args){

		System.out.println("INFO\tHi! The weather Preprocessing is starting now ...");

        File jarPath=new File(Weather_csv_to_bulk_json.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String ROOT = jarPath.getParentFile().getPath();
        System.out.println(" root-" + ROOT);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
		try {
			weather_csv_to_elast(ROOT + "\\src\\csv_dir", "weatherIndex");


			ufo_prep_for_corellate();
			prep_for_correl();
			change_correl_preps_to_bulks(ROOT + "\\docs\\wetterZwischenergebnisse\\prep_for_correl", "justAnIndex");

			create_cmd_file("us_weather_75_to_15", ROOT + "\\docs\\wetterZwischenergebnisse\\bulk_json\\bulk_json_dir", "localhost");

			getWeatherData(ROOT + "\\src\\csv_dir", "testIndexName", ROOT + "\\src\\ufo_sightings\\ufo_data.json");

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} catch (IOException e) {

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();


		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}



//getWeatherData("C:\\Users\\marku\\VS\\webdb\\weather\\csv_dir", "allTog", "C:\\Users\\marku\\VS\\webDB.weather.final\\bin\\weatherPackage\\Input\\ufo_sightings\\ufo_data.json");





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
