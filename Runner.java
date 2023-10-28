import grammar.*;


public class Runner {
    public static void main(String[] args) {
        Grammar grammar = GrammarReader.readGrammarFromFile("grammar.txt");
        grammar.printGrammar();
        
        GrammarTools.removeLeftRecursion(grammar);
        System.out.println("------------------------------------------");
        //GrammarTools.leftFactor(grammar);

        grammar.printGrammar();
        System.out.println("------------------------------------------");

        System.out.println(GrammarTools.computeFirstSet(grammar));
        

        

    }
}
