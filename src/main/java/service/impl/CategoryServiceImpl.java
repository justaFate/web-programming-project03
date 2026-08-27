package service.impl; 

import java.io.File; 
import java.util.List; 
import dao.CategoryDao; 
import dao.impl.CategoryDaoImpl; 
import model.Category; 
import service.CategoryService; 
import util.Constant;

public class CategoryServiceImpl implements CategoryService { 
    CategoryDao categoryDao = new CategoryDaoImpl();

    @Override  
    public void insert(Category category) { 
        categoryDao.insert(category); 
    } 

    @Override
    public void edit(Category newCategory) {
        Category oldCategory = categoryDao.get(newCategory.getId());
        if (oldCategory != null) {
            oldCategory.setName(newCategory.getName());
            if (newCategory.getIcon() != null) {
                String fileName = oldCategory.getIcon();
                if (fileName != null) {
                    File file = new File(Constant.DIR + "/" + fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                oldCategory.setIcon(newCategory.getIcon());
            }
            categoryDao.edit(oldCategory);
        }
    }

    @Override  
    public void delete(int id) { 
        categoryDao.delete(id); 
    }

    @Override  
    public Category get(int id) { 
        return categoryDao.get(id); 
    }

    @Override  
    public Category get(String name) { 
        return categoryDao.get(name); 
    }

    @Override  
    public List<Category> getAll() { 
        return categoryDao.getAll(); 
    }

    @Override  
    public List<Category> search(String catename) { 
        return categoryDao.search(catename); 
    }
}