package controllers;

import models.Player;
import repositories.PlayerRep;
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
@RequestMapping("/player")
public class PlayerController {


    @Autowired
    private PlayerRep PlayerRep;

    @GetMapping("/")
    public String index(Model model)
    {
        Iterable<Player> plane = PlayerRep.findAll();
        model.addAttribute("player",plane);
        return "palyer/index";
    }

    @GetMapping("/add")
    public String addView(Model model, Player player)
    {
        model.addAttribute("player",new Player());
        return "player/add-player";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("player")
            @Valid Player newPlayer,
            BindingResult bindingResult,
            Model model)
    {
        if(bindingResult.hasErrors())
            return "player/add-player";


        PlayerRep.save(newPlayer);
        return "redirect:/player/";
    }

    @GetMapping("/search")
    public String add(
            @RequestParam("name") String name,
            Model model)
    {
        List<Player> PlayerList = PlayerRep.findByNameContains(name);
        model.addAttribute("player",PlayerList);
        return "player/index";

    }
    @GetMapping("/{id}")
    public String read(
            @PathVariable("id") Long id,
            Model model)
    {
        Optional<Player> player = PlayerRep.findById(id);
        ArrayList<Player> playerArrayList = new ArrayList<>();
        player.ifPresent(playerArrayList::add);
        model.addAttribute("player",playerArrayList);
        return "player/info-player";
    }
    @GetMapping("/del/{id}")
    public String del(
            @PathVariable("id") Long id
    )
    {
        Player player = PlayerRep.findById(id).orElseThrow();
        PlayerRep.delete(player);
        return "redirect:/player/";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model
    )
    {
        if(!PlayerRep.existsById(id))
        {
            return "redirect:/dota/";
        }
        Optional<Player> player = PlayerRep.findById(id);
        ArrayList<Player> playerArrayList = new ArrayList<>();
        player.ifPresent(playerArrayList::add);
        model.addAttribute("player",playerArrayList.get(0));
        return "player/edit-player";
    }
    @PostMapping("/edit/{id}")
    public String editGames(
            @PathVariable("id") Long id,
            @ModelAttribute("games") @Valid Player newPlayer,
            BindingResult bindingResult,
            Model model)
    {
        if(!PlayerRep.existsById(id))
        {
            return "redirect:/player";
        }
        if(bindingResult.hasErrors())
            return "player/edit-player";
        newPlayer.setId(id);
        PlayerRep.save(newPlayer);
        return "redirect:/player/";
    }

}