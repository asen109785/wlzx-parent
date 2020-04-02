package net.wanho.common.vo.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class AjaxResult {
    @NonNull
    private boolean success;
    @NonNull
    private int status;
    @NonNull
    private String message;
    private Object data;



    public AjaxResult(CommonCode commonCode) {
            this.success=commonCode.success;
            this.status =commonCode.code;
            this.message = commonCode.message;
    }
}
