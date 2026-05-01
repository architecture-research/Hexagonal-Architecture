package sparta.hexagonal.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.hexagonal.product.adapter.out.ProductPersistenceAdapter;
import sparta.hexagonal.product.domain.Product;
import sparta.hexagonal.product.domain.port.in.DiscountProductUseCase;

@Service
@RequiredArgsConstructor
public class DiscountProductService implements DiscountProductUseCase {

    private final ProductPersistenceAdapter productPersistenceAdapter; // port는 어뎁터에서 주입받고 유스케이스에선 어뎁터를 받아서 사용한다.

    @Override
    public Product discount(Long productId, double rate) {
        Product product = productPersistenceAdapter.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        Product discounted = product.discount(rate); // 도메인 메서드 호출
        return productPersistenceAdapter.save(discounted);
    }
}
