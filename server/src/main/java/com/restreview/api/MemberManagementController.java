package com.restreview.api;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restreview.annotations.PreAuth;
import com.restreview.dto.DtoMap;
import com.restreview.dto.Message;
import com.restreview.etc.ReportStateType;
import com.restreview.etc.UserType;
import com.restreview.models.Member;
import com.restreview.models.User;
import com.restreview.repositories.MemberRepository;
import com.restreview.repositories.ReviewRepository;
@RestController
@RequestMapping("manage/member")
public class MemberManagementController {
	
	@Autowired MemberRepository repos;
	@Autowired ReviewRepository reviewRepos;
	@GetMapping("list/{count}/{page}")
	@PreAuth(UserType.ADMINISTRATOR)
	public Message list(HttpSession session, @PathVariable("page")int page, @PathVariable("count")int count) {

		return Message.listRaw(
				repos.manageList(PageRequest.of(page-1, count, Direction.DESC, "createdAt"))
				.map((us)->{
					// 클라이언트에게 통계 데이터를 넘겨줌
					long revCount = reviewRepos.getReviewCountByUserNo(us.getUserNo());
					long repCount = reviewRepos.getReportedReviewCount(us.getUserNo(), ReportStateType.APPROVED);
					
					return new DtoMap()
							.set("name", us.getName())
							.set("email", us.getEmail())
							.set("userId", us.getUserId())
							.set("userNo", us.getUserNo())
							.set("reviewCount",revCount)
							.set("reportedCount", repCount);
				})
		);
	}
	
	@PostMapping("drop/{userNo}")
	@Transactional
	public Message drop(@PathVariable("userNo")long userNo) {
		Member mem = repos.getOne(userNo);
		if(mem == null) return Message.failure("찾을 수 없는 사용자입니다.");
		repos.delete(mem);
		return Message.success();
	}
	
}
