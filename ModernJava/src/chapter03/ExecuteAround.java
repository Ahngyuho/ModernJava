package chapter03;

import chapter02.Apple;
import chapter02.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.*;

public class ExecuteAround {
    private static Map<String, Function<Integer, Fruit>> map = new HashMap<>();
    static {
        map.put("apple", Apple::new);
        map.put("orange",Orange::new);
    }

    public static Fruit giveMeFruit(String fruit, Integer weight) {
        return map.get(fruit.toLowerCase()).apply(weight);
    }



    public static final String FILE = ExecuteAround.class.getResource("./data.txt").getFile();
    public static void main(String... args) throws IOException{
        //br 로 할 수 있는 동작을 마음대로 지정
        //함수명이 processFile 이므로 file 에대한 작업이라는 것을 함수명이나 doc 을 통해 확인해야 할거 같음
//        processFile(br -> br.readLine() + br.readLine());

        List<String> listOfString = new ArrayList<>();

        //객체로 생성해서 만드는 법



        Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
        Predicate<String> nonEmptyStringPredicateMethodRef = String::isEmpty;



        List<String> nonEmpty = filter(listOfString, nonEmptyStringPredicate);

        //람다 표현식 사용
        filter(listOfString, s -> !s.isEmpty());


        forEach(Arrays.asList(1,2,3,4,5),i -> System.out.println(i));
        forEach(Arrays.asList(1,2,3,4,5),System.out::println);
        System.out.println();


        List<Integer> StringToInteger = map(Arrays.asList("123", "456", "789"), s -> Integer.parseInt(s));
        List<Integer> MethodRef = map(Arrays.asList("123", "456", "789"), Integer::parseInt);



        for(Integer x : StringToInteger) System.out.print(x + " ");

        List<String> str = Arrays.asList("a", "b", "A", "B");

        str.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
        str.sort(String::compareToIgnoreCase);

        for(String x : str) System.out.print(x + " ");

        //인자가 없는 생성자 만약 Apple 에 인자가 없는 생성자가 없다면 만들어줘야 함
        Supplier<Apple> c1 = Apple::new;
        //Supplier 의 get 메서드를 호출!
        Apple a1 = c1.get();

        //Integer -> Apple
        Function<Integer,Apple> c2 = Apple::new;
        Apple a2 = c2.apply(110);

        Function<Integer, Apple> c2Exp = weight -> new Apple(weight);

        BiFunction<Integer,Color,Apple> c3 = Apple::new;
        Apple a3 = c3.apply(110, Color.GREEN);


    }

//    public static String processFileLimited() throws IOException{
//
//    }

    public static  <T> void forEach(List<T> list, Consumer<T> c) {
        for (T x : list) {
            c.accept(x);
        }
    }

    public static String processFile(BufferReaderProcessor p) throws IOException{
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            //이 br이 하는 역할을 동작 파라미터로 받음

            return p.process(br);
        }
    }

    public static  <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for(T x : list){
            if(p.test(x)) result.add(x);
        }
        return result;
    }

    //함수명 인터페이스
    public interface BufferReaderProcessor{
        String process(BufferedReader b) throws IOException;
    }

    public static  <T, R> List<R> map(List<T> list, Function<T, R> f) {
        List<R> result = new ArrayList<>();
        for(T t : list){
            result.add(f.apply(t));
        }
        return result;
    }


}
