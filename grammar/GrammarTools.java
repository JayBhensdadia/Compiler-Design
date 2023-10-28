package grammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Arrays;

public class GrammarTools {

    // can only detect and remove direct left recursion
    // can't detect indirect left recursion
    public static void removeLeftRecursion(Grammar grammar) {

        // creating temporary storages to store newly created productions and
        // nonterminals
        // also productions to be deleted
        // reason: we can't modify the grammar.productions while traversing it otherwise
        // we it will throw concurrent modification exception
        ArrayList<Production> productionsToBeDeleted = new ArrayList<>();
        ArrayList<Production> productionsToBeAdded = new ArrayList<>();
        HashSet<String> newNonTerminals = new HashSet<>();

        // for each production p in grammar.productions
        for (Production p : grammar.productions) {

            /*
             * 
             * A -> Aα1 | Aα2 ... | Aαn | β1 | β2 ... | βm
             * 
             * is converted to
             * 
             * A -> β1A' | β2A' ... | βmA'
             * A' -> α1A' | α2A' ... | αnA' | ε
             * 
             */
            ArrayList<String> alpha = new ArrayList<>();
            ArrayList<String> beta = new ArrayList<>();

            for (String str : p.rhs) {
                if (str.substring(0, 1).equals(p.lhs)) {
                    alpha.add(str.substring(1));
                } else {
                    beta.add(str);
                }
            }

            // if alpha is empty then current production does not have
            // left recursion
            if (alpha.isEmpty()) {
                continue;
            }

            // if not
            String newLhs = p.lhs + "'";
            String[] newRhs1 = new String[beta.size()];
            String[] newRhs2 = new String[alpha.size() + 1];

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

    public static void leftFactor(Grammar grammar) {

        /*
         * 
         * A -> αβ1 | αβ2 ... | αβn | γ1 | γ2 ... | γm
         * 
         * is converted to
         * 
         * A -> αA' | γ1 | γ2 ... | γm
         * A' -> β1 | β2 ... | βn
         * 
         */

        // creating temporary storages to store newly created productions and
        // nonterminals
        // also productions to be deleted
        // reason: we can't modify the grammar.productions while traversing it otherwise
        // we it will throw concurrent modification exception
        ArrayList<Production> productionsToBeDeleted = new ArrayList<>();
        ArrayList<Production> productionsToBeAdded = new ArrayList<>();
        HashSet<String> newNonTerminals = new HashSet<>();


        //for each production in grammar
        for (Production p : grammar.productions) {

            String alpha = null;
            ArrayList<String> beta = new ArrayList<>();
            ArrayList<String> gamma = new ArrayList<>();

            // find alpha, beta and gamma
            //use tempAlpha, tempBeta, tempGamma to store data temporarily
            String tempAlpha = p.rhs[0];
            int matches = 0;
            int maxMatches = 0;
            ArrayList<String> tempBeta = new ArrayList<>();
            ArrayList<String> tempGamma = new ArrayList<>();

            for (int i = 0; i < p.rhs.length; i++) {
                String str = p.rhs[i];

                // now compare characters of tempAlpha and str

                //if str contains tempAlpha then add it to beta array
                if (str.contains(tempAlpha)) {
                    if(str.equals(tempAlpha)){ tempBeta.add(str); }
                    else{ tempBeta.add(str.substring(tempAlpha.length()-1)); }
                    
                    matches++;
                    if (matches > maxMatches) {
                        maxMatches = matches;
                    }
                    continue;
                }

                

                //if not then drop characters from right end in tempAlpha
                //until it matches the current str
                while (!str.contains(tempAlpha)  ) {

                    alpha = tempAlpha;
                    tempAlpha = p.rhs[0].substring(0, tempAlpha.length() - 1);

                    //if it matches then add it to beta array
                    if (str.contains(tempAlpha) && tempAlpha.length()>0 ) {

                        //tempBeta.add(str.substring(tempAlpha.length()-1));

                        if(str.equals(tempAlpha)){ tempBeta.add(str); }
                        else{ tempBeta.add(str.substring(tempAlpha.length()-1)); }
                        
                        matches++;
                        if (matches > maxMatches) {
                            maxMatches = matches;
                        }
                        break;
                    }

                }

                //if tempAlpha becomes empty then add str to gamma array
                if(tempAlpha.isEmpty()){
                    tempGamma.add(str);
                }

            }

            //now check if tempBeta is empty than production
            //does not need left factoring
            if (!tempBeta.isEmpty()) {
                
                if(!tempAlpha.isEmpty()){ alpha = tempAlpha; }
                gamma = tempGamma;

                for (String s : tempBeta) {
                    String temp = s.substring(alpha.length());
                    if(temp.isEmpty()){ beta.add("ε"); }
                    else{ beta.add(temp); }
                    
                }


                //these used for debugging 👇
                // System.out.println(alpha);
                // System.out.println(tempBeta);
                // System.out.println(gamma);



                //create new productions and add to temporary storage
                String newLhs = p.lhs + "'";
                gamma.add(0, alpha+newLhs);
                String[] newRhs1 = gamma.toArray(new String[gamma.size()]);
                String[] newRhs2 = beta.toArray(new String[beta.size()]);

                Production prod1 = new Production(p.lhs, newRhs1);
                Production prod2 = new Production(newLhs, newRhs2);

                productionsToBeAdded.add(prod1);
                productionsToBeAdded.add(prod2);
                productionsToBeDeleted.add(p);
                newNonTerminals.add(newLhs);


                

            }else{
                System.out.println("grammar is already left factored");
            }
            
        }


        //add temporary data to grammar`
        grammar.productions.addAll(productionsToBeAdded);
        grammar.productions.removeAll(productionsToBeDeleted);
        grammar.nonTerminals.addAll(newNonTerminals);

    }
}
