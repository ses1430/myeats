package baedal;

import javax.persistence.*;

@Entity
@Table(name="Menu_table")
public class Menu {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String menuNm;

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
