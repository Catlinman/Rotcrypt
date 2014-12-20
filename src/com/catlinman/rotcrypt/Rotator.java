package com.catlinman.rotcrypt;

public class Rotator {
	String lowercaseCharacters 	= "abcdefghijklmnopqrstuvwxyz";
	String uppercaseCharacters 	= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	String rotation = "14,11,98";
	
	public String rotate(String s, boolean reverse){
		String outputString = "";

		long currentRotation = 13;
		int rotationIndex = -1;

		long[] rotationSet = createRotationArray(rotation);
		
		for(int i = 0; i < s.length(); i++){
			try{
				rotationIndex++;
				currentRotation = rotationSet[rotationIndex] % lowercaseCharacters.length();
			} catch(ArrayIndexOutOfBoundsException e){
				rotationIndex = 0;
				currentRotation = rotationSet[rotationIndex] % lowercaseCharacters.length();
			}

			if(reverse) currentRotation = -currentRotation;

			String currentLetter = Character.toString(s.charAt(i));
			
			if(currentLetter.matches(".*[abcdefghijklmnopqrstuvwxyz].*")){
				int letterIndex = lowercaseCharacters.indexOf(currentLetter);
				long rotatedIndex = letterIndex + currentRotation;

				if(rotatedIndex > lowercaseCharacters.length() - 1){
					rotatedIndex = letterIndex + currentRotation - lowercaseCharacters.length();
				}

				if(rotatedIndex < 0){
					rotatedIndex = letterIndex + currentRotation + lowercaseCharacters.length();
				}
				
				outputString = new StringBuilder()
						.append(outputString)
						.append(Character.toString(lowercaseCharacters.charAt((int) rotatedIndex))).toString();
			}

			else if(currentLetter.matches(".*[ABCDEFGHIJKLMNOPQRSTUVWXYZ].*")){
				int letterIndex = uppercaseCharacters.indexOf(currentLetter);
				long rotatedIndex = letterIndex + currentRotation;

				if(rotatedIndex > lowercaseCharacters.length() - 1){
					rotatedIndex = letterIndex + currentRotation - lowercaseCharacters.length();
				}

				if(rotatedIndex < 0){
					rotatedIndex = letterIndex + currentRotation + lowercaseCharacters.length();
				}

				outputString = new StringBuilder()
						.append(outputString)
						.append(Character.toString(uppercaseCharacters.charAt((int) rotatedIndex))).toString();
			}

			else if(currentLetter.matches(".*[ ].*")){
				outputString = new StringBuilder().append(outputString).append(" ").toString();
			} else{
				outputString = new StringBuilder().append(outputString).append(currentLetter).toString();
			}
		}

		return outputString;
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
