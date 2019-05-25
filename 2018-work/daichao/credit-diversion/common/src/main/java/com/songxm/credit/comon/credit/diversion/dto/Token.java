package com.songxm.credit.comon.credit.diversion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import moxie.cloud.service.common.util.JsonUtil;

@Data
public class Token {
    @JsonProperty("access_token")
    @ApiModelProperty("动态令牌")
    private String token;
    @ApiModelProperty(value = "过期秒数")
    @JsonProperty("expire_in")
    private Long expireIn;

    @Override
    public String toString() {
        try {
            return JsonUtil.getObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
