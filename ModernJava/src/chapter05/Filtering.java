package chapter05;

import chapter04.Dish;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static chapter04.Dish.menu;
import static java.util.stream.Collectors.*;

public class Filtering {
    public static void main(String[] args) {

        List<Dish> vegetarianMenu = menu.stream()
                .filter(Dish::isVegetarian)  //메서드 참조, 채식 요리인지 확인하는 동작 전달
                .collect(toList());

        Arrays.asList(1, 2, 1, 3, 3, 2, 4).stream()
                .filter(i -> i % 2 == 0)
                .distinct()
                .forEach(System.out::println);

        List<Dish> specialMenu = Arrays.asList(
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER));

        specialMenu.stream()
                .takeWhile(dish -> dish.getCalories() < 320)
                .collect(toList());

        specialMenu.stream()
                .dropWhile(dish -> dish.getCalories() < 320)
                .collect(toList());

        specialMenu.stream()
                .filter(dish -> dish.getCalories() < 320)
                .limit(3)
                .collect(toList());


        specialMenu.stream()
                .filter(dish -> dish.getCalories() > 300)
                .skip(2)
                .collect(toList());

        specialMenu.stream()
                .map(Dish::getName)
                .collect(toList());

        Arrays.asList("Modern", "Java", "In", "Action").stream()
                .map(String::length)
                .collect(Collectors.toList());

        specialMenu.stream()
                .map(Dish::getName)
                .map(String::length)
                .collect(toList());

        List<Stream<String>> collect = Arrays.asList("Hello", "World").stream()
                .map(word -> word.split(""))// <- 각 단어를 개별 문자를 포함하는 배열로 변환
                .map(Arrays::stream) // <- 생성된 스트림을 하나의 스트림으로 평면화!
                .collect(toList());

        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);

        
//        List<Stream<int[]>> result = numbers1.stream()
//                .map(i ->
//                        numbers2.stream().map(j -> new int[]{i, j})
//                ).collect(toList());

        numbers1.stream()
                .flatMap(
                        i -> numbers2.stream().map(j -> new int[]{i, j})
                ).
                filter(i -> (i[0] + i[1]) % 3 ==0 )
                .forEach(i -> System.out.println(i[0] + " " + i[1]));

        if(menu.stream().anyMatch(Dish::isVegetarian)) System.out.println("The menu is (somewhat) vegetarian friendly!!");

        boolean isHealthy = menu.stream()
                .allMatch(dish -> dish.getCalories() < 1000);

        menu.stream()
                .noneMatch(dish -> dish.getCalories() >= 1000);

        menu.stream()
                .filter(Dish::isVegetarian)
                .findAny()
                .ifPresent(dish -> System.out.println(dish.getName()));

        Arrays.asList(1,2,3,4,5).stream()
                .map(n -> n*n)
                .filter(n -> n % 3 == 0)
                .findFirst();

        List<Integer> numbers = new ArrayList<>();


        int sum = 0;
        for(int x : numbers) sum += x;

        sum = numbers.stream().reduce(0 ,(a, b) -> a + b);


        int mul = numbers.stream().reduce(1, (a, b) -> a * b);


        sum = numbers.stream().reduce(0, Integer::sum);

        int count = menu.stream()
                .map(d -> 1)
                .reduce(0, (a, b) -> a + b);

        int totalCalories = menu.stream()
                .mapToInt(Dish::getCalories)
                .sum();



        IntStream intStream = menu.stream().mapToInt(Dish::getCalories);

        OptionalInt maxCalories = menu.stream()
                .mapToInt(Dish::getCalories)
                .max();

        int max = maxCalories.orElse(1);

        IntStream evenNumbers = IntStream.rangeClosed(1, 100).filter(i -> i % 2 == 0);

        Stream<int[]> test = IntStream.rangeClosed(1, 100).boxed().
                flatMap(a -> IntStream.rangeClosed(a, 100)
                        .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                        .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)}));


        IntStream.rangeClosed(1,100).boxed()
                .flatMap(a -> IntStream.rangeClosed(a,100)
                        .mapToObj(b -> new double[]{a,b,Math.sqrt(a*a + b*b)})
                        .filter(t -> t[2] % 1 ==0));

        Stream<String> stream = Stream.of("Modern", "Java", "In", "Action");
        stream.map(String::toUpperCase).forEach(System.out::println);

//        String homeValue = System.getProperty("home");
//        Stream<String> homeValueStream = homeValue == null ? Stream.empty() : Stream.of(value);

        int[] number = {2, 3, 5, 7, 11, 13};
        int s = Arrays.stream(number).sum();

        long uniqueWords = 0;
        try(Stream<String> lines =
                    Files.lines(Paths.get("data.txt"), Charset.defaultCharset())){
            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();
        } catch (IOException e){

        }

        Stream.iterate(0,n -> n + 2)
                .limit(10)
                .forEach(System.out::println);

        IntStream.iterate(0,n -> n < 100,n -> n + 4)
                        .forEach(System.out::println);

        IntStream.iterate(0,n -> n + 4)
                .filter(n -> n < 100)
                .forEach(System.out::println);

        IntStream.iterate(0,n -> n + 4)
                .takeWhile(n -> n < 100)
                .forEach(System.out::println);

        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);



    }
}
