package chapter03;

import chapter02.Color;

public class Fruit {
    private int weight;
    private Color color;

    public Fruit() {

    }
    public Fruit(Integer weight) {
        this.weight = weight;
    }

    public Fruit(Integer weight, Color color) {
        this.weight = weight;
        this.color = color;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
