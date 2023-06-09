package com.imooc.article.controller;

import java.io.File;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 昴星
 * @date 2023-05-24 18:52
 * @explain
 */

@Controller
public class FtlTest {
    @Value("${spring.freemarker.html.target}")
    private String htmlPath;

    @GetMapping("/ftl")
    @ResponseBody
    public String test(Model model)  throws  Exception{
        Configuration cfg=new Configuration(Configuration.getVersion());
        String path=this.getClass().getResource("/").getPath();
        cfg.setDirectoryForTemplateLoading(new File(path+"templates"));

        Template template=cfg.getTemplate("hello.ftl","utf-8");
        String stranger = "jjjj";
        model.addAttribute("there",stranger);
        File file = new File(htmlPath);
        if(!file.exists()) file.mkdirs();
        Writer out = new FileWriter(htmlPath+File.separator+"aa"+".html");
        template.process(model,out);
        out.close();
        return "hello";
    }
}
