package com.restreview.api;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restreview.annotations.PreAuth;
import com.restreview.dto.Message;
import com.restreview.etc.ScoreCalculator;
import com.restreview.etc.SessionManager;
import com.restreview.etc.UserType;
import com.restreview.models.Restaurant;
import com.restreview.models.Review;
import com.restreview.models.ReviewReport;
import com.restreview.models.User;
import com.restreview.repositories.RestaurantRepository;
import com.restreview.repositories.ReviewReportRepository;
import com.restreview.repositories.ReviewRepository;

@RestController
@RequestMapping("review")
public class ReviewController {
	
	@Autowired ScoreCalculator calculator;
	@Autowired ReviewRepository repos;
	@Autowired RestaurantRepository restRepos;
	@Autowired ReviewReportRepository reportRepos;
	@Autowired SessionManager sessionManager;
	
	@GetMapping("list/{restNo}/{count}/{page}")
	@PreAuth(UserType.USER)
	public Message list(HttpSession session, @PathVariable("restNo")long no, @PathVariable("count")int count, @PathVariable("page")int page) {
		Page<Review> reviews = repos.getList(no, PageRequest.of(page-1, count, Direction.DESC, "createdAt"));
		return Message.list(reviews);
	}
	
	@GetMapping("get/{reviewNo}")
	@PreAuth(UserType.USER)
	public Message get(@PathVariable("reviewNo")long reviewNo) {
		Review r = repos.getOne(reviewNo);
		if(r == null) {
			return Message.failure("일치하는 리뷰정보를 조회할 수 없ㅅ브니다.");
		}
		return Message.success();
	}
	@PostMapping("add/{restNo}")
	@Transactional
	@PreAuth(UserType.MEMBER)
	public Message add(HttpSession session, @PathVariable("restNo")long restNo,
			@RequestParam(defaultValue="", name="content")String content,
			@RequestParam(defaultValue="", name="imageUrl")String imageUrl,
			@RequestParam("score")Integer score) {
		// 글 혹은 평점 입력 확인
		if(content.isEmpty() || score == null) {
			return Message.failure("글 혹은 평점이 입력되지 않았습니다.");
		}
		//식당정보 가져오기
		Restaurant rest = restRepos.getOne(restNo);
		if(rest == null) {
			return Message.failure("일치하는 식당정보를 찾을 수 없습니다.");
		}
		// 현재 사용자 정보 가져오기
		User us = sessionManager.getUser(session);
		// 리뷰 인스턴스 생성
		Review review = Review.builder().content(content)
				.restaurant(rest).score(score)
				.imageUrl(imageUrl)
				.writer(us).build();
		// 데이터베이스에 리뷰 인스턴스 저장
		repos.save(review);
		// 식당 정보에 리뷰 수 및 평점 계산 결과 저장
		rest.setReviewCount(repos.getReviewCountByRestNo(rest.getRestNo()));
		rest.setScore(calculator.calculate(rest));
		restRepos.save(rest);
		return Message.success();
	}
	
	@PostMapping("report/{reviewNo}")
	@Transactional
	@PreAuth(UserType.RESTAURANTOWNER)
	public Message report(HttpSession session, @PathVariable("reviewNo")long reviewNo
			, @RequestParam(defaultValue="", name="reason")String reason) {
		// 사유없이 등록 불가
		if(reason.isEmpty()) {
			return Message.failure("사유를 입력해주세요");
		}
		// 리뷰 정보 가져오기
		Review review = repos.getOne(reviewNo);
		if(review == null || review.getReport() != null) {
			return Message.failure( "일치하는 리뷰정보를 찾을 수 없거나 이미 신고 처리 대기 혹은 처리된 리뷰입니다.");
		}
		User owner = sessionManager.getUser(session);
		if(owner.getUserNo() != review.getRestaurant().getOwner().getUserNo()) {
			return Message.failure("해당 식당의 소유자만이 신고할 수 있습니다.");
		}
		ReviewReport report = ReviewReport.builder().state("REPORTED").reason(reason).review(review).build();
		reportRepos.save(report);
		return Message.success();
	}
}
