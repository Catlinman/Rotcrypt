package com.catlinman.rotcrypt;

public class Rotator {
	String letters = "abcdefghijklmnopqrstuvwxyz";
	String upperletters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	long rot = 13;
	String rotArray = ("14,11,98");

	boolean useArray = false;
	boolean debug = false;

	public String rotate(String s, boolean reverse){
		String rotatedString = "";

		int[] fixedRotArray = null;
		long currentRot = rot;
		int seedIndex = -1;

		if(!useArray){
			rot = rot % 26;
			currentRot = rot;
		}

		else if(useArray){
			String[] rotItems = rotArray.split(",");

			fixedRotArray = new int[rotItems.length];

			for(int i = 0; i < rotItems.length; i++){
				try{
					fixedRotArray[i] = Integer.parseInt(rotItems[i]);
				} catch(NumberFormatException e){}
			}
		}

		for(int i = 0; i < s.length(); i++){
			if(useArray){
				try{
					seedIndex++;
					currentRot = fixedRotArray[seedIndex] % 26;
				} catch(ArrayIndexOutOfBoundsException e){
					seedIndex = 0;
					currentRot = fixedRotArray[seedIndex] % 26;
				}
			}

			if(reverse)
				currentRot = -currentRot;

			String currentLetter = Character.toString(s.charAt(i));

			if(debug)
				System.out.print(currentLetter + " --> ");

			if(currentLetter.matches(".*[abcdefghijklmnopqrstuvwxyz].*")){
				int letterIndex = letters.indexOf(currentLetter);
				long rotatedIndex = letterIndex + currentRot;

				if(rotatedIndex > letters.length() - 1){
					rotatedIndex = letterIndex + currentRot - letters.length();
				}

				if(rotatedIndex < 0){
					rotatedIndex = letterIndex + currentRot + letters.length();
				}

				rotatedString = new StringBuilder()
						.append(rotatedString)
						.append(Character.toString(letters
								.charAt((int) rotatedIndex))).toString();

				if(debug)
					System.out.print(Character.toString(letters
							.charAt((int) rotatedIndex)) + "\n");
			}

			else if(currentLetter.matches(".*[ABCDEFGHIJKLMNOPQRSTUVWXYZ].*")){
				int letterIndex = upperletters.indexOf(currentLetter);
				long rotatedIndex = letterIndex + currentRot;

				if(rotatedIndex > letters.length() - 1){
					rotatedIndex = letterIndex + currentRot - letters.length();
				}

				if(rotatedIndex < 0){
					rotatedIndex = letterIndex + currentRot + letters.length();
				}

				rotatedString = new StringBuilder()
						.append(rotatedString)
						.append(Character.toString(upperletters
								.charAt((int) rotatedIndex))).toString();

				if(debug)
					System.out.print(Character.toString(upperletters
							.charAt((int) rotatedIndex)) + "\n");
			}

			else if(currentLetter.matches(".*[ ].*")){
				rotatedString = new StringBuilder().append(rotatedString)
						.append(" ").toString();
				if(debug)
					System.out.print("SPACE" + "\n");
			} else{
				rotatedString = new StringBuilder().append(rotatedString)
						.append(currentLetter).toString();
				if(debug)
					System.out.print(currentLetter + "\n");
			}
		}
		if(debug)
			System.out.print(rotatedString);
		return rotatedString;
	}

	public void setRot(long l){
		rot = l;
	}

	public long getRot(){
		return rot;
	}
}
