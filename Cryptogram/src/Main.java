import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {

		Scanner input = new Scanner(System.in);
		System.out.println("Do you want to play a Number or a Letter cryptogram?");
		System.out.println("1. Number");
		System.out.println("2. Letter");
		String cType;
		while (true) {
			cType = input.next();
			if (cType.equals("1")) {
				cType = "NumberCryptogram";
				break;
			} else if (cType.equals("2")) {
				cType = "LetterCryptogram";
				break;
			} else {
				System.out.println("You have entered an invalid input.");
			}
		}

		System.out.println("Enter The Username You Wish To Use: ");
		String username = input.next();
		Game game = new Game(username, cType);
		game.run();
	}
}
