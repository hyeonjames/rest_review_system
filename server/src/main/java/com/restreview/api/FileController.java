package com.restreview.api;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.restreview.annotations.PreAuth;
import com.restreview.dto.Message;
import com.restreview.etc.UserType;

@RestController
@RequestMapping("file")
public class FileController {
	@Value("${project.upload.path.image}")
	private String imageDirectory;
	
	@PostMapping("/image/upload")
	@PreAuth(UserType.USER)
	public Message imageUpload(HttpSession session, HttpServletRequest request) throws IllegalStateException, IOException {
		MultipartHttpServletRequest req = (MultipartHttpServletRequest)request;
		Iterator<String> iterator = req.getFileNames();
		if(iterator.hasNext()) { // 업로드 파일이 존재하면
			File dir = new File(imageDirectory); // application.properties 에 정의되어 있음
			if(!dir.exists()) { // 업로드 디렉터리가 없으면
				dir.mkdir(); // 업로드 디렉터리를 만듬
			}
			MultipartFile file = req.getFile(iterator.next());
			 
			if(!file.isEmpty()) {
				String fileName = file.getOriginalFilename();
				int pos = fileName.lastIndexOf("."); 
				// 확장자 구함
				String extName = "";
				if(pos >= 0) {
					extName = fileName.substring(pos+1).toLowerCase();
				}
				// png, jpg, jpeg만 업로드 가능
				if(!extName.matches("png|jpg|jpeg")) {
					return Message.failure("PNG, JPG, JPEG 파일만 올릴 수 있습니다.");
				}
				String storedFileName = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + "." + extName;
				file.transferTo(new File(imageDirectory + storedFileName)); // 해당 파일 저장
				return Message.success("/images/" + storedFileName);
			}
		}
		return Message.failure("업로드할 파일이 없습니다.");
	}
}
