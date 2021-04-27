package sn.example.demo.utils;

public class TokenGenerator {
	private static final int RADIX = 62;
	private static final String CODEC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final long TIME = 50 * 60 * 60;
	
	public static String getToken() {
		long plain = System.currentTimeMillis() % TIME;
		StringBuffer sb = new StringBuffer();
		while(plain > 0) {
			sb.append(CODEC.charAt((int) (plain % RADIX)));
			plain /= RADIX;
		}
		return sb.toString();
	}
}