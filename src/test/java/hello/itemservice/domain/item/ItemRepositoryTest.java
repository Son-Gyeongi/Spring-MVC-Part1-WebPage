package hello.itemservice.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

// 최근 Junit5에는 public이 없어도 된다.
class ItemRepositoryTest {

    // 테스트하기 위해 가져온다.
    ItemRepository itemRepository = new ItemRepository();

    // 테스트가 끝날 때 마다 실행된다.
    @AfterEach
    void afterEach() {
        // itemRepository를 하나 테스트할 때마다 데이터를 깔끔하게 지워줘야 한다.
        // 그래야 다음 테스트가 영향이 없다.
        itemRepository.clearStore();
    }

    @Test
    void save() {
        // given
        Item item = new Item("itemA", 10000, 10);

        // when
        Item savedItem = itemRepository.save(item);

        // then
        // findById()는 아래와 같은 거로 테스트되서 그냥 넘어간다.
        Item findItem = itemRepository.findById(item.getId());
        // import org.assertj.core.api.Assertions
        // 저장된 값이랑 조회된 값이랑 같다.
        assertThat(findItem).isEqualTo(savedItem);
    }

    @Test
    void findAll() {
        // given
        Item item1 = new Item("item1", 10000, 10);
        Item item2 = new Item("item2", 20000, 20);

        itemRepository.save(item1);
        itemRepository.save(item2);

        // when
        List<Item> result = itemRepository.findAll();

        // then
        assertThat(result.size()).isEqualTo(2); // 결과값 크기 확인
        assertThat(result).contains(item1, item2); // 결과값에 기대하는 값이 있는 지 확인
    }

    @Test
    void updateItem() {
        // given
        Item item = new Item("item1", 10000, 10);

        Item savedItem = itemRepository.save(item);
        Long itemId = savedItem.getId();

        // when
        // 업데이트 할 객체 생성
        Item updateParam = new Item("item2", 20000, 20);
        itemRepository.update(itemId, updateParam);

        // then
        Item findItem = itemRepository.findById(itemId);

        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
    }
}