# Chap06

  - Dynamic Service Discovery
  
  - Eureka
    
## Ribbon의 한계

 - 과부하가 염려되어 서버를 추가한다면, Ribbon의 설정을 변경하여 api-gateway도 다시 빌드하여 배포해야한다.
 
 - Service Registry를 사용하면 다음과 같은 문제를 해결.
 
 - Service Registry를 Spring Cloud에서 DiscorveryClient로 추상화함.
 
 - DiscorveryClient의 구현체로 Eureka, Consul, Zookeeper, etcd 등이 존재.
 
## Service Registry

![Richardson-microservices-part4-2_client-side-pattern](https://user-images.githubusercontent.com/43510811/62765858-03b81080-bacc-11e9-8fc9-f4501d1180e6.png)

 1. 서비스 인스턴스가 시작될 때 자신의 주소를 서비스 레지스트리에 등록.
 
 2. 서비스 레지스트리는 각 서비스 인스턴스의 상태를 계속 체크.
 
 3. 클라이언트는 서비스 레지스트리에 등록된 인스턴스 중 하나를 골라서 요청 보냄.
 
 4. 인스턴스가 종료되면 서비스 레지스트리에 등록된 정보는 삭제.
 
## Spring Cloud Eureka

 - 서버 시작 시 Euraka Server(Registry)에 자동으로 자신의 상태를 등록
      ~~~
      eureka.client.register-with-eureka = true //default
      ~~~
 
 - 주기적으로 Euraka Server에 자신이 살아 있음을 알림(Heart Beat)
      ~~~
      eureka.instance.lease-renewal-interval-in-seconds = 30 //default
      ~~~
 
 - 서버 종료시 Eureka Server에 자신의 상태 변경 혹은 자신의 목록 삭제 
 
 - Eureka Client가 Eureka Server에 등럭하는 이름 : 'spring.application.name'
 
 ## 실습 - Eureka Server 생성
 
 - eureka-server pom.xml dependency
 ~~~
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
~~~

 - eureka-server @EnableEurekaServer
 ~~~
 @EnableEurekaServer
 @SpringBootApplication
 public class EurekaApplication {
 
     public static void main(String[] args) {
         SpringApplication.run(EurekaApplication.class, args);
     }
 
 }
 ~~~
 
 - Display & Product eureka-client setting
 
 ~~~
 <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
     <version>2.1.2.RELEASE</version>
 </dependency>
 ~~~
 
 ~~~
@EnableEurekaClient
@SpringBootApplication
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
~~~
 
 - eureka dashboard
 
 ~~~
 http://localhost:8761
 ~~~
 
 ![스크린샷 2019-08-09 오후 6 47 16](https://user-images.githubusercontent.com/43510811/62770508-264f2700-bad6-11e9-8e3c-4215c5a67d06.png)
 
 
 - Ribbon listOfServers Remove
 
    - 서버 주소는 Eureka Server에서 직접 가져옴.
 
 ~~~
 ##product.ribbon.listOfServers = localhost:7777, localhost:8082
 ~~~
 
 - ProductApplication을 Port 8083으로 하나 더 띄워서 실험.
 
