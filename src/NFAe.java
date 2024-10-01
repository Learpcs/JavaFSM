import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class NFAe {
    List<Map<Character, Set<Integer>>> transitions;
    boolean[] acceptingStates;

    public NFAe(String filename) throws FileNotFoundException {
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
                String transitionString = sc.next();
                char transitionChar = transitionString.equals("epsilon") ? '\0' : transitionString.charAt(0);
                transitions.get(fromState).computeIfAbsent(transitionChar, k -> new HashSet<>()).add(toState);
            }
            int numberOfAcceptingStates = sc.nextInt();
            for (int i = 0; i < numberOfAcceptingStates; ++i) {
                int acceptingState = sc.nextInt();
                acceptingStates[acceptingState] = true;
            }
        }
    }

    public boolean check(String word) {
        Set<Integer> currentStates = new HashSet<>();
        currentStates.add(0);
        currentStates = epsilonClosure(currentStates);

        for (int i = 0; i < word.length(); ++i) {
            char currentChar = word.charAt(i);
            Set<Integer> nextStates = new HashSet<>();
            for (int state : currentStates) {
                if (transitions.get(state).containsKey(currentChar)) {
                    nextStates.addAll(transitions.get(state).get(currentChar));
                }
            }
            currentStates = epsilonClosure(nextStates);
        }

        for (int state : currentStates) {
            if (acceptingStates[state]) {
                return true;
            }
        }
        return false;
    }

    private Set<Integer> epsilonClosure(Set<Integer> states) {
        Stack<Integer> stack = new Stack<>();
        Set<Integer> closure = new HashSet<>(states);
        for (int state : states) {
            stack.push(state);
        }
        while (!stack.isEmpty()) {
            int state = stack.pop();
            if (transitions.get(state).containsKey('\0')) {
                for (int nextState : transitions.get(state).get('\0')) {
                    if (!closure.contains(nextState)) {
                        closure.add(nextState);
                        stack.push(nextState);
                    }
                }
            }
        }
        return closure;
    }

    public static void main(String[] args) {
        try {
            NFAe nfae = new NFAe("nfae.txt");
            System.out.println(nfae.check("ab")); // true
            System.out.println(nfae.check("a")); // true (due to epsilon transition)
            System.out.println(nfae.check("abc")); // false
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}