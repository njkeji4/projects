package com.shicha.yzmgt.bean.basestation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseStationAnalyze {
	
	
	public static List<TimeLine> analyze(List<BaseStation>stations,BaseConf conf){
		
		//合并所有符合条件的时间段
		List<TimeLine> all = new ArrayList<TimeLine>();
		for(BaseStation bs : stations) {
			if(bs.getResults() == null || bs.getResults().size() == 0) {
				continue;
			}
			for(TimeLine tl : bs.getResults()) {
				if(!contain(all, tl)) {
					all.add(tl);
				}
			}
		}
		
		//没有满足条件的记录
		if(all.size() == 0) {
			return null;
		}
		
		//分析每一个时间段，要在至少配置文件里面指定的至少  n 天 里面包含这个时间段
		List<TimeLine> result = new ArrayList<TimeLine>();
		for(TimeLine tl : all) {
			int days = 0;
			
			for(BaseStation bs : stations) {
				if(bs.containTime(tl)) {
					days++;
				}
			}
			
			if(days >= conf.getDaysCount() && !issub(result, tl)) {
				result.add(tl);
			}
		}
		
		return result;
	}
	
	//满足条件的时间段是否是另外一个时间段的子集
	public static boolean issub(List<TimeLine>all, TimeLine toadd) {
		
		for(TimeLine tl : all) {
			if(toadd.getStart() >= tl.getStart() && toadd.getEnd() <= tl.getEnd()) {
				return true;
			}
		}
		
		return false;
	}
	
	//合并时间段的过程中检查是否已经存在重复的
	public static boolean contain(List<TimeLine> all, TimeLine toadd) {
		
		
		for(TimeLine tl : all) {
			if(toadd.getStart() == tl.getStart() && toadd.getEnd() == tl.getEnd()) {
				return true;
			}
		}
		
		return false;
	}
	
}
