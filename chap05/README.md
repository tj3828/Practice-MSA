# Chap05

 - Server Side LoadBalancer
 
 - Client Side LoadBalancer
 
 - Ribbon
 
## Server Side LoadBalancer

 - Client(Caller) -> Load Balancer -> Server1, Server2...
 
 - 일반적으로 L4 Switch 기반의 Load Balancing을 하는데, 이때, L4 Switch 는 Server의 목록을 알고 있고, Client는 L4의 주소만 알고 있음.
 
 - H/W 기반의 Server Side Load Balancer를 사용하면 비용이 커지고, 유연성이 떨어지게 된다. 또한 서버 목록을 추가하기 위해서 설정을 필요로하기 때문에 자동화가 어렵고, 12 factors의 dev/prod를 만족하기가 어렵다.
 
## Client Side LoadBalancer

 - Client(API Caller) : Ribbon -> Server1, Server2...

 - Client (API Caller)에 탑재되는 S/W 모듈.
 
 - 별도의 H/W를 필요로 하지 않기 때문 비용과 유연성 측면에서 좋다.
 
 - 주어진 서버 목록에 대해서 Client Side에서 S/W만으로 Load Balancing을 수행함.
 
 - S/W : Ribbon
 
## 실습 - Ribbon 기본 설정

 - @LoadBalanced가 추가된 RestTemplate에 interceptors를 추가하여 환경변수의 listOfServers 값으로 변경해줌.

 - dependency
 ~~~
 <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
     <version>2.1.2.RELEASE</version>
 </dependency>
 ~~~
 
 - Display의 RestTemplate 빈에 @LoadBalanced 추가
 ~~~
 @Bean
 @LoadBalanced
 public RestTemplate restTemplate() {
     return new RestTemplate();
 }
 ~~~
 
 - Display의 Product 호출 URL 변경
 
 ~~~
 public static final String URL = "http://product/products/";
 ~~~
 
 - product 지정 (application.properties)
 
 ~~~
 product.ribbon.listOfServers = localhost:8082
 ~~~
 
## 실습 - Ribbon Retry

 - listOfServers에 지정된 서버에서 실패시 Round-Robin 방식으로 다른 서버에 재시도하는 기능.
 
 - dependency
 ~~~
 <dependency>
     <groupId>org.springframework.retry</groupId>
     <artifactId>spring-retry</artifactId>
     <version>1.2.4.RELEASE</version>
 </dependency>
~~~
 
 - application.properties
 ~~~
 product.ribbon.listOfServers = localhost:7777, localhost:8082
 product.ribbon.MaxAutoRetries = 1  // Retry 횟수 
 product.ribbon.MaxAutoRetriesNestServer = 1    // Retry할 (다음) 서버의 수
 ~~~
 
## Hystrix & Ribbon

 - 실습과 같이 Hystrix로 Ribbon을 감싸서 호출한 상태라면, Retry를 시도하다가도 HystrixTimeout이 발생하면, 즉시 에러 반환 리턴한다.
 
 - Retry를 끄거나, 재시도 횟수를 0으로 하여도 해당 서버로의 호출이 항상 동일한 비율로 실패하지는 않는다.(실패한 서버로의 호출은 특정 시간동안 Skip되고 그 간격은 조정된다. - BackOff)
 
 - 디버그를 통해 interceptors에 retry가 있는지 확인하는 방법 등으로 테스트해봐야 한다는 점 주의.
 
 
 
 
 


 
 