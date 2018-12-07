package com.restreview.etc;

import java.util.Random;

public class RandomPasswordGenerator implements PasswordGenerator{

	@Override
	public String generate() {
		// TODO Auto-generated method stub
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder("");
		for (int i=0;i<6;i++) {
		
			int indx = rnd.nextInt(26);
			sb.append((char)((int)'a'+indx));
		}
		for(int i=0;i<4;i++) {
			int indx = rnd.nextInt(10);
			sb.append((char)((int)'0'+indx));
		}
		return sb.toString();
	}

}
