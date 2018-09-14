package cn.footman.controller;

import cn.footman.common.utils.FastDFSClient;
import cn.footman.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传
 */
@Controller
public class PictureController {


    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;


    @RequestMapping(value = "/pic/upload",produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    @ResponseBody
    public String uploadFile(MultipartFile uploadFile){

        try {
//        把图片上传到图片服务器
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
//            取得扩展名
            String originalFilename = uploadFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            url = IMAGE_SERVER_URL + url;

            Map map = new HashMap();
            map.put("error",0);
            map.put("url",url);
            return JsonUtils.objectToJson(map);

        } catch (Exception e) {
            e.printStackTrace();

            Map map = new HashMap();
            map.put("error",1);
            map.put("message","上传错误");
            return JsonUtils.objectToJson(map);

        }


    }
}
