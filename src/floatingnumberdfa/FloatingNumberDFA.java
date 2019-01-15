package floatingnumberdfa;

/**
 *
 * @author fabrice.appolinary
 *
 * The DFA can recognise the following strings as numbers:
 *
 * 33 +3.0 .35 0.35E1 -33.33E-9 -3E+8
 *
 * but cannot recognise the following number for example:
 *
 * +.3
 *
 * The runtime of of the program is O(n) on the length of the input string.
 *
 */
public class FloatingNumberDFA {

    /*
    * The transition table that the DFA will use
    * The trabnsition table is defined as a 2d-Array where by T[states][symbols]
    * The states have been encoded by representing them as integers ie 0 to 7 since the DFA has 8 states
    * The symbols too have been encoded by representing them as Intergers from 0 to 3.
    * 0 represents the symbols + or -
    * 1 represents the symboles 0 to 9
    * 2 represents the symbol . ie the dot symbol used as a decimal point
    * 3 represents the symbol E used in exponential form of a number
    *
    * Hence to find the next state given the current state as an interger and the input symbol we just need to lookup
    * in the transition table as T[state][symbol] to get the next state. If the next state is a -1 it means
    * we have reached in a terminating state ie the input is wrong and no further checking is needed
     */
    private static int T[][] = new int[][]{
        {1, 2, 3, -1},
        {-1, 2, -1, -1},
        {-1, 2, 3, 5},
        {-1, 4, -1, -1},
        {-1, 4, -1, 5},
        {6, 7, -1, -1},
        {-1, 7, -1, -1},
        {-1, 7, -1, -1}
    };

    //a state that receives a wrong input goes into a terminating state
    private static final int TERMINATING_STATE = -1;

    //when an input is read and is not in the alphabet
    private static final int NOT_IN_ALPHABET = -1;

    public static void main(String[] args) {

        //test input
        String[] inputs = new String[]{
            "33", "3.0", "+3.0", "-3E+8", "-3e+8", "-33.33E-9", ".390", //should be able to accept these
            "+.3", "-.45.45", "E-9", ".E.9", "nine" // should not be able to accept these   
        };

        for (int i = 0; i < inputs.length; i++) {
            System.out.println("is " + inputs[i] + " valid? " + recognise(inputs[i]));
        }
    }

    public static boolean recognise(String string) {

        //the start state is state 0
        int currentState = 0;

        char symbols[] = string.toCharArray();

        for (int i = 0; i < symbols.length; i++) {

            //get the encoding of the symbol first
            int encodedSymbol = encode(symbols[i]);

            //if the symbol is not in the alphabet then dont bother looking
            if (encodedSymbol == NOT_IN_ALPHABET) {
                return false;
            }

            //othewise use the transition table to move to the next state
            currentState = T[currentState][encodedSymbol];

            //If the current state is a terminating state, then return false right away
            if (currentState == TERMINATING_STATE) {
                return false;
            }
        }

        return isAcceptingState(currentState);
    }

    /* This is a helper routine to return the encoding of the input symbol as an integer.
    * -1 means that its a symbol not from the alphabet
     */
    private static int encode(char symbol) {

        if (symbol == '+' || symbol == '-') {
            return 0;
        }

        if (symbol >= '0' && symbol <= '9') {
            return 1;
        }

        if (symbol == '.') {
            return 2;
        }

        if (symbol == 'E' || symbol == 'e') {
            return 3;
        }

        return NOT_IN_ALPHABET;
    }

    /*A routine to check if a state is an accept state or not
    * The accept states are 2, 4, and 7
     */
    private static boolean isAcceptingState(int state) {

        return state == 2 || state == 4 || state == 7;
    }

}
