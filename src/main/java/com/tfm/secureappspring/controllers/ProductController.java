package com.tfm.secureappspring.controllers;

import com.tfm.secureappspring.data.models.Product;
import com.tfm.secureappspring.data.daos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/Products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/Index", method = RequestMethod.GET)
    public String showAllProducts(Model model) {
        List<Product> products = productRepository.findAll();
        //products = this.productRepository.findByItem("Iphone");
        model.addAttribute("allProducts", products);


        return "Products/Index";
    }
}
