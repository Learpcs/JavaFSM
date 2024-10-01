import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class NFA {
    List<Map<Character, Integer>> transitions;
    boolean[] acceptingStates;

    public NFA(String filename) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new FileReader(filename))) {
            int numberOfStates = sc.nextInt();
            int numberOfTransitions = sc.nextInt();
            if (numberOfStates <= 0 || numberOfTransitions < 0) {
                throw new IllegalArgumentException("Number of states and transitions must be non-negative");
            }
            transitions = new ArrayList<>();
            acceptingStates = new boolean[numberOfStates];
            for (int i = 0; i < numberOfStates; ++i) {
                transitions.add(new HashMap<>());
            }
            for (int i = 0; i < numberOfTransitions; ++i) {
                int fromState = sc.nextInt();
                int toState = sc.nextInt();
                char transitionChar = sc.next(".").charAt(0);
                if (transitions.get(fromState).containsKey(transitionChar)) {
                    throw new RuntimeException("File is not valid: multiple transitions for the same character");
                }
                transitions.get(fromState).put(transitionChar, toState);
            }
            int numberOfAcceptingStates = sc.nextInt();
            for (int i = 0; i < numberOfAcceptingStates; ++i) {
                int acceptingState = sc.nextInt();
                acceptingStates[acceptingState] = true;
            }
        }
    }

    public boolean check(String word) {
        int currentState = 0;
        for (int i = 0; i < word.length(); ++i) {
            char currentChar = word.charAt(i);
            if (!transitions.get(currentState).containsKey(currentChar)) {
                return false;
            }
            currentState = transitions.get(currentState).get(currentChar);
        }
        return acceptingStates[currentState];
    }

    public static void main(String[] args) {
        try {
            NFA nfa = new NFA("nfa.txt");
            System.out.println(nfa.check("ab")); // true
            System.out.println(nfa.check("abc")); // false
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}