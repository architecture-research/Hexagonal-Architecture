# Hexagonal-Architecture
DB가 MySQL이든 MongoDB든, HTTP로 오든 gRPC로 오든 — 도메인 코드는 전혀 신경 쓰지 않는다

## 3layer vs Hexagonal

|  | 3 layered architecture | hexagonal architecture |
| --- | --- | --- |
| 목적 | 빠른 개발 및 도입 | 외부 시스템과의 분리, 도메인 보호 |
| 확장성 | 모든 레이어에 변경내용이 전파됨으로 제한됨. | 어뎁터만 교체 |
| 테스트 구성 | DB등 의존으로 구성 어려움 | 포트 Mock으로 단위 테스트 용이 |
| 결합도 | 레이어간 결합도가 매우 높다. | 포트/어뎁터로 인프라와 도메인 완전 분리 |
| 초기 비용 | 낮음 | 높음(포트/어댑터 설계) |
| 적합 서비스 | 빠른 MVP, 단순 CRUD | 복잡한 도메인, 멀티 모듈등 |

**빠른 개발 목적으로 한다면 익순한 패턴인 3layer가 나쁜 선택은 절대 아니다.**

- 계층형 구조의 문제점
    
    ```
    1) 데이터베이스, 영속성에 대한 의존성
    도메인 계층이 데이터베이스에 의존하게 되어 데이터베이스에 변화가 일어나면 도메인 계층에도 변화가 생긴다.
    서비스 계층에서도 영속성 모델을 도메인 모델처럼 사용하게 된다. 그렇다면 즉시로딩, 지연로딩, 트랜잭션, 플러시 등을 고려해야하고 영속성에 대한 의존이 프로젝트 전체적으로 퍼지게 되어 변경에 취약해진다.
    
    2) 아키텍처 경계를 강제할 수 없다.
    계층형 아키텍처에서는 상위 계층에 있는 컴포넌트에 접근할 목적으로 해당 컴포넌트를 하위 계층으로 내릴 수 있다. 이러한 행위가 반복되면 점점 경계가 모호해지고 허물어지게 된다.
    
    3) 계층을 스킵 할 수 있다.
    계층을 건너뛰는 것이 가능하다. 구현이 간단한 경우 Controller에서 바로 도메인을 참조하는 등의 로직을 작성할 수 있다. 이 경우의 문제는 기능 확장이 어렵고, 테스트가 복잡해진다.
    
    4) 유스케이스를 숨긴다.
    개발자는 유스케이스를 어느 계층에나 자유롭게 생성할 수 있다. 이는 개발자가 유스케이스의 존재 여부를 파악하기 어려워서 동일한 로직을 다른 위치에 새롭게 구현하여 코드를 더럽히게 된다.
    
    5) 서비스의 크기를 강제할 수 없다.
    계층형 구조에서는 서비스의 크기를 강제하지않는다. 수십개의 서비스 로직을 한곳에 전부 작성할 수도 있다. 이 경우에 서비스가 너무 많은 의존을 가져 수많은 웹 계층이 해당 서비스를 의존하게 된다. 결국 서비스를 테스트하기 어려워지고 작업해야할 유스케이스를 찾기도 힘들어진다.
    ```
    
    출처: https://ivory-room.tistory.com/91
    

헥사고날 하나의 아키택쳐로 모든 플랫폼 구현은 안하는거같다.
헥사고날은 **서비스 내부 구조**, 
EDA/CQRS/Saga는 **서비스 간 관계**를 담당하므로 상호 보완적이다. 
실무에서는 이들을 함께 쓰는 게 일반적 패턴 이라고한다.(아닐수도..)

헥사고날 패턴을 도입하기 위해선 몇가지 조건이 필요한것같다. → 헥사고날이 빛나기위한 조건

1. 도메인 모델을 확실하게 정의할 수 있는 서비스
하나의 프로젝트 내에사 관리하는 도메인 모델을 명확히 정의하는 것이 헥사고날 적용의 시작이 되어야 한다. DDD가 필수는 아니지만 그에 준하게 필요하다.

2. 외부 의존성이 많지 않은 서비스(?)
깊이에 대한것이 아닌 넓이에 대한 부분, 로직 대부분이 외부 API에 의존하는 경우 결론적으로 port와 adapter에 로직이 과중 되는 경우가 많아짐. 따라서 코어 로직이 풍부하게 많은 서비스 일때 의미를 가진다.
→**외부 API 연동이 서비스의 대부분을 차지하면, 정작 도메인 코어는 빈약한데 Port/Adapter만 잔뜩 생기는 구조가 된다**

