package edu.calvin.csw61.finalproject.playercommands;

import edu.calvin.csw61.finalproject.Key;
import java.util.ArrayList;

import edu.calvin.csw61.finalproject.ApertureBehavior;
import edu.calvin.csw61.finalproject.Player;
import edu.calvin.csw61.finalproject.Door;

public class Unlock implements Command {
	
	Player myPlayer;
	Key myKey;
	private String myDirection;
	private String result;
	
	//Door myDoor;
	
	public Unlock(Player p, String direction) { //Door door
		myPlayer = p;
		myKey = new Key("key");
		myDirection = direction;
		result = "";
	}
	
	@Override
	public void execute() {
		myDirection.toLowerCase(); //just in case
		if(myDirection.equals("door")){ //if the user tries to unlock a generic door
			result = "Please enter the door direction.";
		} else if (myPlayer.hasItem(myKey.getName())){
			switch (myDirection){ //check the direction
			case "north":	//north door
				if(myPlayer.getRoom().isNorthDoor()){ //if there is a north door
					Door door = myPlayer.getRoom().getDoor("north"); //get it from the array
					if(!door.isLocked()){	//if it's unlocked
						result = "Door is already unlocked!"; //tell user
					} else {
						door.setUnlocked(); //else unlock the door
						result = "The door is now unlocked."; //tell user
						myPlayer.removeObject("key"); //and remove the key
					}
				} else {
					result = "You can't unlock that."; //if there is no door, tell user
				}
				break;
			case "south":
				if(myPlayer.getRoom().isSouthDoor()){
					Door door = myPlayer.getRoom().getDoor("south");
					if(!door.isLocked()){
						result = "Door is already unlocked!";
					} else {
						door.setUnlocked();
						result = "The door is now unlocked.";
						//remove key
						myPlayer.removeObject("key");
					} 
				} else {
					result = "You can't unlock that."; //if there is no door, tell user
				}
				break;
			case "east":
				if(myPlayer.getRoom().isEastDoor()){
					Door door = myPlayer.getRoom().getDoor("east");
					if(!door.isLocked()){
						result = "Door is already unlocked!";
					} else {
						door.setUnlocked();
						result = "The door is now unlocked.";
						//remove key
						myPlayer.removeObject("key");
					}
				} else {
					result = "You can't unlock that."; //if there is no door, tell user
				}
				break;
			case "west":
				if(myPlayer.getRoom().isWestDoor()){
					Door door = myPlayer.getRoom().getDoor("west");
					if(!door.isLocked()){
						result = "Door is already unlocked!";
					} else {
						door.setUnlocked();
						result = "The door is now unlocked.";
						//remove key
						myPlayer.removeObject("key");
					}
				} else {
					result = "You can't unlock that."; //if there is no door, tell user
				}
				break;
			default: result = "Cannot unlock that.";
			}
		} else {
			result = "You don't have a key.";
		}
	}
	
	public String getResult() {
		return result;
	}
	
}
