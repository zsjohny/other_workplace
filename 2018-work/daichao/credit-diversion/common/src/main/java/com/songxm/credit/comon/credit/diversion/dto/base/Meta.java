package com.songxm.credit.comon.credit.diversion.dto.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonInclude(Include.NON_NULL)
public class Meta <T>{
	public static final Integer OK = 200;
	
	private int code = -1;
    private HttpStatus status;
    private String message;
    @JsonIgnore
    private T errors;

//    @JsonProperty("error_type")
//    public String getErrorType() {
//    		if (CollectionUtils.isEmpty(errors)) {
//                return null;
//            }
//    		return StringUtils.collectionToCommaDelimitedString(errors);
//    }

    public Meta(){}
    public Meta(HttpStatus code,String message,T errors){
        this.status = code;
        this.message = message;
        this.errors = errors;
    }
}
