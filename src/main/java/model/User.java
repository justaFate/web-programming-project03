package model;

import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
@NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập từ 3 đến 50 ký tự")
    @Column(name = "username", columnDefinition = "nvarchar(50) not null unique")
    private String username;

    @NotEmpty(message = "Mật khẩu không được để trống")
    @Column(name = "password", columnDefinition = "nvarchar(255) not null")
    private String password;

    @NotEmpty(message = "Họ và tên không được để trống")
    @Size(min = 2, max = 100, message = "Họ và tên từ 2 đến 100 ký tự")
    @Column(name = "fullname", columnDefinition = "nvarchar(100) not null")
    private String fullname;

    @Pattern(regexp = "^(0[3|5|7|8|9][0-9]{8})?$", message = "Số điện thoại không hợp lệ (phải gồm 10 chữ số, bắt đầu bằng 03, 05, 07, 08, 09)")
    @Column(name = "phone", columnDefinition = "nvarchar(20) null")
    private String phone;

    @Column(name = "images", columnDefinition = "nvarchar(500) null")
    private String images;

    @Column(name = "role")
    private int role; // 1: admin, 0: user

    public User() {
    }

    public User(int id, String username, String password, String fullname, String phone, String images, int role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.phone = phone;
        this.images = images;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}

