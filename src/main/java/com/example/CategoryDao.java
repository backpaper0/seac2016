package com.example;

import java.util.List;
import java.util.Optional;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

@Dao
@ConfigAutowireable
public interface CategoryDao {

    @Select
    List<Category> selectAll();

    @Select
    Optional<Category> selectById(Long id);
}
