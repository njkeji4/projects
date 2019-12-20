package com.shicha.dianbiao.demon.netty;

public class MeterData {

	public MeterData() {}
	
	public MeterData(byte[] buf, int type) {		
		if(type == 0) {
			parseSingle(buf);
		}else {
			parseTriple(buf);
		}
	}
	
	public void parseSingle(byte[]  buf) {
		int start = 14;	
	    
	    groupactionEnergy = parseDouble(buf, start, 4, 2);	 start+=4;			
	    actionEnergy = parseDouble(buf, start, 4, 2); start+=4;		
	    reactionEnergy = parseDouble(buf, start, 4, 2);	 start+=4;
		
		vol = parseDouble(buf, start, 2, 1);	 start+=2;
		cur = parseDouble(buf, start, 3, 3);	 start+=3;
		actionPower= parseDouble(buf, start, 3, 4);	 start+=3;
		
		freq = parseDouble(buf, start, 2, 2);	 start+=2;
		factor = parseDouble(buf, start, 2, 3);	 start+=2;
		switchStat = parseInt(buf,start,1);
	}
	
	//68 043493210244 68 91 3c 39459337 
	//33333333 333333333333333333333333333333333333333385553333333333333333333333333333333333333333333343334333433343430316
	public void parseTriple(byte[] buf){
		int start = 14;
		allEnergy = parseDouble(buf, start, 4, 2);	 start+=4;			
		allJianEnvery = parseDouble(buf, start, 4, 2); start+=4;		
		allFengEnvery = parseDouble(buf, start, 4, 2);	 start+=4;
		allPingEnvery =parseDouble(buf, start, 4, 2);	 start+=4;
		allGuEnvery =parseDouble(buf, start, 4, 2);	 start+=4;
		
		avol = parseDouble(buf, start, 2, 1);	 start+=2;
		bvol = parseDouble(buf, start, 2, 1);	 start+=2;
		cvol = parseDouble(buf, start, 2, 1);	 start+=2;
		
		acur = parseDouble(buf, start, 3, 3);	 start+=3;
		bcur = parseDouble(buf, start, 3, 3);	 start+=3;
		ccur = parseDouble(buf, start, 3, 3);	 start+=3;
		
		actionPower= parseDouble(buf, start, 3, 4);	 start+=3;
		aActionPower= parseDouble(buf, start, 3, 4);	 start+=3;
		bActionPower= parseDouble(buf, start, 3, 4);	 start+=3;
		cActionPower= parseDouble(buf, start, 3, 4);	 start+=3;
		
		factor = parseDouble(buf, start, 2, 3);	 start+=2;
		afactor = parseDouble(buf, start, 2, 3);	 start+=2;
		bfactor = parseDouble(buf, start, 2, 3);	 start+=2;
		cfactor = parseDouble(buf, start, 2, 3);	 start+=2;
		
		
		switchStat = parseInt(buf,start,1);
		
	}
	
	//33 3 3 33 33
	public static double parseDouble(byte[]buf, int start, int len, int sublen) {
		int[] v = new int[len*2];
		int idx = 0;
		for(int i = start+len-1; i>= start; i--) {			
			v[idx++] = ((buf[i] -0x33 ) & 0xff) >> 4;
			v[idx++] = (buf[i] -0x33 ) & 0x0f;
		}
		
		double p = 1;
		if(sublen != 0) {
			int t = sublen;
			while(t-- != 0 ) p = p /  10;
		}
		
		double sum = 0;
		for(int i = v.length - 1; i >= 0 ; i--) {
			sum += v[i] * p;
			p = p*10;
		}
		
		return sum;
	}
	
	public static int parseInt(byte[]buf, int start, int len) {		
		return (int)parseDouble(buf, start, len, 0);
		
	}
	
	double allEnergy;  //组合有功总电量		
	double allJianEnvery;  //组合有功尖费率电量		
	double allFengEnvery;  //组合有功峰费率电量		
	double allPingEnvery;  //组合有功平费率电量
	double allGuEnvery; //组合有功谷费率电量/
	double avol;
	double bvol;
	double cvol;
	
