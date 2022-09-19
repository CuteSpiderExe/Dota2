package controllers;

import models.Atribut;
import repositories.AtributRep;
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
@RequestMapping("/atribut")
public class AtributController {


    @Autowired
    private AtributRep AtributRep;

    @GetMapping("/")
    public String index(Model model)
    {
        Iterable<Atribut> plane = AtributRep.findAll();
        model.addAttribute("atribut",plane);
        return "atribut/index";
    }

    @GetMapping("/add")
    public String addView(Model model, Atribut atribut)
    {
        model.addAttribute("atribut",new Atribut());
        return "atribut/add-atribut";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("atribut")
            @Valid Atribut newAtribut,
            BindingResult bindingResult,
            Model model)
    {
        if(bindingResult.hasErrors())
            return "atribut/add-atribut";


        AtributRep.save(newAtribut);
        return "redirect:/atribut/";
    }

    @GetMapping("/search")
    public String add(
            @RequestParam("name") String name,
            Model model)
    {
        List<Atribut> DotaList = AtributRep.findByNameContains(name);
        model.addAttribute("atribut",DotaList);
        return "atribut/index";

    }
    @GetMapping("/{id}")
    public String read(
            @PathVariable("id") Long id,
            Model model)
    {
        Optional<Atribut> atribut = AtributRep.findById(id);
        ArrayList<Atribut> atributArrayList = new ArrayList<>();
        atribut.ifPresent(atributArrayList::add);
        model.addAttribute("atribut",atributArrayList);
        return "atribut/info-atribut";
    }
    @GetMapping("/del/{id}")
    public String del(
            @PathVariable("id") Long id
    )
    {
        Atribut atribut = AtributRep.findById(id).orElseThrow();
        AtributRep.delete(atribut);
        return "redirect:/atribut/";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model
    )
    {
        if(!AtributRep.existsById(id))
        {
            return "redirect:/dota/";
        }
        Optional<Atribut> atribut = AtributRep.findById(id);
        ArrayList<Atribut> atributArrayList = new ArrayList<>();
        atribut.ifPresent(atributArrayList::add);
        model.addAttribute("atribut",atributArrayList.get(0));
        return "atribut/edit-atribut";
    }
    @PostMapping("/edit/{id}")
    public String editGames(
            @PathVariable("id") Long id,
            @ModelAttribute("games") @Valid Atribut newAtribut,
            BindingResult bindingResult,
            Model model)
    {
        if(!AtributRep.existsById(id))
        {
            return "redirect:/atribut";
        }
        if(bindingResult.hasErrors())
            return "atribut/edit-atribut";
        newAtribut.setId(id);
        AtributRep.save(newAtribut);
        return "redirect:/atribut/";
    }

}