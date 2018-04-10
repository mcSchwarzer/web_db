package JsonWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonWriter {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		JsonWriter writer = new JsonWriter();
		// writer.writeOnlyCoordninates();
		writer.convertlocationtogeo();
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
