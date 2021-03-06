package org.dream.utils.mvc;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

public class JsonPMessageConverter extends FastJsonHttpMessageConverter {
	@Override
	protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		String text = "";
		if (obj instanceof String || obj instanceof Number || obj instanceof Boolean) {
			text += obj;
		} else {
			text = JSON.toJSONString(obj, getFeatures());
		}

		String call = EffectInteceptor.callBack.get();
		if (StringUtils.isNotBlank(call)) {

			if(obj instanceof String){
				text = call + "(\"" + text + "\")";
			}else{
				text = call + "(" + text + ")";
			}

		}
		OutputStream out = outputMessage.getBody();
		byte[] bytes = text.getBytes(getCharset());
		out.write(bytes);
	}

}
