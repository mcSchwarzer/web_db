package weatherPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.filechooser.FileNameExtensionFilter;

public class prep_for_correlate {
	static DateTimeFormatter fm = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	static String[] fields = {"no","no","no","no","no","n","nr","se","h","d","tw"};
		
	public static int filenameToIntID(String s){
		if(s.equals("nr")){return 0;}
		if(s.equals("n")){return 1;}
		if(s.equals("d")){return 2;}
		if(s.equals("h")){return 3;}
		if(s.equals("tw")){return 4;}
		if(s.equals("se")){return 5;}
		
		else return 10;
	}
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
	 *
	 * 		csv: 315280,1970,01,01,-30.2,0,0,0,0,0,0
	 *
	 *		n=2,nr=3,se=4,h=5,d=6,tw=7
	 *
	 *		SELECT stn AS stationenNummerW,year AS jahr,mo AS monat,da AS tag,temp AS temperatur,fog AS nebel,rain_drizzle AS nieselRegen,snow_ice_pellets AS schneeEis,hail AS hagel,thunder AS donner,tornado_funnel_cloud as tornadoWolke
	 * @return 
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
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("prep_for_correl_" + fieldname +".csv")));
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
	@SuppressWarnings("resource")
	public static void main (String[] args){
		try{
		System.out.println("input: path to csv dir");

		File CSV_DIR = new File(new Scanner(System.in).nextLine());
		String[] all_weather_events = {"n","nr","se","h","d","tw"};
		for(String s : all_weather_events){
			getField(s, CSV_DIR);
		}
		}catch(Exception e){
			e.printStackTrace();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
