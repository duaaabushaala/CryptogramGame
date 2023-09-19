public class Player {

	public int totalGuesses;
	public int cryptogramsPlayed;
	public int cryptogramsCompleted;
	public int accuracy;
	public int accurateGuesses;
	public int cryptogramPuzzleNumber;
	public String username;


	public Player(String username) {
		this.accuracy = 0;
		this.accurateGuesses = 0;
		this.totalGuesses = 0;
		this.cryptogramsPlayed = 0;
		this.cryptogramsCompleted = 0;
		this.cryptogramPuzzleNumber = 0;
		this.username = username;
	}


	public void updateAccuracy() {
		accuracy = (accurateGuesses * 100 / totalGuesses);
	}

	public void incrementCryptogramsCompleted() {
		cryptogramsCompleted++;
	}

	public void incrementCryptogramsPlayed() {
		cryptogramsPlayed++;
	}

	public void incrementCryptogramPuzzleNumber() {
		cryptogramPuzzleNumber++;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public int getAccurateGuesses() {
		return accurateGuesses;
	}

	public int getTotalGuesses() {
		return totalGuesses;
	}

	public int getNumCryptogramsCompleted() {
		return cryptogramsCompleted;
	}

	public int getNumCryptogramsPlayed() {
		return cryptogramsPlayed;
	}

	public int getCryptogramPuzzleNumber() {
		return cryptogramPuzzleNumber;
	}

	public String getUsername() {
		return this.username;
	}
}