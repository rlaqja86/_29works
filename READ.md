# 1.프로젝트 환경

### SpringBoot, Gradle

# 2.실행 과정

1.OrderController 의 runInput을 통해 콘솔에서 명령어를 입력 받습니다.

2.input을 받으면 input에 해당하는 command 인터페이스를 검색합니다.

3.o를 입력받을 경우 OrderCommand가 매치되어 주문을 진행합니다.

4.상품번호(productId) 와 수량(orderAmount)을 받아 map에 저장합니다.

5.상품번호와 수량의 입력커맨드가 " "일 경우 주문을 끝내고 결제 단계로 넘어갑니다.

6.만약 선택한 상품이 존재하지 않거나. 재고가 부족하거나. 올바른 상품ID를 선택하지 않으면 결제로 넘어가지 않습니다.

7.결제가 완료되면 step 1 로 돌아가 행위를 반복합니다.
