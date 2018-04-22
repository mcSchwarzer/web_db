package weatherPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import weatherPackage.prep_for_correlate.Entry;

public class ufo_prep_for_correllate {

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

	public static void main (String[] args){
		try{

			DateTimeFormatter fm = DateTimeFormatter.ofPattern("MM/dd/YYYY");
			prep_for_correlate.Entry entr = new prep_for_correlate.Entry(1,1,1,1);

			File ufo_file;
			System.out.println("input: path to json dir ");
			ufo_file = new File(new Scanner(System.in).nextLine());

			JSONParser parser = new JSONParser();
			JSONArray JsAr = (JSONArray) parser.parse(new FileReader(ufo_file));
			//ArrayList<webdb.prep_for_correlate.Entry> result = new ArrayList<webdb.prep_for_correlate.Entry>();


			Iterator i = JsAr.iterator();


			ArrayList<Entry>hopeIsThere = new ArrayList<Entry>();
			while(i.hasNext()){
				JSONObject outer = ((JSONObject) i.next());
				String[] hah = ((String) outer.get("datetime")).split(" ",2)[0].split("/",3);
				Entry entt = new Entry(1,Integer.parseInt(hah[2]),Integer.parseInt(hah[0]),Integer.parseInt(hah[1]));

				hopeIsThere.add(entt);
			}
			Collections.sort(hopeIsThere);
			ArrayList<Entry>hopeIsThereRes = new ArrayList<Entry>();

			Entry lastdate = new Entry(1,1,1,1);
			
			for(int ii = 0; ii < hopeIsThere.size(); ii++){
				Entry a = hopeIsThere.get(ii);
				if(a.date.getYear() < 1975){continue;}
				if(a.date.equals(lastdate.date)){
					lastdate.val_per_day += 1;
				}
				else{
					hopeIsThereRes.add(lastdate);
					lastdate = a;
				}
			}
			Collections.sort(hopeIsThereRes);

			//mon,tag,jahr
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("prep_for_c_ufo.csv")));
			bw.write("date(Year,Month,Day),val_per_day\r\n");
			for(Entry en : hopeIsThereRes){
				bw.write(en.toString());
			}
			bw.close();

		}catch(Exception e){
			e.printStackTrace();
		}




		/***
		 * 
		 * fill in all the missing dates for corrcoef
		 * 
		 */

		try{

			/***
			 * create Timeline:
			 */


			ArrayList<String> timeline = createtimeline();
			ArrayList<String> temp = new ArrayList<String>();
			ArrayList<String> to_be_corrected = new ArrayList<String>();
			ArrayList<String> result = new ArrayList<String>();

			BufferedReader br = new BufferedReader(new FileReader(new File("prep_for_c_ufo.csv")));


			BufferedWriter resultBW = new BufferedWriter(new FileWriter(new File("result.csv")));


			String ss ;
			br.readLine();
			br.readLine();
			while ((ss = br.readLine()) != null){
				to_be_corrected.add(ss);
			}
			br.close();
			
			
			for(int index = 0, j = 0; index < timeline.size() && j < to_be_corrected.size(); index++){
				
				System.out.println(timeline.get(index).split(",")[0] + "    " +(to_be_corrected.get(j).split(",")[0]));
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

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
