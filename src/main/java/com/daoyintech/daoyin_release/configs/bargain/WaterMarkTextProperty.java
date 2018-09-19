package com.daoyintech.daoyin_release.configs.bargain;

import lombok.Data;

@Data
public class WaterMarkTextProperty {
        public static final String microsoft = "微软雅黑";
        public static final String songTI = "宋体";

        private String text;
        private String font;
        private String color;
        private Integer fontSize;
        private Integer dissolve;
        private String gravity;
        private Integer dx;
        private Integer dy;
}
