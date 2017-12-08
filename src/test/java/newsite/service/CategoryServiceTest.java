/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newsite.service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import newsite.domain.Category;
import newsite.repository.CategoryRepository;
import newssite.ModelStub;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testAddCategory() {
        categoryService.addCategory("Category");
        assertEquals(1, categoryRepository.findAll().size());
        categoryService.addCategory("Category");
        assertEquals(1, categoryRepository.findAll().size());
    }

    @Test
    public void testModify() {
        Category category = new Category();
        category.setName("Category");
        categoryRepository.save(category);
        assertEquals("Category", categoryRepository.getOne(category.getId()).getName());
        categoryService.modifyCategory("CategoryMod", category.getId());
        assertEquals("CategoryMod", categoryRepository.getOne(category.getId()).getName());
    }

    @Test
    public void testDeleteCategory() {
        Category category = new Category();
        category.setName("Category");
        categoryRepository.save(category);
        assertEquals(1, categoryRepository.findAll().size());
        categoryService.deleteCategory(categoryRepository.findByName("Category").getId());
        assertEquals(0, categoryRepository.findAll().size());
    }

    @Test
    public void testSetModifyModel() {
        ModelStub model = new ModelStub();
        categoryService.setModifyModel(model, 1L);
        assertTrue(model.containsAttribute("category"));
    }

}
