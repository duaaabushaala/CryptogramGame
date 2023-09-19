import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

class Sprint2Tests{

    Game game;

    @BeforeEach
    void setUp() throws IOException {
        game = new Game("Username", "NumberCryptogram");
    }

    @AfterEach
    void tearDown() throws IOException {
        FileWriter clearCG = new FileWriter("SavedCG.txt");
        FileWriter clearPD = new FileWriter("PlayerData.txt");
        clearCG.write("");
        clearPD.write("");
        clearCG.close();
        clearPD.close();
    }

    @Test
    void TestAccuracy() {
        game.currentPlayer.accurateGuesses++;
        game.currentPlayer.totalGuesses += 2;
        game.currentPlayer.updateAccuracy();
        assert(game.currentPlayer.getAccuracy() == 50);
    }

    @Test
    void TestUserData() throws IOException {
        // Test that the user can add a letter so that they can solve the cryptogram

        // Get a valid value to be mapped
        Player testplayer = new Player("Username");
        testplayer.totalGuesses = 2;
        testplayer.accuracy = 3;
        testplayer.accurateGuesses = 4;
        testplayer.cryptogramPuzzleNumber = 5;
        testplayer.cryptogramsPlayed = 6;
        testplayer.cryptogramsCompleted = 7;
        testplayer.username = "Username";

        game.currentPlayer = testplayer;
        game.saveData();
        /* Alter the data to make sure loadPlayer doesnt just do nothing */
        game.currentPlayer.cryptogramsCompleted = 42;

        game.loadPlayer();

        assert(testplayer.totalGuesses == 2);
        assert(testplayer.accuracy == 3);
        assert(testplayer.accurateGuesses == 4);
        assert(testplayer.cryptogramPuzzleNumber == 5);
        assert(testplayer.cryptogramsPlayed == 6);
        assert(testplayer.cryptogramsCompleted == 7);
        assert(testplayer.username.equals("Username"));
    }

    @Test
    void TestMappingSave() {
        String phrase = "Hello";
        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("1", "H");
        mapping.put("2", "e");
        mapping.put("3", "l");
        mapping.put("4", "o");

        game.cryptogram = new NumberCryptogram(phrase, mapping, new HashMap<>());
        game.saveGame();
        /* Change mapping to make sure loading does nothing */
        game.cryptogram.cryptoMapping.put("1", "F");
        game.loadGame();

        assert(game.cryptogram.cryptoMapping.get("1").equals("H"));
        assert(game.cryptogram.cryptoMapping.get("2").equals("e"));
        assert(game.cryptogram.cryptoMapping.get("3").equals("l"));
        assert(game.cryptogram.cryptoMapping.get("4").equals("o"));
    }

    @Test
    void TestUserGuessSave() {
        String phrase = "Hello";
        HashMap<String, String> userGuess = new HashMap<>();
        userGuess.put("10", "H");
        userGuess.put("11", "e");
        userGuess.put("12", "l");
        userGuess.put("13", "o");

        game.cryptogram = new NumberCryptogram("Test Cryptogram");
        game.cryptogram.userGuess = userGuess;
        game.saveGame();
        /* Change mapping to make sure loading does nothing */
        game.cryptogram.userGuess.put("10", "F");
        game.loadGame();

        assert(game.cryptogram.userGuess.get("10").equals("H"));
        assert(game.cryptogram.userGuess.get("11").equals("e"));
        assert(game.cryptogram.userGuess.get("12").equals("l"));
        assert(game.cryptogram.userGuess.get("13").equals("o"));
    }
}