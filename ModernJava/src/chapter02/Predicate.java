package chapter02;
import java.util.*;

//일곱 번째 시도 : 리스트 형식으로 추상화
public interface Predicate<T> {
    boolean test(T t);

    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T e : list) {
            if(p.test(e)) result.add(e);
        }
        return result;
    }
}
