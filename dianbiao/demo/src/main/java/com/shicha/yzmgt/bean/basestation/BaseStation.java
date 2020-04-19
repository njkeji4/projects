package com.shicha.yzmgt.bean.basestation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="base_station")
public class BaseStation {
	
	public static int data_type_traffic = 0;
	public static int data_type_prbdown = 1;
	public static int data_type_prbup = 2;
	public static int data_type_activeuser = 3;
	
	
	@Id
	@Column(name="id", nullable=false, length=36)
	@GenericGenerator(name="system-uuid", strategy="uuid2")
	@GeneratedValue(generator="system-uuid")
	String id;
	
	
	String name;
	long dataDate;
	
	double[] traffic;
	double[] prbdown;
	double[] prbup;
	double[] activeUser;		
	
	@Transient  
	List<TimeLine>results = new ArrayList<TimeLine>();
	
	public BaseStation() {}
	
	public BaseStation(String name, long date) {
		this.name = name;
		this.dataDate = date;
	}
	
	
	public void analyze() {
		int[] fullfill = new int[24];
		for(int i = 0; i < traffic.length; i++) {
			if(traffic[i] < 10000 && prbdown[i] < 3 && prbup[i] < 3 && activeUser[i] < 0.5) {
				fullfill[i] = 1;
			}
		}
		
		int flag = 0;
		int start = 0; 
		int end = 0;
		for(int i = 0; i < fullfill.length; i++) {
			if(fullfill[i] == 1 && flag == 0) {
				start = i;
				flag = 1;
			}
			if(fullfill[i] == 0 && flag == 1) {
				end = i - 1;
				flag = 0;
				
				results.add(new TimeLine(start,end));
			}
		}
	}
	
	public void addData(double[] value, int type) {
		if(type == data_type_traffic) {
			traffic =  value;
		}else if(type == data_type_prbdown) {		
			prbdown =  value;
		}else if(type == data_type_prbup) {		
			prbup =  value;
		}else if(type == data_type_activeuser) {
			activeUser =  value;
		}
	}
	
	public boolean containTime(TimeLine tl) {
		if(results == null || results.size() == 0)
			return false;
		
		for(TimeLine tmp : results) {
			
			
			if(tmp.getStart() <= tl.getStart() && tmp.getEnd() >= tl.getEnd()) {
				return true;
			}
		}
		
		return false;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double[] getTraffic() {
		return traffic;
	}

	public void setTraffic(double[] traffic) {
		this.traffic = traffic;
	}

	public double[] getPrbdown() {
		return prbdown;
	}

	public void setPrbdown(double[] prbdown) {
		this.prbdown = prbdown;
	}

	public double[] getPrbup() {
		return prbup;
	}

	public void setPrbup(double[] prbup) {
		this.prbup = prbup;
	}

	public double[] getActiveUser() {
		return activeUser;
	}

	public void setActiveUser(double[] activeUser) {
		this.activeUser = activeUser;
	}

	public List<TimeLine> getResults() {
		return results;
	}

	public void setResults(List<TimeLine> results) {
		this.results = results;
	}


	public long getDataDate() {
		return dataDate;
	}


	public void setDataDate(long dataDate) {
		this.dataDate = dataDate;
	}	
}


