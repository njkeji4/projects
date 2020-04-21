package com.shicha.yzmgt.bean.basestation;

import java.io.Serializable;

public class BaseStationID implements Serializable{

	private static final long serialVersionUID = 1L;
	
	String name;
	long dataDate;
	
	public BaseStationID() {}
	
	public BaseStationID(String name, long dataDate) {
		this.name = name;
		this.dataDate = dataDate;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseStationID other = (BaseStationID) obj;
		if (dataDate != other.dataDate)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (dataDate ^ (dataDate >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}  

}
