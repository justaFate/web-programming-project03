package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
import model.Product;
import service.ICategoryService;
import service.IProductService;
import service.impl.CategoryServiceImpl;
import service.impl.ProductServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/product", "/product/detail"})
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IProductService productService = new ProductServiceImpl();
    private ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();

        if (uri.contains("/product/detail")) {
            showDetail(req, resp);
        } else {
            showProductsWithPaging(req, resp);
        }
    }

    private void showProductsWithPaging(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int pageSize = 6; // Yêu cầu 6 sản phẩm / trang
        int page = 1;

        String pageStr = req.getParameter("page");
        if (pageStr != null && !pageStr.trim().isEmpty()) {
            try {
                page = Math.max(1, Integer.parseInt(pageStr.trim()));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        String cidStr = req.getParameter("cid");
        List<Product> products;
        long totalProducts;
        Integer selectedCid = null;

        try {
            if (cidStr != null && !cidStr.trim().isEmpty()) {
                selectedCid = Integer.parseInt(cidStr.trim());
                products = productService.findByCategoryId(selectedCid, page, pageSize);
                totalProducts = productService.countByCategoryId(selectedCid);
            } else {
                products = productService.findAll(page, pageSize);
                totalProducts = productService.count();
            }
        } catch (Exception e) {
            e.printStackTrace();
            products = new ArrayList<>();
            totalProducts = 0;
        }

        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        if (totalPages == 0) totalPages = 1;

        List<Category> categories = categoryService.findAll();

        req.setAttribute("products", products);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalProducts", totalProducts);
        req.setAttribute("pageSize", pageSize);
        req.setAttribute("selectedCid", selectedCid);
        req.setAttribute("categories", categories);

        req.getRequestDispatcher("/views/web/product.jsp").include(req, resp);
    }

    private void showDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/product");
            return;
        }

        try {
            int id = Integer.parseInt(idStr.trim());
            Product product = productService.findById(id);

            if (product == null) {
                req.getSession().setAttribute("errorMessage", "Sản phẩm không tồn tại hoặc đã bị gỡ bỏ!");
                resp.sendRedirect(req.getContextPath() + "/product");
                return;
            }

            // Lấy sản phẩm liên quan cùng danh mục (nếu có)
            List<Product> relatedProducts = new ArrayList<>();
            if (product.getCategory() != null) {
                List<Product> rawRelated = productService.findByCategoryId(product.getCategory().getCategoryid(), 1, 5);
                for (Product p : rawRelated) {
                    if (p.getProductId() != product.getProductId()) {
                        relatedProducts.add(p);
                        if (relatedProducts.size() == 4) break;
                    }
                }
            }

            req.setAttribute("product", product);
            req.setAttribute("relatedProducts", relatedProducts);
            req.getRequestDispatcher("/views/web/product-detail.jsp").include(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/product");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Lỗi xử lý dữ liệu: " + e.getMessage());
            req.getRequestDispatcher("/views/web/product.jsp").include(req, resp);
        }
    }
}
