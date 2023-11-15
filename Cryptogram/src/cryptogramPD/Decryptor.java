package cryptogramPD;

import java.util.Map;
import java.util.HashMap;

// Decryptor Class - Used to decrypt letters by associating the encrypted letter with their decrypted counter part
public class Decryptor {
	// Map variable that uses the encryption as the key with the decryption as the value
	private Map<String, String> letters = new HashMap<String, String>();
	
	// Default Constructor
	public Decryptor() {}
	
	// Constructor with another decryptor object as the variable being passed, used to create another object
	public Decryptor(Decryptor decrypt) {
		letters = decrypt.letters;
	}
	
	// decryptLetter - Associates the encrupted letter with a possible decryption
	public void decryptLetter(String encryption, String decryption) {
		letters.put(encryption, decryption);
	}
	
	// getLetter - gets the decrypted letter that has been mapped to the encrypted letter
	public String getLetter(String encryption) {
		return letters.get(encryption);
	}
	
	// hasLetter = checks to see if the encryption key is currently in the decryptor object
	public boolean hasLetter(String encryption) {
		return letters.containsKey(encryption);
	}
	
	// hasMultiple - Checks if two letters in the decryptor object map to the same letter, if it does, return false
	public boolean hasMultiple() {
		String[] allLetters = letters.values().toArray(new String[letters.size()]);
		
		// Goes through each letter and compares it to other letters in the decryptor
		for (String l : allLetters) {
			int cnt = 0;
			// comparing the letter to every other letter, including itself
			for (int i = 0; i < allLetters.length; i++) {
				if(allLetters[i].equals(l)) cnt++;
			}
			// count > 1 is to account for the letter equaling itself
			if (cnt > 1) return false;
		}
		
		return  true;
	}
	
	// compareDecryptor - compares two decryptors to see if they match each other
	public boolean compareDecryptors(Decryptor decrypt) {
		// Goes through each letter of the given decryptor
		for (String l : decrypt.letters.keySet()) {
			// returns false if the letter from the given decryptor if the key is in the 
			// 		object and the two keys do not equal to the same decrypted letter
			if (letters.containsKey(l) && !(letters.get(l).equals(decrypt.letters.get(l)))) {
				return false;
			}
		}
		
		return true;
	}
	
	// concat - Concatonate two decryptors together, making a larger one with more decryption keys
	public void concact(Decryptor decrypt) {
		// Adds each entry in the given decryptor and adds it to this object if it is not already in the decryptor
		for (String l : decrypt.letters.keySet()) {
			if (!(letters.containsKey(l))) {
				letters.put(l, decrypt.letters.get(l));
			}
		}
	}
	
	// Solves the word using the decryptor's map
	public String solveWord(String cipher) {
		String[] arrayWord = cipher.split(""); // Splits the word into individual letters
		String word = ""; // Holds the decrypted word
		
		// Decryptes each letter in the word and adds it to the final word variable
		for (String l : arrayWord) {
			if (letters.containsKey(l)) l = letters.get(l);
			word += l;
		}
		
		return word;
	}
	
	// Solves the entire cryptogram by calling on solveWord for each word
	public String solveCryptogram(String cipher) {
		String[] arrayWord = cipher.split(" "); // Splits the cipher into words
		String plaintext = ""; // Holds the final solution
		
		// Solves by calling solveWord
		for (String w : arrayWord) {
			plaintext += (solveWord(w) + " ");
		}
		
		return plaintext;
	}

}
