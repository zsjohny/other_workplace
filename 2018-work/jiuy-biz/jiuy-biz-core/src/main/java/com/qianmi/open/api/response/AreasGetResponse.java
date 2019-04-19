package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.Area;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.areas.get response.
 *
 * @author auto
 * @since 2.0
 */
public class AreasGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 区域信息
	 */
	@ApiListField("areas")
	@ApiField("area")
	private List<Area> areas;

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
	public List<Area> getAreas( ) {
		return this.areas;
	}

}
