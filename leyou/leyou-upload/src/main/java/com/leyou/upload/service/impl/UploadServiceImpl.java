package com.leyou.upload.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.upload.service.IUploadService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@Service
public class UploadServiceImpl implements IUploadService {
    //图片头信息用户检验图片格式
    private static final List<String> CONTENT_TYPES = Arrays.asList("image/gif","image/jpeg","image/jpg");
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadServiceImpl.class);
    @Autowired
    private FastFileStorageClient storageClient;

    @Override
    public String uploadImage(MultipartFile file) {
        //校验文件类型
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        if(!CONTENT_TYPES.contains(contentType)){
            LOGGER.info("文件类型不合法：{}",originalFilename);
            return null;
        }
        try {
            //校验文件内容
            BufferedImage read = ImageIO.read(file.getInputStream());
            if (read == null){
                LOGGER.info("文件内容不合法：{}",originalFilename);
                return null;
            }
//            //保存到服务器
//            file.transferTo(new File("E:\\leyou\\images",originalFilename));
//            //返回Url,进行回显
//            return "http://image.leyou.com/"+originalFilename;
            // 2、将图片上传到FastDFS
            // 2.1、获取文件后缀名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            // 2.2、上传
            StorePath storePath = this.storageClient.uploadFile(
                    file.getInputStream(), file.getSize(), extension, null);
            // 2.3、返回完整路径
            return "http://image.leyou.com/" + storePath.getFullPath();
        } catch (IOException e) {
            LOGGER.info("服务器内部错误：{}",originalFilename);
            e.printStackTrace();
        }
        return null;
    }
}
