package com.hyc.nsms.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyc.nsms.mapper.ScheduleMapper;
import com.hyc.nsms.model.entity.Department;
import com.hyc.nsms.model.entity.Files;
import com.hyc.nsms.mapper.FilesMapper;
import com.hyc.nsms.model.vo.ScheduleVo;
import com.hyc.nsms.model.vo.StaffTimeVo;
import com.hyc.nsms.service.DepartmentService;
import com.hyc.nsms.service.FilesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.nsms.service.ScheduleService;
import com.hyc.nsms.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files> implements FilesService {
    @Value("${files.upload.path}")
    private String filesUploadPath;

    @Value("${server.ip}")
    private String serverIp;

    @Value("${server.port}")
    private String port;

    @Autowired
    private ScheduleService scheduleService;

    @Resource
    private ScheduleMapper scheduleMapper;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public String upload(MultipartFile file) {
        //将相对路径转化为绝对路径
        String destPath = new File(filesUploadPath).getAbsolutePath();
        //文件夹路径名称
        String originalFilename = file.getOriginalFilename();
        //文件大小
        double size = file.getSize();
        //文件类型
        String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        //使用uuid生成随机唯一值
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //新的文件名称，uuid+文件类型
        String fileUuid = uuid + "." + type;
        //新的文件地址
        File uploadFile = new File(destPath + "/" + fileUuid);

        //判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        try {
            String url;
            //获取文件的md5,通过对比文件md5，防止上传相同内容的文件
            String md5 = DigestUtils.md5DigestAsHex(file.getInputStream());
            //通过md5来查询 文件
            Files dbFiles = this.getFileByMD5(md5);
            if (dbFiles != null) {//如果数据库存在相同文件，直接获取url
                url = dbFiles.getUrl();
            } else {//如果数据库不存在相同文件，先存储到本地磁盘，再设置文件url
                file.transferTo(uploadFile);//把获取到的文件存储带磁盘目录
                url = "http://" + serverIp + ":" + port + "/files/" + fileUuid;//设置文件url
            }

            //将文件存储到数据库
            Files saveFile = new Files();
            saveFile.setFilesName(originalFilename);
            saveFile.setType(type);
            saveFile.setSize(size / 1024);
            saveFile.setUrl(url);
            saveFile.setMd5(md5);
            //保存操作
            save(saveFile);

            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //将文件以流的形式一次性读取到内存，通过响应输出流输出到前端
    @Override
    public void download(String filesUUID, HttpServletResponse response) {
        try {
            //根据文件的唯一标识码获取文件
            File uploadFile = new File(filesUploadPath + filesUUID);

            //读取文件的字节流
            FileInputStream fileInputStream = new FileInputStream(uploadFile);
            //将文件写入输入流
            InputStream inputStream = new BufferedInputStream(fileInputStream);

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();


            //attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.png"
            //filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filesUUID, "UTF-8"));
            response.setContentType("application/octet-stream");

            //设置输出流的格式
            ServletOutputStream os = response.getOutputStream();
            os.write(buffer);
            fileInputStream.close();
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //通过文件MD5查询文件
    private Files getFileByMD5(String md5) {
        //查找数据库是否已经存在一样的图片
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = list(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    //导出排班表
    @Override
    public void exportSchedule(HttpServletResponse response, Integer departmentId, Integer month) {
        try {
            //1.获得month月的开始日期和结束日期
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = simpleDateFormat.format(DateUtils.getMonthStartDate(month));
            String endDate = simpleDateFormat.format(DateUtils.getMonthEndDate(month));

            //2.从数据库查询出需要的数据
            List<StaffTimeVo> list = scheduleService.getScheduleByDepartmentIdAndDate(departmentId, startDate, endDate);
            List<ScheduleVo> nameList = scheduleMapper.getStaffByDepartmentIdAndDate(departmentId, startDate, endDate);

            //3.在内存操作，写出到浏览器
            ExcelWriter writer = ExcelUtil.getWriter(true);

            //4.排班结果
            //①第一行：姓名
            writer.writeCellValue(0, 0, "姓名");
            //②第一行：这个月份的天数
            int days = DateUtils.getNextMonthDays();//下个月天数
            for (int j = 1; j <= days; j++) {
                writer.writeCellValue(j, 0, String.valueOf(j));
            }

            int row = 1;//行
            for (ScheduleVo scheduleVo : nameList) {
                int column = 1;//列
                //③第一行之后：员工姓名
                String name = scheduleVo.getName();
                writer.writeCellValue(0, row, name);
                //④第一行之后：员工排班的班次
                for (StaffTimeVo s : list) {
                    if (name.equals(s.getName())) {
                        String shiftName = "休";//班次名称
                        switch (s.getShiftSign()) {
                            case 1:
                                shiftName = "白";
                                break;
                            case 2:
                                shiftName = "小";
                                break;
                            case 3:
                                shiftName = "大";
                                break;
                            case 4:
                                shiftName = "假";
                                break;
                        }
                        writer.writeCellValue(column++, row, shiftName);
                    }
                }
                row++;//下一行
            }

            //5.一次性写出list内的对象到excel，使用默认样式，强制输出标题
            writer.write(new ArrayList<>(), true);

            //6.设置浏览器响应的格式
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            String fileStringName = month + "月" + this.getDepartmentName(departmentId) + "的排班表";
            String fileName = URLEncoder.encode(fileStringName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            //7.关闭流
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out, true);
            out.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //通过科室id获得科室名称
    private String getDepartmentName(Integer departmentId) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", departmentId);
        Department department = departmentService.getOne(queryWrapper);
        String departmentName = department.getDepartmentName();
        return departmentName;
    }
}
