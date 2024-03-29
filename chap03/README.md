# Chap03

 - Cloud Native
 
 - 전통적 모델 vs DevOps
 
 - Twelve-Factors
 
 ## Cloud Native 란?
 
 - 애플리케이션을 어떻게 만들고 배포하는지가 핵심이며, 어떤 위치(IDC, IP..)는 중요하지 않다.
 
 - '클라우드 서비스를 활용한다는 것은 컨테이너와 같이 민첩하고 확장 가능한 구성 요소를 사용해서 재사용 가능한 개별적인 기능을 제공한다는 것을 의미한다. 이러한 기능은 멀티 클라우드와 같은 여러 기술 경계 간에 매끄럽게 통합되므로 제공 팀이 반복 가능한 자동화와 오케스트레이션을 사용해서 빠르게 작업 과정을 반복할 수 있다.' - 앤디 맨, Chief Technology Advocate at Splunk

 - 신축성(Resiliency) : 서비스가 중단이 되어도, 전체적인 장애로 퍼지지 않고, 빠르게 복구되는 특징.
 
 - 민첩성(Agility) : 빠르게 배포하고, 독립적으로 운영하는 부분.
 
 - 확장 가능성(Scalable) : Scale-out.
 
 - 자동화(Automation) : 일련의 모든 과정을 자동화하여 운영에 수고를 덜음.
 
 - 무상태(State-less) : 각 서비스의 상태는 무상태.
 
 ## 전통적 모델 (Monolithic)
 
  - 개발과 운영 조직의 분리
  
  - 다른 쪽으로 일을 던진 후에 알아서 처리하라며 잊어버리는 방식.
  
 ## DevOps
 
  - 'You run it, you build it. 만들면 운영까지' - 베르너 보겔스, 아마존 CTO
  
  - 개발자가 운영까지 해야한다는 의미가 아닌 한 서비스에 개발자와 운영자가 모두 구성원으로 존재해야 한다는 의미.
  
  - 개별 팀은 프로젝트 그룹이 아닌 제품(Product) 그룹에 소속
  
  - 운영과 제품 관리 모두가 포함되는 조직적 구조, 제품 팀은 소프트웨어를 만들고 운영하는 데 필요한 모든 것을 보유. 
  
 ## Twelve-Factors
 
  - Heroku 클라우드 플랫폼 창시자들이 정립한 애플리케이션 개발 원칙 중 유익한 것을 모아서 정리한 것.
  
  - 탄력적(elastic), 이식성(portability)있는 배포를 위한 Best Practices
  
  - 핵심 사상
  
    - 선언적(functional programing) 형식으로 설정을 자동화해서 프로젝트에 새로 참여하는 동료가 적응하는데 필요한 시간과 비용을 최소화.
    
    - 운영체제에 구애받지 않는 투명한 계약을 통해 다양한 실행 환경에서 작동할 수 있도 이식성을 극대화.
    
    - 현대적인 클라우드 플랫폼 기반 개발을 통해 서버와 시스템 관리에 대한 부담을 줄임.
    
    - 개발과 운영의 간극을 최소화해서 지속적 배포를 가능하게 하고 애자일성을 최대화.
    
    - 도구, 아키텍처, 개발 관행을 크게 바꾸지 않아도 서비스 규모 수직적 확정이 가능.
    
  - 12가지 제약 조건
        
        1. 코드베이스 : 버전 관리되는 하나의 코드베이스가 여러번 배포되고, 코드베이스와 앱 사이에는 항상 1대1 관계가 성립된다.
        
        2. 의존성 : 애플리케이션의 의존관계, 즉, dependency는 명시적으로 선언되어야하고, 모든 의존 라이브러리는 의존관계 도구를 써서 라이브러리 저장소에서 내려받을 수 있어야 한다.
        
        3. 설정 : 설정 정보(배포마다 달라질 수 있는 모든 것. ex. DB 정, 호스트 이름 등...)는 애플리케이션 코드와 염격하게 분리하여, 실행 환경(환경변수,env)에 저장해야한다. 
        
        4. 백엔드 서비스 : 지원 서비스는 필요에 따라 추가되는 리소스(애플리케이션 자원)로 취급한다.
        
        5. 빌드, 릴리즈, 실행 : 철저하게 빌드와 실행 단계가 분리되어야 한다.
            * 빌드 단계 : 소스 코드를 가져와 컴파일 후 하나의 패키지 생성.
            * 릴리즈 단계 : 빌드에 환경설정 정보를 조합한다. 릴리즈 버전은 실행 환경에서 운영될 수 있는 준비가 완료되어 있어야하고, 시매틱 버저닝 등 식별자가 부여된다.
            * 실행 단계 : 보통 런타임이라 불리는 단계로, 릴리즈 버전 중 하나를 선택하여 실행 환경 위에서 애플리케이션을 실행하는 단계이다.
            
        6. 프로세스 : 응용 프로그램은 백엔드 서비스에 저장된 데이터가 하나 이상의 무상태 프로세스로 배포해야 한다.
        
        7. 포트 바인딩 : 서비스는 포트에 연결해 외부에 공개되어야 한다. 실행 환경에 웹 서버를 따로 추가해줄 필요 없이 스스로 웹 서버를 포함하고 있어서 완전히 자기 완비적이다.
        
        8. 동시성 : 프로세스 모델을 통해 애플리케이션는 필요할 때마다 프로세스나 스레드를 수평적으로 확장해서 병렬로 수행할 수 있어야 한다. 장시간 소요되는 데이터 프로세싱 작업은 스레드풀에 할당해서 스레드 실행기를 통해 수행되어야 한다. 예를 들어 HTTP 요청은 서블릿 쓰레드가 처리하고, 시간이 오래 걸리는 작업은 워커 쓰레드가 처리해야 한다.

        9. 처분성 : 빠른 시작과 graceful shutdown을 통한 안정성이 극대화되어야 한다. 가능한 짧은 시간 내에 애플리케이션이 중지될 때, 처리되어야 하는 작업을 모두 수행한 다음 깔끔하게 종료될 수 있다.
        
        10. dev/prod 일치 : 개발 환경과 운영 환경을 가능한 한 동일하게 유지하는 parity을 통해 divergence를 예방할 수 있어야 한다.
            * 시간 차이 : 개발자는 변경 사항을 운영 환경에 빨리 배포해야 한다.
            * 개인 차이 : 코드 변경을 맡은 개발자는 운영 환경으로의 배포 작업까지 할 수 있어야 하고, 모니터링도 할 수 있어야 한다.
            * 도구 차이 : 각 실행 환경에 사용된 기술이나 프레임워크는 동일하게 구성되어야 한다.
            
        11. 로그 : 로그는 이벤트 스트림으로 취급해야 한다. 애플리케이션은 로그 파일 저장에 관여하지 말아야 하고, 로그 집계와 저장은 실행 환경에 의해 처리되어야 한다.
        
        12. Admin 프로세스 : admin/maintenance 작업을 일회성 프로세스로 실행하는 것도, 실행되는 프로세스와 동일한 환경에서 실행해야 하고, 애플리케이션 코드와 함께 배포되어야 한다.
        
               
   
   ![스크린샷 2019-08-09 오전 2 08 58](https://user-images.githubusercontent.com/43510811/62723036-b34c9e80-ba4a-11e9-8448-e009b93f918f.png)


  ## REST API
  
   - 2000년 로이 필딩(Roy Fielding) 박사가 소개한 아키텍처 제약사항.
   
   - 리소스와 엔티티를 다루는 데 초점.
   
   - 동사 대신 명사를, 행위 대신 엔티티에 집중.
   
   - 상태가 없고 요청이 자기 완비적이기 때문에 서비스도 수평적으로 쉽게 확장될 수 있다.
   
   - MSA에서 주로 사용하는 방식.