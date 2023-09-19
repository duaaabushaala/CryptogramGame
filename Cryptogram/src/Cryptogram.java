import java.util.*;

public abstract class Cryptogram {

	protected HashMap<String, String> userGuess;
	protected HashMap<String, String> cryptoMapping;
	protected HashMap<String, String> answerMapping;
	protected HashMap<String, String> hintMapping;
	protected ArrayList<String> encryptedPhrase;
	protected String phrase;

	public void getHint(){
		hintMapping = new HashMap<>();
		for (String s : answerMapping.keySet()) {
			if (!userGuess.containsKey(s) || !userGuess.get(s).equals(answerMapping.get(s))) {
				hintMapping.put(s, answerMapping.get(s));
			}
		}
		Random randomInt = new Random();
		String hint = hintMapping.keySet().toArray()[randomInt.nextInt(hintMapping.size())].toString();
		System.out.println("Value: " + hint + " is " + hintMapping.get(hint));
		if(userGuess.containsKey(hint)){
			userGuess.replace(hint, hintMapping.get(hint));
		}
		else{
			userGuess.put(hint, hintMapping.get(hint));
		}

	}
	
	public boolean hasMapping(String s) {
		return cryptoMapping.containsKey(s);
	}

	public boolean hasUserGuess(String s) {
		return userGuess.containsValue(s);
	}

	public HashMap<String, Float> getFrequences() {
		HashMap<String, Float> freq = new HashMap<>();
		for (Map.Entry<String, String> e: this.cryptoMapping.entrySet()) {
			int n = 0;
			for (Character c: this.phrase.toCharArray()) {
				if (c.toString().equals(e.getKey())) {
					n++;
				}
			}
			freq.put(e.getValue(), (float) n / this.encryptedPhrase.size() * 100);
		}
		return freq;
	}

	public void displayPuzzle() {
		System.out.println(userGuess);
		System.out.println("The current state is: ");
		for (Character c: phrase.toCharArray()) {
			String out = cryptoMapping.get(c.toString());
			if (out != null) {
				if (out.length() == 2) {
					System.out.print(out);
				}
				else{
					System.out.print(" " + out);
				}
			} else {
				System.out.print(c);
			}
			System.out.print(' ');
		}

		System.out.println();

		for (String c: encryptedPhrase) {
			if (c.equals(" ")) {
				System.out.print(' ');
			} else if (userGuess.containsKey(c)) {
				System.out.print(" " + userGuess.get(c));
			} else {
				System.out.print(" _");
			}
		}
		System.out.println();
	}


	public void addLetter(String c, String n) {
		userGuess.put(c, n);
	}


	public void undoLetter(String c) {
		userGuess.remove(c);
	}

	public boolean isSolved() {
		if(userGuess.size() < cryptoMapping.size()) {
			return false;
		}
		if(userGuess.equals(answerMapping)) {
			System.out.println("\nYou have solved the cryptogram.");
			System.out.println("\nThe phrase was: " + phrase);
			System.out.println();
			return true;
		}
		else{
			System.out.println("Your solution is incorrect.");
			return false;
		}
	}

	public Set<String> getKeyList(){
		return answerMapping.keySet();
	}

}
