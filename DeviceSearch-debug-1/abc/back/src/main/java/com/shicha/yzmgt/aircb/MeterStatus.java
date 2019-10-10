package com.shicha.yzmgt.aircb;

public class MeterStatus {

	double GroupactionEnergy; //
    double ActionEnergy;//":65.21,       #正向有功电能量
    double ReactionEnergy; //:219.3,     #反向有功电能量
    double vol; //":220.5,    #电压
    double cur; //":1.503,    #电流
    double ActionPower; //":0.3254,   #有功功率
    double Freq;//":49.92,   #电网频率
    double Factor;//":0.953,     #功率因数
    int SwitchStat;//":1,     #开关状态： 0-拉闸， 1-合闸
    String DateTime;//": "2019-04-11 14:15:00" #数据时间

    public MeterStatus() {}

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

	public double getReactionEnergy() {
		return ReactionEnergy;
	}

	public void setReactionEnergy(double reactionEnergy) {
		ReactionEnergy = reactionEnergy;
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

	public String getDateTime() {
		return DateTime;
	}

	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}
}
