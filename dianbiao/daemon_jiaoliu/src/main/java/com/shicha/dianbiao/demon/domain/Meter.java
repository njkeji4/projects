package com.shicha.dianbiao.demon.domain;

import com.shicha.dianbiao.demon.netty.CmdRes;

public class Meter {

	public static void parseReadData(byte[] buf, CmdRes res, String addr,int cmdCode, int type) {
		
		switch(cmdCode) {
			case Command.READ_METER:
			case Command.READ_METER3:
				res.setData(new MeterData(buf, cmdCode == Command.READ_METER ? 0 : 1, addr));
				break;
				
			case Command.READ_DATE:
				res.setData(new MeterDate(buf));
				break;
				
			case Command.READ_TIME:
				res.setData(new MeterTime(buf));
				break;
			case Command.READ_AUTOONOFF:
				res.setData(new AutoOnOff(buf));
				break;
				
			case Command.READ_PERIOD:
				res.setData(new MeterPeriod(buf));
				break;
				
			case Command.READ_STATE:
				res.setData(new MeterValue(buf, 2, 0));
				break;
				
			case Command.READ_OVERC:
				res.setData(new MeterValue(buf, 2, 1));
				break;
				
			case Command.READ_OVERCT:
				res.setData(new MeterValue(buf, 1, 0));
				break;
				
			case Command.READ_OVERV:
				res.setData(new MeterValue(buf, 2, 1));
				break;
				
			case Command.READ_OVERVT:
				res.setData(new MeterValue(buf, 1, 0));
				break;
				
			case Command.READ_OVERL:
				res.setData(new MeterValue(buf, 3, 4));
				break;
				
			case Command.READ_OVERLT:
				res.setData(new MeterValue(buf, 1, 0));
				break;
				
			case Command.READ_IP:
				res.setData(buf);
				break;
				
			case Command.READ_FEE:
				res.setData(buf);
				break;
				
			default:
				res.setData(buf);
				break;
		}
	}
	
}