	double acur;
	double bcur;
	double ccur;
	
	double aActionPower;
	double bActionPower;
	double cActionPower;
	
	 double afactor;//":0.953,     #功率因数
	 double bfactor;//":0.953,     #功率因数
	 double cfactor;//":0.953,     #功率因数
	
	
	double groupactionEnergy;  //组合有功电能量		
    double actionEnergy;       //":65.21,       #正向有功电能量		
    double reactionEnergy;     //:219.3,     #反向有功电能量	
    double vol;               //":220.5,    #电压	
    double cur; //":1.503,    #电流
    double actionPower; //":0.3254,   #有功功率	
    double freq;//":49.92,   #电网频率		
    double factor;//":0.953,     #功率因数	
    int switchStat;//":1,     #开关状态： 0-合闸， 其他--拉闸状态-合闸
	public double getAllEnergy() {
		return allEnergy;
	}

	public void setAllEnergy(double allEnergy) {
		this.allEnergy = allEnergy;
	}

	public double getAllJianEnvery() {
		return allJianEnvery;
	}

	public void setAllJianEnvery(double allJianEnvery) {
		this.allJianEnvery = allJianEnvery;
	}

	public double getAllFengEnvery() {
		return allFengEnvery;
	}

	public void setAllFengEnvery(double allFengEnvery) {
		this.allFengEnvery = allFengEnvery;
	}

	public double getAllPingEnvery() {
		return allPingEnvery;
	}

	public void setAllPingEnvery(double allPingEnvery) {
		this.allPingEnvery = allPingEnvery;
	}

	public double getAllGuEnvery() {
		return allGuEnvery;
	}

	public void setAllGuEnvery(double allGuEnvery) {
		this.allGuEnvery = allGuEnvery;
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

	public double getaActionPower() {
		return aActionPower;
	}

	public void setaActionPower(double aActionPower) {
		this.aActionPower = aActionPower;
	}

	public double getbActionPower() {
		return bActionPower;
	}

	public void setbActionPower(double bActionPower) {
		this.bActionPower = bActionPower;
	}

	public double getcActionPower() {
		return cActionPower;
	}

	public void setcActionPower(double cActionPower) {
		this.cActionPower = cActionPower;
	}

	public double getAfactor() {
		return afactor;
	}

	public void setAfactor(double afactor) {
		this.afactor = afactor;
	}

	public double getBfactor() {
		return bfactor;
	}

	public void setBfactor(double bfactor) {
		this.bfactor = bfactor;
	}

	public double getCfactor() {
		return cfactor;
	}

	public void setCfactor(double cfactor) {
		this.cfactor = cfactor;
	}

	public double getGroupactionEnergy() {
		return groupactionEnergy;
	}

	public void setGroupactionEnergy(double groupactionEnergy) {
		this.groupactionEnergy = groupactionEnergy;
	}

	public double getActionEnergy() {
		return actionEnergy;
	}

	public void setActionEnergy(double actionEnergy) {
		this.actionEnergy = actionEnergy;
	}

	public double getReactionEnergy() {
		return reactionEnergy;
	}

	public void setReactionEnergy(double reactionEnergy) {
		this.reactionEnergy = reactionEnergy;
	}

	public double getVol() {
		return vol;
	}

	public void setVol(double vol) {
		this.vol = vol;
	}

	public double getCur() {
		return cur;
	}

	public void setCur(double cur) {
		this.cur = cur;
	}

	public double getActionPower() {
		return actionPower;
	}

	public void setActionPower(double actionPower) {
		this.actionPower = actionPower;
	}

	public double getFreq() {
		return freq;
	}

	public void setFreq(double freq) {
		this.freq = freq;
	}

	public double getFactor() {
		return factor;
	}

	public void setFactor(double factor) {
		this.factor = factor;
	}

	public int getSwitchStat() {
		return switchStat;
	}

	public void setSwitchStat(int switchStat) {
		this.switchStat = switchStat;
	}    
}
