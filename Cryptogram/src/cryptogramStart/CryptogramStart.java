package cryptogramStart;

import cryptogramPD.Cryptogram;
import cryptogramUI.cryptogramFrame;

// Starts the program
public class CryptogramStart {
	public static void main(String[] args) {
		// Commented Out code runs the deciphers the cryptogram in the cryptogram object below and prints results to console
//        Cryptogram encryption = new Cryptogram("VMKJ, WCRBVDPCXL MXJ I'D JDYQVKDI. 1234");
//        encryption.decryptCipher();
//        System.out.println(encryption.getAnswer());
		
		// Opens the window that has the Cryptogram solver
		Cryptogram crypto = new Cryptogram();
        cryptogramFrame.start(crypto);
    }
}
