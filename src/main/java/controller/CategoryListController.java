package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import model.Category;
import service.CategoryService;
import service.impl.CategoryServiceImpl;

@WebServlet(urlPatterns = { "/admin/category/list" })  
public class CategoryListController extends HttpServlet { 
    private static final long serialVersionUID = 1L;
    CategoryService cateService = new CategoryServiceImpl();

    @Override  
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { 
        List<Category> cateList = cateService.getAll(); 
        req.setAttribute("cateList", cateList); 
        RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/list-category.jsp"); 
        dispatcher.forward(req, resp); 
    } 
}

