package chapter02;

public class PrintColorAndWeight implements ApplePrint{
    @Override
    public String print(Apple apple) {
        return new String(apple.getWeight() + " " + apple.getColor());
    }
}
