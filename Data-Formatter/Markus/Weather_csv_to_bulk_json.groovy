package weatherDataStuff

class Weather_csv_to_bulk_json {
	
	/***
	 * in the stations csv there are 1000 entries with stationNumber 999999 so i just remove all of those 
	 * after that we still have over 6000 stations in the us ... 
	 * */
	public static HashMap<Integer,String> readStations_csv(String fp = "C:\\Users\\marku\\Desktop\\weather\\stations\\stations.csv"){
		HashMap<Integer,String> retMap = new HashMap<Integer,String>()
		File stationsFile = new File(fp)
		stationsFile.eachLine{
			line -> String[] helper = line.split(",", 4)
			if(helper[0] != "999999"){
				retMap.put(Integer.parseInt(helper[0]), ("\"geop\": { \"lat\": \"" + helper[2] + "\",\"lon\":\"" + helper[3] + "\"}")) //i only need the lat & lon
			}
		}
		return retMap
	}
	
	public static File create_cmd_file(String indexName, String bulk_json_dir, String ip){
		File cmd_file = new File("C:\\Users\\marku\\desk\\Programmierung\\weather_curl.cmd") 
		cmd_file << "cd elasticsearch-6.2.3\\bin\r\nstart elasticsearch.bat\r\ncd .. \r\ncd .. \r\ncd\r\ncd .. \r\ncd .. \r\nDownloads\\curl \r\nping 127.0.0.1 -n 30 > nul\r\n"
		File[] bulk_jsons = new File(bulk_json_dir).listFiles()
		for(File f : bulk_jsons){
			String p = f.getPath()
			cmd_file << "curl -H Content-Type:application/x-ndjson -XPUT $ip:9200/$indexName/doc/_bulk?pretty --data-binary @$p \r\n"
		}
		return cmd_file
	}
		
	static void main (args){
		HashMap<Integer,String> stationMap = readStations_csv()
		File CSV_DIR
		int id = 0	//int : -2,147,483,648 to +2,147,483,647 is enough for all index lines -> shouldnt be more than 30 mio
		String nb = 1
		print 'input: path to csv dir '
		print "csv_directory: ${CSV_DIR = new File(System.in.newReader().readLine())}\n"

		if(CSV_DIR.isDirectory()){
			File[] csv_weather_files = CSV_DIR.listFiles()
			ArrayList<String> result = new ArrayList<String>()
			for(File f : csv_weather_files){
				f.eachLine {
					/**csv: stationenNummer,jahr,monat,tag,temperatur,nebel,nieselRegen,schneeEis,hagel,donner,tornadoWolke**/
					line -> String[] helper = line.split(",", 11)
					if(stationMap.containsKey(Integer.parseInt(helper[0]))){
						/** ndJson String: **/
						String bulk_json_line = String.format("{\"sn\":\"%s\",\"date\":\"%s/%s/%s\",\"n\":\"%s\",\"nr\":\"%s\",\"se\":\"%s\",\"h\":\"%s\",\"d\":\"%s\",\"tw\":\"%s\",${stationMap.get(Integer.parseInt(helper[0]))}}", helper[0],helper[1],helper[2],helper[3],helper[5],helper[6],helper[7],helper[8],helper[9],helper[10])
						result.add("{\"index\":{\"_index\":\"us_weather_70_to_15\",\"_id\":\"$id\"}}")  //index line
						result.add(bulk_json_line)
						if((result.size()) >= 600000){
							File file = new File("C:\\Users\\marku\\Desktop\\weather\\bulk_json_dir\\$nb).ndjson")
							nb += "1"
							BufferedWriter bw = new BufferedWriter(new FileWriter(file))
							for(String s : result){
								bw.write( s + '\n')
							}
							bw.close()
							result = new ArrayList<String>()
							print "done with a file (to $id)\n"
						}
						id++
					}
				}
			}
		}
		File curl_cmd = create_cmd_file("us_weather_70_to_15", "C:\\Users\\marku\\Desktop\\weather\\bulk_json_dir", "141.72.191.130")	
	}
}
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
