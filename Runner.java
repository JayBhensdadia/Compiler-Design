import grammar.*;


public class Runner {
    public static void main(String[] args) {
        Grammar grammar = GrammarReader.readGrammarFromFile("grammar.txt");
        grammar.printGrammar();
        
        GrammarTools.removeLeftRecursion(grammar);

        grammar.printGrammar();

    }
}
