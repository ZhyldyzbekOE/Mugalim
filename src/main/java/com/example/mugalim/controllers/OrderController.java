package com.example.mugalim.controllers;

import com.example.mugalim.models.Order;
import com.example.mugalim.models.Responces.Response;
import com.example.mugalim.models.dto.OrderDto;
import com.example.mugalim.models.dto.OrderSubDto;
import com.example.mugalim.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/sending")
    public Response sendOrder(@RequestBody OrderSubDto orderSubDto){
        System.out.println("Вход " + orderSubDto);
        return orderService.saveOrder(orderSubDto);
    }

    @GetMapping("/getOrdersWhereStatusNew")
    public List<OrderDto> getOrdersWhereStatusNew(){
        return orderService.allOrdersStatusNew();
    }

    @GetMapping("/getAllOrders")
    public List<OrderDto> getAllOrders(){
        return orderService.allOrders();
    }

    @PostMapping("/processingOrder")
    public Response processingOrder(@RequestParam Long ord_id){ // id order
        return orderService.processingOrder(ord_id);
    }

    @PostMapping("/acceptOrder")
    public Response acceptOrder(@RequestParam Long sub_id){ // id subs
        return orderService.acceptOrder(sub_id);
    }

    @PostMapping("/rejectOrder") // id subs
    public Response rejectOrder(@RequestParam Long sub_id, @RequestParam String comment){
        return orderService.rejectOrder(sub_id, comment);
    }

}
