package com.example.mugalim.services.impl;

import com.example.mugalim.dao.SubscriberRepository;
import com.example.mugalim.mappers.SubscriberMapper;
import com.example.mugalim.models.Subscriber;
import com.example.mugalim.models.dto.SubscriberDto;
import com.example.mugalim.services.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Override
    public SubscriberDto saveIfNotExists(SubscriberDto subscriberDto) {
        Subscriber subscriberFromDb = subscriberRepository.findByPhone(subscriberDto.getPhone());
        if (subscriberFromDb == null){
            Subscriber subscriberForSave = subscriberRepository.save(SubscriberMapper.INSTANCE.toSubscriber(subscriberDto));
            System.out.println("На сохранение " + subscriberForSave);
            return SubscriberMapper.INSTANCE.toSubscriberDto(subscriberForSave);
        }else {
            return SubscriberMapper.INSTANCE.toSubscriberDto(subscriberFromDb);
        }
    }
}
