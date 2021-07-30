package com.example.demo.api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.PhotoInfo;
import com.example.demo.model.PhotoInfo.PhotoInfoBuilder;
import com.example.demo.model.PhotoType;
import com.example.demo.model.Result;
import com.example.demo.service.PhotoInfoService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/api/v1/photo")
@RestController
public class RestPhotoController {
	@Autowired
	private PhotoInfoService service;
	
	@GetMapping("/get/types")
	public Result getAllTypes() {
		Result result = Result.successInstance();
		result.setData(PhotoType.getAll());
		return result;
	}
	@GetMapping("/get/types/{type}")
	public Result getType(@PathVariable(required=false) Optional<Integer> type) {
		Result result = Result.successInstance();
		if(type.isPresent()) {
			PhotoType p = PhotoType.valueOf(type.get());
			result.setData(p);
		}
		return result;
	}
	
	@ApiImplicitParam(name="pageNo", value="페이지번호", required=false, dataType = "int")
	@ApiOperation(value="사진리스트", notes="사진리스트", response=Result.class)
	@GetMapping("/list/{pageNo}")
	public Result select(@PathVariable(value="pageNo", required=false)Optional<Integer> pageNo) {
		Result result = Result.successInstance();
		PhotoInfo photoInfo = new PhotoInfo();
		if(pageNo.isPresent()) {
			photoInfo.setPageNo(pageNo.get());
		}else {
			photoInfo.setPageNo(1);
		}
		log.info(photoInfo.toString());
		photoInfo.setTotalCount(service.countAll());
		List<PhotoInfo> list = service.select(photoInfo);
		result.setData(list);
		return result;
	}
	@ApiImplicitParams({
		@ApiImplicitParam(name="user", value="user id", required=true, dataType="int"),
		@ApiImplicitParam(name="type", value="photo type", required=true, dataType="int")
	})
	@ApiOperation(value="사진 업로드", notes="사진 업로드", response=Result.class)
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
			PhotoInfoBuilder builder = PhotoInfo.builder();
			if(user.isPresent()) {
				builder.uploader(Integer.parseInt(user.get()));
			}
			if(type.isPresent()) {
				PhotoType photoType = PhotoType.convertToType(type.get());
				builder.type(photoType.getType());
			}
			builder.name(convertedFile.getName());
			builder.newFilename(newFilename);
			builder.size((int)file.getSize());
			builder.contentType(file.getContentType());
			
			File thumbnailFile = resizeTo(newFile);
			builder.thumbnailFilename(thumbnailFile.getName());
			builder.thumbnailSize((int)thumbnailFile.length());
			
			PhotoInfo photoInfo = builder.build();
			
			service.insert(photoInfo);
			
			photoInfo.setUrl("/api/v1/photo/picture/"+photoInfo.getId());
			result.setData(photoInfo);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
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
	
	@GetMapping(
			value = "/picture/{id}"
			)
    public @ResponseBody byte[] picture(HttpServletRequest request,
    		HttpServletResponse response, @PathVariable int id) {
		byte[] imageByteArray = null;
		
		PhotoInfo param = new PhotoInfo();
		param.setId(id);

        PhotoInfo image = service.selectOne(param);
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
	@RequestMapping(value = "/thumbnail/{id}", method = RequestMethod.GET)
    public @ResponseBody byte[] thumbnail(HttpServletRequest request,
    		HttpServletResponse response, @PathVariable int id) {
		byte[] imageByteArray = null;
		
		PhotoInfo param = new PhotoInfo();
		param.setId(id);
		
		PhotoInfo image = service.selectOne(param);
        File imageFile = new File(getUserPath()+File.separator+image.getThumbnailFilename());
        log.info(imageFile.getAbsolutePath());
        
        response.setContentType(image.getContentType());
        response.setContentLength(image.getThumbnailSize());
		
        try {
            InputStream is = new FileInputStream(imageFile);
            IOUtils.copy(is, response.getOutputStream());
            imageByteArray = IOUtils.toByteArray(is);
            is.close();
        } catch(IOException e) {
            log.info("Could not show picture "+id +"/" + e.getLocalizedMessage());
        }
        
        return imageByteArray;
    }
}
