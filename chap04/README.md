# Chap04

 - Netflix OSS
 
 - Spring Cloud

 - Circuit Breaker 
 
 - Hystrix
 
 ## Netflix OSS
 
  - 50개 이상의 사내 프로젝트를 오픈소스를 공개
  
  - 플랫폼 (AWS) 안의 여러 컴포넌트와 자동화 도구를 사용하면서 파악한 패턴과 해결 방법을 블로그, 오픈소스로 공개했다.
  
 ## Spring-Cloud
 
  - Netflix OSS와 마찬가지로 Spring 환경에서 분산 시스템에서 공통된 패턴을 빠르게 구축할 수 있도록 도구를 제공하는 것이다.
  
  - Netflix OSS에 있는 Hystrix, Eureka, Zuul 등을 차용해서 사용하고 있다.

  ![스크린샷 2019-08-09 오후 12 36 14](https://user-images.githubusercontent.com/43510811/62752400-58df2c80-baa2-11e9-8466-a55958cd038e.png)

  
 ## Circuit Breaker
 
  - 모놀리틱에서의 의존성 호출은 100% 신뢰할 수 있다. (Controller -> Service -> Repository)
  
  - 하지만, MSA에서는 모놀리틱과 같지 않기 때문에 실패를 가장 신경써야하고 우선시해야 한다. (Failure as a First Class Citizen)
  
    - 분산 시스템, 특히 클라우드 환경에서는 실패는 일반적인 표준이다.
    
    - 서비스와 서비스간 연결이 보통 HTTP와 같이 통신을 통해 이루어지기 때문에 100% 신뢰할 수 없다.
    
    - Netflix에서는 한 서비스의 가동율(uptime)이 최대 99.99%라 한다.
        - 30개의 서비스 : 99.99^30 = 99.7% uptime
        - 10억 개의 요청 중 0.3% 실패 = 300만 요청 실패
        - 모든 서비스들이 이상적인 uptime을 가진다 하더라도 매 달마다 2시간 이상의 downtime이 발생
        
  - 이러한 uptime을 높이기 위해 특정 MSA 서비스의 장애로 인해 다른 MSA 서비스에도 장애를 일으킬 수 있는 가능성을 방지하기 위해 사용한다. (Latency Tolerance and Fault Tolerance for Distributed Systems)
  
  ## Hystrix
  
   - @HystrixCommand 어노테이션 이용.
   
   - @HystrixCommand이 붙은 메소드 호출을 intercept하여 대신 실행함.
   
   - 실행된 결과의 성공 / 실패 여부를 기록하고 통계를 낸다.
   
   - 실행 결과의 통계에 따라 Circuit Open 여부를 판단하고 필요한 조치를 취함.
   
        - Circuit Open : 모든 요청이 다음 단계로 넘어가지 않고, 즉시 에러를 반환한다.
        
        - 특정 메소드에서의 지연이 시스템 전체의 리소르를 모두 소모하여 시스템 전체의 장애를 유발하고, 특정 외부 시스템에서 계속 에러를 발생 시킨다면, 지속적인 호출이 에러 상황을 더욱 악화 시킴.
   
   - 기본 설정 : 10초동안 20개 이상의 호출이 발생 했을 때 50% 이상의 호출에서 에러가 발생하면 Circuit Open 시킴.
   
   - Circuit Open이 된 경우나 모든 실패에 대해서 Fallback Method로 전달됨.
   
   - Timeout : 오랫동안 응답이 없는 메소드에 대한 처리 방버으로 Fallback Method를 호출함. (기본이 1초이기 때문에 1초이상 걸리는 요청에서 주의해야함.)
        
        
  ![스크린샷 2019-08-09 오후 1 03 21](https://user-images.githubusercontent.com/43510811/62753342-394a0300-baa6-11e9-8daa-7da4129437df.png)


        - Short-circuit : 써킷이 오픈된 상태
   
        - Calculate Circuit Health : 써킷이 오픈되어야하는지 마는지 결정하는 단계
        
  ## 실습 - Exception 발생.
  
   - Product API에 장애가 나더라도 Display의 다른 서비스에는 영향이 없어야한다.
   
   - 만약, Product API의 응답이 오류일 경우, 그에 대한 Method를 호출시킨다.
   
   - dependency
   ~~~
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
       <version>2.1.2.RELEASE</version>
   </dependency>
   ~~~
   
   - Display 요청에 hystrix 설정
        
        - getProductInfo() Method에 @HystrixCommand 추가
   
        - 애플리케이션 로딩이 시작하는 DisplyApplication에 @EnableCircuitBreaker 추가
        
   - Product 응답에서 강제 Exception 발생
   
        - 컨트롤러의 getProduct()에서 Runtime Exception 발생
   
   - Product 에러에 대한 Display Fallback Method 설정
        
        - @HystrixCommand(fallbackMethod = "getProductInfoFallback")    
   
        - getProductInfoFallback() Method 생성
          ~~~
           public String getProductInfoFallback(String productId, Throwable t) {
               System.out.println("t = " + t);
               return "[This Product is sold out]";
           }
          ~~~
          
   - Display 요청을 통해 응답 확인
   
        - Response : [display id = 112 at 1565325457831 [This Product is sold out]
   
  ## 실습 - Timeout 발생.
  
   - Product에서 강제적으로 2초 지연 발생.
   
     ~~~
     @GetMapping("/{productId}")
         public String getProduct(@PathVariable String productId) {
             try {
                 Thread.sleep(2000);
     
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
     //        throw new RuntimeException("I/O Exception");
             System.out.println("Called product id " + productId);
             return "[product Id = " + productId + " at " + System.currentTimeMillis() + "]";
         }
     ~~~
     
   - default timeout은 1초로, 응답이 2초이상이기 때문에 fallback method가 호출된다. 하지만 실제 응답 서비스에서 진행하던 로직이 중단되는 것은 아니다. 요청의 기준에서 판단할 뿐 Product 컨트롤러의 "System.out.println("Called product id " + productId);" 은 정상적으로 호출된다.
   
   - Display의 application.properties에서 timeout 설정 가능.
   
        - hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds = 3000
        
 ## 실습 - Circuit Open     
   
   - 1개의 요청이 실패중 50% 실패했을 때 Circuit Open (사실상 1개만 에러나도 무조건 오픈됨.)
   
   ~~~
   hystrix.command.default.circuitBreaker.requestVolumeThreshold = 1
   hystrix.command.default.circuitBreaker.errorThresholdPercentage = 50
   ~~~
   
   - Timeout으로 인해 첫번째 요청에서는 Throwable로 HystrixTimeoutException이 발생하지만, 그 다음 작업부터는 Hystrix circuit short-circuited and is OPEN 에러가 발생한다.
   ~~~
   t = com.netflix.hystrix.exception.HystrixTimeoutException
   t = java.lang.RuntimeException: Hystrix circuit short-circuited and is OPEN
   t = java.lang.RuntimeException: Hystrix circuit short-circuited and is OPEN
   t = java.lang.RuntimeException: Hystrix circuit short-circuited and is OPEN
   ...
   ~~~