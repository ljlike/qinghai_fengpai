package com.daoyintech.daoyin_release.enums;

public enum ProductTypeStatus {
    上架,
    下架;
    public static ProductTypeStatus getStatus(int status){
        switch (status){
            case 0:
                return ProductTypeStatus.上架;
            case 1:
                return ProductTypeStatus.下架;
                default:
                    return ProductTypeStatus.上架;
        }
    }
}

