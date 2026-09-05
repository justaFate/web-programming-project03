package model;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p ORDER BY p.productId DESC")
@NamedQuery(name = "Product.findTop10Latest", query = "SELECT p FROM Product p WHERE p.status = 1 ORDER BY p.createDate DESC, p.productId DESC")
@NamedQuery(name = "Product.count", query = "SELECT COUNT(p) FROM Product p")
@NamedQuery(name = "Product.findByCategory", query = "SELECT p FROM Product p WHERE p.category.categoryid = :categoryId ORDER BY p.productId DESC")
@NamedQuery(name = "Product.countByCategory", query = "SELECT COUNT(p) FROM Product p WHERE p.category.categoryid = :categoryId")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
    private int productId;

    @NotEmpty(message = "Tên sản phẩm không được để trống")
    @Size(min = 2, max = 200, message = "Tên sản phẩm phải từ 2 đến 200 ký tự")
    @Column(name = "productName", columnDefinition = "nvarchar(200) not null")
    private String productName;

    @Column(name = "description", columnDefinition = "nvarchar(max) null")
    private String description;

    @PositiveOrZero(message = "Giá sản phẩm phải lớn hơn hoặc bằng 0")
    @Column(name = "price")
    private double price;

    @Column(name = "images", columnDefinition = "nvarchar(500) null")
    private String images;

    @Column(name = "status")
    private int status = 1; // 1: đang bán, 0: tạm dừng

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createDate")
    private Date createDate = new Date();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId")
    private Category category;

    public Product() {
    }

    public Product(int productId, String productName, String description, double price, String images, int status, Date createDate, Category category) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.images = images;
        this.status = status;
        this.createDate = createDate != null ? createDate : new Date();
        this.category = category;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
