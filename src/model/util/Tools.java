package model.util;

import model.users.MemberType;

/**
 * A class containing useful methods to be used anywhere
 * @author Maia
 *
 */
public class Tools {

	public static boolean isBetween(int x, int lower, int upper) {
		return lower <= x && x <= upper;
	}
	
	public static MemberType getMemberTypeFromString(String type) {
		switch (type.toUpperCase()){
		case "REGULAR": 
			return MemberType.REGULAR;
		case "SENIOR":
			return MemberType.SENIOR;
		case "CHILDREN":
			return MemberType.CHILDREN;
		case "STAFF":
			return MemberType.STAFF;
		default:
			throw new IllegalArgumentException("Unexpected value: " + type.toUpperCase());
		}
	}

}
