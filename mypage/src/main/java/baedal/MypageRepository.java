package baedal;

import org.springframework.data.repository.CrudRepository;

public interface MypageRepository extends CrudRepository<Mypage, Long> {
    Mypage findByOrderId(Long orderId);
    Mypage findByDeliveryId(Long deliveryId);
}