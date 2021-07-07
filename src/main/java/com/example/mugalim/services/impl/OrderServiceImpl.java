package com.example.mugalim.services.impl;

import com.example.mugalim.dao.OrderRepository;
import com.example.mugalim.enums.OrderStatus;
import com.example.mugalim.exceptions.OrderException;
import com.example.mugalim.mappers.OrderMapper;
import com.example.mugalim.mappers.OrderSubDtoMapper;
import com.example.mugalim.mappers.SubscriberMapper;
import com.example.mugalim.models.Order;
import com.example.mugalim.models.Responces.Response;
import com.example.mugalim.models.dto.OrderDto;
import com.example.mugalim.models.dto.OrderSubDto;
import com.example.mugalim.models.dto.SubscriberDto;
import com.example.mugalim.services.OrderService;
import com.example.mugalim.services.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SubscriberService subscriberService;

    @Override
    public Response saveOrder(OrderSubDto orderSubDto) {
        Response response = new Response();
        // поделил на два объекта
        SubscriberDto subscriberDto = OrderSubDtoMapper.INSTANCE.toSubscriberDto(orderSubDto);
        OrderDto orderDto = OrderSubDtoMapper.INSTANCE.toOrderDto(orderSubDto);
        //

        // проверил на налличие пользователся в БД
        SubscriberDto subscriberDtoFromDbOrNew = subscriberService.saveIfNotExists(subscriberDto);

        // соеденил пользователя к order
        orderDto.setSubscriber(subscriberDtoFromDbOrNew);

        int codeCheck = lastCurrentOrder(orderDto);
        if (codeCheck == 1 || codeCheck == 0){
            saveNewOrder(orderDto);
            response.setCode(1);
            response.setMessage("Заявка успешно принято!");
            return response;
        }
        if (codeCheck == 2){
            response.setCode(0);
            response.setMessage("Уважаемый абонент, ваша заявка находится на стадии рассмотрении. Дождитесь ответа!");
            return response;
        }
        response.setCode(0);
        response.setMessage("Повторите попытку!");
        return response;
    }

    @Override
    public List<OrderDto> allOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto>orderDtos = orders.stream()
                .map(x -> {
                    OrderDto orderDto = new OrderDto();
                    orderDto.setId(x.getId());
                    orderDto.setSchoolName(x.getSchoolName());
                    orderDto.setSchoolAddress(x.getSchoolAddress());
                    orderDto.setAddDate(x.getAddDate());
                    orderDto.setEndDate(x.getEndDate());
                    orderDto.setNaviDate(x.getNaviDate());
                    orderDto.setComment(x.getComment());
                    orderDto.setSubscriber(SubscriberMapper.INSTANCE.toSubscriberDto(x.getSubscriber()));
                    orderDto.setOrderStatus(x.getOrderStatus());
                    return orderDto;
                })
                .collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public List<OrderDto> allOrdersStatusNew() {
        List<Order> statusNew = orderRepository.findAllByOrderStatusAndEndDate(OrderStatus.NEW, null);
        List<OrderDto> orderDtos = statusNew.stream()
                .map(x ->{
                    OrderDto orderDto = new OrderDto();
                    orderDto.setId(x.getId());
                    orderDto.setSchoolName(x.getSchoolName());
                    orderDto.setSchoolAddress(x.getSchoolAddress());
                    orderDto.setAddDate(x.getAddDate());
                    orderDto.setEndDate(x.getEndDate());
                    orderDto.setNaviDate(x.getNaviDate());
                    orderDto.setComment(x.getComment());
                    orderDto.setSubscriber(SubscriberMapper.INSTANCE.toSubscriberDto(x.getSubscriber()));
                    orderDto.setOrderStatus(x.getOrderStatus());
                    return orderDto;
                })
                .collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public Response processingOrder(Long id) {
        Response response = new Response();
        if (orderRepository.existsById(id)){
            Order order = orderRepository.findById(id).get();
            if (order.getOrderStatus().equals(OrderStatus.NEW) && order.getEndDate()==null){
                order.setEndDate(new Date());
                order.setNaviDate(new Date());
                orderRepository.save(order);
                Order order1 = new Order();
                order1.setAddDate(new Date());
                order1.setOrderStatus(OrderStatus.PROCESSED);
                order1.setSchoolAddress(order.getSchoolAddress());
                order1.setSchoolName(order.getSchoolName());
                order1.setSubscriber(order.getSubscriber());
                orderRepository.save(order1);
                response.setCode(1);
                response.setMessage("Ваша заявка принята на рассмотрение!");
                return response;
            }else {
                response.setCode(0);
                response.setMessage("Невозможно принять заявку на рассмотрение!");
                return response;
            }
        }else {
            response.setCode(0);
            response.setMessage("Результаты по данному ID не найдены. Введите корректные данные!");
            return response;
        }
    }

    @Override
    public Response acceptOrder(Long id) {
        Response response = new Response();
        Order order = orderRepository.findByEndDateAndOrderStatusAndSubscriberSubsId(null, OrderStatus.PROCESSED, id);
        if (order != null){
            order.setEndDate(new Date());
            orderRepository.save(order);

            Order order1 = new Order();
            order1.setAddDate(new Date());
            order1.setOrderStatus(OrderStatus.APPROVED);
            order1.setSchoolAddress(order.getSchoolAddress());
            order1.setSchoolName(order.getSchoolName());
            order1.setSubscriber(order.getSubscriber());
            orderRepository.save(order1);
            response.setCode(1);
            response.setMessage("Ваша заявка принята!");
            return response;
        }
        response.setCode(0);
        response.setMessage("Повторите попытку ещё раз!");
        return response;
    }

    @Override
    public Response rejectOrder(Long id, String comment) {
        Order order = orderRepository.findByEndDateAndOrderStatusAndSubscriberSubsId(null, OrderStatus.PROCESSED, id);
        Response response = new Response();
        if (order != null){
            order.setEndDate(new Date());
            orderRepository.save(order);
            Order order1 = new Order();
            order1.setAddDate(new Date());
            order1.setOrderStatus(OrderStatus.DENIED);
            order1.setSchoolAddress(order.getSchoolAddress());
            order1.setComment(comment);
            order1.setSchoolName(order.getSchoolName());
            order1.setSubscriber(order.getSubscriber());
            orderRepository.save(order1);
            response.setCode(0);
            response.setMessage("Ваша заявка отклонена!");
            return response;
        }
        response.setCode(0);
        response.setMessage("Повторите попытку ещё раз!");
        return response;
    }


    private void saveNewOrder(OrderDto orderDto){
        orderDto.setAddDate(new Date());
        orderDto.setOrderStatus(OrderStatus.NEW);
        Order order = OrderMapper.INSTANCE.toOrder(orderDto);
        orderRepository.save(order);
    }

    private int lastCurrentOrder(OrderDto orderDto){
        Order order = orderRepository.findByEndDateAndSubscriberSubsId(null, orderDto.getSubscriber().getSubsId());
        if (order == null){
            return 0; // не было такой заявки
        }else {
            if (order.getOrderStatus().equals(OrderStatus.NEW)
                    || order.getOrderStatus().equals(OrderStatus.DENIED)
                    || order.getOrderStatus().equals(OrderStatus.APPROVED)) {

                order.setOrderStatus(OrderStatus.CANCELED);
                order.setEndDate(new Date());
                orderRepository.save(order);
                return 1; // заявка была, но мы её перебели
            }
            if (order.getOrderStatus().equals(OrderStatus.PROCESSED)){
                return 2;
            }
        }
        throw new OrderException("Что то пошло не так!");
    }

}
