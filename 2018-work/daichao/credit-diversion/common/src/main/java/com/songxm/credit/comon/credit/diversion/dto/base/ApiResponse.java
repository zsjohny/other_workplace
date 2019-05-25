package com.songxm.credit.comon.credit.diversion.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javafx.scene.control.Pagination;
import lombok.Data;

/**
 * API错误信息
 * `meta` 在返回消息体里面代表额外的信息，如业务状态码、业务错误信息等，如下示例是发生错误时的meta信息：

    ```json
        {
            "meta": {
                "error_type": "IllegalParameters",
                "code": 400,
                "error_message": "..."
            }
        }
    ```

    请求正常时，HTTP状态码为`200`，`meta`里面的`code`按照`代码对照表`来，此时`error_type`、
    `error_message`不存在，在状态码非`200`是，`code`、`error_type`、`error_message`
    必存在。
 * @author junqiangliu
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class ApiResponse<T> {
	
	private T data;
	private Meta meta;
	private Pagination pagination;
    
	public ApiResponse(T object) {
		super();
		
		this.meta = new Meta();
		this.data = object;
	}
	
}
