package sparta.hexagonal.product.domain.port.out;

import sparta.hexagonal.product.domain.Product;

import java.util.Optional;

// port는 명세서만 제공
// 해당 서비스에서 만들어야할 사항을 미리 정의한다. 그걸 어뎁터에서 구현하고 어뎁터를 유스케이스에서 사용한다.
public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
}
