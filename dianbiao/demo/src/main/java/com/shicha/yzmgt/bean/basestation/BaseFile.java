package com.shicha.yzmgt.bean.basestation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BaseFile {		
	
	HashMap<String, List<BaseStation>>map = new HashMap<String, List<BaseStation>>();
	
	public void parse(List<BaseStation>stations) {
		
		for(BaseStation station : stations) {
			
			if(!map.containsKey(station.getName())) {
				map.put(station.getName(), new ArrayList<BaseStation>());
			}
			map.get(station.getName()).add(station);			
		}
		
		analyze(new BaseConf());
	}
	
	public void analyze(BaseConf conf) {
		
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()) {
			String name = it.next();
			List<BaseStation> stations = map.get(name);
			
			List<TimeLine> timeLines = BaseStationAnalyze.analyze(stations, conf);
			
			///////////////////////////////////////////print result
			System.out.println("name=" + name + ":");
			String result = "";
			if(timeLines != null)
				for(TimeLine tl : timeLines) {
					result += tl.getStart() + "-" + tl.getEnd() + "  ";
				}
			System.out.println(result);
			System.out.println("---------------------------------------");
			////////////////////////////////////////////
		}
	}
	
}
