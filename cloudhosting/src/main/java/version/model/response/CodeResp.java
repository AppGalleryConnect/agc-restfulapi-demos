package version.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class CodeResp {
    /**
     * 状态码，参考HTTP错误码的定义
     */
    private String code;

    /**
     * 响应体
     */
    private String message;
}
