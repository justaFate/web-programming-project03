package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import model.Category;
import service.CategoryService;
import service.impl.CategoryServiceImpl;

@WebServlet(urlPatterns = { "/admin/category/edit" })  
public class CategoryEditController extends HttpServlet { 
    private static final long serialVersionUID = 1L;
    CategoryService cateService = new CategoryServiceImpl(); 
    
    @Override  
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { 
        String id = req.getParameter("id");  
        if (id != null) {
            Category category = cateService.get(Integer.parseInt(id));
            req.setAttribute("category", category); 
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/edit-category.jsp"); 
        dispatcher.forward(req, resp); 
    }

    @Override  
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { 
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String icon = req.getParameter("icon");

        Category category = new Category();
        if (id != null && !id.isEmpty()) {
            category.setId(Integer.parseInt(id));
        }
        category.setName(name);
        category.setIcon(icon);

        cateService.edit(category); 
        resp.sendRedirect(req.getContextPath() + "/admin/category/list");  
    }
}