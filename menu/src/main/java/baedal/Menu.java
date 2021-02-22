package baedal;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Menu_table")
public class Menu {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String menuNm;

    @PostPersist
    public void onPostPersist(){
        MenuCreated menuCreated = new MenuCreated();
        BeanUtils.copyProperties(this, menuCreated);
        menuCreated.publishAfterCommit();
    }

    @PostRemove
    public void onPostRemove(){
        MenuDeleted menuDeleted = new MenuDeleted();
        BeanUtils.copyProperties(this, menuDeleted);
        menuDeleted.publishAfterCommit();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getMenuNm() {
        return menuNm;
    }

    public void setMenuNm(String menuNm) {
        this.menuNm = menuNm;
    }
}
