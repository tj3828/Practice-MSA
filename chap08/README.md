# Chap08

- API Gateway
    
- Zuul

- Spring Cloud Config 

- Spring Cloud Sleuth
 
 ## API Gateway
 
 - 클라이언트와 백엔드 서버 사이의 출입문
 
 - 특정 URL 요청이 왔을 때 원하는 서비스로 라우팅해주는 것이 주요 기능.
 
 - 횡단 관심사 (cross-service concerns) : 모든 서비스에서 공통적으로 가지고 있는 기능
    
    - 보안,인증(authentication), 인가(authorization)
    
    - 일정량 이상의 요청 제한(rate limiting)
    
    - 계측(metering)
    
 - 정리하자면, MSA에서 수백개 혹은 수천개의 개별 서비스들이 존재할 때 수많은 백 단의 API Endpoint를 단일화하고, uthentication, Logging, Monitoring, Routing 등 기능을 수행한다. 
                                                                         
 ## Netflix Zuul
 
 - Netflix에서 사용하는 API Gateway.
 
 - 50개 이상의 AWS ELB의 앞단에 위치해 3개의 AWS 리전에 걸쳐 하루 백억 이상의 요청을 처리(2015년 기준)
     ![netflix zuul](https://user-images.githubusercontent.com/43510811/62831133-4822e800-bc55-11e9-9883-f325752d950c.png)

 
 - 많은 트래픽과 여러가지 이슈 상황에 대해서 신속히 대응할 수 있도록 다양한 Filter를 제공
        ![zuul filter](https://user-images.githubusercontent.com/43510811/62831161-ae0f6f80-bc55-11e9-92aa-95b51e217ffe.png)

 

 ## Zuul 동작 원리
 
 1. Zuul의 모든 API 요청은 HystrixCommand로 구성되어 전달됨.
 
    - 각 API 경로(서버군) 별로 Circuit Breaker 생성
    
    - 하나의 서버군이 장애를 일으켜도 다른 서버군의 서비스에는 영향이 없다.
    
    - CircuitBreaker / ThreadPool의 다양한 속성을 통해 서비스 별 속성에 맞는 설정 가능.
 
 2. API를 전달할 서버의 목록을 갖고 Ribbon을 통해 Load-Balancing을 수행함.
 
    - 주어진 서버 목록들을 Round-Robin으로 호출.
    
    - Coding을 통해 Load Balancing 방식을 Customize 가능.
    
 3. Eureka Client를 사용하여 주어진 URL의 호출을 전달할 '서버 리스트'를 찾음.   
 
    - Zuul에는 Eureka Client가 내장되어 있음.
    
    - 각 URL에 Mapping된 서비스 명을 찾아서 Eureka Server를 통해 목록을 조회한다.
    
    - 조회던 서버 목록을 'Ribbon' 클라이언트에게 전달.  
    
 4. Eureka + Ribbon에 의해서 결정된 Server 주소로 HTTP 요청
 
    - Apache Http Client가 기본으로 사용됨.
    
    - OkHtt Client 사용 가능  
    
 5. 선택된 첫 서버로의 호출이 실패할 겨우 Ribbon에 의해서 자동으로 Retry 수행
 
    - Retry 수행 조건
    
        - Http Client에서 Exception 발생 (I/O Exception)
        
        - 설정된 HTTP 응답코드 반환 (ex. 503)
        
## Spring Cloud Zuul

  - Netflix Zuul을 감써서 Spring Cloud Zuul을 만들었는데 많은 부분을 변경했다.
  
  - Isolation
    
     - spring-cloud-zuul의 기본 lsolation은 SEMAPHORE, Netflix Zuul은 threadpool
     
     - Semaphore는 network timeout을 격리하지 못하기 때문에 threadpool로 변경하여 사용할 수 있음
        
        - 기본 설정으로는 모든 서비스에 대한 하나의 ThreadPool을 만들기 때문에 각 서비스에 대한 ThreadPool을 만들도록 설정해야함.
        
## etc

- Spring Cloud Config

  - 중앙화된 설정 서버로 여러가지 서비스에 대한 환경 설정을 효율적으로 하게 해줌.
  
  - 재구동을 하지 않아도 리플레시 이벤트를 통해 변경할 수 있음.
  
- Spring Cloud Sleuth
 
  - Distributed Tracing을 위해 각 서비스들의 응답시간 등을 한눈에 볼 수 있음.
   
          
 ## 실습 - Spring Cloud Zuul
 
  - Zuul용 프로젝트 생성 (dependency)      
  
     ~~~
     <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
     </dependency>
     <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
    </dependency>
     ~~~
     
  - Zuul 환경
  
    - @EnableDiscoveryClient은 @EnableEurekaClient를 추상적으로 사용하길 원한다면 동일하게 사용할 수 있는 어노테이션.
    ~~~
    @EnableZuulProxy
    @EnableDiscoveryClient
    @SpringBootApplication
    public class ZuulApplication {
  
        public static void main(String[] args) {
            SpringApplication.run(ZuulApplication.class, args);
        }
  
    }
    ~~~
  
  - Zuul application.yml
  
    ~~~
    spring:
        application:
        name: zuul
  
    server:
        port: 8765
  
    zuul:
        routes:
        product:
            path: /products/**
            serviceId: product
            stripPrefix: false
        display:
            path: /displays/**
            serviceId: display
            stripPrefix: false
    eureka:
        instance:
            non-secure-port: ${server.port}
            prefer-ip-address: true
        client:
            service-url:
                defaultZone: http://localhost:8761/eureka
    ~~~
           
 - zuul을 통한 dipslay 접속
 
    ~~~
    http://localhost:8765/display/displays/1111
    ~~~