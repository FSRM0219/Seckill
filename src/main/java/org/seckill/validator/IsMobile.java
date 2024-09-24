package org.seckill.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 自定义手机格式校验注解——
 * &#064;Target指定该注解可以应用的Java元素类型：
 * 方法、字段、注解类型、构造函数和参数；
 * &#064;Retention指定注解的保留策略，
 * RUNTIME表示注解运行时可用，从而可以通过反射机制进行访问；
 * &#064;Documented表示注解将包含在Javadoc中；
 * &#064;Constraint来自javax.validation包，约束注解，
 * validatedBy属性指定用于验证的类，IsMobileValidator.class，即实际校验逻辑实现；
 * &#064;Interface用于定义注解关键字；
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {IsMobileValidator.class}
)
public @interface IsMobile {

    boolean required() default true;//默认不为空

    String message() default "手机号码格式错误";//校验不通过输出信息

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
