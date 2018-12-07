package com.restreview.etc;

import org.springframework.beans.factory.annotation.Autowired;

import com.restreview.models.Restaurant;
import com.restreview.repositories.ReviewRepository;

public class AverageScoreCalculator implements ScoreCalculator {

	@Autowired
	ReviewRepository repos;
	@Override
	public double calculate(Restaurant rest) {
		
		return repos.getAvgScore(rest.getRestNo());
	}
	
}
