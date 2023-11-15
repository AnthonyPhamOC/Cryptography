package cryptogramPD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Cryptogram {
	private String cryptogram;		// Holds the ciphertext
	private String oneLetterWords;	// Keeps track of one letter words such as A and I
	private String answer;			// Holds the plaintext
	private String[] words;			// Holds the words in the ciphertext in an array
	private Decryptor decryptionKey; // The decryption key
	private ArrayList<String> thesaurus = new ArrayList<String>(); // holds the words used to decrypt the cipher
	
	// Default Constructor
	public Cryptogram() {
		cryptogram = "";
		answer = "";
		oneLetterWords = null;
		words = null;
		decryptionKey = null;
		// Reads the text file and puts it into thesaurus
		try {
			String word; // The individual word to be added to thesaurus
			FileReader fileReader = new FileReader("webster.txt");
			BufferedReader reader = new BufferedReader(fileReader);
			
			// Adds each word to the list
			while ((word = reader.readLine()) != null) {
				thesaurus.add(word.toUpperCase());
			}
			
			reader.close();
		}
		catch (IOException ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}
	
	// Constructor with parameters
	public Cryptogram(String cryptogram) {
		// Calls on default constructor for variables and word list
		this();
		// Takes parameter and sets it to object variable
		this.cryptogram = cryptogram;
		// Sets words in the words object, removes punctuation before testing
		words = removePunct(cryptogram.split(" "));
	}
	
	// Retrieves ciphertext
	public String getCryptogram() {
		return cryptogram;
	}
	
	// Sets ciphertext
	public void setCryptogram(String cryptogram) {
		this.cryptogram = cryptogram;
		words = removePunct(cryptogram.split(" "));
	}
	
	// Gets oneLetterWords
	public String getOneLetterWords() {
		return oneLetterWords;
	}
	
	// Sets one letter words
	public void setOneLetterWords(String letter) {
		this.oneLetterWords = letter;
	}
	
	// Retrieves the answer
	public String getAnswer() {
		return answer;
	}
	
	// Sets the answer (Pointless, I know, It just felt right to have a getter and setter)
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	// decryptCipher - unencrypts the given cipher
	public String decryptCipher() {
		String[] encryptedWords = sortWords(words); // Sorts words by length
		ArrayList<Decryptor> decryptionKeys = new ArrayList<Decryptor>(); // Lists of possible decryption maps
		
		// Goes through each word and creats matches to test
		for (String word : encryptedWords) {
			// Creates matches based on the pattern of the word
			int[] pattern = getPattern(word);
			ArrayList<String> matches = getMatches(word, pattern);
			
			// Usually the first option, goes through each match and makes a decryptor to map
			if (decryptionKeys.isEmpty()) {
				// Iterates to each match to make a decryptor key
				for (String m : matches) {
					Decryptor key = new Decryptor();
					for (int i = 0; i < word.length(); i++) {
						key.decryptLetter(word.substring(i, i + 1), m.substring(i, i + 1));
					}
					decryptionKeys.add(key);
					// If the one letter words are not "A" or "I", it removes the decryptor key set to save time
					if (key.hasLetter(oneLetterWords)) {
						// Remove any key that does not have single letter words as "a" or "i"
						if (!(key.getLetter(oneLetterWords).equals("I") || key.getLetter(oneLetterWords).equals("A"))) decryptionKeys.remove(key);
					}
				}
				
				// Take all of the created keys (for the word) and makes a complete decryption set using only the keys from words that work together
				for (Decryptor key : decryptionKeys) {
					String[] newWordList = Arrays.copyOfRange(encryptedWords, 1, encryptedWords.length);
					// Solve the cryptogram using recursion
					// If the result is false, then the next key is tested
					if (testKeys(key, newWordList)) {
						answer = decryptionKey.solveCryptogram(cryptogram);
						// will return after finding the FIRST working decryption
						return answer;
					}
				}
			}
			// If there is only one decryptor key, then that must be the one
			else if (decryptionKeys.size() == 1) {
				break;
			}
			// If there are more than one, this block of code is run, informing the user
			else {
                System.out.println("Possible solutions: " + decryptionKeys.size());
			}
		}
		
		// If there is no solution and the number is greater than one, it will tell the user how many options 
		// 		there are and give the decryption of the first one on the list
		if (decryptionKeys.size() > 1) {
			System.out.println("Could not find one solution, " + decryptionKeys.size() + " possible solutions");
			answer = decryptionKeys.get(0).solveCryptogram(cryptogram);
		}
		return answer; // Might need to change later
	}
	
	// Calls on the testKeys function recursivley to solve cryptogram
	// The method will take the first word and create mathces to see if the given key works with the match
	//		If it works, it is added to the decryptor. The method is called again with the new decryptor set and a shortened word list by one
	// 		By shortening the word list by one each time, the method is able to iterate through each word when testing.
	// 		If the word list is equal to one, that means that it has gotten through the entire wordlist with a elgible decryptor key set
	//		If it did not work, the method will pass false and the recursion will go up one level to try the next match and decryptor key set
	private boolean testKeys(Decryptor key, String[] words) {
		// End of recurssion
		if (words.length == 1) {
			decryptionKey = key;
			return true;
		}

		// Gets the first word in the wordlist to do matching
		String wordToTest = words[0];
		// Gets the patter and finds matches based on the patter
		int[] pattern = getPattern(wordToTest);
		ArrayList<String> matches = getMatches(wordToTest, pattern);
		
		// Iterates through each match
		for (String m : matches) {
			// Creates a key to test if it works with the given key set
			Decryptor testKey = new Decryptor();
			for (int i = 0; i < wordToTest.length(); i++) {
				testKey.decryptLetter(wordToTest.substring(i, i+1), m.substring(i, i+1));
			}
			
			// Compares key sets
			if(testKey.compareDecryptors(key)) {
				testKey.concact(key);
				String[] newWordList = Arrays.copyOfRange(words, 1, words.length);
				// After concatonating the key being tested to the key set,
				//		the function is called again to test the next batch until there is one word left
				if (testKeys(new Decryptor(testKey), newWordList)) return true;
			}
		}
		// If all of the recursions fail, then the initial key passed to the first
		//		testKey method did not work. So another match in the level above will be tested
		return false;
	}
	
	// getMatches - Finds words that are the same length as the given encryption and takes only the words that have the same pattern
	private ArrayList<String> getMatches(String encryption, int[] pattern){
		String[] equalLengthWords = findWords(encryption.length()); // Gets words that have the same length as the encrypted word
		ArrayList<String> matches = new ArrayList<String>(); // Arraylist that stores the possible matches
		
		// Iterates through each word to see which one has the same pattern
		for (String word : equalLengthWords) {
			int[] wordPattern = getPattern(word);
			// Checks to see if the pattern for the word given matches with the pattern passed to the method
			//		if it does, the word is added to possible matches (matches)
			if (Arrays.equals(wordPattern, pattern)) matches.add(word);
		}
		
		return matches;
	}
	
	// Finds the patterns in the words
	// Cryptograms only substitute letters, the order stays the same
	//		this means gbbu could mean meet or book
	//		by finding a pattern, the program can compare it to words with the same pattern
	private int[] getPattern(String word) {
		// Creates a map used to save the pattern
		Map<Character, Integer> patternMap = new HashMap<Character, Integer>();
		int[] pattern = new int[word.length()]; // Holds the final pattern to pass
		
		// Iterates through each letter of the word given
		for (int i = 0; i < word.length(); i++) {
			char letter = word.charAt(i);
			// If the letter is already in the map, it puts that integer into the pattern
			if (patternMap.containsKey(letter)) {
				pattern[i] = patternMap.get(letter);
			}
			// If the letter is not in the map, it is added to both the pattern and the map 
			else {
				patternMap.put(letter, i);
				pattern[i] = i;
			}
		}
		
		return pattern;
	}
	
	// getLongestWord - Returns the longest word in the given array of Strings
	private String getLongestWord(String[] arrayWord) {
		int wordLength = 0; // Set the initial longest word to 0
		String longestWord = ""; // Stores the longest word to compare
		
		// Iterates through each word in the array
		for (String word : arrayWord) {
			// If the current word is longer, set it as the new longest word
			if (word.length() > wordLength) {
				longestWord = word;
				wordLength = word.length();
			}
		}
		
		return longestWord;
	}
	
	// findWords - Finds words that have the same length as the given integer
	private String[] findWords(int length) {
		ArrayList<String> words = new ArrayList<String>(); // Holds all the words that have the same length of the given integer
		// Iterates through each word in the word list
        for (String word : thesaurus) {
        	// If the lengths matches, add it to the word list
            if (word.length() == length)
            	words.add(word);
        }
        // Returns the word list as an array instead of an ArrayList
        return words.toArray(new String[words.size()]);
	}
	
	// sortWords - Sorts all the words in the given array by length
	private String[] sortWords(String[] arrayWord) {
		String word = ""; // Holds the word that is being tested
		String[] sortedWords = new String[arrayWord.length]; // Stores the sorted array
		ArrayList<String> words = new ArrayList<String>(); // List of words being sorted
		
		// Adds all of the words to the ArrayList to be sorted
		words.addAll(Arrays.asList(arrayWord));
		
		// Goes through each word in the array and sorts them by length
		for (int i = 0; i < arrayWord.length; i++) {
			// Gets the longest word in the list to place in the right position
			word = getLongestWord(words.toArray(new String[words.size()]));
			// If the word is only one letter, it is stored to use for decrypting
			if (word.length() == 1) oneLetterWords = word;
			sortedWords[i] = word; // Adds the word to the list in the proper position
			words.remove(word); // Removes the word from the list so the next largest can be retrieved
		}
		
		return sortedWords;
	}
	
	// removePunct - Removes punctuation from the cipher to do decryptions
		private String[] removePunct(String[] words) {
			String exceptions = "!?.-,;()\"'—1234567890";
			// Wordlist that will store all of the words that will be passed
			ArrayList<String> wordList = new ArrayList<String>();
			// Iterates through each word
			for (String w : words) {
				String tempWord = ""; // Temp variable that will hold the word as it is being built
				String[] letters = w.split(""); // Splits the word into letters
				for (int i = 0; i < letters.length; i++) {
					// If there is any punctuation ("'" "." or ",") it is removed by not being added to the tempWord
					if (exceptions.indexOf(letters[i]) == -1) tempWord += letters[i];	
				}
				wordList.add(tempWord);
			}
			
			return wordList.toArray(new String[wordList.size()]);
		}
	
}
