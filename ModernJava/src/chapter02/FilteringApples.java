package chapter02;
import java.util.*;

import static chapter02.Color.RED;
import static chapter02.Predicate.filter;

public class FilteringApples {

    //첫번째 시도: 녹색 사과만 필터링 동작을 하는 메서드 ...
    //그런데 이렇게 하면 색을 바꾸고 싶을 때 해당 메서드를 복사해서 다른 색만 구별하는 메서드를 또 만들어야 함
    // -> 중복... 중복되면 그 코드를 추상화 하자!
    public List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getColor() == Color.GREEN) {
                result.add(apple);
            }
        }
        return result;
    }

    //두 번째 시도: 색을 파라미터화 하자
    public List<Apple> filterApplesByColor(List<Apple> inventory, Color color) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getColor().equals(color)) {
                result.add(apple);
            }
        }
        return result;
    }

    //사과를 무게를 기준으로 필터링
    public List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getWeight() > weight) {
                result.add(apple);
            }
        }
        return result;
    }

    //두 메서드를 보면 필터링 기준이 바뀌는 것 이외에 모든 것들이 중복되고 있다
    //목록을 검색하고, 필터링 조건을 적용하는 부분이 중복되고 있음...
    //DRY dont repeat yourself 같은 것을 반복하지 말것!
    //


    //세 번째 시도: 가능한 모든 속성으로 필터링
    //이 방법은 사용 X
    //
//    public static List<Apple> filter(List<Apple> inventory, Color color,int weight,boolean flag) {
//        List<Apple> result = new ArrayList<>();
//        for (Apple apple : inventory) {
//            if ((flag && apple.getColor().equals(color)) || (!flag && apple.getWeight() > weight)) {
//                result.add(apple);
//            }
//        }
//        return result;
//    }

    //2.2 동작 파라미터를 활용한 유연성 획득
    //선택 조건을 결정하는 인터페이스 생성! -> predicate
    //참 거짓을 반환하는 함수를 프레디케이트라고 함.
    //한 걸을 물러서서 전체를 봐야한다...
    //선택 조건을 추상화!!

    //네 번째 시도 : 추상적 조건으로 필터링
    // 동작 파라미터화!
    //메서드가 동작을 받아서 내부적으로 다양한 동작을 수행!

    //ApplePredicate 객체에 의해 filterPredicate 메서드의 동작이 결정된다
    //
    //filterPredicate 메서드의 동작을 정의하는 것은 ApplePredicate 의 test 메서드
    //메서드는 객체를 인수로 받기 때문에 ApplePredicate 라는 객체를 인자로 넣어주고
    //그 메서드를 실행하도록 한 것이다.
    //test 객체를 구현하는 객체를 이용한 것이므로 이를 '코드 전달' 이라고 할 수 있다!


    public List<Apple> filterApple(List<Apple> inventory, ApplePredicate p) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if(p.test(apple)){//프레디케이트 객체로 사과 검사 조건 추상화(캡슐화)!
                result.add(apple);
            }
        }
        return result;
    }



    public static void prettyPrintApple(List<Apple> inventory, ApplePrint print) {
        for (Apple apple : inventory) {
            System.out.println(print.print(apple));
        }
    }


    public static void main(String[] args) {
        FilteringApples filteringApples = new FilteringApples();
        List<Apple> inventory = Arrays.asList( new Apple(80, Color.GREEN),
                new Apple(155, Color.GREEN),
                new Apple(120, RED));
//        List<Apple> greenApples = filteringApples.filterGreenApples(inventory);
        List<Apple> greenApples = filteringApples.filterGreenApples(inventory);
        FilteringApples.prettyPrintApple(inventory,new PrintColorAndWeight());
        System.out.println(greenApples);

        //다섯 번째 시도 : 익명 클래스 사용
        List<Apple> redApples = filteringApples.filterApple(inventory,new ApplePredicate(){
            public boolean test(Apple apple) {
                return RED.equals(apple.getColor());
            }
        });

        //여섯 번째 시도 : 람다 표현식 사용
        List<Apple> redApplesUserLambda = filteringApples.filterApple(inventory,
                (Apple apple) -> RED.equals(apple.getColor()));

        List<Apple> redAppleList = filter(inventory, (Apple apple) -> RED.equals(apple.getColor()));
        List<Integer> numbers = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        List<Integer> evenNumbers = filter(numbers, (Integer i) -> i % 2 == 0);

    }
}
