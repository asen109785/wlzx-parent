package net.wano.po.ucenter.ext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthToken {
    String jti_token;//jwt  jti短令牌
    String refresh_token;//刷新token令牌
    String access_token;//访问token令牌
}
