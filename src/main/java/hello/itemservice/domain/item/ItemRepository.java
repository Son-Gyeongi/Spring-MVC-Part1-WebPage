package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ItemRepository - 상품 저장소
@Repository // Component 스캔의 대상이 된다.
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>(); // static
    private static long sequence = 0L; // static

    // item 저장
    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    // item 조회
    public Item findById(Long id) {
        return store.get(id);
    }

    /**
     * item 전체 조회
     * List import 시 java.util로 해야한다.
     * 반환을 컬렉션 타입으로 해서 store.values()를 그대로 반환해도 되는데
     * 한 번 ArrayList로 감싸서 반환하게 되면 ArrayList에 값을 넣어도
     * 더이상 store에는 변함이 없기 떄문에 안전하게 감쌌다.(그리고 타입도 바꿔야되는 문제가 있어서 감쌌다.)
     */
    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    // item 수정
    // updateParam- Item 에서 id는 사용이 되지 않으니 3개만 있는 객체를 만드는 게 좋다.
    // 정석으로 하려면 ItemParamDto 만들어서 3개 값만 넣는 게 맞다.
    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    // store 다 날리는 거, 테스트하려고 쓴거다.
    public void clearStore() {
        store.clear(); // HashMap에 있는 데이터 다 날라간다.
    }
}
