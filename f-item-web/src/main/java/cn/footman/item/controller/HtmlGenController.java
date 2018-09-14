package cn.footman.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HtmlGenController {


    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @RequestMapping("/genHtml")
    @ResponseBody
    public String genHtml() {
        try {
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("hello.ftl");

            Map map = new HashMap<>();
            map.put("hello","nihao a !!!!");
            Writer writer = new FileWriter(new File("D:\\tmp\\test1.txt"));
            template.process(map,writer);
            writer.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ok";
    }

}
