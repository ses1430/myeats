
package baedal.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="delivery", url="http://localhost:8082")
public interface DeliveryService {

    @RequestMapping(method= RequestMethod.PUT, path="/deliveries/{id}")
    public void cancel(@PathVariable(value="id") long id, @RequestBody Delivery delivery);

    /*
    @RequestMapping(method= RequestMethod.PATCH, path="/deliveries/{id}")
    public void cancel(@PathVariable(value="id") long id, @RequestBody Delivery delivery);
    */
}