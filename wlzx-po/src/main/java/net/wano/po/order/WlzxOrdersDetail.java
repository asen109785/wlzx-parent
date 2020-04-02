package net.wano.po.order;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;


@Data
@TableName("wlzx_orders_detail")

public class WlzxOrdersDetail implements Serializable {
    private static final long serialVersionUID = -916357210051689789L;
   @TableId
    private String id;
    @Column(name = "order_number")
    private String orderNumber;
    @Column(name = "item_id")
    private String itemId;
    @Column(name = "item_num")
    private Integer itemNum;
    @Column(name = "item_price")
    private Float itemPrice;
    private String valid;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
}
