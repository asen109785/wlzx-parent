package net.wano.po.order.request;

import lombok.Data;
import lombok.ToString;
import net.wanho.common.vo.request.RequestData;


/**
 * Created by mrt on 2018/3/26.
 */
@Data
@ToString
public class CreateOrderRequest extends RequestData {

    String courseId;

}
