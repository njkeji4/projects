package com.shicha.dianbiao.demon.domain;

import java.util.HashMap;
import java.util.Map;

import com.shicha.dianbiao.demon.netty.Utils;

public class Command {

	public static int READ_METER = 0;
	public static int OFF = 1;
	public static int ON = 2;
	public static int WRITE_METER = 8;	//write ok
	
	public static final Map<String,Integer> map = new HashMap<String,Integer>() ;
	
	static {
	       map.put("04601001",READ_METER) ;	//单相集抄
	       
	       map.put("04601206",READ_METER) ; //三相集抄
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
		
		if(map.containsKey(key))
			return map.get(key);
		
		return -1;
	}
}
