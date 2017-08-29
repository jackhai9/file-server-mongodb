package com.jackhai.spring.boot.fileserver.repository;

import com.jackhai.spring.boot.fileserver.domain.File;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 *
 * @author jackhai
 * @date 2017年8月26日
 */
public interface FileRepository extends MongoRepository<File, String> {
}
