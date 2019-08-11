# Chap07

  - Declarative Http Client 
  
  - Feign

## RestTemplate 단점

  - 프로그래밍 관점에서 명령적 프로그래밍이기 때문에 여러가지 불편함을 가지고 있다.
  
  - 쉽고 편리하지만, 아래와 같은 작업들을 반복적으로 수행하게 됨.
  
    - RestTemplate 인스턴스 또는 DI 주입
    
    - 타켓 URL 정의
    
    - Request 헤더 설정
    
    - Contents 타입 설정
    
    - URL 호출
    
  - RestTemplate은 concreate 클래스이기 때문에 테스트가 힘들다.  
     * concreate class (구상 클래스) : 모든 연산에 대한 구현을 가지고 있는 클래스 (추상클래스가 아닌 클래스)
     
## Feign

   - Interface 선언을 통해 자동으로 Http Client를 생성한다. (Rest 기반 서비스 호출을 위한 추상화)
   
   - 선언적 방식으로 인터페이스를 통해 Client 측 프로그램 작
   
   - 관심사의 분리
   
        - 서비스의 관심 : 다른 리소스, 외부 서비스 호출과 리턴값에 초점을 둔다.
        
        - 어떤 URL, 파싱 방법 등은 관심에 두지 않는다.   
        
   - Spring Cloud에서 Open-Feign 기반으로 Wrapping 한 것이 Spring Cloud Feign 
   
## 실습 - Feign 적용

   - API를 호출하는 Display에서 사용되는 RestTemplate을 변경한다.
   
   - Display dependency 추가
   
   ~~~
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-openfeign</artifactId>
       <version>2.1.2.RELEASE</version>
   </dependency>   
   ~~~
   
   - DisplayApplication @EnableFeignClient 추가
   
   ~~~
   @EnableEurekaClient
   @EnableCircuitBreaker
   @SpringBootApplication
   @EnableFeignClients
   public class DisplayApplication {
   
      ...
   
   }
   ~~~      
   
   - Feign용 interface 추가 (FeignProductRemoteService.java)
        - @FeignClient는 내부적으로 Ribbon을 사용하기 때문에 url을 지운다면 eureka 적용으로 로드밸런싱됨.
        
        - Ribbon + Eureka + Hystrix가 통합적으로 적용됨.
        
        - name 속성의 서버를 호출함.
        
        - Hystrix를 적용할 경우 feign.hystrix.enabled = true로 해주어야함.
   
   ~~~
   @FeignClient(name = "product", url = "http://localhost:8082/")
   public interface FeignProductRemoteService {
   
       @RequestMapping("/products/{productId}")
       String getProductInfo(@PathVariable("productId") String productId);
   
   }
   ~~~
   
   - DisplayController 변경
   
   ~~~
   public class DisplayController {
   
       private final ProductRemoteService productRemoteService;
       private final FeignProductRemoteService feignProductRemoteService;
   
       public DisplayController(ProductRemoteService productRemoteService, FeignProductRemoteService feignProductRemoteService) {
           this.productRemoteService = productRemoteService;
           this.feignProductRemoteService = feignProductRemoteService;
       }
       
       ...
   
       private String getProductInfo() {
   //        return productRemoteService.getProductInfo("12345");
           return feignProductRemoteService.getProductInfo("12345");
       }
   
   }
   ~~~
   
## 실습 - Feign + Hystrix Fallback

- 기본 Fallback 방식으로만 처리한다면 실제 정확한 에러가 무엇인지 알 수 없음.

- 따라서 FallbackFactory를 이용하여 구현
      
- FallbackFactory를 구현한 FeignProductRemoteServiceFallbackFactory 클래스 생성
        
     ~~~
     @Component
     public class FeignProductRemoteServiceFallbackFactory implements FallbackFactory<FeignProductRemoteService> {
         @Override
         public FeignProductRemoteService create(Throwable throwable) {
             System.out.println("t = " + throwable);
             return productId -> "[ this product is sold out ]";
         }
     }
     ~~~
 - @FeignClient의 fallbackFactory 속성 사용
 
    ~~~
    @FeignClient(name = "product", fallbackFactory = FeignProductRemoteServiceFallbackFactory.class)
    ~~~
    
 - Feign용 Hystrix 프로퍼티
 
 ~~~
 hystrix:
   command:
     productInfo:
       execution:
         isolation:
           thread:
             timeoutInMilliseconds: 3000
       circuitBreaker:
         requestVolumeThreshold: 1
         errorThresholdPercentage: 50
     FeignProductRemoteService#getProductInfo(String):
       execution:
         isolation:
           thread:
             timeoutInMilliseconds: 3000
       circuitBreaker:
        requestVolumeThreshold: 1
        errorThresholdPercentage: 50
 ~~~   
 