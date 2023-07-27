package chapter02;

import chapter03.Fruit;

public class Apple extends Fruit{

    public Apple(){
        super();
    }

    public Apple(Integer weight) {
        super(weight);
    }

    public Apple(int weight, Color color) {
        super(weight,color);
    }

    public int getWeight() {
        return super.getWeight();
    }

    public void setWeight(int weight) {
        super.setWeight(weight);
    }

    public Color getColor() {
        return super.getColor();
    }

    public void setColor(Color color) {
        super.setColor(color);
    }

    @Override
    public String toString() {
        return String.format("Apple {color=%s, weight=%d}", super.getWeight(), super.getColor());
    }

}
