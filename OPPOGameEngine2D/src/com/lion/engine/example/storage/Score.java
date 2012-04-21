package com.lion.engine.example.storage;

public class Score {

	private String name;
	private int score;
	private long time;
	
	public Score(String n, int s, long t) {
		name = n;
		score = s;
		time = t;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
}
