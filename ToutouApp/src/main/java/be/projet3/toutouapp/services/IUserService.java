package be.projet3.toutouapp.services;
import be.projet3.toutouapp.models.User;

import java.util.List;

public interface IUserService {
    public User addUser(User user);
    public List<User> getAllUsers();
    public User getUserById(int id);
    public User updateUser(User user);
    public  void deleteUser(int id);
}