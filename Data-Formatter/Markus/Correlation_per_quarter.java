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
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import weatherPackage.prep_for_correlate.Entry;;

public class Correlation_per_month_and_state {
	
	public static DateTimeFormatter dfm = DateTimeFormatter.ofPattern("yyyy/MM/dd");

	public static HashMap<Integer, String> getStationsData(String stationsPath) throws NumberFormatException, IOException {

		HashMap<Integer,String> retMap = new HashMap<Integer,String>();
		File stationsFile = new File(stationsPath);
		BufferedReader br = new BufferedReader(new FileReader(stationsFile));
		String s = "";
		while((s = br.readLine()) != null){
			String[] helper = s.split(",");
			retMap.put(Integer.parseInt(helper[0]), helper[1] + "," + helper[2] + "," + helper[3]);
		}
		br.close();
		return retMap;
	}



	public static HashMap<Integer, String> getWeatherData(String dirPath, String indexName) throws IOException, InterruptedException{
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
			
//
//			for(String i : file){
//				System.out.println(i);
//			}
//			Thread.sleep(5000);
		}
		Iterator it = weather_per_month.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry entr = (Map.Entry) it.next();
			file.add(((LocalDateTime) entr.getKey()).format(dfm) + "," + entr.getValue());
		}
		
		
		
		Collections.sort(file);
		int id = 0;
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("iHateMyLife.csv")));
		BufferedWriter bwJS = new BufferedWriter(new FileWriter(new File("iHateMyLife.json")));
		for(String s : file){
			

			bwJS.write(String.format("{\"index\":{\"_index\":\"%s\",\"_id\":\"%d\"}}\r\n", indexName, id));
			String[] helper3 = s.split(",");
			bwJS.write(String.format("{\"quarter\": \"%s\",\"n\": %s,\"nr\": %s,\"se\": %s,\"h\": %s,\"d\": %s,\"tw\": %s}\r\n", helper3[0], helper3[1], helper3[2],helper3[3],helper3[4],helper3[5],helper3[6]));
				
			
			bw.write(s + "\r\n");
			id++;
		}
		bw.close();
		bwJS.close();
		return null;

	}
	
	public static void groupUfoSightingsPerQuarter(String filePath, String indexname) throws IOException, ParseException{
		
		JSONParser parser = new JSONParser();
		JSONArray JsAr = (JSONArray) parser.parse(new FileReader(new File(filePath)));
		

		Iterator i = JsAr.iterator();


		ArrayList<Entry>ufoJson = new ArrayList<Entry>();
		HashMap<LocalDateTime, Integer> grouping = new HashMap<LocalDateTime,Integer>();
		
		while(i.hasNext()){
			JSONObject obj = ((JSONObject) i.next());
			String dateString = (String) obj.get("datetime");
			String[] helper = dateString.split(" ")[0].split("/");
			System.out.println(dateString);
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
		Iterator it = grouping.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry entr = (Map.Entry) it.next();
			finalFile.add(((LocalDateTime) entr.getKey()).format(dfm) + "," + entr.getValue());
		}
		Collections.sort(finalFile);
		
		
		
		
		int id = 0;
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("groupedUFOquarter.csv")));
		BufferedWriter bwJS = new BufferedWriter(new FileWriter(new File("groupedUFOquarter.json")));
		
		for(String s : finalFile){
			bw.write(s + "\r\n");

			bwJS.write(String.format("{\"index\":{\"_index\":\"%s\",\"_id\":\"%d\"}}\r\n", indexname, id));
			String[] helper3 = s.split(",");
			bwJS.write(String.format("{\"quarter\": \"%s\",\"amount_of_sightings\": %s}\r\n", helper3[0], helper3[1]));
			
			id++;
		}
		bw.close();
		bwJS.close();
		
	}
	
	public static void main(String[] args){




		try {
			groupUfoSightingsPerQuarter("C:\\Users\\marku\\Downloads\\UFO-Sightings_fullStatenames.json", "grouped_wigga");
			getWeatherData("C:\\Users\\marku\\VS\\webdb\\weather\\csv_dir", "grouped_deinemutterficktarschkuh");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

/**
 *  +5
 "SELECT stn AS stationenNummerW,year AS jahr,mo AS monat,da AS tag,temp AS temperatur,fog AS nebel,rain_drizzle AS nieselRegen,snow_ice_pellets AS schneeEis,hail AS hagel,thunder AS donner,tornado_funnel_cloud as tornadoWolke FROM `bigquery-public-data.noaa_gsod.gsod%d`"		
 		if(s.equals("nr")){return 0;}
		if(s.equals("n")){return 1;}
		if(s.equals("d")){return 2;}
		if(s.equals("h")){return 3;}
		if(s.equals("tw")){return 4;}
		if(s.equals("se")){return 5;}

 **/
