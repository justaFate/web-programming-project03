package dao;
import java.util.List;
import dao.impl.CategoryDaoImpl;
import model.Category;

public class TestCRUD {
    public static void main(String[] args) {
        // 1. Khởi tạo đối tượng DAO từ class triển khai
        ICategoryDao cateDao = new CategoryDaoImpl();

        // 2. TEST CREATE (Thêm mới)
        Category newCate = new Category();
        newCate.setCategoryname("Samsung");
        newCate.setImages("samsung.png");
        newCate.setStatus(1);
        cateDao.insert(newCate);
        System.out.println("-> [CREATE] Đã thêm mới danh mục thành công!");

        // 3. TEST READ (Lấy danh sách và tìm kiếm)
        List<Category> list = cateDao.findAll();
        System.out.println("-> [READ ALL] Tổng số category hiện có: " + list.size());
        for (Category c : list) {
            System.out.println("   + ID: " + c.getCategoryid() + " | Name: " + c.getCategoryname());
        }

        // 4. TEST UPDATE (Sửa thông tin)
        Category cateUpdate = cateDao.findById(newCate.getCategoryid());
        if (cateUpdate != null) {
            cateUpdate.setCategoryname("Samsung Galaxy");
            cateDao.update(cateUpdate);
            System.out.println("-> [UPDATE] Đã cập nhật tên thành: " + cateUpdate.getCategoryname());
        }

        // 5. TEST DELETE (Xóa danh mục)
        try {
            cateDao.delete(newCate.getCategoryid());
            System.out.println("-> [DELETE] Đã xóa thành công Category ID: " + newCate.getCategoryid());
        } catch (Exception e) {
            System.err.println("-> [DELETE ERROR] Lỗi khi xóa: " + e.getMessage());
            e.printStackTrace();
        }
    }
}