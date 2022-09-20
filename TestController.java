package com.zhoukai.msb.apipassmanger.controller;

import com.sun.deploy.net.URLEncoder;
import com.zhoukai.msb.apipassmanger.api.ServiceVerificationClient;
import com.zhoukai.msb.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private ServiceVerificationClient serviceVerificationClient;

    @Value("${textvalue}")
    private String textValue;

    @Resource
    private HttpServletResponse response;

    @GetMapping("/test")
    public Result getTest() {
        return Result.success();
    }

    @GetMapping("/test1")
    public Result getTest1() {
        log.error("textValue-->{}", textValue);
        return serviceVerificationClient.getTest();
    }

    @GetMapping("/download")
    public Result downloadFile() throws FileNotFoundException {
        String fileName = "E:\\周凯.docx";
        File file = new File(fileName);
        //获得输入流
        if(!file.exists()){
            return Result.fail("下载文件不存在");
        }
        String name = fileName.substring(fileName.lastIndexOf("\\") + 1);
        response.reset();
        log.error("name-->{}", name);
        try{

//            XWPFDocument document;
//            String filename = file.getName();
//            FileInputStream fis = null;
//            fis = new FileInputStream(file);
//            //设置文件名及后缀
//            response.setHeader("Content-Disposition", "attachment; filename=" +
//                    new String(filename.getBytes(), "ISO-8859-1"));
//            response.setHeader("content-Type", "docx");
//            String fileType = "docx";
//            if ("docx".equals(fileType) || "doc".equals(fileType)) {//Office的doc与docx输出流，使用poi-ooxml 3.17可用
//                document = new XWPFDocument(OPCPackage.open(fis));
//                document.write(response.getOutputStream());
//            }

            //读取文件内容
            FileInputStream is = new FileInputStream(new File(fileName));
            response.setHeader("content-disposition", "inline;fileName=" + URLEncoder.encode(name, "UTF-8"));
            ServletOutputStream os = response.getOutputStream();
            System.out.println("duwnload文件------------");
            IOUtils.copy(is, os);
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        } catch (IOException e) {
            log.error("下载失败", e);
            return Result.fail("下载失败");
//        } catch (InvalidFormatException e) {
//            log.error("下载失败", e);
//            return Result.fail("下载失败");
        }
        return Result.success();
    }

}
