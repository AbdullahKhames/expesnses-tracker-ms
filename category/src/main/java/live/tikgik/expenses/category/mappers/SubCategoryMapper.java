package live.tikgik.expenses.category.mappers;

import live.tikgik.expenses.category.dtos.request.SubCategoryReqDto;
import live.tikgik.expenses.category.dtos.request.SubCategoryUpdateDto;
import live.tikgik.expenses.category.dtos.response.SubCategoryRespDto;
import live.tikgik.expenses.category.entity.SubCategory;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "JAKARTA",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
                CategoryMapper.class

        }
        ,imports = {
            UUID.class,
            LocalDateTime.class
})
public abstract class SubCategoryMapper {


    @Mappings(

            {
                    @Mapping(target = "id", ignore = true),
                    @Mapping(target = "deleted", ignore = true),
                    @Mapping(target = "refNo", ignore = true),
                    @Mapping(target = "createdAt", ignore = true),
                    @Mapping(target = "updatedAt", ignore = true),

            }

    )
    public  abstract SubCategory reqDtoToEntity(SubCategoryReqDto entityReqDto);
    @Mappings({
    })
    public  abstract SubCategoryRespDto entityToRespDto(SubCategory entity);
    public  abstract Set<SubCategoryRespDto> entityToRespDto(Set<SubCategory> entities);
    public  abstract List<SubCategoryRespDto> entityToRespDto(List<SubCategory> entities);

    @Mappings(

            {
                    @Mapping(target = "id", ignore = true),
                    @Mapping(target = "deleted", ignore = true),
                    @Mapping(target = "refNo", ignore = true),
                    @Mapping(target = "createdAt", ignore = true),
                    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())"),
                    @Mapping(target = "customerIds", ignore = true),

            }

    )
    public  abstract void update(@MappingTarget SubCategory entity, SubCategoryUpdateDto entityUpdateDto);
    @Mappings(
            {
//                    @Mapping(target = "refNo", expression = "java(subCategoryUpdateDto.getRefNo() != null ? subCategoryUpdateDto.getRefNo() : UUID.randomUUID().toString())"),
                    @Mapping(target = "deleted", ignore = true),
                    @Mapping(target = "id", ignore = true),
                    @Mapping(target = "refNo", ignore = true),
                    @Mapping(target = "createdAt", ignore = true),
                    @Mapping(target = "updatedAt", ignore = true),


            }
    )
    public  abstract SubCategory reqEntityToEntity(SubCategoryUpdateDto subCategoryUpdateDto);

}
