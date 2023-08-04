package chapter06;

import chapter04.Dish;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static chapter04.Dish.menu;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;

public class PartitionPrimeNumbers {
    public static List<Transaction> transactions = asList(
            new Transaction(Currency.EUR, 1500.0),
            new Transaction(Currency.USD, 2300.0),
            new Transaction(Currency.GBP, 9900.0),
            new Transaction(Currency.EUR, 1100.0),
            new Transaction(Currency.JPY, 7800.0),
            new Transaction(Currency.CHF, 6700.0),
            new Transaction(Currency.EUR, 5600.0),
            new Transaction(Currency.USD, 4500.0),
            new Transaction(Currency.CHF, 3400.0),
            new Transaction(Currency.GBP, 3200.0),
            new Transaction(Currency.USD, 4600.0),
            new Transaction(Currency.JPY, 5700.0),
            new Transaction(Currency.EUR, 6800.0)
    );

    public static void main(String[] args) {
        //통화별로 트랜잭션을 그룹화한 코드
//        Map<Currency,List<Transaction>> transactionsByCurrencies = new HashMap<>();
//        for(Transaction transaction : transactions){
//            Currency currency = transaction.getCurrency();
//            List<Transaction> transactionsForCurrency = transactionsByCurrencies.get(currency);
//            if (transactionsForCurrency == null) {
//                transactionsForCurrency = new ArrayList<>();
//                transactionsByCurrencies.put(currency, transactionsForCurrency);
//            }
//            transactionsForCurrency.add(transaction);
//        }

        Map<Currency, List<Transaction>> transactionsByCurrencies = transactions.stream().collect(groupingBy(Transaction::getCurrency));

//        Long howManyDishes = menu.stream().collect(Collectors.counting());

        long howManyDishes = menu.stream().count();

        Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);

        Optional<Dish> mostCaloriesDish = menu.stream()
                .collect(maxBy(dishCaloriesComparator));

        menu.stream().collect(Collectors.summingInt(Dish::getCalories));

        IntSummaryStatistics summarize = menu.stream().collect(summarizingInt(Dish::getCalories));
        System.out.println(summarize);

        System.out.println(menu.stream().map(Dish::getName).collect(joining(", ")));

        System.out.println(menu.stream().collect(reducing(0, Dish::getCalories, (i, j) -> i + j)));

        menu.stream().collect(reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));

        //Collector의 toList()를 reduce로 구현한 코드
        Stream<Integer> stream = asList(1,2,3,4,5,6).stream();
        stream.reduce(
                new ArrayList<>(),(List<Integer> l,Integer e) -> {
                    l.add(e);
                    return l;
                }, (List<Integer> l1,List<Integer> l2) -> {
                    l1.addAll(l2);
                    return l1;
                }
        );

        int totalCalories = menu.stream().collect(reducing(0,
                Dish::getCalories,
                Integer::sum));

        Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType));

        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream()
                .collect(groupingBy(
                        dish -> {
                            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                            else return CaloricLevel.FAT;
                        }
                ));

        Map<Dish.Type, List<Dish>> caloricDishesByType = menu.stream()
                .filter(dish -> dish.getCalories() > 500)
                .collect(groupingBy(Dish::getType));
        System.out.println(caloricDishesByType);

        menu.stream()
                .collect(groupingBy(Dish::getType,
                        filtering(dish -> dish.getCalories() > 500, toList())));

        Map<Dish.Type, List<Dish>> tt = menu.stream().collect(groupingBy(Dish::getType));
        System.out.println(menu.stream()
                .collect(groupingBy(Dish::getType, mapping(Dish::getName, toList()))));

        Map<Dish.Type, List<String>> t = menu.stream()
                .collect(groupingBy(Dish::getType, mapping(Dish::getName, toList())));

        Map<Dish.Type, Set<String>> dishNamesByType = menu.stream()
                .collect(groupingBy(Dish::getType, flatMapping(dish -> dishTags.get(dish.getName()).stream(), toSet())));
        System.out.println(dishNamesByType);

        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel = menu.stream()
                .collect(groupingBy(Dish::getType, groupingBy(dish -> {
                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT;
                })));
        System.out.println(dishesByTypeCaloricLevel);

        menu.stream()
                .collect(groupingBy(Dish::getType, counting()));

//        Map<Dish.Type, Optional<Dish>> mostCaloricByType = menu.stream()
//                .collect(groupingBy(Dish::getType, maxBy(Comparator.comparingInt(Dish::getCalories))));
//        System.out.println(mostCaloricByType);

        Map<Dish.Type, Long> typesCount = menu.stream()
                .collect(groupingBy(Dish::getType, counting()));
        System.out.println(typesCount);

        Map<Dish.Type, Dish> mostCaloricByType = menu.stream()
                .collect(groupingBy(Dish::getType, collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get)));

        System.out.println(mostCaloricByType);

        Map<Dish.Type, Set<CaloricLevel>> caloricLevelByType = menu.stream()
                .collect(groupingBy(Dish::getType, mapping(
                        dish -> {
                            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                            else return CaloricLevel.FAT;
                        }
                        , toCollection(HashSet::new))));


        System.out.println(caloricLevelByType);

        Map<Boolean, List<Dish>> partitionedMenu = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian));
        System.out.println(partitionedMenu);

        partitionedMenu.get(true);
        partitionedMenu.get(false);

        menu.stream()
                .filter(Dish::isVegetarian)
                .collect(toList());

        Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishes = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType)));
        System.out.println(vegetarianDishes);

        Map<Boolean, Dish> mostCaloricPartitionedByVegetarian = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian, collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get)));

        System.out.println(mostCaloricPartitionedByVegetarian);

//        List<Dish> dishes = menu.stream().collect(new toListCollector<Dish>());

        ArrayList<Object> dishes = menu.stream()
                .collect(
                        ArrayList::new,
                        List::add,
                        List::addAll
                );

    }

    //1-n을 까지의 수 중 소수와 비소수를 구분하는 함수
    public static Map<Boolean, List<Integer>> partitionPrimesWithCustomCollector(int n) {
        return IntStream.rangeClosed(2, n).boxed()
                .collect(new PrimeNumberCollector());
    }

    //Predicate 로 전달할 함수
    public static boolean isPrime(List<Integer> primes,int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return primes.stream()
                .takeWhile(i -> i <= candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }

    //1-n을 까지의 수 중 소수와 비소수를 구분하는 함수
    public static Map<Boolean, List<Integer>> partitionPrimes(int n) {
        return IntStream.rangeClosed(1, n).boxed()
                .collect(partitioningBy(i -> isPrime(i)));
    }

    public static boolean isPrime(int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return IntStream.rangeClosed(2, candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }

    public static <T> Collector<T, ?, Long> counting() {
        return reducing(0L, e -> 1L, Long::sum);
    }

    public static final Map<String, List<String>> dishTags = new HashMap<>();
    static {
        dishTags.put("pork", asList("greasy", "salty"));
        dishTags.put("beef", asList("salty", "roasted"));
        dishTags.put("chicken", asList("fried", "crisp"));
        dishTags.put("french fries", asList("greasy", "fried"));
        dishTags.put("rice", asList("light", "natural"));
        dishTags.put("season fruit", asList("fresh", "natural"));
        dishTags.put("pizza", asList("tasty", "salty"));
        dishTags.put("prawns", asList("tasty", "roasted"));
        dishTags.put("salmon", asList("delicious", "fresh"));
    }


}
