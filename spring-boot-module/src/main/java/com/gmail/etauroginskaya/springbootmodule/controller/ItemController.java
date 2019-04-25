package com.gmail.etauroginskaya.springbootmodule.controller;

import com.gmail.etauroginskaya.service.ItemService;
import com.gmail.etauroginskaya.service.model.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public String showItems(Model model) {
        List<ItemDTO> items = itemService.getItems();
        model.addAttribute("items", items);
        model.addAttribute("updateItem", new ItemDTO());
        return "items";
    }

    @GetMapping("/add")
    public String showFormCreateItem(ItemDTO item) {
        return "item-create";
    }

    @PostMapping("/add")
    public String addItem(@Valid ItemDTO item, BindingResult
            bindingResult) {
        if (bindingResult.hasErrors()) {
            return "item-create";
        }
        itemService.add(item);
        return "redirect:/result-add";
    }

    @GetMapping("/result-add")
    public String showResult() {
        return "result-add";
    }

    @PostMapping("/update")
    public String updateItem(ItemDTO itemDTO) {
        String oldStatus = itemService.getStatusById(Long.valueOf(itemDTO.getId()));
        if (!Objects.equals(oldStatus, itemDTO.getStatus())) {
            int quantityUpdateItem = itemService.update(Long.valueOf(itemDTO.getId()), itemDTO.getStatus());
            if (quantityUpdateItem == 1) {
                return "redirect:/update-true";
            }
        }
        return "redirect:/update-false";
    }

    @GetMapping("/update-true")
    public String showResultUpdateTrue() {
        return "update-true";
    }

    @GetMapping("/update-false")
    public String showResultUpdateFalse() {
        return "update-false";
    }

}
