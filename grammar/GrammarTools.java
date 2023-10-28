package grammar;

import java.util.ArrayList;
import java.util.HashSet;

public class GrammarTools {
    public static void removeLeftRecursion(Grammar grammar){
        ArrayList<Production> productionsToBeDeleted = new ArrayList<>();
        ArrayList<Production> productionsToBeAdded = new ArrayList<>();
        HashSet<String> newNonTerminals = new HashSet<>();

        for(Production p: grammar.productions){
            
            ArrayList<String> alpha = new ArrayList<>();
            ArrayList<String> beta = new ArrayList<>();

            for(String str: p.rhs){
                if(str.substring(0, 1).equals(p.lhs)){
                    alpha.add(str.substring(1));
                }else{
                    beta.add(str);
                }
            }

            //if alpha is empty then current production does not have
            //left recursion
            if(alpha.isEmpty()){ continue; }
            
            //if not
            String newLhs = p.lhs + "'";
            String[] newRhs1 = new String[beta.size()];
            String[] newRhs2 = new String[alpha.size()+1];

            int i1 = 0;
            int i2 = 0;

            for (int i = 0; i < beta.size(); i++) {
                String newrhs1 = beta.get(i) + newLhs;
                newRhs1[i1] = newrhs1;
                i1++;    
            }

            for (int i = 0; i < alpha.size(); i++) {
                String newrhs2 = alpha.get(i) + newLhs;
                newRhs2[i2] = newrhs2;
                i2++;
            }

            newRhs2[i2] = "ε";

            Production prod1 = new Production(p.lhs, newRhs1);
            Production prod2 = new Production(newLhs, newRhs2);
            productionsToBeAdded.add(prod1);
            productionsToBeAdded.add(prod2);
            productionsToBeDeleted.add(p);
            newNonTerminals.add(newLhs);

        }

        grammar.productions.addAll(productionsToBeAdded);
        grammar.productions.removeAll(productionsToBeDeleted);
        grammar.nonTerminals.addAll(newNonTerminals);
        
    }
}
