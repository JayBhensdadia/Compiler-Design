package grammar;

public class Production {
    
    String lhs;
    String[] rhs;

    public Production(String lhs, String[] rhs){
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
