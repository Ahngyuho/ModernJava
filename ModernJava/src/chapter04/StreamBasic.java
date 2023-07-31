package chapter04;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static chapter04.Dish.menu;
import static java.util.stream.Collectors.*;

public class StreamBasic {
    public static void main(String[] args) {
        //낮은 칼로리는 가지는 음식을 뽑아내기 위한 연산
        List<Dish> lowCaloriesDishes = new ArrayList<>();
        for(Dish dish : menu){
            if (dish.getCalories() < 400) {
                lowCaloriesDishes.add(dish);
            }
        }

        //익명 클래스로 칼로리 기준 오름차순 정렬
        Collections.sort(lowCaloriesDishes, new Comparator<Dish>() {
            @Override
            public int compare(Dish o1, Dish o2) {
                return Integer.compare(o1.getCalories(), o2.getCalories());
            }
        });

        //정렬된 리스트를 처리하면서 이름 선택
        List<String> lowCaloriesName = new ArrayList<>();
        for (Dish dish : lowCaloriesDishes) {
            lowCaloriesName.add(dish.getName());
        }

        //스트림 이용시 코드
        List<String> lowCaloriesNameUsedStream = menu.stream()
                .filter(dish -> dish.getCalories() < 400) //400 칼로리 이하의 음식 선택
                .sorted(Comparator.comparing(Dish::getCalories)) //칼로리로 요리 정렬
                .map(Dish::getName)
                .collect(toList());

        //멀티코어 아키텍처로 병렬 처리 실행 가능
        List<String> lowCaloriesNameUserParallel = menu.parallelStream()
                .filter(dish -> dish.getCalories() < 400) //400 칼로리 이하의 음식 선택
                .sorted(Comparator.comparing(Dish::getCalories)) //칼로리로 요리 정렬
                .map(Dish::getName)
                .collect(toList());


        List<String> threeHighCaloricDishNames =
                menu.stream() // 메뉴에서 스트림 획득
                        .filter(dish -> dish.getCalories() > 300)
                        .map(Dish::getName)
                        .limit(3)
                        .collect(toList());



        List<String> names = new ArrayList<>();
        for (Dish dish : menu) {
            names.add(dish.getName());
        }

        List<String> namesUserStream = menu.stream()
                .map(Dish::getName) //map 의 매개변수로 동작 전달
                .collect(toList()); // 파이프라인 실행 시점

        List<String> name =
                menu.stream()
                        .filter(dish -> {
                            System.out.println("filtering:" + dish.getName());
                            return dish.getCalories() > 300;
                        })
                        .map(dish ->{
                            System.out.println("mapping:" + dish.getName());
                            return dish.getName();
                        })
                        .limit(3)
                        .collect(toList());
        System.out.println(name);
    }

}
