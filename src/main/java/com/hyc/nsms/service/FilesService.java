package com.hyc.nsms.service;

import com.hyc.nsms.model.entity.Files;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FilesService extends IService<Files> {
    //上传文件
    String upload(MultipartFile file);

    //下载文件
    void download(String filesUUID, HttpServletResponse response);

    //导出排班表
    void exportSchedule(HttpServletResponse response, Integer departmentId, Integer month);
}
