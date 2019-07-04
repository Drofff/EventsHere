package service;

import entity.Event;
import repository.EventRepository;
import repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class StorageService implements Serializable {

    private static StorageService storageService;

    private static final String PHOTO_BASE = "data:image/jpeg;base64,";

    private static String base_url;

    private StorageService() {}

    public static StorageService getInstance() {

        if (storageService == null) {
            storageService = new StorageService();
            base_url = System.getProperty("user.dir") + PropertiesService.getInstance().getRootDir();
        }

        return storageService;

    }

    public static Map<String, Event> putPhotos(List<Event> eventList, HttpServletRequest request) {

        StorageService storageService = StorageService.getInstance();
        UserRepository userRepository = UserRepository.getInstance(request.getSession());

        Map<String, Event> photoAndEvents = new LinkedHashMap<>();

        for (Event event : eventList) {

            try {

                photoAndEvents.put(storageService.getPhoto(userRepository.findById(event.getOwner().getUserId()), event.getPhotoUrl()), event);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return photoAndEvents;

    }

    public static Map<String, Map.Entry<String, Event>> putPhotos(Map<String, Event> eventMap, HttpServletRequest request) {

        StorageService storageService = StorageService.getInstance();
        UserRepository userRepository = UserRepository.getInstance(request.getSession());

        Map<String, Map.Entry<String, Event>> photoAndEvents = new LinkedHashMap<>();

        for (Map.Entry<String, Event> event : eventMap.entrySet()) {

            try {

                photoAndEvents.put(storageService.getPhoto(userRepository.findById(event.getValue().getOwner().getUserId()), event.getValue().getPhotoUrl()), event);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return photoAndEvents;

    }

    public void savePhoto(String username, Part part) throws IOException {

        Path basePath = Paths.get(base_url);
        Path userDir = Paths.get(base_url, username);
        Path filePath = Paths.get(base_url, username, part.getSubmittedFileName());

        if (Files.notExists(basePath)) {

            Files.createDirectory(basePath);

            if (Files.notExists(userDir)) {
                Files.createDirectory(userDir);
            }

        }

        if (new File(userDir.toString()).list().length < 5 && Files.notExists(filePath)) {
            Files.copy(part.getInputStream(), filePath);
        }

    }

    public Integer getPhotosCount(String username) {

        try {

            return new File(Paths.get(base_url, username).toString()).list().length;

        } catch (Exception e) {

            return 0;

        }

    }

    public void delete(String username, String fileName) throws IOException {

        Path file = Paths.get(base_url, username, fileName);

        if (Files.exists(file)) {

            Files.delete(file);

        }

    }

    public Map<String, String> getAllPhotos(String username) throws IOException {

        Map<String, String> base64Photos = new HashMap<>();

        String [] photos = new File(Paths.get(base_url, username).toString()).list();

        for (String s : photos) {
            base64Photos.put(s, getPhoto(username, s));
        }

        return base64Photos;

    }

    public String getPhoto(String username, String photoUrl) throws IOException {

        Path filePath = Paths.get(base_url, username, photoUrl);

        if (Files.notExists(filePath)) {
            return null;
        }

        return PHOTO_BASE + new String(Base64.getEncoder().encode(Files.readAllBytes(filePath)));

    }

}
