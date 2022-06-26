package com.tfm.secureappspring.controllers;

import com.tfm.secureappspring.data.models.Product;
import com.tfm.secureappspring.data.daos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/Products")
public class ProductController {
    private final String HTTP_STATUS_KEY = "httpStatus";
    private final String HTTP_STATUS_400 = "400";
    private final String HTTP_STATUS_REASON_PHRASE_KEY = "httpStatus.reasonPhrase";
    private final String HTTP_STATUS_REASON_PHRASE_400 = "BAD REQUEST";

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    EntityManager entityManager;

    @GetMapping(value = "/Index")
    public String index(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        return "Products/Index";
    }

    @GetMapping(value = "/Details")
    public String details(@RequestParam(required = false) String id, Model model) {
        Object[] product = entityManager
                .createNativeQuery("SELECT * FROM products WHERE id = " + id).getResultList().toArray();
        model.addAttribute("product", product);

        return "Products/Details";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/Edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute(HTTP_STATUS_KEY, HTTP_STATUS_400);
            redirectAttributes.addFlashAttribute(HTTP_STATUS_REASON_PHRASE_KEY, HTTP_STATUS_REASON_PHRASE_400);

            return "redirect:/error";
        }
        Product product = productRepository.getById(id);
        if (product.getId() == null) {
            return "redirect:/error/404";
        }
        model.addAttribute("product", product);

        return "Products/Edit";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/Edit/{id}")
    public String edit(@PathVariable Integer id,@Valid @ModelAttribute Product product, BindingResult bindingResult,
                       Model model, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute(HTTP_STATUS_KEY, HTTP_STATUS_400);
            redirectAttributes.addFlashAttribute(HTTP_STATUS_REASON_PHRASE_KEY, HTTP_STATUS_REASON_PHRASE_400);

            return "redirect:/error";
        }
        if (product.getId() == null) {

            return "redirect:/error/404";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
            return "Products/Edit";
        }
        productRepository.save(product);
        model.addAttribute("product", product);

        return "Products/Edit";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/Delete/{id}")
    public String delete(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute(HTTP_STATUS_KEY, HTTP_STATUS_400);
            redirectAttributes.addFlashAttribute(HTTP_STATUS_REASON_PHRASE_KEY, HTTP_STATUS_REASON_PHRASE_400);

            return "redirect:/error";
        }
        Product product = productRepository.getById(id);
        if (product.getId() == null) {
            return "redirect:/error/404";
        }
        model.addAttribute("product", product);

        return "Products/Delete";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/Delete/{id}")
    public String delete(@PathVariable Integer id,@ModelAttribute Product product,
                         RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute(HTTP_STATUS_KEY, HTTP_STATUS_400);
            redirectAttributes.addFlashAttribute(HTTP_STATUS_REASON_PHRASE_KEY, HTTP_STATUS_REASON_PHRASE_400);

            return "redirect:/error";
        }
        if (product.getId() == null) {
            return "redirect:/error/404";
        }
        productRepository.deleteById(id);

        return "redirect:/Products/Index";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/Add")
    public String add(Model model) {
        model.addAttribute("product", new Product());

        return "Products/Add";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/Add")
    public String add(@Valid @ModelAttribute Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
            return "Products/Add";
        }
        productRepository.save(product);
        model.addAttribute("product", product);

        return "redirect:/Products/Index";
    }

    @GetMapping(value = "Search")
    public String search(@RequestParam(required = false) String name, Model model) {
        List<Object[]> productsArray = entityManager
                .createNativeQuery("SELECT * FROM products WHERE name LIKE '%" + name + "%'").getResultList();
        if (productsArray.isEmpty()) {
            model.addAttribute("product404", name);

            return "Products/Index";
        }

        List<Product> products = new ArrayList<>();
        for (Object[] objects : productsArray) {
            Product product = new Product();
            product.setId((Integer) objects[0]);
            product.setName((String) objects[1]);
            product.setAmount((Integer) objects[2]);
            product.setPrice((Double) objects[3]);
            products.add(product);
        }
        model.addAttribute("products", products);

        return "Products/Index";
    }
}
