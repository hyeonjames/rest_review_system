package com.restreview.api;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.restreview.etc.UserType;
import com.restreview.models.ReviewReport;
import com.restreview.repositories.ReviewReportRepository;
import com.restreview.repositories.ReviewRepository;

@RequestMapping("manage/report")
@RestController
public class ReviewManagementController {
	@Autowired ReviewReportRepository reportRepos;
	@GetMapping("list/{count}/{page}")
	@PreAuth(UserType.ADMINISTRATOR)
	
	public Message reportedList(HttpSession session, @PathVariable("count")int count, @PathVariable("page")int page) {
		return Message.list(reportRepos.findAll(PageRequest.of(page-1, count,Direction.DESC, "createdAt")));
	}
	
	@PostMapping("update/state/{reportNo}")
	@Transactional
	@PreAuth(UserType.ADMINISTRATOR)
	
	public Message updateState(HttpSession session, @PathVariable("reportNo")long reportNo, @RequestParam("state") String state) {
		ReviewReport report = reportRepos.getOne(reportNo);
		if(report == null) {
			return Message.failure("리뷰 신고 정보를 찾을 수 없습니다.");
		}
		report.setState(state);
		reportRepos.save(report);
		return Message.success();
	}
}
