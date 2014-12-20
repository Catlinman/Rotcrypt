package com.catlinman.rotcrypt;

public class Rotator {
	String lowercaseCharacters 	= "abcdefghijklmnopqrstuvwxyz";
	String uppercaseCharacters 	= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	String numberCharacters 	= "1234567890";
	String specialCharacters	= "!\"§$%&/()=?´`*+~@^'<>|,;.:-";

	String rotation = "14,11,98";
	
	public String rotate(String s, boolean reverse){
		String outputString = "";

		int rotationIndex = -1;
		long[] rotationSet = createRotationArray(rotation);
		
		for(int i = 0; i < s.length(); i++){
			String out;
			if((out = applyRotation(s, outputString, lowercaseCharacters, rotationIndex, rotationSet, i, reverse)) != ""){
				outputString = out;
			} else if((out = applyRotation(s, outputString, uppercaseCharacters, rotationIndex, rotationSet, i, reverse)) != ""){
				outputString = out;
			} else if((out = applyRotation(s, outputString, numberCharacters, rotationIndex, rotationSet, i, reverse)) != ""){
				outputString = out;
			} else if((out = applyRotation(s, outputString, specialCharacters, rotationIndex, rotationSet, i, reverse)) != ""){
				outputString = out;
			} else if((out = applyRotation(s, outputString, " ", rotationIndex, rotationSet, i, reverse)) != ""){
				outputString = out;
			} else if((out = applyRotation(s, outputString, "*", rotationIndex, rotationSet, i, reverse)) != ""){
				outputString = out;
			}
		}

		return outputString;
	}
	
	private String applyRotation(String input, String output, String sequence, int setPosition, long[] set, int inputPosition, boolean reverse){
		long currentRotation = 13;
		
		try{
			setPosition++;
			currentRotation = set[setPosition] % sequence.length();
		} catch(ArrayIndexOutOfBoundsException e){
			setPosition = 0;
			currentRotation = set[setPosition] % sequence.length();
		}

		if(reverse) currentRotation = -currentRotation;

		String currentLetter = Character.toString(input.charAt(inputPosition));
		
		if(currentLetter.matches(".*["+sequence+"].*")){
			int letterIndex = sequence.indexOf(currentLetter);
			long rotatedIndex = letterIndex + currentRotation;

			if(rotatedIndex > sequence.length() - 1){
				rotatedIndex = letterIndex + currentRotation - sequence.length();
			}

			if(rotatedIndex < 0){
				rotatedIndex = letterIndex + currentRotation + sequence.length();
			}
			
			output = new StringBuilder()
					.append(output)
					.append(Character.toString(sequence.charAt((int) rotatedIndex))).toString();
			
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
