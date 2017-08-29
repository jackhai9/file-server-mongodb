# 基于 MongoDB 搭建的文件服务器

* MongoDB 3.4.7
* Spring Boot 1.5.6.RELEASE
* Thymeleaf 3.0.7.RELEASE
* Thymeleaf Layout Dialect 2.2.2
* Gradle 4.1

致力于小型文件的存储，比如博客中图片、普通文档等。
由于 MongoDB 的 BSON 文档对于数据量大小的限制（每个文档不超过16M），所以本文件服务器主要针对的是小型文件的存储。
对于大型文件的存储（比如超过16M），MongoDB 官方已经提供了成熟的产品  [GridFS](https://docs.mongodb.com/manual/core/gridfs/)。


## RESTful APIs

* GET  /files/{pageIndex}/{pageSize} : Paging query file list.(分页查询文件列表)
* GET  /files/{id} : Download file.(下载某个文件)
* GET  /view/{id} : View file online.(在线预览某个文件。比如，显示图片)
* POST /upload : Upload file.(上传文件)
* DELETE /{id} : Delete file.(删除文件)
