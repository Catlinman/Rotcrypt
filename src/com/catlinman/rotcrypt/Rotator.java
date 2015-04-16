package com.catlinman.rotcrypt;

public class Rotator {
	// Match sequences
	private String lowercaseCharacters 	= "abcdefghijklmnopqrstuvwxyz";
	private String uppercaseCharacters 	= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private String numberCharacters 	= "1234567890";
	private String specialCharacters	= "\\[\\]!\"§$%&#_/()=?´`*+~@^'<>|,;.:-{}\\\\";
	private String localizedLowercaseCharacters	= "öäüæœøāåâáčçďēëéěêģīíîķļņňóôřšťūúůûýÿž";
	private String localizedUppercaseCharacters = "ÖÄÜÆŒØĀÅÂÁČÇĎĒËÉĚÊĢĪÍÎĶĻŅŇÓÔŘŠŤŪÚŮÛÝŸŽ";
	
	// A string containing the the information gathered from either the text boxes or the console rotation argument.
	private String rotation = "14,11,98";
	
	private int rotationIndex = -1; // The rotation index marks the currently used rotation in the rotation set.
	private long[] rotationSet = null; // Contains the set of rotations designated by the user.
	
	// This method is used to rotate a supplied string. The reverse boolean designates the direction of the rotation.
	public String rotate(String s, boolean reverse) {
		String outputString = "";
		String mutatedString = "";
		
		// Convert the rotation string to an array of long integers.
		rotationSet = createRotationArray(rotation);
		
		// The matches and mutations here are sorted by their chance to appear in a given string
		for(int i = 0; i < s.length(); i++) {
			if((mutatedString = applyRotation(s, outputString, i, lowercaseCharacters, reverse)) != "") {
				outputString = mutatedString;

			} else if((mutatedString = applyRotation(s, outputString, i, uppercaseCharacters, reverse)) != "") {
				outputString = mutatedString;

			} else if((mutatedString = applyRotation(s, outputString, i, numberCharacters, reverse)) != "") {
				outputString = mutatedString;

			} else if((mutatedString = applyRotation(s, outputString, i, specialCharacters, reverse)) != "") {
				outputString = mutatedString;

			} else if(s.charAt(i) == ' ') {
				outputString = outputString + ' ';

			} else if((mutatedString = applyRotation(s, outputString, i, localizedLowercaseCharacters, reverse)) != "") {
				outputString = mutatedString;

			} else if((mutatedString = applyRotation(s, outputString, i, localizedUppercaseCharacters, reverse)) != "") {
				outputString = mutatedString;

			} else {
				// System.out.println("Unknown character: " + Character.toString(s.charAt(i)));
				outputString = outputString + Character.toString(s.charAt(i));
			}
		}

		return outputString;
	}
	
	// Applies a rotation on a given string based on a matched input sequence. The set is an array containing the rotations.
	private String applyRotation(String input, String output, int inputIndex, String matchsequence, boolean reverse) {
		rotationIndex = -1; // We reset the rotation index so the output string always remains the same. 
		long currentRotation = 13; // Stores the current rotation.
		
		// Increment the rotation index and set the current rotation to the correct value from the rotation set.
		try {
			rotationIndex++;
			currentRotation = rotationSet[rotationIndex] % matchsequence.length();

		} catch(ArrayIndexOutOfBoundsException e) {
			rotationIndex = 0;
			currentRotation = rotationSet[rotationIndex] % matchsequence.length();
		}
		
		// Reverse the rotation if specified.
		if(reverse) currentRotation = -currentRotation;
		
		// Get the character of the input string from the supplied argument index.
		String currentLetter = Character.toString(input.charAt(inputIndex));
		
		// Check if the letter matches the supplied match sequence argument.
		if(currentLetter.matches(".*["+matchsequence+"].*")) {
			// Stores the position of the current letter relative to the match sequence.
			int letterIndex = matchsequence.indexOf(currentLetter);
			
			// Move the letter index by the previously designated rotation from the rotation set. 
			long rotatedIndex = letterIndex + currentRotation;
			
			// Wrap the rotated index value so it is still in the bounds of the match sequence argument. 
			if(rotatedIndex > matchsequence.length() - 1) {
				rotatedIndex = letterIndex + currentRotation - matchsequence.length();
			}

			if(rotatedIndex < 0) {
				rotatedIndex = letterIndex + currentRotation + matchsequence.length();
			}
			
			// Merge the rotated string with the input string. 
			output = new StringBuilder()
					.append(output)
					.append(Character.toString(matchsequence.charAt((int) rotatedIndex))).toString();
			
			return output;

		} else {
			return "";
		}
	}
	
	// Splits the rotation string into an array of long integers.
	public long[] createRotationArray(String s) {
		String[] values = s.split(",");
		long[] set = new long[values.length];

		for(int i = 0; i < values.length; i++) {

			try {
				set[i] = Long.parseLong(values[i]);

			} catch(NumberFormatException e) {}
		}
		
		return set;
	}
	
	// Getter and setter functions.
	public void setRotation(String s) {
		rotation = s;
	}
	
	public String getRotation() {
		return rotation;
	}
}
