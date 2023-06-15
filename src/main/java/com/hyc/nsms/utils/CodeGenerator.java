package com.hyc.nsms.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

//mp代码生成器
public class CodeGenerator {
    private static String url = "jdbc:mysql://localhost:3306/nsdb?serverTimezone=GMT%2B8";

    private static String username = "root";

    private static String password = "011023";

    private static void generator() {
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("hyc") // 设置作者
//                            .fileOverride() // 覆盖已生成文件
                            .outputDir("C:\\Users\\14248\\IdeaProjects\\nsms\\src\\main\\java\\"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.hyc.nsms") // 设置父包名
                            .moduleName(null) // 设置父包模块名 无
                            .entity("model.entity")

                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "C:\\Users\\14248\\IdeaProjects\\nsms\\src\\main\\java\\com\\hyc\\nsms\\mapper\\xml\\")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
                    builder.mapperBuilder().enableMapperAnnotation().build();
                    builder.controllerBuilder().enableHyphenStyle()  // 开启驼峰转连字符
                            .enableRestStyle(); // 开启生成@RestController 控制器
                    builder.addInclude("expectation") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }

    public static void main(String[] args) {
        generator();
    }
}
