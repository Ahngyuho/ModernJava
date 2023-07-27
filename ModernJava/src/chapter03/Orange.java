package chapter03;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Orange extends Fruit{
    public Orange() {

    }

    public Orange(Integer weight){
        super(weight);
    }
}
