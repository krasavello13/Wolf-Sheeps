package com.example.wolfandsheeps;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	protected final int LEFT_NEIGHBOR       = 1;
	protected final int RIGHT_NEIGHBOR      = 2;
	protected final int LEFT_BACK_NEIGHBOR  = 3;
	protected final int RIGHT_BAKC_NEIGHBOR = 4;
	
	GameLogic gameLogic    = new GameLogic();
	int [] lastCoordinates = new int[5];
	boolean sheepStart     = false;
	boolean wolfMakeMove   = false;
	Point size; 
	ImageView[] image;
	GridLayout gl;
	
	int item;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		final int fullHeightScreen = size.y + getActionBar().getHeight(); 
		final int screenWidth = size.x;
		final int padding = (fullHeightScreen - screenWidth) / 3;
		
		gl = new GridLayout(MainActivity.this);
        gl.setBackground(getResources().getDrawable(R.drawable.background_game_feeld));
        gl.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        gl.setOrientation(0);
        gl.setColumnCount(8);
        gl.setRowCount(8);
        gl.setPadding(0, padding, 0, 0);
        image = new ImageView[64];
        gameLogic.createLogicFeeld(); 
        uiDrawBoard();
        showToast(this, getResources().getString(R.string.game_start));
        setContentView(gl);
        setOnClickListenerBoard();		
	}
    
    public void setOnClickListenerBoard(){
    	for(item = 0 ; item < image.length; item ++){
        	image[item].setOnClickListener(new View.OnClickListener() {
        		
        		int pos = item;
			
        		public void onClick(View v) {
        	
        			
        			switch (gameLogic.checkCell(getCoordinates(pos))) {
					case 1:
						if(!wolfMakeMove){
							if(lastCoordinates[0] != 0){
								uiSetCellBackground(0,0);
								lastCoordinates[LEFT_NEIGHBOR]  = 0;
								lastCoordinates[RIGHT_NEIGHBOR] = 0;
							}
							uiMakeStep(gameLogic.getNeighborsCell(getCoordinates(pos))[gameLogic.LEFT_NEIGHBOR], gameLogic.getNeighborsCell(getCoordinates(pos))[gameLogic.RIGHT_NEIGHBOR], 0, 0, R.drawable.contour_sheep, pos);
						
						}	
						break;
					case 2:
						if(sheepStart){
							uiSetCellBackground(0,0);
							uiMakeStep(gameLogic.getNeighborsCell(getCoordinates(pos))[gameLogic.LEFT_NEIGHBOR], gameLogic.getNeighborsCell(getCoordinates(pos))[gameLogic.RIGHT_NEIGHBOR], gameLogic.getNeighborsCell(getCoordinates(pos))[gameLogic.LEFT_BACK_NEIGHBOR], gameLogic.getNeighborsCell(getCoordinates(pos))[gameLogic.RIGHT_BAKC_NEIGHBOR], R.drawable.contour_wolf, pos);
						}
					break;	
					}
        			
        			if(pos == lastCoordinates[LEFT_NEIGHBOR] || pos == lastCoordinates[RIGHT_NEIGHBOR] || pos == lastCoordinates[LEFT_BACK_NEIGHBOR] || pos == lastCoordinates[RIGHT_BAKC_NEIGHBOR]){
        				switch (gameLogic.checkCell(getCoordinates(lastCoordinates[0]))) {
						case 1:
							uiSetCellBackground(pos, R.drawable.sheep);
							gameLogic.makeMove(getCoordinates(lastCoordinates[0]), getCoordinates(pos), gameLogic.SHEEP);
							
							sheepStart = true;
							wolfMakeMove = true;
							break;
						case 2:
							uiSetCellBackground(pos, R.drawable.wolf);
							gameLogic.makeMove(getCoordinates(lastCoordinates[0]), getCoordinates(pos), gameLogic.WOLF);
							sheepStart = false;
							wolfMakeMove = false;
							break;
						}
        				for(int i = 1; i < lastCoordinates.length; i++){
        					lastCoordinates[i] = 0;
        				}
        			}
        		
        			switch (gameLogic.isWin()) {
					case 1:
						showToast(MainActivity.this, getResources().getString(R.string.sheep_win));
						break;
					case 2:
						showToast(MainActivity.this, getResources().getString(R.string.wolf_win));
						break;
					}
        		}
			});
		}
    }
    
    public void showToast(Activity activity, String text){
		Toast toast = Toast.makeText(activity, text, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM, 10, 50);
		toast.show();
	}
    
    public void uiDrawBoard() {
    	size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size); 
		final int screenWidth = size.x;
		final int cellWidth = screenWidth / 8;
		int count = 0;
		for (int i = 0; i < image.length; i++) {

			image[i] = new ImageView(MainActivity.this);
			image[i].setLayoutParams(new LayoutParams(cellWidth, cellWidth));

			if (i == 3) {
				image[i].setImageDrawable(getResources().getDrawable(R.drawable.wolf));
			}
			if (i % 8 == 0) {
				count++;
			}
			if (count % 2 == 0) {
				if (i % 2 == 0) {
					image[i].setBackgroundColor(Color.BLACK);
				} else {
					image[i].setBackgroundColor(Color.WHITE);
				}
			} else {
				if (i % 2 == 0) {
					image[i].setBackgroundColor(Color.WHITE);
				} else {
					image[i].setBackgroundColor(Color.BLACK);
				}
			}

			if (count == 8) {
				if (i % 2 == 0) {
					image[i].setImageDrawable(getResources().getDrawable(R.drawable.sheep));
				}
			}

			image[i].setPadding(15, 10, 15, 10);
			gl.addView(image[i]);
		}
	}
    
    public void  uiSetCellBackground(int pos, int idCercle){
    	if(pos != 0 && idCercle != 0){
    		image[lastCoordinates[0]].setImageDrawable(getResources().getDrawable(R.drawable.backgroud));
			image[pos].setImageDrawable(getResources().getDrawable(idCercle));
    	}
    	image[lastCoordinates[0]].setBackground(getResources().getDrawable(R.drawable.backgroud));
		if(lastCoordinates[1] != 0){
			image[lastCoordinates[1]].setBackground(getResources().getDrawable(R.drawable.backgroud));
		}
		if(lastCoordinates[2] != 0){
			image[lastCoordinates[2]].setBackground(getResources().getDrawable(R.drawable.backgroud));
		}
		if(lastCoordinates[3] != 0){
			image[lastCoordinates[3]].setBackground(getResources().getDrawable(R.drawable.backgroud));
		}
		if(lastCoordinates[4] != 0){
			image[lastCoordinates[4]].setBackground(getResources().getDrawable(R.drawable.backgroud));
		}
    }
    
    public void uiMakeStep(int leftNeighbor, int rightNeighbor, int leftBackNeighbor, int rightBackNeighbor, int idIMG, int pos){
    	image[pos].setBackground(getResources().getDrawable(idIMG));
		lastCoordinates[0] = pos;
		if(leftNeighbor != 0){
			image[leftNeighbor].setBackground(getResources().getDrawable(idIMG));
			lastCoordinates[1] = leftNeighbor;
		}
		if(rightNeighbor != 0){
			image[rightNeighbor].setBackground(getResources().getDrawable(idIMG));
			lastCoordinates[2] = rightNeighbor;
		}
		if(leftBackNeighbor != 0){
			image[leftBackNeighbor].setBackground(getResources().getDrawable(idIMG));
			lastCoordinates[3] = leftBackNeighbor;
		}
		if(rightBackNeighbor != 0){
			image[rightBackNeighbor].setBackground(getResources().getDrawable(idIMG));
			lastCoordinates[4] = rightBackNeighbor;
		}
    }
    
    public int[] getCoordinates(int position) {
		int[] IandJ = new int[2];
		IandJ[0] = position / 8;
		IandJ[1] = position % 8;
		return IandJ;
	}
        
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.restart_game:
			gl.removeAllViews();
			uiDrawBoard();
			gameLogic.createLogicFeeld();
			setOnClickListenerBoard();
			for(int i = 0; i < lastCoordinates.length; i ++){
				lastCoordinates[i] = 0;
			}
			sheepStart     = false;
			wolfMakeMove   = false;
			return true;
		case R.id.exit:
			finish();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}

