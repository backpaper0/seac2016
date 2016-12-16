package com.example;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.IssueDao.IssueView;

@Controller
public class IssueController {

    final IssueDao issueDao;
    final CategoryDao categoryDao;

    public IssueController(IssueDao issueDao, CategoryDao categoryDao) {
        this.issueDao = Objects.requireNonNull(issueDao);
        this.categoryDao = Objects.requireNonNull(categoryDao);
    }

    @GetMapping
    String home(Model model) {
        List<Category> categories = categoryDao.selectAll();
        model.addAttribute("categories", categories);
        List<IssueView> issues = issueDao.selectAll();
        model.addAttribute("issues", issues);
        return "issues";
    }

    @GetMapping("new")
    String blank(Model model, @RequestParam Long categoryId) {

        //Categoryの存在チェック
        Category category = categoryDao.selectById(categoryId).orElseThrow(BadRequest::new);

        model.addAttribute("category", category);
        return "new-issue";
    }

    @PostMapping("new")
    String post(@RequestParam String content, @RequestParam Long categoryId) {

        //登録時にもう一回存在チェック
        categoryDao.selectById(categoryId).orElseThrow(BadRequest::new);

        Issue entity = new Issue();
        entity.content = content;
        entity.categoryId = categoryId;
        issueDao.insert(entity);
        return "redirect:/";
    }
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequest extends RuntimeException {
}