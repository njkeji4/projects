package com.shicha.yzmgt.aircb;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class MeterStatus {
	
	public double GroupactionEnergy; //	
	
    public double ActionEnergy;//":65.21,       #正向有功电能量
   
	
	public double ReactionEnergy; //:219.3,     #反向有功电能量
   
	
	public double vol; //":220.5,    #电压
    public double cur; //":1.503,    #电流
   
    
    public double ActionPower; //":0.3254,   #有功功率
    public double Freq;//":49.92,   #电网频率
    public double Factor;//":0.953,     #功率因数
    public int SwitchStat;//":1,     #开关状态： 0-拉闸， 1-合闸
    public String DataTime;//": "2019-04-11 14:15:00" #数据时间

    public MeterStatus() {}

	public double getReactionEnergy() {
		return ReactionEnergy;
	}

	public void setReactionEnergy(double reactionEnergy) {
		ReactionEnergy = reactionEnergy;
	}

	public double getGroupactionEnergy() {
		return GroupactionEnergy;
	}

	public void setGroupactionEnergy(double groupactionEnergy) {
		GroupactionEnergy = groupactionEnergy;
	}

	public double getActionEnergy() {
		return ActionEnergy;
	}

	public void setActionEnergy(double actionEnergy) {
		ActionEnergy = actionEnergy;
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
		return ActionPower;
	}

	public void setActionPower(double actionPower) {
		ActionPower = actionPower;
	}

	public double getFreq() {
		return Freq;
	}

	public void setFreq(double freq) {
		Freq = freq;
	}

	public double getFactor() {
		return Factor;
	}

	public void setFactor(double factor) {
		Factor = factor;
	}

	public int getSwitchStat() {
		return SwitchStat;
	}

	public void setSwitchStat(int switchStat) {
		SwitchStat = switchStat;
	}	
}
