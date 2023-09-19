import java.util.*;

public class NumberCryptogram extends Cryptogram {

	public NumberCryptogram(String file) {
		this.phrase = file;
		this.userGuess = new HashMap < > ();
		matchLetterToNumber();

		this.encryptedPhrase = new ArrayList < > ();
		for (Character c: phrase.toCharArray()) {
			String out = cryptoMapping.get(c.toString());
			encryptedPhrase.add(Objects.requireNonNullElseGet(out, c::toString));
			encryptedPhrase.add(" ");
		}
	}

	public NumberCryptogram(String phrase, HashMap < String, String > mapping, HashMap < String, String > userGuess) {
		this.phrase = phrase;
		this.userGuess = userGuess;
		this.cryptoMapping = mapping;
		this.answerMapping = new HashMap < > ();

		for (Map.Entry < String, String > entry: cryptoMapping.entrySet()) {
			answerMapping.put(entry.getValue(), entry.getKey());
		}

		this.encryptedPhrase = new ArrayList < > ();
		for (Character c: phrase.toCharArray()) {
			String out = cryptoMapping.get(c.toString());
			encryptedPhrase.add(Objects.requireNonNullElseGet(out, c::toString));
			encryptedPhrase.add(" ");
		}
	}

	public void matchLetterToNumber() {
		this.cryptoMapping = new HashMap < > ();
		this.answerMapping = new HashMap < > ();

		HashSet < String > unique = new HashSet < > ();
		for (Character c: phrase.toCharArray()) {
			if (Character.isDigit(c) || Character.isLetter(c)) {
				unique.add(c.toString());
			}
		}

		Integer[] integerArray = new Integer[26];
		for (int i = 0; i < integerArray.length; i++) {
			integerArray[i] = i + 1;
		}
		Collections.shuffle(Arrays.asList(integerArray));

		int i = 0;
		for (String c: unique) {
			cryptoMapping.put(c, integerArray[i].toString());
			answerMapping.put(integerArray[i].toString(), c);
			i++;
		}
	}
}