package sparta.hexagonal.product.adapter.out;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import sparta.hexagonal.product.domain.Product;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@NoArgsConstructor
public class ProductJpaEntity {
    // in - out은 누가 누구를 호출 하는가에 따라 결정
    // 외부 → 도메인 방향  = Driving (in)   : 외부가 내 서비스를 호출
    // 도메인 → 외부 방향  = Driven  (out)  : 내 서비스가 외부를 호출
    //
    // HTTP 요청이 들어옴        → Controller     → in 어댑터
    // 도메인이 DB에 저장 요청   → Repository     → out 어댑터
    // 도메인이 외부 API 호출    → ExternalClient → out 어댑터

    //도메인과 JPA 엔티티는 의도적으로 다를 수 있다. 그게 분리한 이유
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long price;

    private Long quantity;

    //--------------- DB에만 필요한 데이터들 ---------------
    @CreatedDate
    private LocalDateTime createdAt;

    @Version  // 낙관적 락
    private Long version;

    // createdAt, version은 도메인에 안 넘김
    public Product toDomain() {
        return new Product(id, name, price, quantity);
    }

    public static ProductJpaEntity fromDomain(Product product) {
        ProductJpaEntity entity = new ProductJpaEntity();
        entity.id = product.getId();
        entity.name = product.getName();
        entity.price = product.getPrice();
        entity.quantity = product.getQuantity();
        // createdAt, version은 JPA가 알아서 관리
        return entity;
    }
}
