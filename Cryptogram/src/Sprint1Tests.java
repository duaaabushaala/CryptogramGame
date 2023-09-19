import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LetterTests {

    Cryptogram gram;

    @BeforeEach
    void setUp() {
        gram = new LetterCryptogram("Test Phrase");
    }

    @Test
    void addLetter() {
        // Test that the user can add a letter so that they can solve the cryptogram

        // Get a valid value to be mapped
        String tester = "";
        for (String s: gram.cryptoMapping.keySet()) {
            tester = s;
        }

        gram.addLetter(tester, "x");

        assert(gram.hasUserGuess("x"));
        assert(!gram.hasUserGuess("y"));
    }

    @Test
    void undoLetter() {
        // Get a valid value to be mapped
        String tester = "";
        for (String s: gram.cryptoMapping.keySet()) {
            tester = s;
        }

        gram.addLetter(tester, "x");
        assert(gram.hasUserGuess("x"));

        // Check successful removal
        gram.undoLetter("x");
        assert(gram.hasUserGuess("x"));
    }
}