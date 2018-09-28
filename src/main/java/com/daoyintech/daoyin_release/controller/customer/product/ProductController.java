package com.daoyintech.daoyin_release.controller.customer.product;

import com.daoyintech.daoyin_release.controller.customer.user.BaseUserController;
import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.response.product.ProductFavoriteResponse;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.product.ProductResponse;
import com.daoyintech.daoyin_release.response.result.ProductResult;
import com.daoyintech.daoyin_release.service.product.ProductService;
import com.daoyintech.daoyin_release.service.user.UserFavoriteService;
import com.daoyintech.daoyin_release.service.user.UserService;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController extends BaseUserController{

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserFavoriteService userFavoriteService;

    /*@GetMapping("/{id}")
    @ApiOperation("获取活动图片对应商品详情")
    public ResultResponse getBannerProductDetail(@ApiParam("banner对应商品id")@PathVariable Long id){
        return ResultResponseUtil.success(productService.findByProductId(id));
    }*/

    @GetMapping()
    @ApiOperation("获取活动图片对应商品详情")
    public ResultResponse getBannerProductDetail(@ApiParam("banner对应商品id") String link){
//        System.out.println(link);
        String[] links = link.split("/");
        String name = links[1];
        Long id = Long.valueOf(links[2]);
        if ("products".equals(name)){
            ProductResponse productResponse = productService.findByProductId(id);
            return ResultResponseUtil.success(productResponse);
        }else if("banners".equals(name)){
            String url = productService.findBannerUrlById(id);
            return ResultResponseUtil.success(url);
        }
        return ResultResponseUtil.error(ResultEnum.NO_MATCHING_GOODS_WHERE_FOUND.getCode(),ResultEnum.NO_MATCHING_GOODS_WHERE_FOUND.getMessage());
    }

    @ApiOperation("获取商品分类对应的商品详情")
    @GetMapping("/category/{id}")
    public ResultResponse getProductCategoryDetail(@ApiParam("商品分类id")@PathVariable Long id){
        List<ProductFavoriteResponse> responses = productService.findProductsAndFavoriteByProductTypeIdAndUserId(id, getUserIdByUnionId());
        return ResultResponseUtil.success(responses);
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("获取商品详情")
    public ResultResponse getProductDetail(@ApiParam("商品id")@PathVariable Long id){
        return ResultResponseUtil.success(productService.findProductDetailByProductId(getUserIdByUnionId(),id));
    }

    @GetMapping("/formats/{id}")
    @ApiOperation("获取商品的颜色和规格")
    public ResultResponse getProductColorsAndFormats(@ApiParam("商品id")@PathVariable Long id){
        return ResultResponseUtil.success(productService.findProductColorsAndFormatsByProductId(id));
    }

    @ApiOperation("商品搜索结果展示")
    @GetMapping("/seek/{name}")
    public ResultResponse seekProduct(@ApiParam("收缩的关键字")@PathVariable String name){
        List<ProductResult> results = productService.seekProductLikeProductName(name);
        if (results.size()>0){
            return ResultResponseUtil.success(results);
        }else {
            return ResultResponseUtil.error(ResultEnum.NO_MATCHING_GOODS_WHERE_FOUND.getCode(),ResultEnum.NO_MATCHING_GOODS_WHERE_FOUND.getMessage());
        }
    }

    @ApiOperation("收藏或取消收藏")
    @GetMapping("/fav/{productId}")
    public Object fav(@PathVariable Long productId){
        userFavoriteService.isFavChange(getUserIdByUnionId(),productId);
        return ResultResponseUtil.success();
    }

    private Long getUserIdByUnionId(){
        String unionId = getCurrentUnionId();
        return userService.findByUnionId(unionId).getId();
    }
}