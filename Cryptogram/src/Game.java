import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class Game {

	public ArrayList<String> phrases;
	public Cryptogram cryptogram;
	public Players allPlayers = new Players();
	public Player currentPlayer;
	public String cryptType;
	public String currentPhrase;

	public Game(String p, String cryptType) {
		if(allPlayers.findPlayer(p) == null) {
			currentPlayer = new Player(p);
			allPlayers.addPlayer(currentPlayer);
		}
		else{
			currentPlayer = allPlayers.findPlayer(p);
		}
		this.cryptType = cryptType;

		try {
			loadPlayer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Game(String p) {
		if(allPlayers.findPlayer(p) == null) {
			currentPlayer = new Player(p);
			allPlayers.addPlayer(currentPlayer);
		}
		else{
			currentPlayer = allPlayers.findPlayer(p);
		}
		cryptType = cryptTypeDecider();
		loadPhrases();
	}

	public void run() throws IOException {
		boolean wasWin = true;
		Scanner input = new Scanner(System.in);
		while (true) {
			if (loadPhrases() == 1) {
				System.out.println("Stats: ");
				printPlayerStats();
				System.out.println("Leaderboard: ");
				getTopPlayers();
				break;
			}
			generateCryptogram(cryptType);
			currentPlayer.incrementCryptogramsPlayed();
			label:
			while (!cryptogram.isSolved()) {
				wasWin = true;
				printPlayerStats();
				cryptogram.displayPuzzle();
				System.out.println("(1) add - adds a character to the cryptogram");
				System.out.println("(2) remove - remove the last guess in the cryptogram");
				System.out.println("(3) hint  - gives you a letter for free");
				System.out.println("(4) frequencies - gives you the frequencies of letters");
				System.out.println("(5) Solution - gives you the solution to the cryptogram");
				System.out.println("(6) save - saves the current game");
				System.out.println("(7) load - loads previous game");
				System.out.println("(8) exit - exits to main menu");
				System.out.println("(9) leaderboard - Check where you rank amongst other players");
				String ans = input.next();
				switch (ans) {
					case "1":
						enterLetter(); //Enter an input
						break;
					case "2":
						undoLetter(); //Undo an input
						break;
					case "3":
						cryptogram.getHint(); //Get a hint
						break;
					case "4":
						HashMap<String, Float> freq = cryptogram.getFrequences(); //Looks at the frequencies of characters
						System.out.println("Character : Frequency");
						for (Map.Entry<String, Float> e : freq.entrySet()) {
							System.out.println(String.format("%s : %s%%", e.getKey(), e.getValue()));
						}
						break;
					case "5":
						System.out.println("Solution: " + cryptogram.phrase); //Completes the Cryptogram game
						wasWin = false;
						break label;
					case "6":
						if (!promptSave()) { //Save the current game
							wasWin = false;
							break label;
						}
						break;
					case "7":
						if (loadGame()) { //Load game
							currentPlayer.incrementCryptogramsPlayed();
						}
						break;
					case "8":
						System.out.println("The application has been closed, GOODBYE!");
						System.exit(0); // Exit the application
						break label;
					case "9":
						getTopPlayers(); //Check where you rank amongst other players
						break;
					default:
						System.out.println("INVALID INPUT!");
						break;
				}
			}
			System.out.println("GAME OVER");

			if (wasWin) {
				currentPlayer.incrementCryptogramsCompleted();
			}
			currentPlayer.incrementCryptogramPuzzleNumber();
			saveData();
			System.out.println("Would you like to play another cryptogram? Please type 'Yes' or 'No' ");
			String yesNo = input.next();
			if (yesNo.equals("Yes")) {
				generateCryptogram(cryptType);
			}
			else if (yesNo.equals("No")) {
				System.out.println("\nThe application has been closed.");
				break;
			}
			else {
				System.out.println("Command not understood.");
			}
		}
	}

	public boolean promptSave() {
		saveGame();
		Scanner input = new Scanner(System.in);
		System.out.println("Would you like to continue playing this cryptogram? (Y/N)");
		while (true) {
			String yn = input.next();
			if (yn.equals("Y")) {
				return true;
			} else if (yn.equals("N")) {
				return false;
			} else {
				System.out.println("Command not understood.");
			}
		}
	}

	public String cryptTypeDecider() {
		Random randomInt = new Random();

		if (randomInt.nextInt(2) == 1) {
			return "LetterCryptogram";
		} else {
			return "NumberCryptogram";
		}
	}

	public void printPlayerStats() {
		System.out.printf("Cryptograms Completed: %d\nCryptograms Played: %d\nAccuracy: %d\n",
				currentPlayer.getNumCryptogramsCompleted(),
				currentPlayer.getNumCryptogramsPlayed(),
				currentPlayer.getAccuracy());
	}

	public void loadPlayer() throws IOException {
		File file = new File("PlayerData.txt");
		List<String> lines = Files.readAllLines(file.toPath());

		String target_line = null;
		for (String line: lines) {
			if (line.contains(currentPlayer.username)) {
				target_line = line;
			}
		}

		if (target_line != null) {
			String[] words = target_line.split(" ");
			currentPlayer.cryptogramsCompleted = Integer.parseInt(words[0]);
			currentPlayer.cryptogramsPlayed = Integer.parseInt(words[1]);
			currentPlayer.cryptogramPuzzleNumber = Integer.parseInt(words[2]);
			currentPlayer.accurateGuesses = Integer.parseInt(words[3]);
			currentPlayer.totalGuesses = Integer.parseInt(words[4]);
			currentPlayer.accuracy = Integer.parseInt(words[5]);
		}
	}

	public int loadPhrases() {
		phrases = new ArrayList<>();
		File file = new File("Crypto-Phrases.txt");
		try {
			Scanner reader = new Scanner(file);
			while (reader.hasNextLine()){
				phrases.add(reader.nextLine());
			}
			reader.close();
		} catch (FileNotFoundException e){
			System.out.println("Error: the Crypto-Phrases file was not found.");
		}
		if(currentPlayer.getCryptogramPuzzleNumber() < 16) {
			currentPhrase = phrases.get(currentPlayer.getCryptogramPuzzleNumber());
			return 0;
		}
		else{
			System.out.println("There are no more cryptograms.");
			return 1;
		}
	}

	public void generateCryptogram(String cryptoType) {
		if (cryptoType.equals("NumberCryptogram")) {
			cryptogram = new NumberCryptogram(currentPhrase);
		}
		else if (cryptoType.equals("LetterCryptogram")) {
			cryptogram = new LetterCryptogram(currentPhrase);
		}
	}

	public void enterLetter() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Please enter the character you would like to map: ");
		String symbol = scanner.next();

		if (!cryptogram.getKeyList().contains(symbol)) {
			System.out.println("The symbol you entered is not in the phrase.");
			return;
		}

		System.out.print("\nPlease enter the mapping for that symbol: ");
		String mapping = scanner.next();

		if (cryptogram.hasUserGuess(mapping)) {
			System.out.println("The mapping you entered is already in use.");
			return;
		}

		cryptogram.addLetter(symbol, mapping);

		if (mapping.equals(cryptogram.cryptoMapping.get(symbol))) {
			currentPlayer.accurateGuesses++;
		}

		currentPlayer.totalGuesses++;
		currentPlayer.updateAccuracy();
	}


	public void undoLetter() {
		Scanner input = new Scanner(System.in);
		System.out.print("Enter the character you would like to remove: ");
		String sym = input.next();
		if (!cryptogram.hasMapping(sym)) {
			cryptogram.undoLetter(sym);
		} else {
			System.out.print("The character you entered is not currently mapped.");
		}
	}

	public void saveData() throws IOException {
		File file = new File("PlayerData.txt");
		List<String> data = Files.readAllLines(file.toPath());
		String line = currentPlayer.getNumCryptogramsCompleted() + " " +
				currentPlayer.getNumCryptogramsPlayed() + " " +
				currentPlayer.getCryptogramPuzzleNumber() + " " +
				currentPlayer.getAccurateGuesses() + " " +
				currentPlayer.getTotalGuesses() + " " +
				currentPlayer.getAccuracy() + " " +
				currentPlayer.getUsername();
		int pos = allPlayers.getPlayerId(currentPlayer);
		if(pos == data.size()){
			data.add(line);
		}
		else {
			data.set(pos, line);
		}

		FileWriter writer = new FileWriter("PlayerData.txt");
		BufferedWriter bwriter = new BufferedWriter(writer);
		for(String l: data) {
			bwriter.write(l);
			bwriter.newLine();
		}

		bwriter.close();
		writer.close();
	}

	public void getTopPlayers() throws IOException {
		File file = new File("PlayerData.txt");
		List<String> lines = Files.readAllLines(file.toPath());
		Scanner reader;
		List<ArrayList<String>> leaderb = new ArrayList<>();
		ArrayList<String> temp;

		for (String line: lines) {
			reader = new Scanner(line);
			temp = new ArrayList<>();
			int score = reader.nextInt();
			temp.add(Integer.toString(score));
			for(int a = 0; a < 5; a++){ reader.next(); }
			String name = reader.next();
			temp.add(name);
			leaderb.add(temp);
		}

		leaderb.sort((o1, o2) -> {
			Integer a1 = Integer.parseInt(o1.get(0));
			Integer a2 = Integer.parseInt(o2.get(0));
			return a2.compareTo(a1);
		});

		for (int i = 0; i < 10 && i < leaderb.size(); i++){
			System.out.println((i + 1) + " : " + leaderb.get(i));
		}
	}

	public boolean checkSaveExists() {
		boolean exists = false;
		try {
			Scanner input = new Scanner(new FileInputStream("SavedCG.txt"));
			while (input.hasNext()) {
				String line = input.nextLine().trim();
				if (line.equals(currentPlayer.username)) {
					exists = true;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error: the SavedCG file was not found.");
		}
		return exists;
	}

	public void saveGame() {
		if (checkSaveExists()) {
			System.out.println("Game already saved with this username, Do you want to OVERWRITE.");
			System.out.println("1. Yes");
			System.out.println("2. No");
			Scanner input = new Scanner(System.in);
			while (true) {
				String ans = input.next();
				if (ans.equals("2")) {
					System.out.println("Save aborted.");
					return;
				} else if (!ans.equals("1")) {
					System.out.println("Command not understood.");
				} else {
					break;
				}
			}
		}

		eraseLastGame();
		BufferedWriter output = null;
		try {
			FileWriter fw = new FileWriter("SavedCG.txt", true);
			output = new BufferedWriter(fw);

			output.write(currentPlayer.username + "\n");
			output.write(cryptType + "\n");
			output.write(cryptogram.phrase+ "\n");
			output.write(cryptogram.cryptoMapping.size() + "\n");
			for (Map.Entry<String,String> entry: cryptogram.cryptoMapping.entrySet()) {
				output.write(entry.getKey() + " " + entry.getValue() + "\n");
			}

			output.write(cryptogram.userGuess.size() + "\n");
			for (Map.Entry<String,String> entry: cryptogram.userGuess.entrySet()) {
				output.write(entry.getKey() + " " + entry.getValue() + "\n");
			}
			output.write("\n");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				assert output != null;
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void eraseLastGame() {
		File tmp = new File("tmp.tmp");
		if (!tmp.isFile()) {
			try {
				tmp.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		File current = new File("SavedCG.txt");

		if (!current.isFile()) {
			try {
				current.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try (
				BufferedReader curr = new BufferedReader(new FileReader(current));
				BufferedWriter temp = new BufferedWriter(new FileWriter(tmp, false))) {
			String line;
			while ((line = curr.readLine()) != null) {
				String nonl = line.trim();
				if (nonl.equals(currentPlayer.username)) {
					curr.readLine();
					curr.readLine();
					int n = Integer.parseInt(curr.readLine().trim());
					for (int i=0; i<n; i++) {
						curr.readLine();
					}
					int m = Integer.parseInt(curr.readLine().trim());
					for (int i=0; i<m; i++) {
						curr.readLine();
					}
				} else {
					temp.write(line + "\n");
				}
			}
			tmp.renameTo(current);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Returns true if a game exists and was successfully loaded */
	public boolean loadGame() {
		if (!checkSaveExists()) {
			System.out.println("Error: You do not yet have a save file under this name.");
			return true;
		}

		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader((new FileInputStream("SavedCG.txt"))));

			String username;
			while((username = input.readLine()) != null) {
				username = username.trim();
				if (username.equals(currentPlayer.username)) {
					String type = input.readLine().trim();
					String phrase = input.readLine().trim();

					int elems = Integer.parseInt(input.readLine().trim());
					HashMap<String, String> cryptMapping = new HashMap<>();
					for (int i = 0; i < elems; i++) {
						String[] kv = input.readLine().trim().split(" ");
						String key = kv[0];
						String val = kv[1];
						cryptMapping.put(key, val);
					}

					int nguess = Integer.parseInt(input.readLine().trim());
					HashMap<String, String> userGuess = new HashMap<>();
					for (int i = 0; i < nguess; i++) {
						String[] kv = input.readLine().trim().split(" ");
						String key = kv[0];
						String val = kv[1];
						userGuess.put(key, val);
					}

					Cryptogram loaded;
					if (type.equals("NumberCryptogram")) {
						loaded = new NumberCryptogram(phrase, cryptMapping, userGuess);
					} else {
						loaded = new LetterCryptogram(phrase, cryptMapping, userGuess);
					}
					this.cryptogram = loaded;
					return true;
				} else {
					input.readLine();
					input.readLine();

					int elems = Integer.parseInt(input.readLine().trim());
					for (int i = 0; i < elems; i++) {
						input.readLine();
					}

					int nguess = Integer.parseInt(input.readLine().trim());
					for (int i = 0; i < nguess; i++) {
						input.readLine();
					}
					input.readLine();
				}
			}
			return false;
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				assert input != null;
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
