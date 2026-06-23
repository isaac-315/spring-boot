package ec.edu.ups.icc.fundamentos01.products.mappers;

import ec.edu.ups.icc.fundamentos01.products.dto.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dto.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.models.ProductModel;

public class ProductMapper {
    public static ProductModel toModel(CreateProductDto dto){
        ProductModel model = new ProductModel();

        model.setProductName(dto.getProductName());
        model.setPrice(dto.getPrice());

        return model;
    }

    public static ProductResponseDto toResponse(ProductModel model){
        ProductResponseDto response = new ProductResponseDto();

        response.setId(model.getId());
        response.setProductName(model.getProductName());
        response.setPrice(model.getPrice());

        return response;
    }
}
