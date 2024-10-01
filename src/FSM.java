import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class FSM {
    List<Map<Character, List<Integer>>> go;
    boolean[] f;

    FSM(String filename) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new FileReader(filename))) {
            int n = sc.nextInt(), m = sc.nextInt();
            assert(n > 0 && m >= 0);
            go = new ArrayList<>();
            for (int i = 0; i < n; ++i) {
                go.add(new HashMap<>());
            }
            for (int i = 0; i < m; ++i) {
                int from = sc.nextInt(), to = sc.nextInt();
                char ch = sc.next(".").charAt(0);
                go.get(from).putIfAbsent(ch, new ArrayList<>());
                go.get(from).get(ch).add(to);
            }
            for (int i = 0; i < n; ++i) {
                for (Map.Entry<Character, List<Integer>> lst : go.get(i).entrySet()) {
                    if (lst.getValue().size() != 1) {
                        throw new RuntimeException("File is not valid");
                    }
                }
            }
        }
    }

    boolean check(String word) {
        int v = 0;
        for (int i = 0; i < word.length(); ++i) {
            if (go.get(v).containsKey(word.charAt(i))) {
                return false;
            }
        }
        return false;
    }
}
