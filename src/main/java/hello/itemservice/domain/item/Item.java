package hello.itemservice.domain.item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// Item - 상품 객체
@Data // 핵심 도메인에서 사용하기 위험하다. 예제니깐 그냥 쓰자.
//@Getter
//@Setter
public class Item {

    private Long id;
    private String itemName;
    private Integer price; // Integer 쓴 이유: 값이 안들어 갈때도 있다는 가정 (null이 들어올 수 있다.)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
