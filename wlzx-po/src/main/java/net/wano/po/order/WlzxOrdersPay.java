package net.wano.po.order;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;


@Data
@TableName("wlzx_orders_pay")
public class WlzxOrdersPay implements Serializable {
    private static final long serialVersionUID = -916357210051689789L;
    @TableId
    private String id;
    @Column(name = "order_number")
    private String orderNumber;
    @Column(name = "pay_number")
    private String payNumber;
    private String status;

}
