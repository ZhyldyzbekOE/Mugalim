package com.example.mugalim.services;

import com.example.mugalim.models.dto.SubscriberDto;

public interface SubscriberService {

    SubscriberDto saveIfNotExists(SubscriberDto subscriberDto);
}
