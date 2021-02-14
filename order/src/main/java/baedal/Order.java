package baedal;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name="Order_table")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long menuId;
    private Long qty;
    private String status;
    private String deliveryStatus;
    private Long deliveryId;

    @PrePersist
    public void onPrePersist(){
        this.setStatus("ordered");
    }

    @PostPersist
    public void onPostPersist(){
        Ordered ordered = new Ordered();
        BeanUtils.copyProperties(this, ordered);
        ordered.publishAfterCommit();
    }

    @PreUpdate
    public void onPreUpdate(){
        System.out.println("##### listener Order onPreUpdate : " + this.toJson());

        if ("cancel".equals(this.getStatus())) {
            OrderCancelled orderCancelled = new OrderCancelled();
            this.setDeliveryStatus("cancelled");
            BeanUtils.copyProperties(this, orderCancelled);
            orderCancelled.publishAfterCommit();

            //Following code causes dependency to external APIs
            // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.
            baedal.external.Delivery delivery = new baedal.external.Delivery();
            delivery.setId(this.getDeliveryId());
            delivery.setOrderId(this.getId());
            delivery.setStatus(this.getDeliveryStatus());
            // mappings goes here
            OrderApplication.applicationContext.getBean(baedal.external.DeliveryService.class).cancel(this.getDeliveryId(), delivery);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String toJson(){
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        return json;
    }
}
