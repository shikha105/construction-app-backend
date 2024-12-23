package com.citb.app.utils;

import java.util.Random;

public class RandomUtil {
	
	
	public static String randomIdentity(String IdentityName) {
		
		
		Random randomNumber = new Random();
		
		int randomInt = randomNumber.nextInt(1000) + 1;		
		return IdentityName + "-" + randomInt;
	}

}
