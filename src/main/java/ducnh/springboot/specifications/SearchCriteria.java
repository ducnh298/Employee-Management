package ducnh.springboot.specifications;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchCriteria {
    public enum Operation{
        EQUAL,LIKE,GREATER,LESS,GREATER_, LESS_, BETWEEN
    }
        private String key;
        private Operation operation;
        private Object value;
        private Object value2;

    public SearchCriteria(String key, Operation operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
}
