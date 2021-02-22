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
    OrderRepository orderRepository;

    @Autowired
    MenuRepository menuRepository;

    /*
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrdered_UpdateMenuNm(@Payload Ordered ordered) {
        if(ordered.isMe()) {
            System.out.println("##### listener wheneverOrdered_UpdateMenuNm : " + ordered.toJson());

            Order order = orderRepository.findById(ordered.getId()).get();
            Menu menu = menuRepository.findById(ordered.getMenuId()).get();
            order.setMenuNm(menu.getMenuNm());
            orderRepository.save(order);
        }
    }
    */

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDeliveryStarted_UpdateStatus(@Payload DeliveryStarted deliveryStarted){
        if(deliveryStarted.isMe()){
            System.out.println("##### listener wheneverDeliveryStarted_UpdateStatus : " + deliveryStarted.toJson());

            Order order = orderRepository.findById(deliveryStarted.getOrderId()).get();
            order.setDeliveryId(deliveryStarted.getId());
            order.setDeliveryStatus(deliveryStarted.getStatus());
            orderRepository.save(order);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDeliveryCompleted_UpdateStatus(@Payload DeliveryCompleted deliveryCompleted){
        if(deliveryCompleted.isMe()){
            System.out.println("##### listener wheneverDeliveryCompleted_UpdateStatus : " + deliveryCompleted.toJson());

            Order order = orderRepository.findById(deliveryCompleted.getOrderId()).get();            
            order.setDeliveryStatus(deliveryCompleted.getStatus());
            orderRepository.save(order);
        }
    }
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverMenuCreated_CreateMenu(@Payload MenuCreated menuCreated){

        if(menuCreated.isMe()){
            System.out.println("##### listener  : " + menuCreated.toJson());

            Menu menu = new Menu();
            menu.setId(menuCreated.getId());
            menu.setMenuNm(menuCreated.getMenuNm());
            menuRepository.save(menu);
        }
    }
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverMenuDeleted_DeleteMenu(@Payload MenuDeleted menuDeleted){

        if(menuDeleted.isMe()){
            System.out.println("##### listener  : " + menuDeleted.toJson());
            menuRepository.deleteById(menuDeleted.getId());
        }
    }
}
