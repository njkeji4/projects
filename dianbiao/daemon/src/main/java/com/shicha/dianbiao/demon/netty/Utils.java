package com.shicha.dianbiao.demon.netty;

import java.text.SimpleDateFormat;

import io.netty.buffer.ByteBuf;

public class Utils {

	public static String current() {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			return sdf.format(System.currentTimeMillis());
		
	}
	
	public static int bcd(byte b) {
		
		int t = (b & 0xff) - 0x33;
		
		t = (t & 0x0f) + (t >> 4) * 10;
		
		return t;
	}
	
	public static String byte2str(byte[] byteArray) {
	    if (byteArray == null || byteArray.length ==0) {
	        return null;
	    }
	   
	    String ret = "";	   
	    for (int j = 0; j < byteArray.length; j++) {
	        int v = byteArray[j] & 0xFF;
	        String hv = Integer.toHexString(v);
	        if(hv.length() < 2)
	        	hv = "0"+hv;
	        ret += hv;
	    }
	    return ret;
	}
	
	public static String byte2str(ByteBuf buf) {
	   
	   buf.markReaderIndex();	   
	 
	   int length = buf.readableBytes();
	    String ret = "";	   
	    for (int j = 0; j < length; j++) {
	        int v = buf.readByte() & 0xFF;
	        String hv = Integer.toHexString(v);
	        if(hv.length() < 2)
	        	hv = "0"+hv;
	        ret += hv;	      
	    }
	    
	    buf.resetReaderIndex();
	    
	    
	    
	    return ret;
	}
	
}
