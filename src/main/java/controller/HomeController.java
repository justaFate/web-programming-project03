package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;
import service.IProductService;
import service.impl.ProductServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/home", "/index"})
public class HomeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Product> topProducts = productService.findTop10Latest();
            req.setAttribute("topProducts", topProducts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        req.getRequestDispatcher("/views/index.jsp").include(req, resp);
    }
}
