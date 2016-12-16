package com.example;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Entity;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

@Dao
@ConfigAutowireable
public interface IssueDao {

    @Select
    List<IssueView> selectAll();

    @Insert
    int insert(Issue entity);

    @Entity
    public class IssueView extends Issue {
        public String category;
    }
}
