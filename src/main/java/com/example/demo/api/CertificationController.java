package com.example.demo.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.CertificationType;
import com.example.demo.model.CertificationVO;
import com.example.demo.model.CertificationVO.CertificationVOBuilder;
import com.example.demo.model.Result;
import com.example.demo.service.CertificationService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/api/v1/certification")
@RestController
public class CertificationController{
	@Autowired
	CertificationService service;
	
	@ApiImplicitParam(name="id", value="인증서 ID", required=true, dataType = "int")
	@ApiOperation(value="인증서 가져오기", notes="인증서 가져오기", response=Result.class)
	@GetMapping("/get/{id}")
	public Result select(HttpServletRequest request,
    		HttpServletResponse response, @PathVariable int id) {
		Result result = Result.successInstance();
		CertificationVO certificationVO = new CertificationVO();
		certificationVO.setId(id);
		log.info(certificationVO.toString());
		CertificationVO list = service.selectById(certificationVO);
		result.setData(list);
		return result;
	}

	@ApiOperation(value="인증서 사진 출력", notes="인증서 사진 출력", response=Result.class)
	@GetMapping(
			value = "certification/{id}"
			)
    public @ResponseBody byte[] selectOne(HttpServletRequest request,
    		HttpServletResponse response, @PathVariable int id) {
		byte[] imageByteArray = null;
		
		CertificationVO param = new CertificationVO();
		param.setId(id);

        CertificationVO image = service.selectOne(param);
        try {
	        File imageFile = new File(getUserPath()+File.separator+image.getNewFilename());
	        log.info(imageFile.getAbsolutePath());
	        
	        response.setContentType(image.getContentType());
	        response.setContentLength(image.getSize());
	        
            InputStream is = new FileInputStream(imageFile);
            
            IOUtils.copy(is, response.getOutputStream());
            imageByteArray = IOUtils.toByteArray(is);
            is.close();
        } catch(IOException e) {
            log.info("Could not show picture "+id +"/" + e.getLocalizedMessage());
        }catch (NullPointerException e) {
        	e.printStackTrace();
		}
        return imageByteArray;
    }
	
