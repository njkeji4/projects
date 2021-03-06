package com.shicha.dianbiao.demon.domain;

import com.shicha.dianbiao.demon.netty.Device;

public class MeterData extends Meter{

	public MeterData() {}
	
	public MeterData(byte[] buf, int type, String deviceNo) {	
		this.deviceNo = deviceNo;
		try {
			
			if(type == Device.DEVICE_DC)
				parseDC(buf);
			if(type == Device.DEVICE_AC1)
				parseSingle(buf);
			if(type == Device.DEVICE_AC3)
				parseTriple(buf);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	//6818000000000068911d344393378373353383733533333333339555743533bb37333383c83b33de16
	public void parseSingle(byte[]  buf) {
		int start = 14;	
	    
	    double groupactionEnergy = parseDouble(buf, start, 4, 2);	 start+=4;			
	    allEnergy = parseDouble(buf, start, 4, 2); start+=4;		
	    double reactionEnergy = parseDouble(buf, start, 4, 2);	 start+=4;
		
		avol = parseDouble(buf, start, 2, 1);	 start+=2;
		acur = parseDouble(buf, start, 3, 3);	 start+=3;
		double aactionPower= parseDouble(buf, start, 3, 4);	 start+=3;
		
		double freq = parseDouble(buf, start, 2, 2);	 start+=2;
		double factor = parseDouble(buf, start, 2, 3);	 start+=2;
		switchStat = parseInt(buf,start,1);
	}
	
	//6804349321024468913c3945933733333333333333333333333333333333333333333333333385553333333333333333333333333333333333333333333343334333433343430316 
	//
	public void parseTriple(byte[] buf){
		int start = 14;
		allEnergy = parseDouble(buf, start, 4, 2);	 start+=4;			
		double allJianEnvery = parseDouble(buf, start, 4, 2); start+=4;		
		double allFengEnvery = parseDouble(buf, start, 4, 2);	 start+=4;
		double allPingEnvery =parseDouble(buf, start, 4, 2);	 start+=4;
		double allGuEnvery =parseDouble(buf, start, 4, 2);	 start+=4;
		
		avol = parseDouble(buf, start, 2, 1);	 start+=2;
		bvol = parseDouble(buf, start, 2, 1);	 start+=2;
		cvol = parseDouble(buf, start, 2, 1);	 start+=2;
		
		acur = parseDouble(buf, start, 3, 3);	 start+=3;
		bcur = parseDouble(buf, start, 3, 3);	 start+=3;
		ccur = parseDouble(buf, start, 3, 3);	 start+=3;
		
		double _allEnergy= parseDouble(buf, start, 3, 4);	 start+=3;
		aEnergy= parseDouble(buf, start, 3, 4);	 start+=3;
		bEnergy= parseDouble(buf, start, 3, 4);	 start+=3;
		cEnergy= parseDouble(buf, start, 3, 4);	 start+=3;
		
		double factor = parseDouble(buf, start, 2, 3);	 start+=2;
		double afactor = parseDouble(buf, start, 2, 3);	 start+=2;
		double bfactor = parseDouble(buf, start, 2, 3);	 start+=2;
		double cfactor = parseDouble(buf, start, 2, 3);	 start+=2;
		
		
		switchStat = parseInt(buf,start,1);
		
	}
	
	//6844479121024468912d3a459337334b33333b333333b83333337438333399443333ac37ac37a537a537333333333333cb343399363336d216
	public void parseDC(byte[] buf){
		int start = 14;
		allEnergy = parseDouble(buf, start, 4, 2);	 start+=4;			
		aEnergy = parseDouble(buf, start, 4, 2); start+=4;		
		bEnergy = parseDouble(buf, start, 4, 2);	 start+=4;
		cEnergy =parseDouble(buf, start, 4, 2);	 start+=4;
		dEnergy =parseDouble(buf, start, 4, 2);	 start+=4;
		
		avol = parseDouble(buf, start, 2, 1);	 start+=2;
		bvol = parseDouble(buf, start, 2, 1);	 start+=2;
		cvol = parseDouble(buf, start, 2, 1);	 start+=2;
		dvol = parseDouble(buf, start, 2, 1);	 start+=2;
		
		
		acur = parseDouble(buf, start, 3, 3);	 start+=3;
		bcur = parseDouble(buf, start, 3, 3);	 start+=3;
		ccur = parseDouble(buf, start, 3, 3);	 start+=3;
		dcur = parseDouble(buf, start, 3, 3);	 start+=3;		
		
		int tmp = buf[start] - 0x33;
		switchStat = tmp;//buf[start];//parseInt(buf,start,1);
		
	}
	
	//33 3 3 33 33
	public static double parseDouble(byte[]buf, int start, int len, int sublen) {
		int[] v = new int[len*2];
		int idx = 0;
		for(int i = start+len-1; i>= start; i--) {			
			v[idx++] = ((buf[i] -0x33 ) & 0xff) >> 4;
			v[idx++] = (buf[i] -0x33 ) & 0x0f;
		}
		
		double p = Math.pow(10, -sublen);		
		double sum = 0;		
		for(int i = v.length - 1; i >= 0 ; i--) {
			sum += v[i] * p;
			p = p*10;
		}
		
		//保留小数
		double pow = Math.pow(10, sublen);			
		sum = (double)Math.round(sum * pow)/pow;
		
		return sum;
	}
	
	public static int parseInt(byte[]buf, int start, int len) {		
		return (int)parseDouble(buf, start, len, 0);
		
	}
	
	double allEnergy;  //组合有功总电量	
	double aEnergy;
	double bEnergy;
	double cEnergy;	
	double dEnergy;
	
	double avol;
	double bvol;
	double cvol;
	double dvol;
	
	double acur;
	double bcur;
	double ccur;
	double dcur;	
	
    int switchStat;//":1,     #开关状态： 0-合闸， 其他--拉闸状态-合闸
    
    String deviceNo;

	public double getAllEnergy() {
		return allEnergy;
	}

	public void setAllEnergy(double allEnergy) {
		this.allEnergy = allEnergy;
	}

	public double getaEnergy() {
		return aEnergy;
	}

	public void setaEnergy(double aEnergy) {
		this.aEnergy = aEnergy;
	}

	public double getbEnergy() {
		return bEnergy;
	}

	public void setbEnergy(double bEnergy) {
		this.bEnergy = bEnergy;
	}

	public double getcEnergy() {
		return cEnergy;
	}

	public void setcEnergy(double cEnergy) {
		this.cEnergy = cEnergy;
	}

	public double getdEnergy() {
		return dEnergy;
	}

	public void setdEnergy(double dEnergy) {
		this.dEnergy = dEnergy;
	}

	public double getAcur() {
		return acur;
	}

	public void setAcur(double acur) {
		this.acur = acur;
	}

	public double getBcur() {
		return bcur;
	}

	public void setBcur(double bcur) {
		this.bcur = bcur;
	}

	public double getCcur() {
		return ccur;
	}

	public void setCcur(double ccur) {
		this.ccur = ccur;
	}

	public double getDcur() {
		return dcur;
	}

	public void setDcur(double dcur) {
		this.dcur = dcur;
	}

	public int getSwitchStat() {
		return switchStat;
	}

	public void setSwitchStat(int switchStat) {
		this.switchStat = switchStat;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public double getAvol() {
		return avol;
	}

	public void setAvol(double avol) {
		this.avol = avol;
	}

	public double getBvol() {
		return bvol;
	}

	public void setBvol(double bvol) {
		this.bvol = bvol;
	}

	public double getCvol() {
		return cvol;
	}

	public void setCvol(double cvol) {
		this.cvol = cvol;
	}

	public double getDvol() {
		return dvol;
	}

	public void setDvol(double dvol) {
		this.dvol = dvol;
	}
}
