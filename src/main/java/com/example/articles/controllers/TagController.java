package com.example.articles.controllers;

import com.example.articles.entities.Tag;
import com.example.articles.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public String listTags(Model model) {
        model.addAttribute("tags", tagService.getAllTags());
        return "tags/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("tag", new Tag());
        return "tags/add";
    }

    @PostMapping
    public String createTag(@ModelAttribute Tag tag) {
        tagService.createTag(tag);
        return "redirect:/tags";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Tag tag = tagService.getTagById(id);
        model.addAttribute("tag", tag);
        return "tags/edit";
    }

    @PostMapping("/update/{id}")
    public String updateTag(@PathVariable Long id, @ModelAttribute Tag tag) {
        tagService.updateTag(id, tag);
        return "redirect:/tags";
    }

    @GetMapping("/delete/{id}")
    public String deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return "redirect:/tags";
    }
}
