package chapter02;

//전략 디자인 패턴
//런타임에 알고리즘을 선택하는 기법!
//ApplePredicate 가 알고리즘, AppleHeavyWeightPredicate 가 전략!
public interface ApplePredicate {
    boolean test(Apple apple);
}
