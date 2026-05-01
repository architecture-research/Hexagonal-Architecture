package sparta.hexagonal.product.domain.port.in;

import sparta.hexagonal.product.domain.Product;

public interface DiscountProductUseCase {
    Product discount(Long productId, double rate);
}
