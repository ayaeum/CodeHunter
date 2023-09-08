package org.jeecg;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 功能描述  mybatis-plus代码生成器
 *
 * @author: caiguapi
 * @date: 2022年08月29日 16:30
 */
public class GenerateUtil {
    public static void main(String[] args){
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/jeecg-boot-vue3?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai","root","Ww8868648")
                .globalConfig(builder ->{
                    builder.author("菜瓜皮")
                            .outputDir("F:\\jeecg-boot-master\\jeecg-boot-master\\jeecg-boot\\jeecg-boot-module-system\\src\\main\\java");
                })
                .packageConfig(builder -> {
                    builder.parent("org.jeecg.modules.system")
                            .pathInfo(Collections.singletonMap(OutputFile.xml,"F:\\jeecg-boot-master\\jeecg-boot-master\\jeecg-boot\\jeecg-boot-module-system\\src\\main\\java\\org\\jeecg\\modules\\system\\mapper\\xml"));
                }).strategyConfig(builder -> {
            builder.addInclude("scan_result_count") // 设置需要生成的表名
                    .addTablePrefix("t_", "c_"); // 设置过滤表前缀
        })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
