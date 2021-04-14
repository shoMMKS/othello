package src.modules.othello;

import java.util.*;
import java.util.Map.Entry;

class Square {
	private int id;
	private boolean isPlaced;
	private boolean isBlack;
	private boolean isTurn;
	private HashMap<String, Square> nexts = new HashMap<>();

	Square(int id, boolean isPlaced, boolean isBlack, boolean isTurn) {
		this.id = id;
		this.isPlaced = isPlaced;
		this.isBlack = isBlack;
		this.isTurn = isTurn;
	}

	public int getId() {
		return this.id;
	}

	public boolean getIsPlaced() {
		return this.isPlaced;
	}

	public boolean getIsBlack() {
		return this.isBlack;
	}

	public boolean getIsTurn() {
		return this.isTurn;
	}

	public void setNexts(String key, Square value) {
		this.nexts.put(key, value);
	}

	public boolean isTurnOver(boolean isBlack, String nextKey) {
		Square next = this.nexts.get(nextKey);
		if (next == null || !next.isPlaced) {
			return false;
		}
		if (next.isBlack == isBlack) {
			return true;
		}
		return next.isTurnOver(isBlack, nextKey);
	}

	public void turnOver(boolean isBlack, String nextKey) {
		this.isBlack = isBlack;
		Square next = this.nexts.get(nextKey);
		if (next.isPlaced && next.isBlack != isBlack) {
			next.turnOver(isBlack, nextKey);
		}
	}

	public void put(boolean isBlack) {
		this.isPlaced = true;
		this.isBlack = isBlack;
		for (Entry<String, Square> entry : this.nexts.entrySet()) {
			Square value = entry.getValue();
			if (value == null || !value.isPlaced || value.isBlack == isBlack) {
				continue;
			}
			if (value.isTurnOver(isBlack, entry.getKey())) {
				value.turnOver(isBlack, entry.getKey());
			}
		}
	}

	public boolean turnOverCheck(boolean isBlack) {
		if (this.isPlaced) {
			this.isTurn = false;
			return false;
		}
		for (Entry<String, Square> entry : this.nexts.entrySet()) {
			Square value = entry.getValue();
			if (value == null || !value.isPlaced || value.isBlack == isBlack) {
				continue;
			}
			if (value.isTurnOver(isBlack, entry.getKey())) {
				this.isTurn = true;
				return true;
			}
		}
		this.isTurn = false;
		return false;
	}
}

public class OthelloModel {
	boolean isBlack = true;
	boolean isFinished = false;
	int blackCount = 2;
	int whiteCount = 2;
	List<Square> squares = new ArrayList<>();

	OthelloModel() {
		List<Integer> placesList = Arrays.asList(new Integer[] { 27, 28, 35, 36 });
		List<Integer> turnsList = Arrays.asList(new Integer[] { 20, 29, 34, 43 });
		String[] nextKeys = { "left", "left_up", "up", "right_up", "right", "right_down", "down", "left_down" };
		int[] nextIndexs = { -1, -9, -8, -7, 1, 9, 8, 7 };
		for (int i = 0; i < 64; i++) {
			squares.add(new Square(i, placesList.contains(i), (i == 27 || i == 36), turnsList.contains(i)));
		}
		for (int i = 0; i < 64; i++) {
			for (int index = 0; index < nextKeys.length; index++) {
				int calcedIndex = i + nextIndexs[index];
				if (0 <= calcedIndex && calcedIndex <= 63) {
					this.squares.get(i).setNexts(nextKeys[index], this.squares.get(calcedIndex));
				} else {
					this.squares.get(i).setNexts(nextKeys[index], null);
				}
			}
			if (i % 8 == 0) {
				this.squares.get(i).setNexts("left", null);
				this.squares.get(i).setNexts("left_up", null);
				this.squares.get(i).setNexts("left_down", null);
			}
			if (i % 8 == 7) {
				this.squares.get(i).setNexts("right", null);
				this.squares.get(i).setNexts("right_up", null);
				this.squares.get(i).setNexts("right_down", null);
			}
		}
	}

	public void turnChange(int id) {
		this.squares.get(id).put(this.isBlack);
		this.isBlack = !this.isBlack;
		boolean isAllPlaced = true, isSomeTurn = false;
		this.blackCount = 0;
		this.whiteCount = 0;
		for (Square square : squares) {
			if (!square.getIsPlaced()) {
				isAllPlaced = false;
			} else {
				if (square.getIsBlack())
					this.blackCount++;
				if (!square.getIsBlack())
					this.whiteCount++;
			}
			if (square.turnOverCheck(this.isBlack)) {
				isSomeTurn = true;
			}
		}
		if (isAllPlaced) {
			this.isFinished = true;
		}
		if (!isSomeTurn) {
			this.isBlack = !this.isBlack;
			boolean tempSomeTurn = false;
			for (Square square : squares) {
				if (square.turnOverCheck(this.isBlack)) {
					tempSomeTurn = true;
				}
			}
			if (!tempSomeTurn) {
				this.isFinished = true;
			}
		}
	}
}
