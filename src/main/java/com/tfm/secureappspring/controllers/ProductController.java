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

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/Products")
public class ProductController {
    private final String HTTP_STATUS_KEY = "httpStatus";
    private final String HTTP_STATUS_400 = "400";
    private final String HTTP_STATUS_404 = "404";

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(value = "/Index")
    public String index(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        return "Products/Index";
    }

    @GetMapping(value = "/Details/{id}")
    public String details(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addAttribute(HTTP_STATUS_KEY, HTTP_STATUS_400);

            return "redirect:/error";
        }
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            redirectAttributes.addAttribute(HTTP_STATUS_KEY, HTTP_STATUS_404);

            return "redirect:/error";
        }
        model.addAttribute("product", product.get());

        return "Products/Details";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/Edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addAttribute(HTTP_STATUS_KEY, HTTP_STATUS_400);

            return "redirect:/error";
        }
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            redirectAttributes.addAttribute(HTTP_STATUS_KEY, HTTP_STATUS_404);

            return "redirect:/error";
        }
        model.addAttribute("product", product.get());

        return "Products/Edit";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/Edit/{id}")
    public String edit(@PathVariable Integer id,@Valid @ModelAttribute Product product, BindingResult bindingResult,
                       Model model, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addAttribute(HTTP_STATUS_KEY, HTTP_STATUS_400);

            return "redirect:/error";
        }
        if (product.getId() == null) {
            redirectAttributes.addAttribute(HTTP_STATUS_KEY, HTTP_STATUS_404);

            return "redirect:/error";
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
            redirectAttributes.addAttribute(HTTP_STATUS_KEY, HTTP_STATUS_400);

            return "redirect:/error";
        }
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            redirectAttributes.addAttribute(HTTP_STATUS_KEY, HTTP_STATUS_404);

            return "redirect:/error";
        }
        model.addAttribute("product", product.get());

        return "Products/Delete";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/Delete/{id}")
    public String delete(@PathVariable Integer id,@ModelAttribute Product product,
                         RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addAttribute(HTTP_STATUS_KEY, HTTP_STATUS_400);

            return "redirect:/error";
        }
        if (product.getId() == null) {
            redirectAttributes.addAttribute(HTTP_STATUS_KEY, HTTP_STATUS_404);

            return "redirect:/error";
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
    public String search(@RequestParam(required = false) String name, Model model,
                         RedirectAttributes redirectAttributes) {
        if (name == null || (!name.isEmpty() && !name.matches("[a-zA-Z]"))) {
            redirectAttributes.addAttribute(HTTP_STATUS_KEY, HTTP_STATUS_400);

            return "redirect:/error";
        }
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        if (products.isEmpty()) {
            model.addAttribute("product404", name);

            return "Products/Index";
        }
        model.addAttribute("products", products);

        return "Products/Index";
    }
}
