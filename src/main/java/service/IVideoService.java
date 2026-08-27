package service;

import java.util.List;
import model.Video;

public interface IVideoService {
    void insert(Video video);
    void update(Video video);
    void delete(String videoId);
    Video findById(String videoId);
    List<Video> findAll();
    List<Video> findAll(int page, int pagesize);
    List<Video> searchByTitle(String title);
    List<Video> findByCategoryId(int categoryId);
    int count();
}