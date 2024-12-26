package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Request;
import be.projet3.toutouapp.repositories.jpa.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class RequestService implements IRequestService{

    @Autowired
    private RequestRepository requestRepository;



    @Override
    public List<Request> getRequests() {
        return requestRepository.findAll();
    }


    @Override
    public Request addRequest(Request request) {
        return requestRepository.save(request);
    }


    @Override
    public Request updateRequest(Request request) {
        if (requestRepository.existsById(request.getRequestId())) {
            return requestRepository.save(request);
        }
        return requestRepository.save(request);
    }


    @Override
    public void deleteRequest(int id) {
        if (requestRepository.existsById(id)) {
            requestRepository.deleteById(id);
        }

    }

    @Override
    public List<Request> getRequestsByUserId(int userId) {
        return requestRepository.findByOwner_Id(userId);
    }
}
