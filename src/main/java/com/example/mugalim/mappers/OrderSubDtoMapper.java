package com.example.mugalim.mappers;

import com.example.mugalim.models.dto.OrderDto;
import com.example.mugalim.models.dto.OrderSubDto;
import com.example.mugalim.models.dto.SubscriberDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderSubDtoMapper {

    OrderSubDtoMapper INSTANCE = Mappers.getMapper(OrderSubDtoMapper.class);

    OrderDto toOrderDto(OrderSubDto orderSubDto);
    SubscriberDto toSubscriberDto(OrderSubDto orderSubDto);
}
