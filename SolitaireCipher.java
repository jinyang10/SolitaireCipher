package assignment2;


public class SolitaireCipher {
	public Deck key;
	
	public SolitaireCipher (Deck key) {
		this.key = new Deck(key); // deep copy of the deck
	}
	
	/* 
	 * Generates a keystream of the given size
	 */
	public int[] getKeystream(int size) {
		SolitaireCipher newDeck = new SolitaireCipher(key);

		int[] keystreams = new int[size];

		for (int i=0; i<size; i++) {
			keystreams[i] = newDeck.key.generateNextKeystreamValue();
		}
		return keystreams;
	}

	private static boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}
		
	/* 
	 * Encodes the input message using the algorithm described in the pdf.
	 */
	public String encode(String msg) {
		StringBuilder formatted = new StringBuilder();

		for (int i=0; i<msg.length(); i++) {
			char c = msg.charAt(i);
			if (isLetter(c)) {
				formatted.append(Character.toUpperCase(c));
			}
		}
		int[] keystream = getKeystream(formatted.length());

		StringBuilder encoded = new StringBuilder();

		for (int i=0; i<keystream.length; i++) {
			char c = formatted.charAt(i);
			int key = keystream[i];
			int formatKey;
			char formatC;

			if ((key + c) > 90) {
				formatKey = (key + c) - 91;
				if (formatKey > 25) {
					if (formatKey % 25 == 0) {
						formatKey = 25;
					} else {
						formatKey = (formatKey % 25) - 1;
					}

				}
				formatC = (char) ('A' + formatKey);
			} else {
				formatC = (char) (c + key);
			}
			encoded.append(formatC);

		}
		return encoded.toString();
	}
	
	/* 
	 * Decodes the input message using the algorithm described in the pdf.
	 */
	public String decode(String msg) {
		SolitaireCipher newDeck = new SolitaireCipher(key);

		int[] keystream = newDeck.getKeystream(msg.length());
		StringBuilder decoded = new StringBuilder();

		for (int i=0; i<keystream.length; i++) {

			//max = 54, min value = 1
			int key = keystream[i];
			//max = 90, min = 65
			char c = msg.charAt(i);
			int formatKey;
			char formatC;

			if ((c - key) < 65) {
				formatKey = 64 - (c-key);
				if (formatKey > 25) {
					if (formatKey % 25 == 0) {
						formatKey = 25;
					} else {
						formatKey = (formatKey % 25) - 1;
					}

				}
				formatC = (char) ('Z' - formatKey);
			} else {
				formatC = (char) (c - key);
			}
			decoded.append(formatC);
		}
		return decoded.toString();
	}

	
}
