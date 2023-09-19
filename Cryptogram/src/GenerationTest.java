import org.junit.jupiter.api.Test;

class GenerationTest {

    Cryptogram gram;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        gram = new NumberCryptogram("Test Phrase");
    }

    @Test
    void test_correct_generation() {
        // Test that the cryptogram is generated correctly
        // I.e, that all characters are correctly mapped, and whitespace is excluded
        for (Character c: gram.phrase.toCharArray()) {
            if (Character.isLetter(c) || Character.isDigit(c)) {
                assert (gram.cryptoMapping.containsKey(c.toString()));
            }
        }
    }
}