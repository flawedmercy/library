package com.lab11.library.service;

import com.lab11.library.dto.CategoryDto;
import com.lab11.library.entity.Category;
import com.lab11.library.exception.DuplicateResourceException;
import com.lab11.library.exception.ResourceNotFoundException;
import com.lab11.library.repository.CategoryRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = Category.builder()
                .id(1L)
                .name("Fiction")
                .description("Fictional books")
                .build();
    }

    @Test
    void findAll_returnsAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(sampleCategory));

        List<CategoryDto.Response> result = categoryService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Fiction");
    }

    @Test
    void findById_existingId_returnsCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));

        CategoryDto.Response result = categoryService.findById(1L);

        assertThat(result.getName()).isEqualTo("Fiction");
    }

    @Test
    void findById_missingId_throwsNotFoundException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_newName_savesAndReturns() {
        CategoryDto.Request request = new CategoryDto.Request("Fiction", "Fictional books");
        when(categoryRepository.existsByName("Fiction")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(sampleCategory);

        CategoryDto.Response result = categoryService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void create_duplicateName_throwsDuplicateException() {
        CategoryDto.Request request = new CategoryDto.Request("Fiction", "Fictional books");
        when(categoryRepository.existsByName("Fiction")).thenReturn(true);

        assertThatThrownBy(() -> categoryService.create(request))
                .isInstanceOf(DuplicateResourceException.class);

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void update_existingCategory_updatesFields() {
        CategoryDto.Request request = new CategoryDto.Request("Non-Fiction", "Real world books");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));
        when(categoryRepository.existsByName("Non-Fiction")).thenReturn(false);
        when(categoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CategoryDto.Response result = categoryService.update(1L, request);

        assertThat(result.getName()).isEqualTo("Non-Fiction");
    }

    @Test
    void delete_existingId_deletesCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));

        categoryService.delete(1L);

        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void delete_missingId_throwsNotFoundException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
