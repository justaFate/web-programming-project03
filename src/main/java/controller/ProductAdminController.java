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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Category;
import model.Product;
import service.ICategoryService;
import service.IProductService;
import service.impl.CategoryServiceImpl;
import service.impl.ProductServiceImpl;
import util.Constant;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
@WebServlet(urlPatterns = {
    "/admin/products",
    "/admin/product/add",
    "/admin/product/insert",
    "/admin/product/edit",
    "/admin/product/update",
    "/admin/product/delete"
})
public class ProductAdminController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IProductService productService = new ProductServiceImpl();
    private ICategoryService categoryService = new CategoryServiceImpl();

    private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = validatorFactory.getValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();

        try {
            if (uri.contains("/admin/products")) {
                List<Product> list = productService.findAll();
                req.setAttribute("productList", list);
                req.getRequestDispatcher("/views/admin/product-list.jsp").include(req, resp);

            } else if (uri.contains("/admin/product/add")) {
                List<Category> categories = categoryService.findAll();
                req.setAttribute("categories", categories);
                req.getRequestDispatcher("/views/admin/product-add.jsp").include(req, resp);

            } else if (uri.contains("/admin/product/edit")) {
                int id = Integer.parseInt(req.getParameter("id"));
                Product product = productService.findById(id);
                List<Category> categories = categoryService.findAll();
                req.setAttribute("product", product);
                req.setAttribute("categories", categories);
                req.getRequestDispatcher("/views/admin/product-edit.jsp").include(req, resp);

            } else if (uri.contains("/admin/product/delete")) {
                int id = Integer.parseInt(req.getParameter("id"));
                try {
                    Product product = productService.findById(id);
                    if (product != null && product.getImages() != null && !product.getImages().startsWith("http")) {
                        deleteFile(Constant.DIR + File.separator + product.getImages());
                    }
                    productService.delete(id);
                    req.getSession().setAttribute("message", "Đã xóa sản phẩm thành công!");
                } catch (Exception e) {
                    e.printStackTrace();
                    req.getSession().setAttribute("errorMessage", "Không thể xóa sản phẩm: " + e.getMessage());
                }
                resp.sendRedirect(req.getContextPath() + "/admin/products");
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Lỗi xử lý hệ thống: " + e.getMessage());
            req.getRequestDispatcher("/views/admin/product-list.jsp").include(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();

        if (uri.contains("/admin/product/insert")) {
            insertProduct(req, resp);
        } else if (uri.contains("/admin/product/update")) {
            updateProduct(req, resp);
        }
    }

    private void insertProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productName = req.getParameter("productName");
        String description = req.getParameter("description");
        String priceStr = req.getParameter("price");
        String statusStr = req.getParameter("status");
        String categoryIdStr = req.getParameter("categoryId");
        String imagesUrl = req.getParameter("images");

        Map<String, String> errors = new HashMap<>();

        double price = 0.0;
        try {
            if (priceStr != null && !priceStr.trim().isEmpty()) {
                price = Double.parseDouble(priceStr.trim());
                if (price < 0) {
                    errors.put("price", "Giá sản phẩm không được là số âm");
                }
            } else {
                errors.put("price", "Vui lòng nhập giá sản phẩm");
            }
        } catch (NumberFormatException e) {
            errors.put("price", "Giá sản phẩm phải là một số hợp lệ");
        }

        int status = (statusStr != null && statusStr.equals("0")) ? 0 : 1;

        Category category = null;
        try {
            if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
                int categoryId = Integer.parseInt(categoryIdStr.trim());
                category = categoryService.findById(categoryId);
                if (category == null) {
                    errors.put("categoryId", "Danh mục đã chọn không tồn tại");
                }
            } else {
                errors.put("categoryId", "Vui lòng chọn danh mục cho sản phẩm");
            }
        } catch (NumberFormatException e) {
            errors.put("categoryId", "Mã danh mục không hợp lệ");
        }

        Product product = new Product();
        product.setProductName(productName != null ? productName.trim() : "");
        product.setDescription(description != null ? description.trim() : "");
        product.setPrice(price);
        product.setStatus(status);
        product.setCreateDate(new Date());
        product.setCategory(category);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        for (ConstraintViolation<Product> violation : violations) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        Part part = null;
        try {
            part = req.getPart("imageFile");
            if (part != null && part.getSize() > 0) {
                String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                String lower = filename.toLowerCase();
                if (!lower.endsWith(".jpg") && !lower.endsWith(".jpeg") && !lower.endsWith(".png") 
                        && !lower.endsWith(".webp") && !lower.endsWith(".gif")) {
                    errors.put("imageFile", "Chỉ chấp nhận file ảnh (JPG, PNG, WEBP, GIF)");
                }
            }
        } catch (Exception e) {
            errors.put("imageFile", "Lỗi đọc file tải lên: " + e.getMessage());
        }

        if (!errors.isEmpty()) {
            req.setAttribute("product", product);
            req.setAttribute("categories", categoryService.findAll());
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/views/admin/product-add.jsp").include(req, resp);
            return;
        }

        String imageName = "";
        try {
            if (part != null && part.getSize() > 0) {
                String uploadPath = Constant.DIR;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                int index = filename.lastIndexOf(".");
                String ext = (index > 0) ? filename.substring(index) : ".png";
                imageName = "prod_" + System.currentTimeMillis() + ext;
                part.write(uploadPath + File.separator + imageName);
                product.setImages(imageName);
            } else if (imagesUrl != null && !imagesUrl.trim().isEmpty()) {
                product.setImages(imagesUrl.trim());
            } else {
                product.setImages("product-default.png");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            productService.insert(product);
            req.getSession().setAttribute("message", "Thêm sản phẩm mới thành công!");
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("product", product);
            req.setAttribute("categories", categoryService.findAll());
            req.setAttribute("errorMessage", "Không thể thêm sản phẩm: " + e.getMessage());
            req.getRequestDispatcher("/views/admin/product-add.jsp").include(req, resp);
        }
    }

    private void updateProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int productId = Integer.parseInt(req.getParameter("productId"));
        Product oldProduct = productService.findById(productId);
        if (oldProduct == null) {
            req.getSession().setAttribute("errorMessage", "Sản phẩm không tồn tại!");
            resp.sendRedirect(req.getContextPath() + "/admin/products");
            return;
        }

        String productName = req.getParameter("productName");
        String description = req.getParameter("description");
        String priceStr = req.getParameter("price");
        String statusStr = req.getParameter("status");
        String categoryIdStr = req.getParameter("categoryId");
        String imagesUrl = req.getParameter("images");

        Map<String, String> errors = new HashMap<>();

        double price = 0.0;
        try {
            if (priceStr != null && !priceStr.trim().isEmpty()) {
                price = Double.parseDouble(priceStr.trim());
                if (price < 0) {
                    errors.put("price", "Giá sản phẩm không được là số âm");
                }
            } else {
                errors.put("price", "Vui lòng nhập giá sản phẩm");
            }
        } catch (NumberFormatException e) {
            errors.put("price", "Giá sản phẩm phải là một số hợp lệ");
        }

        int status = (statusStr != null && statusStr.equals("0")) ? 0 : 1;

        Category category = null;
        try {
            if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
                int categoryId = Integer.parseInt(categoryIdStr.trim());
                category = categoryService.findById(categoryId);
                if (category == null) {
                    errors.put("categoryId", "Danh mục đã chọn không tồn tại");
                }
            } else {
                errors.put("categoryId", "Vui lòng chọn danh mục cho sản phẩm");
            }
        } catch (NumberFormatException e) {
            errors.put("categoryId", "Mã danh mục không hợp lệ");
        }

        Product product = new Product();
        product.setProductId(productId);
        product.setProductName(productName != null ? productName.trim() : "");
        product.setDescription(description != null ? description.trim() : "");
        product.setPrice(price);
        product.setStatus(status);
        product.setCreateDate(oldProduct.getCreateDate());
        product.setCategory(category);
        product.setImages(oldProduct.getImages());

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        for (ConstraintViolation<Product> violation : violations) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        Part part = null;
        try {
            part = req.getPart("imageFile");
            if (part != null && part.getSize() > 0) {
                String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                String lower = filename.toLowerCase();
                if (!lower.endsWith(".jpg") && !lower.endsWith(".jpeg") && !lower.endsWith(".png") 
                        && !lower.endsWith(".webp") && !lower.endsWith(".gif")) {
                    errors.put("imageFile", "Chỉ chấp nhận file ảnh (JPG, PNG, WEBP, GIF)");
                }
            }
        } catch (Exception e) {
            errors.put("imageFile", "Lỗi đọc file tải lên: " + e.getMessage());
        }

        if (!errors.isEmpty()) {
            req.setAttribute("product", product);
            req.setAttribute("categories", categoryService.findAll());
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/views/admin/product-edit.jsp").include(req, resp);
            return;
        }

        try {
            if (part != null && part.getSize() > 0) {
                String uploadPath = Constant.DIR;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                if (oldProduct.getImages() != null && !oldProduct.getImages().startsWith("http") 
                        && !oldProduct.getImages().equals("product-default.png")) {
                    deleteFile(uploadPath + File.separator + oldProduct.getImages());
                }

                String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                int index = filename.lastIndexOf(".");
                String ext = (index > 0) ? filename.substring(index) : ".png";
                String fname = "prod_" + System.currentTimeMillis() + ext;
                part.write(uploadPath + File.separator + fname);
                product.setImages(fname);
            } else if (imagesUrl != null && !imagesUrl.trim().isEmpty()) {
                product.setImages(imagesUrl.trim());
            } else {
                product.setImages(oldProduct.getImages());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            productService.update(product);
            req.getSession().setAttribute("message", "Cập nhật sản phẩm thành công!");
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("product", product);
            req.setAttribute("categories", categoryService.findAll());
            req.setAttribute("errorMessage", "Không thể cập nhật sản phẩm: " + e.getMessage());
            req.getRequestDispatcher("/views/admin/product-edit.jsp").include(req, resp);
        }
    }

    private static void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
