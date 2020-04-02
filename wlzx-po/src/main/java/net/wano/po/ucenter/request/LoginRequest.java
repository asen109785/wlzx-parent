package net.wano.po.ucenter.request;


import lombok.Data;
import lombok.ToString;
import net.wanho.common.vo.request.RequestData;


/**
 * Created by admin on 2018/3/5.
 */
@Data
@ToString
public class LoginRequest extends RequestData {

    String username;
    String password;
    String verifycode;

}
