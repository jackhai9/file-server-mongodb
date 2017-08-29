package com.jackhai.spring.boot.fileserver.controller;

import com.jackhai.spring.boot.fileserver.domain.File;
import com.jackhai.spring.boot.fileserver.service.FileService;
import com.jackhai.spring.boot.fileserver.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
/**
 *
 * @author jackhai
 * @date 2017年8月26日
 */
@CrossOrigin(origins = "*", maxAge = 3600)  // 允许所有域名访问
@Controller
public class FileController {

    @Autowired
    private FileService fileService;
    
    @Value("${server.address}")
    private String serverAddress;
    
    @Value("${server.port}")
    private String serverPort;
    
    @RequestMapping(value = "/")
    public String index(Model model) {
    	// 展示最新二十条数据
        model.addAttribute("files", fileService.listFilesByPage(0,20)); 
        return "index";
    }

    /**
     * 分页查询
     * @param pageIndex
     * @param pageSize
     * @return
     */
	@GetMapping("files/{pageIndex}/{pageSize}")
    @ResponseBody
	public List<File> listFilesByPage(@PathVariable int pageIndex, @PathVariable int pageSize){
		return fileService.listFilesByPage(pageIndex, pageSize);
	}
			
    /**
     * 获取文件信息
     * @param id
     * @return
     */
    @GetMapping("files/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFile(@PathVariable String id) {

        File file = fileService.getFileById(id);

        if (file != null) {
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + file.getName() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream" )
                    .header(HttpHeaders.CONTENT_LENGTH, file.getSize()+"")
                    .header("Connection",  "close") 
                    .body( file.getContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("文件找不到!");
        }

    }
    
    /**
     * 在线显示文件
     * @param id
     * @return
     */
    @GetMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFileOnline(@PathVariable String id) {

        File file = fileService.getFileById(id);

        if (file != null) {
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "fileName=\"" + file.getName() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, file.getContentType() )
                    .header(HttpHeaders.CONTENT_LENGTH, file.getSize()+"")
                    .header("Connection",  "close") 
                    .body( file.getContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("文件找不到!");
        }

    }
    
    /**
     * 上传文件
     * @param file
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        try {
        	File f = new File(file.getOriginalFilename(),  file.getContentType(), file.getSize(), file.getBytes());
        	f.setMd5( MD5Util.getMD5(file.getInputStream()) );
            if ("".equals(f.getName()) || f.getSize()==0){
                redirectAttributes.addFlashAttribute("message", "文件不能为空!");
                return "redirect:/";
            }
            fileService.saveFile(f);
        } catch (IOException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message",
                    "您上传的文件 " + file.getOriginalFilename() + " 出错了!");
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("message",
                "您已成功上传文件 " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
 
    /**
     * 上传接口
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
    	File returnFile = null;
        try {
        	File f = new File(file.getOriginalFilename(),  file.getContentType(), file.getSize(),file.getBytes());
        	f.setMd5( MD5Util.getMD5(file.getInputStream()) );
        	returnFile = fileService.saveFile(f);
        	String path = "//"+ serverAddress + ":" + serverPort + "/view/"+returnFile.getId();
        	return ResponseEntity.status(HttpStatus.OK).body(path);
 
        } catch (IOException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
 
    }
    
    /**
     * 删除文件
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteFile(@PathVariable String id) {
 
    	try {
			fileService.removeFile(id);
			return ResponseEntity.status(HttpStatus.OK).body("删除成功!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
    }
}
