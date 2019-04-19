package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.MemberlevelDeleteResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.memberlevel.delete request
 *
 * @author auto
 * @since 1.0
 */
public class MemberlevelDeleteRequest implements QianmiRequest<MemberlevelDeleteResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 等级id
	 */
	private String levelIds;

	public void setLevelIds(String levelIds) {
		this.levelIds = levelIds;
	}
	public String getLevelIds() {
		return this.levelIds;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.memberlevel.delete";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("level_ids", this.levelIds);
		if(udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public void putOtherTextParam(String key, String value) {
		if(this.udfParams == null) {
			this.udfParams = new QianmiHashMap();
		}
		this.udfParams.put(key, value);
	}

	public Class<MemberlevelDeleteResponse> getResponseClass() {
		return MemberlevelDeleteResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(levelIds, "levelIds");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}