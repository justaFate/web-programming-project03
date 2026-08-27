package service.impl;

import java.util.List;
import dao.IVideoDao;
import dao.impl.VideoDaoImpl;
import model.Video;
import service.IVideoService;

public class VideoServiceImpl implements IVideoService {

    private IVideoDao videoDao = new VideoDaoImpl();

    @Override
    public void insert(Video video) {
        // Kiểm tra xem ID video đã tồn tại trước khi thêm mới
        Video existing = videoDao.findById(video.getVideoId());
        if (existing == null) {
            videoDao.insert(video);
        }
    }

    @Override
    public void update(Video video) {
        // Đảm bảo video có tồn tại mới cập nhật
        Video existing = videoDao.findById(video.getVideoId());
        if (existing != null) {
            videoDao.update(video);
        }
    }

    @Override
    public void delete(String videoId) {
        try {
            videoDao.delete(videoId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Video findById(String videoId) {
        return videoDao.findById(videoId);
    }

    @Override
    public List<Video> findAll() {
        return videoDao.findAll();
    }

    @Override
    public List<Video> findAll(int page, int pagesize) {
        return videoDao.findAll(page, pagesize);
    }

    @Override
    public List<Video> searchByTitle(String title) {
        return videoDao.searchByTitle(title);
    }

    @Override
    public List<Video> findByCategoryId(int categoryId) {
        return videoDao.findByCategoryId(categoryId);
    }

    @Override
    public int count() {
        return videoDao.count();
    }
}