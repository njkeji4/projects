package com.shicha.dianbiao.demon.domain;

import java.util.HashMap;
import java.util.Map;

import com.shicha.dianbiao.demon.netty.Utils;

public class Command {

//	public static int OFF = 1;
//	public static int ON = 2;
//	public static int WRITE_METER = 8;	//write ok
	
	public static final int READ_METER = 100;
	public static final int READ_METER3 = 200;		//三相表
	public static final int READ_TIME = 101;
	public static final int READ_DATE = 102;
	
	public static final int READ_IP = 103;
	public static final int READ_STATE = 104;
	public static final int READ_FEE = 105;	
	public static final int READ_AUTOONOFF = 106;
	public static final int READ_PERIOD = 107;
	
	public static final int READ_OVERV = 108;
	public static final int READ_OVERVT = 109;
	public static final int READ_OVERC = 110;
	public static final int READ_OVERCT = 111;
	
	public static final int READ_OVERL = 112;
	public static final int READ_OVERLT = 113;	
	
	public static final Map<String,Integer> map = new HashMap<String,Integer>() ;	
	static {
		
	       map.put("04601001",READ_METER) ;	//单相集抄	       
	       map.put("04601206",READ_METER3) ; //三相集抄
	       
	       map.put("04000101",READ_DATE) ; //
	       map.put("04000102",READ_TIME) ; //
	       
	       map.put("04000401",READ_IP) ; //
	       map.put("04000503",READ_STATE) ; //
	       map.put("04010001",READ_FEE) ; //
	       map.put("04020001",READ_AUTOONOFF) ; //
	       
	       map.put("04040400",READ_PERIOD) ; //
	       map.put("0400010D",READ_PERIOD) ; //
	       
	       map.put("04090301",READ_OVERV) ; //
	       map.put("04090302",READ_OVERVT) ; //
	       map.put("04090801",READ_OVERC) ; //
	       map.put("04090801",READ_OVERCT) ; //
	       
	       map.put("04090B01",READ_OVERL) ; //
	       map.put("04090B02",READ_OVERLT) ; //   
	  }
	
	
	//单相映射
	public static final Map<String,byte[]> readCmdMap = new HashMap<String,byte[]>();
	static {		
		readCmdMap.put("date",new byte[] {04,00,01,01});   //日期及星期
		readCmdMap.put("time",new byte[] {04,00,01,02});	//时间
		
		readCmdMap.put("ip",new byte[] {04,00,04,01});  //通信地址
		readCmdMap.put("state",new byte[] {04,00,05,03}); //运行状态字
		readCmdMap.put("fee",new byte[] {04,01,00,01}); //起始时间及费率号
		readCmdMap.put("autoonoff",new byte[] {04,02,00,01}); //通断电时间控制
		
		readCmdMap.put("period",new byte[] {04,04,04,00}); //主动上报间隔时间
		
		readCmdMap.put("overload",new byte[] {04,0x09,0x0B,01});  //超负荷限值
		readCmdMap.put("overloadtime",new byte[] {04,0x09,0x0B,02});  //超负荷延迟时间
	}
	
	//三相映射
	public static final Map<String,byte[]> readCmdMap3 = new HashMap<String,byte[]>();
	static {		
		readCmdMap3.put("date",new byte[] {04,00,01,01});   //日期及星期
		readCmdMap3.put("time",new byte[] {04,00,01,02});	//时间
		
		readCmdMap3.put("ip",new byte[] {04,00,04,01});  //通信地址
		readCmdMap3.put("state",new byte[] {04,00,05,03}); //运行状态字
		readCmdMap3.put("fee",new byte[] {04,01,00,01}); //起始时间及费率号
		readCmdMap3.put("autoonoff",new byte[] {04,02,00,01}); //通断电时间控制
		
		readCmdMap3.put("period",new byte[] {04,00,01,0x0d}); //主动上报间隔时间
		
		readCmdMap3.put("overv",new byte[] {04,0x09,03,01}); //过压阀值（三相）
		readCmdMap3.put("overvt",new byte[] {04,0x09,03,02}); //过压延迟时间（三相）
		readCmdMap3.put("overc",new byte[] {04,0x09,0x08,01}); //过流阀值（三相）
		readCmdMap3.put("overct",new byte[] {04,0x09,0x08,01}); //过流延迟时间（三相）
		
		readCmdMap3.put("overload",new byte[] {04,0x09,0x0B,01});  //超负荷限值
		readCmdMap3.put("overloadtime",new byte[] {04,0x09,0x0B,02});  //超负荷延迟时间
	}
	
	
	public static int getCmdCode(byte[] buf) {
		
		int idx = 13;		
		if(buf.length <= idx)
			return -1;
			
		byte[] data = new byte[4];
		data[0] =  (byte)((buf[idx--] & 0xff) - 0x33);
		data[1] =  (byte)((buf[idx--] & 0xff) - 0x33);
		data[2] =  (byte)((buf[idx--] & 0xff) - 0x33);
		data[3] =  (byte)((buf[idx--] & 0xff) - 0x33);
		
		String key = Utils.byte2str(data);
		
		if(map.containsKey(key.toUpperCase()))
			return map.get(key.toUpperCase());
		
		return -1;
	}
}
