package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
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
    // @ModelAttribute 이름 생략
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        // @ModelAttribute 이름을 지정하지 않으면 Item 클래스명을 첫글자만 소문자로 바꾼다.
        // Item -> item 그게 이름이 된다. 예시 model.addAttribute("item", item);
        itemRepository.save(item);
        return "basic/item";
    }
    // @ModelAttribute 생략
//    @PostMapping("/add")
    public String addItemV4(Item item) { // 우리가 만든 임의의 객체일 경우 @ModelAttribute 적용된다.
        itemRepository.save(item);
        return "basic/item";
    }
    /**
     * PRG - Post/Redirect/Get
     */
//    @PostMapping("/add")
    public String addItemV5(Item item) { // 우리가 만든 임의의 객체일 경우 @ModelAttribute 적용된다.
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }
    /**
     * RedirectAttributes
     * Redirect 할 때 파라미터를 붙여서 보낸다.
     * 저장이라는 flag에 파라미터가 있으면 '저장이 되었다'는 메시지를 보여줄거다.
     */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId()); // redirect와 관련된 속성을 넣는다.
        redirectAttributes.addAttribute("status", true); // 저장해서 넘어온 거다라고 생각
        // true는 쿼리 파라미터 형식으로 들어간다. ?status=true
        // 기본적인 url 인코딩 다 들어간다.
        // http://localhost:8080/basic/items/3?status=true
        return "redirect:/basic/items/{itemId}"; // redirectAttributes에 넣은 itemId값이 ${itemId}로 치환이 된다.
        // redirect:/basic/items/{itemId} 되는 곳으로 가보자.
        // - 상품 상세 컨트롤 호출 되니깐 상품 상세 뷰(item.html)에 작업해주어야 한다.
    }

    /**
     * 상품 수정
     */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }
    // 상품 수정 처리 (저장)
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
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
