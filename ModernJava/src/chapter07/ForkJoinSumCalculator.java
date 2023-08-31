package chapter07;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

import static chapter07.SideEffect.measurePerf;

public class ForkJoinSumCalculator extends RecursiveTask<Long> {
    private final long[] numbers;
    private final int start;
    private final int end;
    public static final long THRESHOLD = 10_000; //이 값 이하로 서브태스크 분할 X

    public ForkJoinSumCalculator(long[] numbers) {  //메인 태스크 생성할 public 생성자
        this(numbers,0,numbers.length);
    }

    private ForkJoinSumCalculator(long[] numbers, int start, int end) { //메인 태스크의 서브태스크를 재귀적으로 만들 때 사용할 private 생성자
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            return computeSequentially();
        }

        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length / 2); //왼쪽 절반 부분을 수행할 작업 생성
        leftTask.fork(); //ForkJoinPool 의 다른 스레드로 새로 생성한 태스크를 비동기 실행
        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length / 2, end); //나머지 절반 부분을 수행할 작업 생성
        Long rightResult = rightTask.compute(); //이 태스크는 동기 실행
        Long leftResult = leftTask.join(); //첫 번째 서브태스크 결과를 읽거나 결과가 없으면 기다림
        return leftResult + rightResult; //두 서브태스크의 결과를 조합한 값이 해당 태스크의 결과
    }

    //더 분해할 수 없는 경우 서브태스크의 결과를 계산할 알고리즘
    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }

    public static long forkJoinSum(long n) {
        long[] numbers = LongStream.rangeClosed(1,n).toArray();
        ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
        return new ForkJoinPool().invoke(task);
    }

    public static void main(String[] args) {
        System.out.println("SideEffect parallel sum done in: " + measurePerf(ForkJoinSumCalculator::forkJoinSum,10_000_000L) + "msecs");
    }
}
