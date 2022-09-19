package controllers;

import models.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/items")
public class ItemsController {


    @Autowired
    private repositories.ItemsRep ItemsRep;

    @GetMapping("/")
    public String index(Model model)
    {
        Iterable<Items> items = ItemsRep.findAll();
        model.addAttribute("items",items);
        return "items/index";
    }

    @GetMapping("/add")
    public String addView(Model model, Items items)
    {
        model.addAttribute("items",new Items());
        return "items/add-items";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("items")
            @Valid Items newItems,
            BindingResult bindingResult,
            Model model)
    {
        if(bindingResult.hasErrors())
            return "items/add-items";


        ItemsRep.save(newItems);
        return "redirect:/items/";
    }

    @GetMapping("/search")
    public String add(
            @RequestParam("name") String name,
            Model model)
    {
        List<Items> ItemsList = ItemsRep.findByNameContains(name);
        model.addAttribute("items",ItemsList);
        return "items/index";

    }
    @GetMapping("/{id}")
    public String read(
            @PathVariable("id") Long id,
            Model model)
    {
        Optional<Items> items = ItemsRep.findById(id);
        ArrayList<Items> itemsArrayList = new ArrayList<>();
        items.ifPresent(itemsArrayList::add);
        model.addAttribute("items",itemsArrayList);
        return "items/info-items";
    }
    @GetMapping("/del/{id}")
    public String del(
            @PathVariable("id") Long id
    )
    {
        Items items = ItemsRep.findById(id).orElseThrow();
        ItemsRep.delete(items);
        return "redirect:/items/";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model
    )
    {
        if(!ItemsRep.existsById(id))
        {
            return "redirect:/items/";
        }
        Optional<Items> items = ItemsRep.findById(id);
        ArrayList<Items> itemsArrayList = new ArrayList<>();
        items.ifPresent(itemsArrayList::add);
        model.addAttribute("items",itemsArrayList.get(0));
        return "items/edit-items";
    }
    @PostMapping("/edit/{id}")
    public String editGames(
            @PathVariable("id") Long id,
            @ModelAttribute("games") @Valid Items newItems,
            BindingResult bindingResult,
            Model model)
    {
        if(!ItemsRep.existsById(id))
        {
            return "redirect:/items";
        }
        if(bindingResult.hasErrors())
            return "items/edit-items";
        newItems.setId(id);
        ItemsRep.save(newItems);
        return "redirect:/items/";
    }

}
