package sparta.hexagonal.product.domain;

import lombok.Getter;

// 순수 객체 (JPA 의존성 X)
// setter 는 불변성을 해침으로 X
@Getter
public class Product {

    // 타입 안정성이 중요하다면 ProductId 같은 Value Object 를 만들어서 사용, 단 복잡성 증가
    Long id;

    String name;

    Long price;

    Long quantity;

    public Product(Long id, String name, Long price, Long quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Product 자신의 상태를 변경하는 비즈니스 규칙은 여기서만 작성
    // 모든 변경은 도메인 메서드를 통해서만 가능하게 됩니다.(본인 데이터 한정)
    public Product discount(double rate) {
        if (rate < 0.0 || rate > 1.0) throw new IllegalArgumentException("잘못된 할인율");
        return new Product(id, name, (long) (price * (1 - rate)) , quantity);
    }

}
