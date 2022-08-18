package ducnh.springboot.specifications;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchCriteria {
    public enum Operation{
        EQUAL,LIKE,GREATER,LESS,GREATER_, LESS_, JOIN
    }
        private String key;
        private Operation operation;
        private Object value;


}
