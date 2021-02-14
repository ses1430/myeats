package baedal;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name="Delivery_table")
public class Delivery {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long orderId;
    private String status;

    @PostPersist
    public void onPostPersist(){
        System.out.println("##### listener Delivery onPostPersist : " + this.toJson());

        if ("started".equals(this.getStatus())) {
            DeliveryStarted deliveryStarted = new DeliveryStarted();
            BeanUtils.copyProperties(this, deliveryStarted);
            deliveryStarted.publishAfterCommit();
        }
    }

    @PostUpdate
    public void onPostUpdate(){
        System.out.println("##### listener Delivery onPostUpdate : " + this.toJson());

        if ("complete".equals(this.getStatus())) {
            DeliveryCompleted deliveryCompleted = new DeliveryCompleted();
            BeanUtils.copyProperties(this, deliveryCompleted);
            deliveryCompleted.publishAfterCommit();
        }
        // cancel은 req/res이므로 불필요
    }

    /*
    @PrePersist
    public void onPrePersist(){
        DeliveryCancelled deliveryCancelled = new DeliveryCancelled();
        BeanUtils.copyProperties(this, deliveryCancelled);
        deliveryCancelled.publishAfterCommit();
    }
    */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
