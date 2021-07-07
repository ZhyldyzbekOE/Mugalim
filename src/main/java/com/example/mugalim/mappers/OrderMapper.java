package com.example.mugalim.mappers;

import com.example.mugalim.models.Order;
import com.example.mugalim.models.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order toOrder(OrderDto orderDto);
    OrderDto toOrderDto(Order order);

    List<Order> toOrders(List<OrderDto> orderDtos);
    List<OrderDto> toOrderDtos(List<Order> orders);

}
