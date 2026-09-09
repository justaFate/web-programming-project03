package controller;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import util.Constant;

import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Controller
public class ImageController {

    @GetMapping("/image")
    public void downloadImage(@RequestParam("fname") String fileName, HttpServletResponse resp) {
        if (fileName == null || fileName.trim().isEmpty()) return;
        File file = new File(Constant.DIR + File.separator + fileName);
        resp.setContentType("image/jpeg");
        if (file.exists()) {
            try (InputStream in = new FileInputStream(file)) {
                IOUtils.copy(in, resp.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
