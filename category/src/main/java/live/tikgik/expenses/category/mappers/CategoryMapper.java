package live.tikgik.expenses.category.mappers;


import live.tikgik.expenses.category.dtos.request.CategoryReqDto;
import live.tikgik.expenses.category.dtos.request.CategoryUpdateDto;
import live.tikgik.expenses.category.dtos.response.CategoryRespDto;
import live.tikgik.expenses.category.dtos.response.SubCategoryRespDto;
import live.tikgik.expenses.category.entity.Category;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "JAKARTA",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
            SubCategoryMapper.class
        },
        imports = {LocalDateTime.class})
public abstract class CategoryMapper {

    @Mappings(

            {
                    @Mapping(target = "id", ignore = true),
                    @Mapping(target = "deleted", ignore = true),
                    @Mapping(target = "refNo", ignore = true),
                    @Mapping(target = "createdAt", ignore = true),
                    @Mapping(target = "updatedAt", ignore = true),
                    @Mapping(target = "customerIds", ignore = true),

            }

    )
    public abstract Category reqDtoToEntity(CategoryReqDto entityReqDto);
    public abstract CategoryRespDto entityToRespDto(Category entity);
    public abstract Set<CategoryRespDto> entityToRespDto(Set<Category> entities);
    public abstract List<CategoryRespDto> entityToRespDto(List<Category> entities);

    @Mappings(

            {
                    @Mapping(target = "id", ignore = true),
                    @Mapping(target = "deleted", ignore = true),
                    @Mapping(target = "refNo", ignore = true),
                    @Mapping(target = "createdAt", ignore = true),
                    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())"),
                    @Mapping(target = "subCategories", ignore = true),
                    @Mapping(target = "customerIds", ignore = true),

            }

    )
    public abstract void update(@MappingTarget Category entity, CategoryUpdateDto entityUpdateDto);
    @AfterMapping
    public CategoryRespDto afterEntityToRespDto(Category entity,
                                                @MappingTarget CategoryRespDto.CategoryRespDtoBuilder categoryRespDtoBuilder) {

        CategoryRespDto categoryRespDto = categoryRespDtoBuilder.build();
        categoryRespDto.setTotalSpent(
                categoryRespDto.getSubCategories()
                        .stream()
                        .map(SubCategoryRespDto::getTotalSpent)
                        .reduce(0.0, Double::sum)
        );
        return categoryRespDto;
    }
}
