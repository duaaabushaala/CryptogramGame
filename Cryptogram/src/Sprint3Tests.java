import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class Sprint3Tests {
    Cryptogram gram;

    @Test
    void TestEqualFrequencies() {
        gram = new LetterCryptogram("abcd");
        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("d", "f");
        mapping.put("a", "l");
        mapping.put("b", "g");
        mapping.put("c", "z");
        gram.cryptoMapping = mapping;

        HashMap<String, Float> freq = gram.getFrequences();
        assert(Math.abs(freq.get("f") - 12.5) < 0.001);
        assert(Math.abs(freq.get("g") - 12.5) < 0.001);
        assert(Math.abs(freq.get("z") - 12.5) < 0.001);
        assert(Math.abs(freq.get("l") - 12.5) < 0.001);
    }

    @Test
    void TestOutlierFrequencies() {
        gram = new LetterCryptogram("abcdd");
        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("d", "f");
        mapping.put("a", "l");
        mapping.put("b", "g");
        mapping.put("c", "z");
        gram.cryptoMapping = mapping;

        HashMap<String, Float> freq = gram.getFrequences();
        assert(Math.abs(freq.get("f") - 20) < 0.001);
        assert(Math.abs(freq.get("g") - 10) < 0.001);
        assert(Math.abs(freq.get("z") - 10) < 0.001);
        assert(Math.abs(freq.get("l") - 10) < 0.001);
    }
}
