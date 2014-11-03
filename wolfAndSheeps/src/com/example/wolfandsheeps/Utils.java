package com.example.wolfandsheeps;

import android.view.ViewGroup;

public final class Utils {
	private Utils(){
		
	}
	
	public static void setClickableForChildren(ViewGroup viewGroup, boolean clickable){
		for(int i = 0; i < viewGroup.getChildCount(); i++){
			viewGroup.getChildAt(i).setClickable(clickable);
			viewGroup.getChildAt(i).setFocusable(clickable);
		}
	}
}
