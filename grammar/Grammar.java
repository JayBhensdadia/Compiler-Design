package grammar;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Grammar
 */
public class Grammar {

    HashSet<String> terminals;
    HashSet<String> nonTerminals;
    ArrayList<Production> productions;
    String startSymbol;

    //constructor
    public Grammar(){
        terminals = new HashSet<>();
        nonTerminals = new HashSet<>();
        productions = new ArrayList<>();

    }


    public void printGrammar(){
        System.out.println("terminals: "+terminals);
        System.out.println("non terminals: "+nonTerminals);
        printProdutions();
        System.out.println("start symbol: "+ startSymbol);
    }

    public void printProdutions(){

        System.out.println(productions.size());

        for(Production p : productions){

            System.out.print(p.lhs + " -> ");

            for(int i = 0; i < p.rhs.length; i++){
                String str = p.rhs[i];
                System.out.print(str);
                if(i!=p.rhs.length-1){
                    System.out.print(" | ");
                }
            }
            System.out.println();

            
        }
    }


}