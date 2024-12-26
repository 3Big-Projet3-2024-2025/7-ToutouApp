package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Request;

import java.util.List;

public interface IRequestService {
public List<Request> getRequests();
public Request  addRequest(Request request);
public Request updateRequest(Request request);
public  void deleteRequest(int id);
public List<Request> getRequestsByUserId(int userId);
}
