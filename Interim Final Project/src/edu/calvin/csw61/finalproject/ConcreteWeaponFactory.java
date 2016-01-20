package edu.calvin.csw61.finalproject;

import edu.calvin.csw61.weapons.*;

public class ConcreteWeaponFactory extends WeaponFactory {
	
	ObjectInterface myKnifeObject;  //Weapons are also considered Objects, so we need a way
								   //to convert them to Objects
	//Create a Weapon
	public Weapon createWeapon(String name) {
		if(name.equals("knife")) {
			return new Knife();
		} else if(name.equals("sword")) {
			return new Sword();
		} else if(name.equals("shotgun")) {
			return new Shotgun();
		} else return null;
	}
	
}
