package com.example;

import java.security.GeneralSecurityException;
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

    final Verifier verifier;

    public IssueController(IssueDao issueDao, CategoryDao categoryDao, Verifier verifier) {
        this.issueDao = Objects.requireNonNull(issueDao);
        this.categoryDao = Objects.requireNonNull(categoryDao);
        this.verifier = Objects.requireNonNull(verifier);
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
    String blank(Model model, @RequestParam Long categoryId) throws GeneralSecurityException {

        //Categoryの存在チェック
        Category category = categoryDao.selectById(categoryId).orElseThrow(BadRequest::new);

        //ハッシュ作ってhiddenフィールドで埋め込む
        String hash = verifier.hash(categoryId);

        model.addAttribute("category", category);
        model.addAttribute("hash", hash);
        return "new-issue";
    }

    @PostMapping("new")
    String post(@RequestParam String content, @RequestParam Long categoryId,
            @RequestParam String hash)
            throws GeneralSecurityException {

        //ハッシュ値のチェックをするだけ。DBアクセスがなくなる。
        if (verifier.verify(hash, categoryId) == false) {
            throw new BadRequest();
        }

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