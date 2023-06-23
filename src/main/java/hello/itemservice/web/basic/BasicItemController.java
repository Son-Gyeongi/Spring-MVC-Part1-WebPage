package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // final이 붙은 애를 가지고 생성자를 만들어준다.
public class BasicItemController {

    private final ItemRepository itemRepository;

    /**
     * BasicItemController가 spring bean에 등록이 된다.
     * 생성자 주입으로 ItemRepository가 스프링 빈에서 주입이 된다.
     */
//    @Autowired // 스프링에서 생성자 하나만 있으면 생략 가능
    // @RequiredArgsConstructor 아래와 같다.
//    public BasicItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }

    /**
     * 아이템 목록 출력
     */
    @GetMapping
    public String items(Model model) { // Model만 받을 거다.
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items); // model에 items 컬렉션이 담긴다.
        return "basic/items"; // 이 위치에 뷰를 만들어 넣는다.(화면을 만들자)
    }

    /**
     * 상품 상세
     */
    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item"; // 뷰로 간다.
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
