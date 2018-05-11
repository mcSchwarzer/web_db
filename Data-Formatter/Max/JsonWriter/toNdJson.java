package JsonWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class toNdJson {

	public static void main(String[] args) {
		toNdJson obj = new toNdJson();
		try {
			obj.toNdJson("src/fulldata.json", "src/ndjson/fulldatandjson.json", "ufo_data_refined");
			obj.toNdJson("src/drug_poisining_mortality_usa.json", "src/ndjson/drug_poisining_mortality_usandjson.json",
					"drug_poisining_mortality_usa");
			obj.toNdJson("src/correlation_Drug_Poisining_mortality-UFO-Sightings.json", "src/ndjson/correlation_Drug_Poisining_mortality-UFO-Sightingsndjson.json",
					"drug_correlation");
			obj.toNdJson("src/sichtungen_bildung.json", "src/ndjson/sichtungen_bildungndjson.json",
					"sichtungen_edu");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void toNdJson(String path_to_json, String path_target, String scheme_name)
			throws JSONException, IOException {
			System.out.println("Datei: " + path_to_json);
			File fout = new File(path_target);
			FileOutputStream fos = new FileOutputStream(fout);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			InputStream is = new FileInputStream(path_to_json);
			String jsonTxt = IOUtils.toString(is, "UTF-8");
			JSONArray jsonArray = new JSONArray(jsonTxt);
			JSONObject jsonObj = null;
			JSONObject locJson = null;

			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObj = jsonArray.getJSONObject(i);
				locJson = new JSONObject();
				bw.write("{\"index\":{\"_index\":\"" + scheme_name + "\",\"_id\":" + i + "}}" + "\r\n");
				bw.write(jsonObj.toString() + "\r\n");
				System.out.println("Progress: " + i + "/" + jsonArray.length());
			}
			bw.close();
			System.out.println("finished");
	}
}
