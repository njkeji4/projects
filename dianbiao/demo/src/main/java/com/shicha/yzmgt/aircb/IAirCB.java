package com.shicha.yzmgt.aircb;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;

import org.springframework.beans.factory.ObjectFactory;

import feign.FeignException;
import feign.Headers;
import feign.Response;
import feign.Response.Body;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;


@FeignClient(url = "${dianbiao.server.url}",name="aircb",  configuration = IAirCB.Configuration.class)
public interface IAirCB {	
	
	@RequestMapping(method = RequestMethod.POST, value = "/getdata", consumes = "application/json" , produces="application/x-www-form-urlencoded")
	@Headers({"Content-Type: application/x-www-form-urlencoded"})
	AirResult getData(CbCommand cmd);
	
	@RequestMapping(method = RequestMethod.POST, value = "/getdata", consumes = "application/x-www-form-urlencoded", produces="application/x-www-form-urlencoded")
	AirResult getData(CbCommand2 cmd);
	
	
	 class Configuration {

//	        @Bean
//	        Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converters) {
//	            return new SpringFormEncoder(new SpringEncoder(converters));
//	        }
	        
	        @Bean
	        Decoder feignDecoder(ObjectFactory<HttpMessageConverters> converters) {
	        	return new ResponseEntityDecoder1(new SpringDecoder(converters));
	        }
	 }
	 
	 class ResponseEntityDecoder1 implements Decoder {

			private Decoder decoder;

			public ResponseEntityDecoder1(Decoder decoder) {
				this.decoder = decoder;
			}

			@Override
			public Object decode(final Response response, Type type)
					throws IOException, FeignException {

				if(AirResult.class.equals(type)) {
					Body body = response.body();
					
					InputStream is = body.asInputStream();
					String json = ConvertStream2Json(is);
					System.out.println(json);
					
					JSONObject userJson = JSONObject.parseObject(json);
					AirResult result = JSON.toJavaObject(userJson,AirResult.class);
					
					return result;
				}
				return null;
					
			}
			
			private static String ConvertStream2Json(InputStream inputStream)
		    {
		        String jsonStr = "";
		        // ByteArrayOutputStream相当于内存输出流
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        byte[] buffer = new byte[1024];
		        int len = 0;
		        // 将输入流转移到内存输出流中
		        try
		        {
		            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1)
		            {
		                out.write(buffer, 0, len);
		            }
		            // 将内存流转换为字符串
		            jsonStr = new String(out.toByteArray());
		        }
		        catch (IOException e)
		        {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }
		        return jsonStr;
		    }

			private boolean isParameterizeHttpEntity(Type type) {
				if (type instanceof ParameterizedType) {
					return isHttpEntity(((ParameterizedType) type).getRawType());
				}
				return false;
			}

			private boolean isHttpEntity(Type type) {
				if (type instanceof Class) {
					Class c = (Class) type;
					return HttpEntity.class.isAssignableFrom(c);
				}
				return false;
			}

			@SuppressWarnings("unchecked")
			private <T> ResponseEntity<T> createResponse(Object instance, Response response) {

				MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
				for (String key : response.headers().keySet()) {
					headers.put(key, new LinkedList<>(response.headers().get(key)));
				}

				return new ResponseEntity<>((T) instance, headers,
						HttpStatus.valueOf(response.status()));
			}

		}

	
}