3. 코어 모듈을 사용하는 모듈이2개 이상인 서비스 
2개 이상의 서비스가 사용하지 않으면 out port에 대한 부분만 추상화 하는 것이 더 나은 방향이 될 수 있다. 코어 모듈의 로직을 재사용할 수 있도록 해당 모듈을 사용하는 컴포넌트 모듈이 2개 이상인 서비스 일때 헥사고날을 고려 해야한다.

## 도메인이 무엇인지 명확히 판단하는 기준 근거로는 무엇이 있을까?

- 비즈니스 규칙을 코드로 표현할 수 있다 — `"주문은 결제 완료 후에만 배송 시작 가능하다"` 같은 규칙이 존재
- 외부 시스템 없이도 로직 설명이 가능하다 — `"DB가 어떻게 생겼는지 몰라도 이 비즈니스 흐름은 설명된다"`
- 도메인 언어가 존재한다 — 개발팀과 기획팀이 같은 단어(Order, Payment, Member)를 쓰고 의미가 일치

***이러한 조건들로 실제 카카오에서 헥사고날을 도입후 의미가 퇴색되어 변경한 사례도있는듯하다***

https://tech.kakaopay.com/post/home-hexagonal-architecture/

핵사고날은 DDD와 유사하고(도메인주도 라는 관점에서), 클린 아키택처를 일반화한 구조중 하나인듯 하다.

## hexagonal 주요 컴포넌트

**Adapter** 
포트를 통해 인프라와 실제로 연결하는 부분을 담당하는 구현체를 의미 Adapter는 크게 두 종류로 구분된다.

| 이름 | 설명  | 예시 |
| --- | --- | --- |
| Driving Adapter= Primary Adapter | 사용자의 요청을 받아들일 때 사용되는 Adapter | AWS Lambda의 HandlerWebApplication의 Controller  |
| Driven Adapter= Secondary Adapter | 도메인 모델의 처리에 사용되는 Adapter | MessageQueue, Persistence Adapter |

**Port**
서비스(또는 usecase)에 어댑터에 대한 명세만을 제공하는 계층
단순히 인터페이스 정의만 존재하며, DI를 위해 사용된다.

**Application Service(usecase)**
어댑터를 주입 받아 도메인 모델과 어댑터를 적절히 오케스트레이션하는 계층을 의미

**Domain Model**
DDD의 도메인 모델과 동일한 개념을 지닌 계층
비즈니스 로직이라 부르는 엔티티에 대한 변경은 모두 해당 계층에서만 실행된다.
원칙적으로는 어떠한 의존성도 없어야 하지만 Entity를 만들 때 Database에 적재되어 있는 데이터를 참고해야하는 경우와 같은 상황에서는 Port를 이용해 Adapter를 주입받아서 사용할 수 있다는 예외사항이 존재하기도 한다.
이는 클린 아키텍처와 동일하다.
<img width="1054" height="770" alt="image" src="https://github.com/user-attachments/assets/400b1adf-19a9-4eda-a388-afd21acf300d" />
<img width="1280" height="1760" alt="image" src="https://github.com/user-attachments/assets/f0592b9e-7196-4e6e-b277-dbb7e180ad7a" />

## 패키지 구조

```java
order-service/
├── domain/                        ← 외부 의존성 0
│   ├── model/Order
│   ├── service/OrderDomainService
│   └── port/
│       ├── in/ConfirmOrderPort    (Driving Port)
│       └── out/OrderRepository   (Driven Port)
│
├── application/                   ← 유즈케이스 조합
│   └── ConfirmOrderUseCase
│
└── adapter/                       ← 인프라 세부사항
    ├── in/
    │   ├── web/OrderController
    │   └── messaging/OrderEventConsumer
    └── out/
        ├── persistence/JpaOrderRepository
        └── external/PaymentGatewayAdapter
```


## 헥사고날 테스트

간단한 도메인에 대해서 실제로 작업해보면서 확인 한내용.

https://github.com/architecture-research/Hexagonal-Architecture

1. 교체될 가능성이 있는 외부 API(지도, 결제 등)를 Port로 추상화하면 교체할 때 어댑터만 바꾸면 된다
2. port를 interface를 통해 추상화하여 구현해야 할 사항일 미리 정의한다.
3. JPA, REST같은 변경될 일이 거의 없는 경우 사실 의미가 많지않다.
4. 진짜 큰 의미가 있는게 아니면 굳이굳이 가 될 가능성이 높다 차라리 카카오처럼 3Layer에서 변형을하여 레이어를 더 쌓아 추상화를 하는 방식이 현실적이다.

**참고자료**

https://devkingdom.tistory.com/342#google_vignette

https://ivory-room.tistory.com/91

https://cantcoding.tistory.com/107
