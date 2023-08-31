package chapter07;

import lombok.Getter;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Getter
public class WordCount {
    public static final String SENTENCE =
            " Nel   mezzo del cammin  di nostra  vita "
                    + "mi  ritrovai in una  selva oscura"
                    + " che la  dritta via era   smarrita ";

    private final int counter;
    private final boolean lastSpace;


    public static int countWords(Stream<Character> stream) {
        WordCount wordCounter = stream.reduce(new WordCount(0, true),
                WordCount::accumulate,
                WordCount::combine);

        return wordCounter.getCounter();
    }

    public WordCount(int counter, boolean lastSpace) {
        this.counter = counter;
        this.lastSpace = lastSpace;
    }

    public WordCount accumulate(Character c) {
        if(Character.isWhitespace(c)){
            return lastSpace ? this : new WordCount(counter, true);
        }else {
            return lastSpace ?
                    new WordCount(counter + 1, false) :
                    this;
        }
    }

    public WordCount combine(WordCount wordCount) {
        return new WordCount(counter + wordCount.counter, wordCount.lastSpace);
    }

    private static class WordCounterSpliterator implements Spliterator<Character> {
        private final String string;
        private int currentChar = 0;

        public WordCounterSpliterator(String string) {
            this.string = string;
        }
        @Override
        public boolean tryAdvance(Consumer<? super Character> action) {
            //
            action.accept(string.charAt(currentChar++)); //현재 문자를 소비
            return currentChar < string.length();   //소비할 문자가 남아 있으면 true 반환
        }

        @Override
        public Spliterator<Character> trySplit() {
            int currentSize = string.length() - currentChar;
            if(currentSize < 10){
                return null; //파싱할 문자열을 순차 처리할 수 있을 만큼 충분히 작아졌음을 알리는 null 반환
            }
            for (int splitPos = currentSize / 2 + currentChar;
                 splitPos < string.length(); //파싱할 문자열의 중간을 분할 위치로 설정
                 splitPos++) {
                if(Character.isWhitespace(string.charAt(splitPos))){ //다음 공백이 나올 때까지 분할 위치를 뒤로 이동
                    Spliterator<Character> spliterator = //처음부터 분할 위치까지 문자열을 파싱할 새로운 WordCounterSpliterator 를 생성
                            new WordCounterSpliterator(string.substring(currentChar,splitPos));
                    currentChar = splitPos;
                    return spliterator; //공백을 찾았고 문자열을 분리했으므로 루프를 종료
                }
            }
            return null;
        }

        @Override
        public long estimateSize() {
            return string.length() - currentChar;
        }

        @Override
        public int characteristics() {
            return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
        }
    }

    public static void main(String[] args) {
        Stream<Character> stream = IntStream.range(0, SENTENCE.length())
                .mapToObj(SENTENCE::charAt);
//        System.out.println("Found " + countWords(stream) + " words");
//        stream = IntStream.range(0, SENTENCE.length())
//                .mapToObj(SENTENCE::charAt);
        Spliterator<Character> spliterator = new WordCounterSpliterator(SENTENCE);
        stream = StreamSupport.stream(spliterator, true);
        System.out.println("Found " + countWords(stream) + " words");
    }
}
