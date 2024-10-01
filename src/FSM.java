import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class FSM {
    List<Map<Character, Integer>> transitions;
    boolean[] acceptingStates;

    FSM(String filename) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new FileReader(filename))) {
            int n = sc.nextInt(), m = sc.nextInt();
            if (n <= 0 || m < 0) {
                throw new IllegalArgumentException("Number of states and transitions must be non-negative");
            }
            transitions = new ArrayList<>();
            acceptingStates = new boolean[n];
            for (int i = 0; i < n; ++i) {
                transitions.add(new HashMap<>());
            }
            for (int i = 0; i < m; ++i) {
                int from = sc.nextInt(), to = sc.nextInt();
                char ch = sc.next(".").charAt(0);
                if (transitions.get(from).containsKey(ch)) {
                    throw new RuntimeException("File is not valid: multiple transitions for the same character");
                }
                transitions.get(from).put(ch, to);
            }
        }
    }

    boolean check(String word) {
        int v = 0;
        for (int i = 0; i < word.length(); ++i) {
            char ch = word.charAt(i);
            if (!transitions.get(v).containsKey(ch)) {
                return false;
            }
            v = transitions.get(v).get(ch);
        }
        return acceptingStates[v];
    }

    public static void main(String[] args) {
        try {
            FSM fsm = new FSM("fsm.txt");
            System.out.println(fsm.check("ab")); // true
            System.out.println(fsm.check("abc")); // false
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}