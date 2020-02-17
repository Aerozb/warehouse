package com.yhy.bus.controller;

import com.yhy.sys.common.DataGridView;
import com.yhy.sys.common.MyFileUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 上传
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @PostMapping("/image")
    public DataGridView upload(MultipartFile image) {
        String newFileName = MyFileUtil.createNewFileName(image.getOriginalFilename());
        String path = null;
        try {
            path = MyFileUtil.save((FileInputStream) image.getInputStream(), newFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DataGridView(path);
    }
}
