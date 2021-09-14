package model.util;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Scanner;


/**
 * An abstract class that makes a certain number of verifications of user inputs
 * @author Maia
 *
 */
public abstract class DataChecker {

	private static String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";


	public static int checkPwdStrength(String pwd) {
		boolean hasLetter = false;
		boolean hasDigit = false;
		boolean hasSpecialChar = false;

		if (pwd.length() >= 8 && pwd.length() <= 50) {
			for (int i = 0; i < pwd.length(); i++) {
				char x = pwd.charAt(i);
				if (Character.isLetter(x)) {
					hasLetter = true;
				} else if (Character.isDigit(x)) {
					hasDigit = true;
				} else if (!Character.isDigit(x) && !Character.isLetter(x)) {
					hasSpecialChar = true;
				}

				if(hasLetter && hasDigit && hasSpecialChar){
					break;
				}
			}
			if (hasLetter && hasDigit && hasSpecialChar) {
				return 10;
			} else if (hasLetter && hasDigit || hasDigit && hasSpecialChar || hasSpecialChar && hasLetter) {
				return 6;
			} else {
				return 2;
			}
		} else {
			return 0;
		}
	}

	/**
	 * Checks if username only contains letters and numbers
	 * @param username
	 * @return true if username is valid, false otherwise
	 */
	public static boolean checkUsername(String username) {
		if (username.length() < 3) {
			return false;
		}

		for (int i = 0; i < username.length(); i++) {
			char x = username.charAt(i);
			if (!Character.isDigit(x) && !Character.isLetter(x)) {
				return false;
			}
		}

		return true;
	}
	
	/**
	 * Checks if birthday date is logic, meaning it is before today's date
	 * @param date
	 * @return true if valid, false otherwise
	 */
	public static boolean checkBirthdayDate(LocalDate date) {
		if (date == null || date.isAfter(LocalDate.now())) {
			return false;
		}
		
		return true;
	}


}
