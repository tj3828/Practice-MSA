# Chap02

 - MSA?
 
 - 대표적 사례
 
 - Monolithic vs Microservices
 
 ## MSA
 - 아직 공식적인 정의는 없지만, 공감대를 통해 다음과 같이 정의할 수 있다.

   - 시스템을 여러개의 독립된 서비스로 나눠서, 이 서비스를 조합함으로서 기능을 제공하는 아키텍처 디자인 패턴
 
   - 각 서비스 간에 연결은 Network를 통해 이루어진다.(보통 HTTP)
 
   - 독립된 배포 단위를 가진다.
 
   - 각 서비스는 쉽게 교체가 가능하고 기능 중심으로 구성된다.
 
   - 서비스마다 적합한 프로그래밍 언어, 데이터베이스, 환경으로 만들어진다.
 
   - 서비스는 크기가 작고, 상황에 따라 경계를 정하고, 자율적으로 개발되고, 독집적으로 배포되고, 분산되고, 자동화 된 프로세스로 구축되고 배포된다.
 
 - 한 팀에 의해 개발할 수 있는 크기가 상한선이다. 절대로 3~9명의 사람들이 스스로 더 많은 개발을 할 수 없을 정도로 커지면 안된다.
 
  
 ## 대표적 사례 1 (아마존 제프 베조스의 메일)
 
 - MSA의 대표적인 기업으로 꼽히는 아마존의 CEO 제프 베조스는 다음과 같은 메일을 전 직원에서 보냈다고 한다.
 
 - 아마존 웹 서비스를 릴리즈하면서 내부적으로 사용한 것과 똑같은 플랫폼이라는 것을 밝혔다.
 
 
 1. All teams will henceforth expose their data and functionality through service interfaces.
 2. Teams must communicate with each other through these interfaces.
 3. There will be no other form of interprocess communication allowed: no direct linking, no direct reads of another team’s data store, no shared-memory model, no back-doors whatsoever. The only communication allowed is via service interface calls over the network.
 4. It doesn’t matter what technology they use. HTTP, Corba, Pubsub, custom protocols — doesn’t matter.
 5. All service interfaces, without exception, must be designed from the ground up to be externalizable. That is to say, the team must plan and design to be able to expose the interface to developers in the outside world. No exceptions.
 6. Anyone who doesn’t do this will be fired.
 
 
  1. 모든 팀들은 데이터와 기능들을 서비스 인터페이스로 연결시켜라.
  
  2. 팀들은 이 인터페이스를 통해서만 연락해야 한다.
  
  3. 다른 어떤 커뮤니케이션 방법도 허용되지 않는다. 직접 링크를 보내거나 다른 팀의 스토리지에 직접 억세스 해서도 안 되며, 공유 메모리나 백도어 같은 것도 안 된다. 모든 커뮤니케이션은 네트워크를 통한 서비스 인터페이스로 이루어져야 한다.
  
  4. 어떤 기술을 사용하던 상관없다. HTTP, Cobra, Pubsub, 독자 프로토콜.. 그건 상관없다. 베조스는 그런데 관심 없다.
  
  5. 모든 서비스 인터페이스는 예외 없이 외부에서 이용 가능하게 만들어져야 한다. 그 말은 팀들은 외부 개발자들이 인터페이스를 이용할 수 있게 해야한다는 것이다. 예외는 없다.
  
  6. 이를 실천하지 않는 사람은 누구든 해고될 것이다.
  
  
 ## 대표적 사례 2 (넷플릭스)
 
  - 2008년 데이터베이스의 심각한 문제가 발생해 고객에게 DVD를 보낼 수 없는 사태가 발생했다.(Sigle points of failure)
  
  - 이 후, Scale-up 확장만 가능한 인프라 스트럭처와 단일 장애 지점 (SPOF)이라는 한계에서 벗어나겠다고 선언했다.
  
  - 확장성(Scalability), 성능(Performance), 가용성(Availability)의 목표에 집중하여 AWS로 이관 시작.
  
  - AWS로 이전하는 것을 통해 온프로미스 환경에 비해 18배의 비용을 감소했다고도 한다.
     * 온프로미스(On-premise) : 클라우드 같이 원격 환경이 아닌 자체적으로 보유한 전산실 서버에 직접 설치하여 운영하는 방식.
  
  
  ## MSA의 지금
  
  - '마이크로서비스는 아직까지 아이디어 수준에서 크게 벗어나 있지 않다. 다양한 산업 분야에서 폭넓게 적용되고 있지만, 그것기 좋은지 나쁜지는 시간이 더 지나봐야 알 수 있다.' - 조쉬 롱, 케니 바스타니, 피보탈
  
  - [마이크로 서비스 실패](http://www.ciokorea.com/news/39258)
  
  ## Monolithic vs Microservices

![asdasd](https://user-images.githubusercontent.com/43510811/62443835-d5b69180-b796-11e9-9877-51601c42ef8f.png)
 
  ## MSA 실습
  
  - Display
  
   1. localhost:8081/displays/{displayId}
   
   2. Display(8081)
   ~~~ 
   request : /display/{displayId}
   
   response : "[displayId = %s at %s %s]", displayId, currentMillis, /products/12345의 응답
   
   ~~~
   
  - Product
    
   3. localhost:8082/products/12345
   
   4. Product(8082)
   
   
   ~~~
   request : /products/{productId}
   
   response : "[product id = ? at ?]"
   ~~~
   
   - 실제로 DB도 각 서비스마다 별도로 사용하는데, 이 때 처리해야할 기술이 까다로움.
   
   - 실제로 비지니스에서 공통적으로 사용되는 DTO 같은 객체를 MSA에서는 중복을 허용한다.
   
   