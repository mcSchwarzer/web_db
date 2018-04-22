package weatherPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

public class correl_data_to_bulk {

	static int id = 0;
	public static void change_correl_prep_to_bulk(String filePath, String indexName, String filename){
		ArrayList<String> fileLines = new ArrayList<String>();
		try {
			System.out.println("give me the zuuucccc: ");
			BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
			String s = "";
			br.readLine(); // header
			while((s = br.readLine()) != null){
				fileLines.add(s);
			}
			br.close();

			BufferedReader brr = new BufferedReader(new FileReader(new File("C:\\Users\\marku\\VS\\webdb\\used_FOR_PREP\\result.csv")));
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
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename + ".json")));
			for(String sss : result){
				bw.write(sss + "\r\n");
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main (String[] args){
		try{
			File[] files = new File(args[0]).listFiles();
			File a = new File("yeUknowMe.cmd");
			BufferedWriter bw = new BufferedWriter(new FileWriter(a));

			bw.write("cd C:\\Users\\marku\\Downloads\\curl\r\n");
			for(File f : files){
				change_correl_prep_to_bulk(f.getPath(), args[1], "BULK_correl\\bulk_weather" + f.getName());
			}
			
			File[] filesbulk = new File(args[2]).listFiles();
			for (File bf : filesbulk){
				bw.write(String.format("curl -H Content-Type:application/x-ndjson -XPUT localhost:9200/correltest/doc/_bulk?pretty --data-binary @%s\r\n",bf.getPath()));
			}
			bw.close();
			Runtime.getRuntime().exec("cmd /c start \"\" " + a.getPath());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
