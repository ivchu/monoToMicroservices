package com.epam.mentoring.controllers;

import com.epam.mentoring.models.Order;
import com.epam.mentoring.services.EcommerceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    Validator orderValidator;
    @Autowired
    private EcommerceService ecommerceService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(orderValidator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Order> index() {
        return ecommerceService.getOrders();
    }

    @PostMapping
    public Order create(@RequestBody @Valid Order order) {

        // Required by Hibernate ORM to save properly
        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setOrder(order));
        }
        return ecommerceService.saveOrder(order);
    }

    @RequestMapping("/{id}")
    public Order view(@PathVariable("id") long id) {
        return ecommerceService.getOrder(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public Order edit(@PathVariable("id") long id, @RequestBody @Valid Order order) {

        Order updatedOrder = ecommerceService.getOrder(id);

        if (updatedOrder == null) {
            return null;
        }

        return ecommerceService.saveOrder(updatedOrder);
    }

    @PostMapping("{id}/place")
    public Order createOrder(@PathVariable("id") String cartId, @RequestBody @Valid Order order) {
        if (order == null) {
            System.out.println("Order not in POST");
            return null;
        }
        Order orderResource = ecommerceService.createOrder(cartId, order);

        if (orderResource.getId() < 1) {
            System.out.println("Resource not created");
            return null;
        }
        return orderResource;
    }
}
