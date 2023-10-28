package grammar;

import java.io.BufferedReader;
import java.io.FileReader;

public class GrammarReader {
    public static Grammar readGrammarFromFile(String fileName){

        Grammar grammar = new Grammar();
        boolean isFirstProduction = true;

        try {


            //create file reader
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            while((line = reader.readLine())!=null){
                line = line.trim();

                if(line.isEmpty()){ continue; /*skip empty lines */ }

                //split by ->
                String[] parts = line.split("->");
                if(parts.length!=2){
                    System.out.println("Invalid production : "+line);
                    break;
                }

                String lhs = parts[0];
                String rhs = parts[1];

                lhs.trim();
                rhs.trim();

                //if grammar does not have lhs non terminal then add it
                if(!grammar.nonTerminals.contains(lhs)){
                    grammar.nonTerminals.add(lhs);
                }

                //now rhs
                //first split by '|'

                //rhs.trim();
                String[] rhsProductions = rhs.split("\\|");

                for(String str : rhsProductions){

                    str.trim();

                    //discovering terminals and non terminals
                    for(int i = 0; i < str.length(); i++){
                        char ch = str.charAt(i);

                        //check wether the character is non terminal
                        if(ch>='A' && ch<='Z'){
                            //add to grammar.nonTerminals if not present
                            if(!grammar.nonTerminals.contains(String.valueOf(ch))){
                                grammar.nonTerminals.add(String.valueOf(ch));
                            }
                        }else{

                            //now check wether the ch is 'i' and next character is 'd'
                            //if yes then consider it as single terminal
                            if(ch=='i' && i<str.length()-1 && str.charAt(i+1)=='d'){
                                if(!grammar.terminals.contains("id")){
                                    grammar.terminals.add("id");
                                    i++;
                                }
                            }

                            //now check for string like S'
                            //rather than adding it as S being non terminal and ' being 
                            //terminal we will add it as single non terminal S'

                            else if(ch=='\'' && i!=0){
                                String nt = String.valueOf(str.charAt(i-1)) + String.valueOf(ch);

                                //add that non terminal to grammar if not present
                                if(!grammar.nonTerminals.contains(nt)){
                                    grammar.nonTerminals.add(nt);
                                }
                            }

                            else if(!grammar.terminals.contains(String.valueOf(ch))){
                                grammar.terminals.add(String.valueOf(ch));
                            }
                        }
                    }

                    

                }

                Production p = new Production(lhs, rhsProductions);
                    //add to grammar if not already present 
                    if(!grammar.productions.contains(p)){
                        grammar.productions.add(p);
                    }

                    if(isFirstProduction){
                        grammar.startSymbol = lhs;
                        isFirstProduction = false;
                    }




            }


            
        } catch (Exception e) {
            System.out.println("ERROR IN READING GRAMMAR FROM FILE");
        }

        return grammar;

    }
}
