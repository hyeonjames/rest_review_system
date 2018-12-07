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
import com.restreview.etc.SessionManager;
import com.restreview.etc.UserType;
import com.restreview.models.Category;
import com.restreview.models.Restaurant;
import com.restreview.models.RestaurantOwner;
import com.restreview.repositories.CategoryRepository;
import com.restreview.repositories.RestaurantRepository;

@RestController
@RequestMapping("restaurant")
public class RestaurantController {
	
	@Autowired
	RestaurantRepository repos;
	@Autowired CategoryRepository cateRepos;
	@Autowired SessionManager sessionManager;
	
	@GetMapping("/popular/{cateNo}/{count}/{page}")
	@PreAuth(UserType.MEMBER)
	public Message popularList(HttpSession session,@PathVariable("cateNo")long cateNo, @PathVariable("page")int page, @PathVariable("count")int countPerPage) {
		PageRequest req = PageRequest.of(page-1, countPerPage,Direction.DESC, "score");
		return Message.list(repos.findAllByCateNo(cateNo, req));
	}
	
	@PostMapping("/search")
	@PreAuth(UserType.MEMBER)
	public Message search(HttpSession session,
			@RequestParam("coordX")double coordX,
			@RequestParam("coordY")double coordY,
			@RequestParam(defaultValue="",name="text")String text,
			@RequestParam("count") int count,
			@RequestParam("page") int page) {
		if(text.isEmpty()) {
			return Message.failure("내용을 입력해주세요.");
		}
		PageRequest req = PageRequest.of(page-1,count);
		return Message.list(repos.search(text, coordX, coordY, req));
	}
	
	@GetMapping("/get/{restNo}")
	@PreAuth({UserType.MEMBER, UserType.RESTAURANTOWNER})
	public Message get(HttpSession session,@PathVariable("restNo")long restNo) {
		
		Restaurant info = repos.getOne(restNo);
		if(info == null) {
			return Message.failure("일치하는 식당 정보를 조회할 수 없습니다.");
		}
		return Message.dto(info);
	}
	
	@GetMapping("/get/own-list")
	@PreAuth(UserType.RESTAURANTOWNER)
	public Message ownList(HttpSession session) {
		RestaurantOwner owner = sessionManager.getUser(session);
		return Message.dto(repos.findOwnList(owner.getUserNo()));
	}
	
	@PostMapping("/add")
	@Transactional
	@PreAuth(UserType.RESTAURANTOWNER)
	public Message add(HttpSession session, 
			@RequestParam(defaultValue="", name="name")String name,
			@RequestParam(defaultValue="", name="description")String description,
			@RequestParam(defaultValue="", name="address")String address,
			@RequestParam(defaultValue="", name="imageUrl")String imageUrl,
			@RequestParam("coordX")double coordX,
			@RequestParam("coordY")double coordY,
			@RequestParam(defaultValue="", name="phoneNumber")String phoneNumber,
			@RequestParam("categoryNo")long cateNo) {
		if(name.isEmpty() || description.isEmpty()
				|| address.isEmpty()
				|| phoneNumber.isEmpty()) {
			return Message.failure("유효하지 않은 정보 입니다.");
		}
		
		RestaurantOwner owner = sessionManager.getUser(session);
		Category cate = cateRepos.getOne(cateNo);
		// 식당카테고리 유효성 체크
		if(cate == null) {
			return Message.failure("존재하지 않는 카테고리 입니다.");
		}
		// 식당 인스턴스 생성
		Restaurant rest = Restaurant.builder()
				.address(address)
				.description(description)
				.coordX(coordX)
				.coordY(coordY)
				.name(name)
				.imageUrl(imageUrl)
				.phoneNumber(phoneNumber)
				.owner(owner)
				.cate(cate).build();
		repos.save(rest);
		return Message.success();
	}
	
	@PostMapping("update/{restNo}")
	@Transactional
	@PreAuth(UserType.RESTAURANTOWNER)
	public Message update(HttpSession session,@PathVariable("restNo")long restNo, 
			@RequestParam(defaultValue="", name="name")String name,
			@RequestParam(defaultValue="", name="description")String description,
			@RequestParam(defaultValue="", name="address")String address,
			@RequestParam(defaultValue="", name="imageUrl")String imageUrl,
			@RequestParam("coordX")double coordX,
			@RequestParam("coordY")double coordY,
			@RequestParam(defaultValue="", name="phoneNumber")String phoneNumber,
			@RequestParam("categoryNo")long cateNo) {
		if(name.isEmpty() || description.isEmpty()
				|| address.isEmpty()
				|| phoneNumber.isEmpty()) {
			return Message.failure("유효하지 않은 정보 입니다.");
		}

		Category cate = cateRepos.getOne(cateNo);
		if(cate == null) {
			return Message.failure("일치하는 카테고리가 없습니다.");
		}
		RestaurantOwner owner = sessionManager.getUser(session);
		Restaurant rest = repos.getOne(restNo);
		
		if(rest == null || rest.getOwner().getUserNo() != owner.getUserNo()) {
			return Message.failure("일치하는 식당정보를 찾을 수 없습니다.");
		}
		rest.setAddress(address);
		rest.setName(name);
		rest.setDescription(description);
		rest.setPhoneNumber(phoneNumber);
		rest.setImageUrl(imageUrl);
		rest.setCoordX(coordX);
		rest.setCoordY(coordY);
		rest.setCategory(cate);
		repos.save(rest);
		
		return Message.success();
	}
	@PostMapping("delete/{restNo}")
	@Transactional
	@PreAuth(UserType.RESTAURANTOWNER)
	public Message delete(HttpSession session, @PathVariable("restNo")long restNo) {
		RestaurantOwner owner = sessionManager.getUser(session);
		Restaurant rest = repos.getOne(restNo);
		
		if(rest == null || rest.getOwner().getUserNo() != owner.getUserNo()) {
			return Message.failure("식당 주인만 삭제할 수 있습니다.");
		}
		repos.delete(rest);
		return Message.success();
	}
}
