package controllers;

import models.Dota;
import repositories.DotaRep;
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
@RequestMapping("/dota")
public class DotaController {


    @Autowired
    private DotaRep DotaRep;

    @GetMapping("/")
    public String index(Model model)
    {
        Iterable<Dota> plane = DotaRep.findAll();
        model.addAttribute("dota",plane);
        return "dota/index";
    }

    @GetMapping("/add")
    public String addView(Model model, Dota dota)
    {
        model.addAttribute("dota",new Dota());
        return "dota/add-dota";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("dota")
            @Valid Dota newDota,
            BindingResult bindingResult,
            Model model)
    {
        if(bindingResult.hasErrors())
            return "dota/add-dota";


        DotaRep.save(newDota);
        return "redirect:/dota/";
    }

    @GetMapping("/search")
    public String add(
            @RequestParam("name") String name,
            Model model)
    {
        List<Dota> DotaList = DotaRep.findByNameContains(name);
        model.addAttribute("dota",DotaList);
        return "dota/index";

    }
    @GetMapping("/{id}")
    public String read(
            @PathVariable("id") Long id,
            Model model)
    {
        Optional<Dota> dota = DotaRep.findById(id);
        ArrayList<Dota> dotaArrayList = new ArrayList<>();
        dota.ifPresent(dotaArrayList::add);
        model.addAttribute("dota",dotaArrayList);
        return "dota/info-dota";
    }
    @GetMapping("/del/{id}")
    public String del(
            @PathVariable("id") Long id
    )
    {
        Dota dota = DotaRep.findById(id).orElseThrow();
        DotaRep.delete(dota);
        return "redirect:/dota/";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model
    )
    {
        if(!DotaRep.existsById(id))
        {
            return "redirect:/dota/";
        }
        Optional<Dota> dota = DotaRep.findById(id);
        ArrayList<Dota> dotaArrayList = new ArrayList<>();
        dota.ifPresent(dotaArrayList::add);
        model.addAttribute("dota",dotaArrayList.get(0));
        return "dota/edit-dota";
    }
    @PostMapping("/edit/{id}")
    public String editGames(
            @PathVariable("id") Long id,
            @ModelAttribute("games") @Valid Dota newDota,
            BindingResult bindingResult,
            Model model)
    {
        if(!DotaRep.existsById(id))
        {
            return "redirect:/dota";
        }
        if(bindingResult.hasErrors())
            return "dota/edit-dota";
        newDota.setId(id);
        DotaRep.save(newDota);
        return "redirect:/dota/";
    }

}
