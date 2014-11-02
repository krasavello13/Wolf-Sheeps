package com.example.wolfandsheeps;

import android.util.Log;

public class GameLogic {
	protected int[][] feeld = new int[8][8];
	
	protected final int SHEEP 			   = 1;
	protected final int WOLF  			   = 2;
	protected final int FREE_CELL 		   = 0;
	
	protected final int LEFT_NEIGHBOR       = 0;
	protected final int RIGHT_NEIGHBOR      = 1;
	protected final int LEFT_BACK_NEIGHBOR  = 2;
	protected final int RIGHT_BAKC_NEIGHBOR = 3;
	
	public void createLogicFeeld() {
		for (int i = 0; i < feeld.length; i++) {
			for (int j = 0; j < feeld[i].length; j++) {
				feeld[i][j] = FREE_CELL;
				if(i == 7){
					if(j % 2 == 0){
						feeld[i][j] = SHEEP;
					}
				}
			}
		}
		feeld[0][3] = WOLF;
	}

	public int getPosition(int i, int j) {
		return i * 8 + j;
	}

	public int checkCell(int [] IandJ){
		int cell = FREE_CELL;
		switch(feeld[IandJ[0]][IandJ[1]]){
		case 1:
			cell = SHEEP;
			break;
		case 2:
			cell = WOLF;
			break;	
		}
		return cell;
	}
	
	public boolean [] existCell(int i, int j){
		boolean [] existCell = new boolean [4];
		for(int z = 0; z < existCell.length; z++){
			existCell[z] = false;
		} 
		
		if(i - 1 >= 0 && j - 1 >= 0){
			existCell[LEFT_NEIGHBOR]       = true;
		}
		if(i - 1 >= 0 && j + 1 < feeld.length){
			existCell[RIGHT_NEIGHBOR]      = true;
		}
		if(i + 1 < feeld.length && j - 1 >= 0){
			existCell[LEFT_BACK_NEIGHBOR]  = true;
		}
		if(i + 1 < feeld.length && j + 1 < feeld.length){
			existCell[RIGHT_BAKC_NEIGHBOR] = true;
		}
		return existCell; 
	}
	
	public int [] getNeighborsCell(int [] IandJ) {
		int [] neighbors = new int [4];
		boolean [] existCell = existCell(IandJ[0], IandJ[1]);
		for(int i = 0; i < neighbors.length; i++){
			neighbors[i] = FREE_CELL;
		}
		
		if(existCell[LEFT_NEIGHBOR] && feeld[IandJ[0] - 1][IandJ[1] - 1] == FREE_CELL){
			neighbors[LEFT_NEIGHBOR] = getPosition(IandJ[0] - 1, IandJ[1] - 1); 
		}	
		
		if(existCell[RIGHT_NEIGHBOR] && feeld[IandJ[0] - 1][IandJ[1] + 1] == FREE_CELL){
			neighbors[RIGHT_NEIGHBOR] = getPosition(IandJ[0] - 1, IandJ[1] + 1); 
		}
	
		if(existCell[LEFT_BACK_NEIGHBOR] && feeld[IandJ[0] + 1][IandJ[1] - 1] == FREE_CELL){
			neighbors[LEFT_BACK_NEIGHBOR] = getPosition(IandJ[0] + 1, IandJ[1] - 1); 
		}
		
		if(existCell[RIGHT_BAKC_NEIGHBOR] && feeld[IandJ[0] + 1][IandJ[1] + 1] == FREE_CELL){
			neighbors[RIGHT_BAKC_NEIGHBOR] = getPosition(IandJ[0] + 1, IandJ[1] + 1); 
		}
		
		return neighbors;
	}
	
	public void makeMove(int [] from, int [] to, int person) {
		
		if (feeld[to[0]][to[1]] == FREE_CELL) {
			feeld[from[0]][from[1]] = FREE_CELL;
			feeld[to[0]][to[1]]     = person;
		}
	}

	public int [] lastRow(int [][] feeld){
		int [] lastRow = new int [2];
		lastRow[0] = -1;
		lastRow[1] = -1;
		for(int i = 0; i < feeld.length; i++){
			for(int j = 0; j < feeld.length; j++){
				if(feeld[i][j] == SHEEP){
					if(lastRow[0] < i){
						Log.d("DEBUG", "Last Row Sheep= " + i);
						lastRow[0] = i;	
					}
				}
				if(feeld[i][j] == WOLF){
					if(lastRow[1] < i){
						Log.d("DEBUG", "Last Row Wolf= " + i);
						lastRow[1] = i;	
					}
				}
			}
		}
		return lastRow ;
	}
	
	public int isWin() {
		int[] IandJ = new int[2];		 
		int count   = 0;
		for(int row = 0; row < feeld.length; row++){
			for(int column = 0; column < feeld[row].length; column++){
				if(feeld[row][column] == WOLF){
					IandJ[0] = row;
					IandJ[1] = column;
					boolean [] existCell = existCell(IandJ[0], IandJ[1]);
					if(IandJ[0] == 0 || IandJ[1] == 0 || IandJ[1] == (feeld.length - 1)){
						
						if(existCell[LEFT_NEIGHBOR]  && feeld[IandJ[0] - 1][IandJ[1] - 1] == SHEEP){
							count ++;
						}	
						if(existCell[RIGHT_NEIGHBOR] && feeld[IandJ[0] - 1][IandJ[1] + 1] == SHEEP){
							count ++;
						}
						if(existCell[LEFT_BACK_NEIGHBOR]  && feeld[IandJ[0] + 1][IandJ[1] - 1] == SHEEP){
							count ++;	
						}	
						if(existCell[RIGHT_BAKC_NEIGHBOR] && feeld[IandJ[0] + 1][IandJ[1] + 1] == SHEEP){
							count ++;
						}
						if(count == 2){
							return SHEEP;
						}
					}else{
						if(existCell[LEFT_NEIGHBOR]  && feeld[IandJ[0] - 1][IandJ[1] - 1] == SHEEP){
							count ++;
						}	
						if(existCell[RIGHT_NEIGHBOR] && feeld[IandJ[0] - 1][IandJ[1] + 1] == SHEEP){
							count ++;
						}
						if(existCell[LEFT_BACK_NEIGHBOR]  && feeld[IandJ[0] + 1][IandJ[1] - 1] == SHEEP){
							count ++;	
						}	
						if(existCell[RIGHT_BAKC_NEIGHBOR] && feeld[IandJ[0] + 1][IandJ[1] + 1] == SHEEP){
							count ++;
						}
						if(count == 4){
							return SHEEP;
						}
					}
				}
				lastRow(feeld);
				if(lastRow(feeld)[0] == lastRow(feeld)[1]){
					return WOLF;
				}
			}
		}
		
		return 0;
	}
}