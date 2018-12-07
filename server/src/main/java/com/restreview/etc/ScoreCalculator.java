package com.restreview.etc;

import com.restreview.models.Restaurant;

public interface ScoreCalculator {
	
	public double calculate(Restaurant rest);
}
