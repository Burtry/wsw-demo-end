package com.example.wswdemo.controller;

import com.example.wswdemo.utils.AliOSSUtil;
import com.example.wswdemo.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private AliOSSUtil aliOSSUtil;


    @PutMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("进行文件上传:{}",file);
        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名后缀(拓展名)    找到最后一个 . 的索引
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            //构建新文件名称
            String objectName = UUID.randomUUID().toString() + extension;

            //真正的文件路径
            String filePath = aliOSSUtil.upload(file.getBytes(), objectName);

            return Result.success(filePath,"文件上传成功!");
        } catch (IOException e) {
            log.error("文件上传失败：{}",e);
            return Result.error("文件上传失败!");
        }
    }
}
