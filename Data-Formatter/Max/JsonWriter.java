package JsonWriter;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonWriter {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		JsonWriter writer = new JsonWriter();
		writer.addStatenames();
		writer.repairdates();
	}

	public void bubblesort() {
		JSONParser parser = new JSONParser();
		JSONArray array, arrayTarget = new JSONArray();
		try {
			array = (JSONArray) parser.parse(new FileReader(
					"/Users/Max/Desktop/Dokumente/Theoriephase IV/Webbasierte Datenbanken/Numpy-WS/correlation_Drug_Poisining_mortality-UFO-Sightings.json"));
			int size = array.size();
			for (int j = 0; j < size; j++) {
				int smallestCorrIndex = 0;
				for (int i = 0; i < array.size() -1 ; i++) {
					if ((double) (((JSONObject) array.get(smallestCorrIndex))
							.get("Correlation-Coefficient")) > ((double)((JSONObject)array.get(i + 1))
									.get("Correlation-Coefficient"))) {

						smallestCorrIndex = i + 1;
						// System.out.println((double) ((JSONObject) array.get(i
						// + 1))
						// .get("Correlation-Coefficient"));

					}
				}
				
				JSONObject tmp = (JSONObject) array.get(smallestCorrIndex);
				array.remove(smallestCorrIndex);
				arrayTarget.add(tmp);
				
			}
			System.out.println(arrayTarget.size());
			BufferedWriter writer = new BufferedWriter(new FileWriter("sortierteKorrelation.txt"));
			for(int cnt = 0; cnt < arrayTarget.size();cnt++){
				JSONObject lul = (JSONObject)arrayTarget.get(cnt);
				writer.write(lul.get("State")+": "+lul.get("Correlation-Coefficient")+"\r\n");
			}
			for(int cnt = 0; cnt < arrayTarget.size();cnt++){
				JSONObject lul = (JSONObject)arrayTarget.get(cnt);
				writer.write(lul.get("State")+"\r\n");
			}
			for(int cnt = 0; cnt < arrayTarget.size();cnt++){
				JSONObject lul = (JSONObject)arrayTarget.get(cnt);
				int lululul = (int) ((double)lul.get("Correlation-Coefficient") * 10000);
				double xnew = (Math.round(lululul) / 10000.0);
				writer.write(xnew + "\r\n");
			}
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getvorlage() {
		JSONParser parser = new JSONParser();
		int wrongcounter = 0;
		int fullcounter = 0;
		try {
			JSONArray array = (JSONArray) parser.parse(new FileReader("stateabbrev Kopie.json"));
			Iterator i = array.iterator();
			JSONArray valuearray = new JSONArray();
			JSONObject obj = new JSONObject();
			for (int a = 1; a < 17; a++) {
				valuearray.add(0);
			}
			while (i.hasNext()) {
				String name = (String) i.next();
				obj.put(name, valuearray);

			}
			FileWriter writer = new FileWriter("auswertungsvorlage.json");
			writer.write(obj.toJSONString());
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void repairdates() {
		JSONParser parser = new JSONParser();
		int wrongcounter = 0;
		int fullcounter = 0;
		try {
			JSONArray array = (JSONArray) parser.parse(new FileReader(
					"/Users/Max/Desktop/Dokumente/Theoriephase IV/Webbasierte Datenbanken/Numpy-WS/finaledata.json"));
			Iterator i = array.iterator();
			JSONArray finalarray = new JSONArray();
			while (i.hasNext()) {
				JSONObject obj = (JSONObject) i.next();
				String date = (String) obj.get("datetime");
				Date df = new SimpleDateFormat("MM/dd/yyyy hh:mm").parse(date);
				if (date.contains("24:00")) {
					System.out.println(date);
					// Pattern pattern =
					// Pattern.compile("[0-9][0-9]?/[0-9][0-9]?/[0-9][0-9][0-9][0-9]
					// (24:00)");
					// Matcher matcher = pattern.matcher(date);
					// System.out.println(matcher.find() + matcher.group(1));
					date = date.replaceAll("(24:00)", "00:00");
					wrongcounter++;
					System.out.println(date);
				}
				obj.put("datetime", date);
				finalarray.add(obj);
				// System.out.println(df);
				// Calendar cal = Calendar.getInstance();
				// cal.setTime(df);
				// if (df.getHours() == new SimpleDateFormat("hh").parse("24"))
				// {
				// wrongcounter++;
				// cal.set(cal.YEAR, cal.MONTH, cal.DATE + 1, 0, 0);
				// System.out.println(cal.MONTH+"/"+cal.DATE+"/"+cal.YEAR+"
				// "+cal.HOUR_OF_DAY+":"+cal.MINUTE);
				// System.out.println("lul");
				// }
				fullcounter++;
			}
			FileWriter writer = new FileWriter("UFO-Sightings_fullStatenames.json");
			writer.write(finalarray.toString());
			writer.flush();
			System.out.println("Falsche Daten: " + wrongcounter + "\n Fullcounter: " + fullcounter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addStatenames() {
		JSONParser parser = new JSONParser();
		int wrongcounter = 0;
		try {
			JSONArray array = (JSONArray) parser.parse(new FileReader("/Users/Max/Downloads/fulldata.json"));
			JSONObject abbrev = (JSONObject) parser.parse(new FileReader("stateabbrev.json"));

			JSONObject errorobj = new JSONObject();
			// System.out.println(abbrev.get("AK"));
			Iterator i = array.iterator();
			JSONArray finalarray = new JSONArray();
			while (i.hasNext()) {

				JSONObject temp = (JSONObject) i.next();
				String state = (String) temp.get("state");

				String fullstate = (String) abbrev.get(state.toUpperCase());
				if (fullstate != null) {
					temp.put("fullState", fullstate);
					if (errorobj.get(state) != null) {
						int lul = (int) errorobj.get(state);
						lul++;
						errorobj.put(state, lul);
					} else {
						errorobj.put(state, 1);
					}
				} else {
					temp.put("fullState", null);
					if (state.isEmpty()) {
						state = "empty";
					}

					if (errorobj.get(state) == null) {
						errorobj.put(state, 1);
					} else {
						int tempanz = (int) errorobj.get(state);
						tempanz++;
						errorobj.put(state, tempanz);
					}
					wrongcounter++;

				}
				System.out.println(wrongcounter);
				finalarray.add(temp);

			}
			System.out.println("Fehlerhafte Staaten: " + wrongcounter);
			// FileWriter writer = new FileWriter("wrongstateauswertung.json");
			// writer.write(errorobj.toJSONString().replace("\\", ""));
			// writer.flush();
			//Gson gson = new GsonBuilder().setPrettyPrinting().create();
//			JsonParser jp = new JsonParser();
//			JsonElement je = jp.parse(finalarray.toJSONString().replace("\\", ""));
			String tempstring = finalarray.toString().replace("\\", "");
//			JsonArray arr = (JsonArray) jp.parse(tempstring);
//			String prettyJsonString = gson.toJson(arr);

			FileWriter writer = new FileWriter("UFO-Sightings_fullStatenames.json");
			writer.write(tempstring);
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
			e.toString();
		}
	}

	public void convertlocationtogeo() {
		JSONParser parser = new JSONParser();
		try {
			JSONArray array = (JSONArray) parser.parse(new FileReader(
					"/Users/Max/Desktop/Dokumente/Theoriephase IV/Webbasierte Datenbanken/Daten/Accidental Drug Related Deaths 2012-2017.json"));

			Iterator i = array.iterator();
			JSONArray finalarray = new JSONArray();
			int counter = 0;
			int wrongdata = 0;
			while (i.hasNext()) {
				JSONObject obj = (JSONObject) i.next();
				String oldloc = obj.get("DeathLoc").toString();
				// System.out.println(oldloc);
				Pattern pattern = Pattern
						.compile("(?:[A-Z]|[a-z]|[\\s]*, )?CT\\\n\\(([0-9]*.[0-9]*), (-[0-9]*.[0-9]*)\\)");
				Matcher matcher = pattern.matcher(oldloc);
				String lat;
				String lon;

				if (!obj.get("Date").toString().equals("")) {
					SimpleDateFormat a = new SimpleDateFormat("dd/MM/yyyy");
					System.out.println(obj.toString());
					// System.out.println((a.parse(obj.get("Date").toString())));
					Date date = a.parse(obj.get("Date").toString());
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					String finaldate = cal.get(cal.YEAR) + "-" + cal.get(cal.MONTH) + "-" + cal.get(cal.DAY_OF_MONTH);
					obj.put("Date", finaldate);
				}
				if (matcher.find()) {
					lat = matcher.group(1);
					lon = matcher.group(2);
					// System.out.println("Longitude: " + lat + "\n Latitude: "
					// + lon);
				} else {
					lat = "";
					lon = "";
					wrongdata++;
					System.out.println(obj.toJSONString());
				}
				Double finallat = new Double(lat);
				Double finallon = new Double(lon);

				JSONObject locationobj = new JSONObject();
				locationobj.put("lat", finallat);
				locationobj.put("lon", finallon);
				obj.put("DeathLoc", locationobj);
				finalarray.add(obj);

				counter++;
			}

			FileWriter writer = new FileWriter("accidentaldrugdeathswithcoordinates.json");
			writer.write(finalarray.toString());
			writer.flush();

			System.out.println("Done with " + counter + "\n Wrong Coordinates: " + wrongdata);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void getDate() {
		JSONParser parser = new JSONParser();
		try {
			JSONArray array = (JSONArray) parser
					.parse(new FileReader("/Users/Max/Documents/Coding/UFO-Sightings/my-app/src/fulldata.json"));

			Iterator i = array.iterator();
			JSONArray finalarray = new JSONArray();
			int counter = 0;
			while (i.hasNext()) {
				JSONObject obj = (JSONObject) i.next();
				String input = obj.get("datetime").toString();
				System.out.println(input);
				Pattern pattern = Pattern
						.compile("([0-9][0-9]/[0-9][0-9]/[1-2][0-9][0-9][0-9])\\s([0-9][0-9]:[0-9][0-9])");
				Matcher matcher = pattern.matcher(input);
				String time;
				String date;
				if (matcher.find()) {
					date = matcher.group(1);
					time = matcher.group(2);
				} else {
					time = "";
					date = "";
				}
				obj.put("time", time);
				obj.put("datetime", date);
				finalarray.add(obj);
				String test2 = obj.toString();
				String test = obj.toString();
				counter++;
			}

			FileWriter writer = new FileWriter("datawithdate.json");
			writer.write(finalarray.toString());
			writer.flush();

			System.out.println("Done with " + counter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeAmericans() {
		JSONParser parser = new JSONParser();
		JSONArray finalarray = new JSONArray();
		int notcounter = 0;
		int counter = 0;
		try {
			JSONArray array = (JSONArray) parser
					.parse(new FileReader("/Users/Max/Documents/Coding/UFO-Sightings/my-app/src/fulldata.json"));

			Iterator i = array.iterator();

			while (i.hasNext()) {
				JSONObject temp = (JSONObject) i.next();
				// System.out.println(temp.get("longitude") + "" +
				// temp.get("longitude").getClass());
				if (Double.parseDouble(temp.get("longitude").toString()) != 0.0
						&& Double.parseDouble(temp.get("latitude").toString()) != 0.0) {
					double longitude = Double.parseDouble(temp.get("longitude").toString());
					double latitude = Double.parseDouble(temp.get("latitude").toString());

					if (latitude < 50.00 && latitude > 25.00) {
						if (longitude < -63.00 && longitude > -162.00) {
							finalarray.add(temp);
						}
					} else {
						notcounter++;
						// System.out.println("Nicht in Amerika: " + counter);
					}
					counter++;
				}
			}

			FileWriter writer = new FileWriter("testingdata.json");
			writer.write(finalarray.toString());
			writer.flush();
			System.out.println("done" + "\n Gesamt: " + counter + "\n not in America: " + notcounter);

			writer.close();
		} catch (Exception e) {
			System.out.println(counter);
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void writeOnlyCoordninates() {
		JSONParser parser = new JSONParser();
		JSONArray finalarray = new JSONArray();
		int notcounter = 0;
		int counter = 0;

		final int MAXLENGTH = 87183;
		String[] inputs = new String[MAXLENGTH + 1];

		List x = new ArrayList<>();

		try {
			JSONArray array = (JSONArray) parser
					.parse(new FileReader("/Users/Max/Documents/Coding/UFO-Sightings/my-app/src/fulldata.json"));

			Iterator i = array.iterator();

			while (i.hasNext()) {
				JSONObject temp = (JSONObject) i.next();
				// System.out.println(temp.get("longitude") + "" +
				// temp.get("longitude").getClass());
				if (Double.parseDouble(temp.get("longitude").toString()) != 0.0
						&& Double.parseDouble(temp.get("latitude").toString()) != 0.0) {
					double longitude = Double.parseDouble(temp.get("longitude").toString());
					double latitude = Double.parseDouble(temp.get("latitude").toString());

					if (latitude < 50.00 && latitude > 25.00) {
						if (longitude < -63.00 && longitude > -162.00) {
							x.add("new google.maps.LatLng(" + latitude + ", " + longitude + ")");
							// finalarray.add("new
							// google.maps.LatLng("+latitude+", "+
							// longitude+")");
							System.out.println(counter);
						}
					} else {
						notcounter++;
						// System.out.println("Nicht in Amerika: " + counter);
					}
					counter++;
				}
			}
			// String temp = finalarray.toString().replaceAll("\"", "");
			// inalarray.clear();
			// for(int j = 0; j < inputs.length; j++){
			finalarray.addAll(x);
			String finalstring = finalarray.toString().replaceAll("\"", "");

			// finalarray.add(inputs);
			System.out.println(finalstring);
			FileWriter writer = new FileWriter("onlycoordinates.json");
			writer.write(finalstring);
			writer.flush();
			System.out.println("done" + "\n Gesamt: " + counter + "\n not in America: " + notcounter);

			writer.close();
		} catch (Exception e) {
			System.out.println(counter);
			e.printStackTrace();
		}

	}
}
