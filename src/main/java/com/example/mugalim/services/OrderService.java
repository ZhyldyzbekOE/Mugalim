package com.example.mugalim.services;

import com.example.mugalim.models.Order;
import com.example.mugalim.models.Responces.Response;
import com.example.mugalim.models.dto.OrderDto;
import com.example.mugalim.models.dto.OrderSubDto;

import java.util.List;

public interface OrderService {

    Response saveOrder(OrderSubDto orderSubDto);

    List<OrderDto> allOrders();

    List<OrderDto> allOrdersStatusNew();

    Response processingOrder(Long id);

    Response acceptOrder(Long id);

    Response rejectOrder(Long id, String comment);
}
