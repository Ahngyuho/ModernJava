package chapter10;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter
@ToString
public class Order {
    private String customer;
    private List<Trade> trades = new ArrayList<>();

    public void addTrade( Trade trade ) {
        trades.add( trade );
    }

    public double getValue() {
        return trades.stream().mapToDouble( Trade::getValue ).sum();
    }
}
