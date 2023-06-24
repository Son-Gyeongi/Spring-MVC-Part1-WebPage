package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
     * 상품 등록 폼
     * addForm.html의 form을 열 때는 GET 실제 저장할 때는 POST
     * 같은 url이지만 http method로 기능을 구분해준다.
     */
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm"; // 뷰로 간다.
    }

    /**
     * 상품 등록 저장
     */
//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {
        // 파라미터 넘어온 거로 실제 Item 객체 생성
        Item item = new Item();
        // @ModelAttribute도 set을 이용해서 값을 넣는다. 비교할 겸 생성자 안쓰고 set 사용
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        // 상품 저장 후 상세 화면에서 보여주기 위해서 model에 저장
        model.addAttribute("item", item);

        return "basic/item";
    }
    // 위 addItemV1()메서드를 @ModelAttribute로 바꿔보자.
    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {
        // 파라미터 넘어온 거로 실제 Item 객체 생성
        // 아래 부분 @ModelAttribute가 자동으로 만들어준다.
//        Item item = new Item();
//        item.setItemName(itemName);
//        item.setPrice(price);
//        item.setQuantity(quantity);

        itemRepository.save(item);

        // 상품 저장 후 상세 화면에서 보여주기 위해서 model에 저장
        // @ModelAttribute는 자동으로 model에 넣어주는 역할도 한다.
        // 파라미터에 Model model도 없어도 된다.
//        model.addAttribute("item", item); // 자동 추가, 생략 가능

        return "basic/item";
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
