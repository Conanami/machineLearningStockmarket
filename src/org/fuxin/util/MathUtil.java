package org.fuxin.util;

public class MathUtil {

	public static boolean isBetween(Float x, Float a, Float b) {
		// TODO Auto-generated method stub
		if(x<=Max(a,b) && x>=Min(a,b)) return true;
		else
			return false;
	}

	
	public static Float Min(Float a, Float b) {
		// TODO Auto-generated method stub
		return (a<b?a:b);
	}

	public static Float Max(Float a, Float b) {
		// TODO Auto-generated method stub
		return (a>=b?a:b);
	}


	public static boolean isNear(Float x, Float a, Float percent) {
		// TODO Auto-generated method stub
		Float max = a*(1+percent/100);
		Float min = a*(1-percent/100);
		return isBetween(x,min,max);
	}

	
}
