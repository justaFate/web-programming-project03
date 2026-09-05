package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Category;
import service.ICategoryService;
import service.impl.CategoryServiceImpl;
import util.Constant;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
@WebServlet(urlPatterns = { 
    "/admin/categories", 
    "/admin/category/add", 
    "/admin/category/insert", 
    "/admin/category/edit", 
    "/admin/category/update", 
    "/admin/category/delete" 
})
public class CategoryController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public ICategoryService cateService = new CategoryServiceImpl();
    private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = validatorFactory.getValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        try {
            if (url.contains("/admin/categories")) {
                List<Category> list = cateService.findAll();
                req.setAttribute("listcate", list);
                req.getRequestDispatcher("/views/admin/category-list.jsp").include(req, resp);
            } else if (url.contains("/admin/category/add")) {
                req.getRequestDispatcher("/views/admin/category-add.jsp").include(req, resp);
            } else if (url.contains("/admin/category/edit")) {
                int id = Integer.parseInt(req.getParameter("id"));
                Category category = cateService.findById(id);
                req.setAttribute("cate", category);
                req.getRequestDispatcher("/views/admin/category-edit.jsp").include(req, resp);
            } else if (url.contains("/admin/category/delete")) {
                int id = Integer.parseInt(req.getParameter("id"));
                try {
                    Category category = cateService.findById(id);
                    if (category != null && category.getImages() != null && !category.getImages().startsWith("http")) {
                        deleteFile(Constant.DIR + File.separator + category.getImages());
                    }
                    cateService.delete(id);
                    req.getSession().setAttribute("message", "Đã xóa danh mục thành công!");
                } catch (Exception e) {
                    e.printStackTrace();
                    req.getSession().setAttribute("errorMessage", "Không thể xóa danh mục: " + e.getMessage());
                }
                resp.sendRedirect(req.getContextPath() + "/admin/categories");
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Lỗi kết nối cơ sở dữ liệu hoặc xử lý hệ thống: " + e.getMessage());
            req.getRequestDispatcher("/views/admin/category-list.jsp").include(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String url = req.getRequestURI();

        if (url.contains("/admin/category/insert")) {
            String categoryname = req.getParameter("categoryname");
            String statusStr = req.getParameter("status");
            int status = (statusStr != null && statusStr.equals("0")) ? 0 : 1;
            String images = req.getParameter("images");

            Category category = new Category();
            category.setCategoryname(categoryname != null ? categoryname.trim() : "");
            category.setStatus(status);

            Map<String, String> errors = new HashMap<>();

            Set<ConstraintViolation<Category>> violations = validator.validate(category);
            for (ConstraintViolation<Category> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }

            if (!errors.containsKey("categoryname") && categoryname != null && !categoryname.trim().isEmpty()) {
                Category existing = cateService.findByCategoryname(categoryname.trim());
                if (existing != null) {
                    errors.put("categoryname", "Tên danh mục này đã tồn tại trong hệ thống!");
                }
            }

            Part part = null;
            try {
                part = req.getPart("images1");
                if (part != null && part.getSize() > 0) {
                    String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    String lower = filename.toLowerCase();
                    if (!lower.endsWith(".jpg") && !lower.endsWith(".jpeg") && !lower.endsWith(".png") 
                            && !lower.endsWith(".webp") && !lower.endsWith(".gif")) {
                        errors.put("images1", "Định dạng file không hợp lệ! Vui lòng chọn ảnh JPG, PNG, WEBP, GIF");
                    }
                }
            } catch (Exception e) {
                errors.put("images1", "Lỗi đọc tệp tải lên: " + e.getMessage());
            }

            if (!errors.isEmpty()) {
                category.setImages(images);
                req.setAttribute("category", category);
                req.setAttribute("errors", errors);
                req.getRequestDispatcher("/views/admin/category-add.jsp").include(req, resp);
                return;
            }

            String fname = "";
            String uploadPath = Constant.DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            try {
                if (part != null && part.getSize() > 0) {
                    String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    int index = filename.lastIndexOf(".");
                    String ext = (index > 0) ? filename.substring(index) : ".png";
                    fname = "cate_" + System.currentTimeMillis() + ext;
                    part.write(uploadPath + File.separator + fname);
                    category.setImages(fname);
                } else if (images != null && !images.trim().isEmpty()) {
                    category.setImages(images.trim());
                } else {
                    category.setImages("avatar.png");
                }
            } catch (FileNotFoundException fne) {
                fne.printStackTrace();
            }

            cateService.insert(category);
            req.getSession().setAttribute("message", "Thêm mới danh mục thành công!");
            resp.sendRedirect(req.getContextPath() + "/admin/categories");

        } else if (url.contains("/admin/category/update")) {
            int categoryid = Integer.parseInt(req.getParameter("categoryid"));
            String categoryname = req.getParameter("categoryname");
            String statusStr = req.getParameter("status");
            int status = (statusStr != null && statusStr.equals("0")) ? 0 : 1;
            String images = req.getParameter("images");

            Category category = cateService.findById(categoryid);
            if (category == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/categories");
                return;
            }

            Map<String, String> errors = new HashMap<>();

            Category tempCategory = new Category();
            tempCategory.setCategoryid(categoryid);
            tempCategory.setCategoryname(categoryname != null ? categoryname.trim() : "");
            tempCategory.setStatus(status);

            Set<ConstraintViolation<Category>> violations = validator.validate(tempCategory);
            for (ConstraintViolation<Category> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }

            if (!errors.containsKey("categoryname") && categoryname != null && !categoryname.trim().isEmpty()) {
                Category existing = cateService.findByCategoryname(categoryname.trim());
                if (existing != null && existing.getCategoryid() != categoryid) {
                    errors.put("categoryname", "Tên danh mục này đã được sử dụng bởi danh mục khác!");
                }
            }

            Part part = null;
            try {
                part = req.getPart("images1");
                if (part != null && part.getSize() > 0) {
                    String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    String lower = filename.toLowerCase();
                    if (!lower.endsWith(".jpg") && !lower.endsWith(".jpeg") && !lower.endsWith(".png") 
                            && !lower.endsWith(".webp") && !lower.endsWith(".gif")) {
                        errors.put("images1", "Định dạng file không hợp lệ! Vui lòng chọn ảnh JPG, PNG, WEBP, GIF");
                    }
                }
            } catch (Exception e) {
                errors.put("images1", "Lỗi đọc tệp tải lên: " + e.getMessage());
            }

            if (!errors.isEmpty()) {
                tempCategory.setImages(category.getImages());
                req.setAttribute("cate", tempCategory);
                req.setAttribute("errors", errors);
                req.getRequestDispatcher("/views/admin/category-edit.jsp").include(req, resp);
                return;
            }

            String fileold = category.getImages();
            category.setCategoryname(categoryname.trim());
            category.setStatus(status);

            String fname = "";
            String uploadPath = Constant.DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            try {
                if (part != null && part.getSize() > 0) {
                    if (fileold != null && !fileold.startsWith("http") && !fileold.equals("avatar.png")) {
                        deleteFile(uploadPath + File.separator + fileold);
                    }
                    String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    int index = filename.lastIndexOf(".");
                    String ext = (index > 0) ? filename.substring(index) : ".png";
                    fname = "cate_" + System.currentTimeMillis() + ext;
                    part.write(uploadPath + File.separator + fname);
                    category.setImages(fname);
                } else if (images != null && !images.trim().isEmpty()) {
                    category.setImages(images.trim());
                } else {
                    category.setImages(fileold);
                }
            } catch (FileNotFoundException fne) {
                fne.printStackTrace();
            }

            cateService.update(category);
            req.getSession().setAttribute("message", "Cập nhật danh mục thành công!");
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        }
    }

    public static void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }
}
