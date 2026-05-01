package sparta.hexagonal.product.adapter.out;

import org.springframework.stereotype.Repository;
import sparta.hexagonal.product.domain.Product;
import sparta.hexagonal.product.domain.port.out.ProductRepository;

import java.util.Optional;

// 교체를 한다면 해당 영역을 교채를 해야한다.
// 그러나 JPA같은 애들은 사실상 교체될 일이 없고 있어도 1번이고 그 비용이 너무나 크다.
// 오히려, 지도API, 결제 API, 등등 교체 될 수 있는 외부 모듈에 한에서 이런 레이어를 쌓는 방식으로 비슷한 구성을 가져갈 수 는 있지만 이렇게
// 각잡고 헥사고날을 하는 경우는 매우 드물다.
@Repository
public class ProductPersistenceAdapter implements ProductRepository {
    private final SpringDataProductRepository jpa;

    public ProductPersistenceAdapter(SpringDataProductRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = ProductJpaEntity.fromDomain(product);
        return jpa.save(entity).toDomain();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpa.findById(id)
                .map(ProductJpaEntity::toDomain);
    }
}
