package sparta.hexagonal.product.adapter.in;

import org.springframework.web.bind.annotation.*;
import sparta.hexagonal.product.domain.Product;
import sparta.hexagonal.product.domain.port.in.DiscountProductUseCase;

@RestController
@RequestMapping("/products")
// 교체 가능한 영역 rest -> 다른걸로 바꿀 수 있음
public class ProductController {

    private final DiscountProductUseCase discountProduct; // in port 주입

    public ProductController(DiscountProductUseCase discountProduct) {
        this.discountProduct = discountProduct;
    }

    @PostMapping("/{id}/discount")
    public Product discount(@PathVariable Long id, @RequestParam double rate) {
        return discountProduct.discount(id, rate);
    }
}
