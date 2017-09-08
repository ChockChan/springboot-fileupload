package com.scut.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenwp on 17-9-8.
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @RequestMapping(value = "/file", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Object uploadFile(HttpServletRequest request){

        Map<String, String> response = new HashMap<>();


        ServletFileUpload upload = new ServletFileUpload();
        try {
            FileItemIterator item = upload.getItemIterator(request);

            while(!item.hasNext()) {
                response.put("message", "empty file");
                return response;
            }

            while(item.hasNext()){
                FileItemStream itemStream = item.next();
                String fieldName = itemStream.getFieldName();
                logger.info("fieldName: " + fieldName);
                String name = itemStream.getName();
                logger.info("name: " + name);
                if(!itemStream.isFormField()){
                    InputStream inputStream = itemStream.openStream();
                    OutputStream out = new FileOutputStream(name);
                    IOUtils.copy(inputStream, out);
                    inputStream.close();
                    out.close();
                }

            }

            response.put("message", "success");
            return response;
        }catch (FileUploadException fe){
            logger.error(fe.getMessage());
            logger.error(fe.toString());
            response.put("message", fe.getMessage());
            return response;
        }catch (IOException e){
            logger.error(e.getMessage());
            logger.error(e.toString());
            response.put("message", e.getMessage());
            return response;
        }
    }
}
