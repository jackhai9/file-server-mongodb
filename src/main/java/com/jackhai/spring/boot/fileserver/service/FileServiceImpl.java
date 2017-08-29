package com.jackhai.spring.boot.fileserver.service;

import com.jackhai.spring.boot.fileserver.domain.File;
import com.jackhai.spring.boot.fileserver.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author jackhai
 * @date 2017年8月26日
 */
@Service
public class FileServiceImpl implements FileService {
	
	@Autowired
	public FileRepository fileRepository;

	@Override
	public File saveFile(File file) {
		return fileRepository.save(file);
	}

	@Override
	public void removeFile(String id) {
		fileRepository.delete(id);
	}

	@Override
	public File getFileById(String id) {
		return fileRepository.findOne(id);
	}

	@Override
	public List<File> listFilesByPage(int pageIndex, int pageSize) {
		
		Sort sort = new Sort(Direction.DESC,"uploadDate"); 
		Pageable pageable = new PageRequest(pageIndex, pageSize, sort);

		Page<File> page = fileRepository.findAll(pageable);
		List<File> list = page.getContent();
		return list;
	}
}
