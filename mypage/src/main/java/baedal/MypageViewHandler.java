package baedal;

import baedal.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MypageViewHandler {

    @Autowired
    private MypageRepository mypageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrdered_then_CREATE_1 (@Payload Ordered ordered) {
        try {
            if (ordered.isMe()) {
                Mypage mypage = new Mypage();
                mypage.setOrderId(ordered.getId());
                mypage.setMenuId(ordered.getMenuId());
                mypage.setMenuNm(ordered.getMenuNm());
                mypage.setQty(ordered.getQty());
                mypage.setStatus(ordered.getStatus());
                mypageRepository.save(mypage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrdered_then_UPDATE_1 (@Payload Ordered ordered) {
        System.out.println("##### listener whenOrdered_then_UPDATE_1 : " + ordered.toJson());
        try {
            if (ordered.isMe()) {
                Mypage mypage = mypageRepository.findByOrderId(ordered.getId());
                mypage.setMenuNm(ordered.getMenuNm());
                mypageRepository.save(mypage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryStarted_then_UPDATE_1(@Payload DeliveryStarted deliveryStarted) {
        try {
            if (deliveryStarted.isMe()) {
                Mypage mypage = mypageRepository.findByOrderId(deliveryStarted.getOrderId());
                mypage.setDeliveryId(deliveryStarted.getId());
                mypage.setDeliveryStatus(deliveryStarted.getStatus());
                mypageRepository.save(mypage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryCompleted_then_UPDATE_2(@Payload DeliveryCompleted deliveryCompleted) {
        try {
            if (deliveryCompleted.isMe()) {
                Mypage mypage = mypageRepository.findByOrderId(deliveryCompleted.getOrderId());
                mypage.setDeliveryStatus(deliveryCompleted.getStatus());
                mypageRepository.save(mypage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderCancelled_then_UPDATE_3(@Payload OrderCancelled orderCancelled) {
        try {
            if (orderCancelled.isMe()) {
                // view 객체 조회
                Mypage mypage = mypageRepository.findByOrderId(orderCancelled.getId());
                mypage.setStatus(orderCancelled.getStatus());
                mypage.setDeliveryStatus(orderCancelled.getDeliveryStatus());
                mypageRepository.save(mypage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}