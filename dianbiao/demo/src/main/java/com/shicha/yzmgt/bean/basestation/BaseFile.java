package com.shicha.yzmgt.bean.basestation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BaseFile {		
	
	HashMap<String, List<BaseStation>>map = new HashMap<String, List<BaseStation>>();
	
	public List<BaseAnalyzeResult> parse(List<BaseStation>stations, BaseConf conf) {
		
		for(BaseStation station : stations) {
			
			if(!map.containsKey(station.getName())) {
				map.put(station.getName(), new ArrayList<BaseStation>());
			}
			map.get(station.getName()).add(station);			
		}
		
		return analyze(conf);
	}
	
	public List<BaseAnalyzeResult> analyze(BaseConf conf) {
		
		List<BaseAnalyzeResult>result = new ArrayList<BaseAnalyzeResult>();
		
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()) {
			String name = it.next();
			List<BaseStation> stations = map.get(name);
			
			List<TimeLine> timeLines = BaseStationAnalyze.analyze(stations, conf);
			if(timeLines != null && timeLines.size() > 0) {
				for(TimeLine tl : timeLines)
					result.add(new BaseAnalyzeResult(name, tl.getStart(), tl.getEnd()));
			}
			
			///////////////////////////////////////////print result
//			System.out.println("name=" + name + ":");
//			String output = "";
//			if(timeLines != null)
//				for(TimeLine tl : timeLines) {
//					output += tl.getStart() + "-" + tl.getEnd() + "  ";
//				}
//			System.out.println(output);
//			System.out.println("---------------------------------------");
			////////////////////////////////////////////
		}
		return result;
	}
	
}
