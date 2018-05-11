package JsonWriter;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class csvtojsonconverter {

	public static void main(String[] args) {
		csvtojsonconverter conv = new csvtojsonconverter();
		conv.converttojson();
	}
	
	public void converttojson(){
		
		ArrayList finallist = new ArrayList();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("/Users/Max/Downloads/Drug_Poisoning_Mortality_by_State__United_States-2.csv")));
			String line;
			String header = reader.readLine();
			String [] keys = header.split(",");
			for(String i : keys){
				System.out.println("\"" + i + "\",");
			}
			int counter = 0;
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
