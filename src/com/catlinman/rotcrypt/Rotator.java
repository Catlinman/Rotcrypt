package com.catlinman.rotcrypt;

public class Rotator {
	private String lowercaseCharacters 	= "abcdefghijklmnopqrstuvwxyz";
	private String uppercaseCharacters 	= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private String numberCharacters 	= "1234567890";
	private String specialCharacters	= "\\[\\]!\"§$%&#_/()=?´`*+~@^'<>|,;.:-{}\\\\";
	private String localizedLowercaseCharacters	= "öäüæœøāåâáčçďēëéěêģīíîķļņňóôřšťūúůûýÿž";
	private String localizedUppercaseCharacters = "ÖÄÜÆŒØĀÅÂÁČÇĎĒËÉĚÊĢĪÍÎĶĻŅŇÓÔŘŠŤŪÚŮÛÝŸŽ";
	private String rotation = "14,11,98";
	
	private int rotationIndex = 13;
	private long[] rotationSet = null;
			
	public String rotate(String s, boolean reverse){
		String outputString = "";
		String mutatedString = "";
		rotationSet = createRotationArray(rotation);
		
		// The matches and mutations here are sorted by their chance to appear in a given string
		for(int i = 0; i < s.length(); i++){
			if((mutatedString = applyRotation(s, outputString, i, lowercaseCharacters, reverse)) != ""){
				outputString = mutatedString;
			} else if((mutatedString = applyRotation(s, outputString, i, uppercaseCharacters, reverse)) != ""){
				outputString = mutatedString;
			} else if((mutatedString = applyRotation(s, outputString, i, numberCharacters, reverse)) != ""){
				outputString = mutatedString;
			} else if((mutatedString = applyRotation(s, outputString, i, specialCharacters, reverse)) != ""){
				outputString = mutatedString;
			} else if(s.charAt(i) == ' '){
				outputString = outputString + ' ';
			} else if((mutatedString = applyRotation(s, outputString, i, localizedLowercaseCharacters, reverse)) != ""){
				outputString = mutatedString;
			} else if((mutatedString = applyRotation(s, outputString, i, localizedUppercaseCharacters, reverse)) != ""){
				outputString = mutatedString;
			} else{
				// System.out.println("Unknown character: " + Character.toString(s.charAt(i)));
				outputString = outputString + Character.toString(s.charAt(i));
			}
		}

		return outputString;
	}
	
	// Applies a rotation on a given string based on a matched input sequence. The set is an array containing the rotations. The set position is the rotation to be used.
	private String applyRotation(String input, String output, int inputIndex, String matchsequence, boolean reverse){
		long currentRotation = 13;
		
		try{
			rotationIndex++;
			currentRotation = rotationSet[rotationIndex] % matchsequence.length();
		} catch(ArrayIndexOutOfBoundsException e){
			rotationIndex = 0;
			currentRotation = rotationSet[rotationIndex] % matchsequence.length();
		}

		if(reverse) currentRotation = -currentRotation;

		String currentLetter = Character.toString(input.charAt(inputIndex));
		
		if(currentLetter.matches(".*["+matchsequence+"].*")){
			int letterIndex = matchsequence.indexOf(currentLetter);
			long rotatedIndex = letterIndex + currentRotation;

			if(rotatedIndex > matchsequence.length() - 1){
				rotatedIndex = letterIndex + currentRotation - matchsequence.length();
			}

			if(rotatedIndex < 0){
				rotatedIndex = letterIndex + currentRotation + matchsequence.length();
			}
			
			output = new StringBuilder()
					.append(output)
					.append(Character.toString(matchsequence.charAt((int) rotatedIndex))).toString();
			
			return output;
		} else{
			return "";
		}
	}
	
	public long[] createRotationArray(String s){
		String[] values = s.split(",");
		long[] set = new long[values.length];
		for(int i = 0; i < values.length; i++){
			try{
				set[i] = Long.parseLong(values[i]);
			} catch(NumberFormatException e){}
		}
		
		return set;
	}
	
	public void setRotation(String s){
		rotation = s;
	}
	
	public String getRotation(){
		return rotation;
	}
}
