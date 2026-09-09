package controller;

import jakarta.servlet.http.HttpSession;
import model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.ICategoryService;
import util.Constant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/categories")
    public String listCategories(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Category> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = categoryService.searchByName(keyword.trim());
            model.addAttribute("keyword", keyword.trim());
        } else {
            list = categoryService.findAll();
        }
        model.addAttribute("listcate", list);
        return "admin/category-list";
    }

    @GetMapping("/category/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category-add";
    }

    @PostMapping("/category/insert")
    public String insertCategory(@RequestParam("categoryname") String categoryname,
                                 @RequestParam(value = "status", defaultValue = "1") int status,
                                 @RequestParam(value = "images", required = false) String images,
                                 @RequestParam(value = "images1", required = false) MultipartFile file,
                                 Model model, HttpSession session) {
        Map<String, String> errors = new HashMap<>();
        if (categoryname == null || categoryname.trim().isEmpty()) {
            errors.put("categoryname", "Tên danh mục không được để trống!");
        } else if (categoryname.trim().length() < 2 || categoryname.trim().length() > 50) {
            errors.put("categoryname", "Tên danh mục phải từ 2 đến 50 ký tự!");
        } else if (categoryService.findByCategoryname(categoryname.trim()) != null) {
            errors.put("categoryname", "Tên danh mục này đã tồn tại trong hệ thống!");
        }

        Category category = new Category();
        category.setCategoryname(categoryname != null ? categoryname.trim() : "");
        category.setStatus(status);

        if (!errors.isEmpty()) {
            category.setImages(images);
            model.addAttribute("category", category);
            model.addAttribute("errors", errors);
            return "admin/category-add";
        }

        String fname = handleFileUpload(file, images);
        category.setImages(fname);

        categoryService.insert(category);
        session.setAttribute("message", "Thêm danh mục mới thành công!");
        return "redirect:/admin/categories";
    }

    @GetMapping("/category/edit")
    public String editCategoryForm(@RequestParam("id") int id, Model model) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return "redirect:/admin/categories";
        }
        model.addAttribute("cate", category);
        return "admin/category-edit";
    }

    @PostMapping("/category/update")
    public String updateCategory(@RequestParam("categoryid") int categoryid,
                                 @RequestParam("categoryname") String categoryname,
                                 @RequestParam(value = "status", defaultValue = "1") int status,
                                 @RequestParam(value = "images", required = false) String images,
                                 @RequestParam(value = "images1", required = false) MultipartFile file,
                                 Model model, HttpSession session) {
        Category category = categoryService.findById(categoryid);
        if (category == null) {
            session.setAttribute("errorMessage", "Không tìm thấy danh mục để cập nhật!");
            return "redirect:/admin/categories";
        }

        Map<String, String> errors = new HashMap<>();
        if (categoryname == null || categoryname.trim().isEmpty()) {
            errors.put("categoryname", "Tên danh mục không được để trống!");
        } else if (categoryname.trim().length() < 2 || categoryname.trim().length() > 50) {
            errors.put("categoryname", "Tên danh mục phải từ 2 đến 50 ký tự!");
        } else {
            Category existing = categoryService.findByCategoryname(categoryname.trim());
            if (existing != null && existing.getCategoryid() != categoryid) {
                errors.put("categoryname", "Tên danh mục này đã tồn tại cho danh mục khác!");
            }
        }

        if (!errors.isEmpty()) {
            category.setCategoryname(categoryname);
            category.setStatus(status);
            model.addAttribute("cate", category);
            model.addAttribute("errors", errors);
            return "admin/category-edit";
        }

        String oldFile = category.getImages();
        category.setCategoryname(categoryname.trim());
        category.setStatus(status);

        if (file != null && !file.isEmpty()) {
            if (oldFile != null && !oldFile.startsWith("http")) {
                deleteFileQuietly(Constant.DIR + File.separator + oldFile);
            }
            String fname = handleFileUpload(file, null);
            category.setImages(fname);
        } else if (images != null && !images.trim().isEmpty()) {
            category.setImages(images.trim());
        }

        categoryService.update(category);
        session.setAttribute("message", "Cập nhật danh mục thành công!");
        return "redirect:/admin/categories";
    }

    @GetMapping("/category/delete")
    public String deleteCategory(@RequestParam("id") int id, HttpSession session) {
        try {
            Category category = categoryService.findById(id);
            if (category != null && category.getImages() != null && !category.getImages().startsWith("http")) {
                deleteFileQuietly(Constant.DIR + File.separator + category.getImages());
            }
            categoryService.delete(id);
            session.setAttribute("message", "Đã xóa danh mục thành công!");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Không thể xóa danh mục: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    private String handleFileUpload(MultipartFile file, String defaultImage) {
        if (file != null && !file.isEmpty()) {
            String uploadPath = Constant.DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String originalFilename = file.getOriginalFilename();
            String ext = ".png";
            if (originalFilename != null && originalFilename.lastIndexOf(".") > 0) {
                ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fname = "cate_" + System.currentTimeMillis() + ext;
            try {
                file.transferTo(new File(uploadPath + File.separator + fname));
                return fname;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (defaultImage != null && !defaultImage.trim().isEmpty()) {
            return defaultImage.trim();
        }
        return "avatar.png";
    }

    private void deleteFileQuietly(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (Exception ignored) {}
    }
}
