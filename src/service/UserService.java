package service;

import model.User;
import structures.MyHashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserService {
    private MyHashMap<String, User> users = new MyHashMap<>();

    public boolean loadUsers(String filePath) {
        users.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    // Skip header if it looks like a header
                    if (line.toLowerCase().contains("username") || line.toLowerCase().contains("user")) {
                        firstLine = false;
                        continue;
                    }
                    firstLine = false;
                }

                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    String name = parts[2].trim();

                    if (!username.isEmpty()) {
                        users.put(username, new User(username, password, name));
                    }
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public int getUserCount() {
        return users.size();
    }
}
