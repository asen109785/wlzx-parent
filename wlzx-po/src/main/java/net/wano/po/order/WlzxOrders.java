package net.wano.po.order;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;


@Data
@TableName("wlzx_orders")
public class WlzxOrders implements Serializable {
    private static final long serialVersionUID = -916357210051689789L;
    @TableId
    private String orderNumber;
    @Column(name = "initial_price")
    private Float initialPrice;
    private Float price;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    private String status;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "details")
    private String details;

}
