  package toNdjson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ToNdJson {

	public static void main(String[] args) throws IOException, JSONException {		
		
		
		File fout = new File("<Path to the file where data should be written to>");
		FileOutputStream fos = new FileOutputStream(fout);
	 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		InputStream is = new FileInputStream("<Path to UFO Data JSON>");
        String jsonTxt = IOUtils.toString(is, "UTF-8");
        JSONArray jsonArray = new JSONArray(jsonTxt);
        JSONObject jsonObj = null;
        JSONObject locJson = null;
        
        for(int i = 0; i < jsonArray.length(); i ++ ) {
        	jsonObj = jsonArray.getJSONObject(i);
        	locJson = new JSONObject();
        	
        	locJson.put("lon", jsonObj.get("lon"));
        	locJson.put("lat", jsonObj.get("lat"));
        	
        	String datetime = (String) jsonObj.get("datetime");
        	String[] dateandtime = datetime.split(" ");
        	
        	jsonObj.put("date", dateandtime[0]);
        	jsonObj.put("time", dateandtime[1]);
        	
        	
        	jsonObj.remove("lon");
        	jsonObj.remove("lat");
        	jsonObj.remove("datetime");

        	jsonObj.put("location", locJson);
        	jsonObj.put("data_id", i);
        	
        	bw.write("{\"index\":{\"_index\":\"ufo_data\",\"_id\":" + i + "}}" + "\r\n");
			//bw.newLine();
			bw.write(jsonObj.toString() + "\r\n");
			//bw.newLine();
        	System.out.println("Progress: " + i + "/" +jsonArray.length());
        }
		bw.close();
    	System.out.println("finished");
	}

}