	@ApiOperation(value="인증서 업로드", notes="인증서 업로드", response=Result.class)
	@PostMapping("/upload")
	public Result upload(@RequestParam("data")MultipartFile file,
			@RequestParam("user")Optional<String> user,
			@RequestParam("type")Optional<String> type)throws IOException{
		Result result = Result.successInstance();
		
		File convertedFile = convert(file);
		
		String newFilenameBase = UUID.randomUUID().toString();
		String originalFileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String newFilename = newFilenameBase + originalFileExtension;
		String srcPath = makeUserPath();
		File newFile = new File(srcPath + newFilename);
		
		try {
			file.transferTo(newFile);
			if(newFile.exists()) {
				convertedFile.delete();
			}
			CertificationVOBuilder builder = CertificationVO.builder();
			if(user.isPresent()) {
				builder.writer_id(Integer.parseInt(user.get()));
			}
			if(type.isPresent()) {
				CertificationType certificationType = CertificationType.convertToType(type.get());
				builder.type(certificationType.getType());
			}
			builder.name(convertedFile.getName());
			builder.newFilename(newFilename);
			builder.size((int)file.getSize());
			builder.contentType(file.getContentType());
			builder.status(0);
			/*
			File thumbnailFile = resizeTo(newFile);
			builder.thumbnailFilename(thumbnailFile.getName());
			builder.thumbnailSize((int)thumbnailFile.length());
			*/
			CertificationVO certificationVO = builder.build();
			
			certificationVO.setUrl("/api/v1/certification/get/"+certificationVO.getId());
			
			service.insert(certificationVO);
			
			result.setData(certificationVO);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/*
	private File resizeTo(File input) throws IOException {
		final String ext = input.getName().substring(input.getName().lastIndexOf(".") + 1);
		
		// 저장된 원본파일로부터 BufferedImage 객체를 생성합니다.
		// File originFile = new File(filePath);
		BufferedImage srcImg = ImageIO.read(input); 

		// 썸네일의 너비와 높이 입니다.
		final int dw = (int)(srcImg.getWidth() / 3);
		final int dh = (int)(srcImg.getHeight() / 3);
		
		// 원본 이미지의 너비와 높이 입니다. 
		final int ow = srcImg.getWidth(); 
		final int oh = srcImg.getHeight();
		
		// 원본 너비를 기준으로 하여 썸네일의 비율로 높이를 계산합니다. 
		int nw = ow; 
		int nh = (ow * dh) / dw;
		
		// 계산된 높이가 원본보다 높다면 crop이 안되므로 
		// 원본 높이를 기준으로 썸네일의 비율로 너비를 계산합니다. 
		if(nh > oh) { 
			nw = (oh * dw) / dh; 
			nh = oh; 
		}
		
		// 계산된 크기로 원본이미지를 가운데에서 crop 합니다. 
		BufferedImage cropImg = Scalr.crop(srcImg, (ow-nw)/2, (oh-nh)/2, nw, nh);

		BufferedImage destImg = Scalr.resize(cropImg, dw, dh);

		String destFilename = new StringBuilder()
								.append(input.getName().substring(0, input.getName().lastIndexOf(".")))
								.append("-thumbnail")
								.append(input.getName().substring(input.getName().lastIndexOf(".")))
								.toString();
		
		File destFile = new File(input.getParent() + File.separator + destFilename);
		ImageIO.write(destImg, ext, destFile);		
		
		return destFile;
	}
	*/
	private File convert(MultipartFile file) throws IOException {
		File convertFile = new File(makeUserPath() + file.getOriginalFilename());
		
		if (convertFile.createNewFile()) {
			try (FileOutputStream fos = new FileOutputStream(convertFile)) {
				fos.write(file.getBytes());
			}
			return convertFile;
		}
		throw new IllegalArgumentException(String.format("파일 변환이 실패했습니다. 파일 이름: %s", file.getName()));
	}
	
	private String makeUserPath() {
		File file = new File(getUserPath());
		file.mkdirs();
		
		final String result = file.getAbsolutePath() + File.separator;
		return result;
	}
	private String getUserPath() {
		String path = System.getProperty("user.dir");
		StringBuilder builder = new StringBuilder()
				.append(path)
				.append(File.separator).append("webapps").append(File.separator)
				.append("repository").append(File.separator)
				.append("upload").append(File.separator);
		return builder.toString();
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="id", required=true, dataType="int"),
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header"),
	})
	@ApiOperation(value="인증서 삭제하기", notes="인증서 삭제하기", response=Result.class)
	@PostMapping(value="/delete/{id}")
	public Result delete(
			@PathVariable(value="id", required=true)int id,
			@RequestHeader String Authorization) {
		Result result = Result.successInstance();
		
		CertificationVO vo = new CertificationVO();
		vo.setId(id);
		
		int deleted = service.delete(vo);
		result.setData(deleted);
		
		return result;
	}
	
	/*
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="id", required=true, dataType="int"),
		@ApiImplicitParam(name="writer", value="작성자 아이디", required=true, dataType="int"),
		@ApiImplicitParam(name="Authorization", value="auth", required=true, dataType="String", paramType="header"),
	})
	@ApiOperation(value="인증서 수정", notes="인증서 수정", response = Result.class)
	@PostMapping(value="update")
	public Result update(
			@RequestParam("data")MultipartFile file,
			@RequestParam(value="id")int id,
			@RequestHeader String Authorization,
			@RequestParam(value="writer")Optional<Integer>writer,
			@RequestBody String[] pictures) {
		Result result = Result.successInstance();
		CertificationVO vo = new CertificationVO();
		

		File convertedFile = convert(file);
		
		String newFilenameBase = UUID.randomUUID().toString();
		String originalFileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String newFilename = newFilenameBase + originalFileExtension;
		String srcPath = makeUserPath();
		File newFile = new File(srcPath + newFilename);
		
		vo.setId(id);
		CertificationVO selected = service.selectOne(vo);
		

		builder.name(convertedFile.getName());
		builder.newFilename(newFilename);
		builder.size((int)file.getSize());
		builder.contentType(file.getContentType());
		
		certificationVO.setUrl("/api/v1/certification/get/"+certificationVO.getId());

		if(writer.isPresent()) {
			selected.setWriter_id(writer.get());
		}
		if(service.update(selected) > 0) {
			result.setData(selected);
			result.setTotalCount(1);
		}

		
		return result;
	}
	*/
}
