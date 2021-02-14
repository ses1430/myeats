package baedal;

import baedal.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    DeliveryRepository deliveryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrdered_Request(@Payload Ordered ordered){
        if(ordered.isMe()){
            System.out.println("##### listener wheneverOrdered_Request : " + ordered.toJson());
            Delivery delivery = new Delivery();
            delivery.setOrderId(ordered.getId());
            delivery.setStatus("started");
            deliveryRepository.save(delivery);
        }
    }

    /*
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderCancelled_Request(@Payload OrderCancelled orderedCancelled){
        if(orderedCancelled.isMe()){
            System.out.println("##### listener wheneverOrderCancelled_Request : " + orderedCancelled.toJson());
            Delivery delivery = deliveryRepository.findById(orderedCancelled.getDeliveryId()).get();
            delivery.setStatus("cancelled");
            deliveryRepository.save(delivery);
        }
    }
    */
}
