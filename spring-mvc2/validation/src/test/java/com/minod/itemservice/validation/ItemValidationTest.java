package com.minod.itemservice.validation;

import com.minod.itemservice.domain.ItemValidation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ItemValidationTest {

    @Test
    @DisplayName("bean 벨리데이션 기본")
    void beanValidation() {
        // 검증기 생성, (ValidatorFactory)에 있는 (validator)를 가져온다.
        // 검증기 사용방법 국룰이라 걍 외워야된다.
        // 검증기로 Item객체의 하이버네이트 Validator는 검증 조건을 어노테이션으로 적어놓은 것.
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory(); // 뭐냐이건
        Validator validator = factory.getValidator();

        ItemValidation item = new ItemValidation();
        item.setItemName("  "); //공백
        item.setPrice(0);
        item.setQuantity(10000);
        // item 객체를 검증기(validator)에 넣고 돌린다. validator.validate(item) 돌리면 검증 오류가 Set형태의 violations에 담긴다.
        Set<ConstraintViolation<ItemValidation>> violations = validator.validate(item);

        for (ConstraintViolation<ItemValidation> violation : violations) {
            System.out.println("violation=" + violation);
            System.out.println("violation.message=" + violation.getMessage());
        }
        /* 스프링이 검증기와 통합되어 있다고한다. 이미. 그래서 메시지도 자동으로 다 완성되어 있다고 함. 국제화되어있음.
violation=ConstraintViolationImpl{interpolatedMessage='공백일 수 없습니다', propertyPath=itemName, rootBeanClass=class com.minod.itemservice.domain.ItemValidation, messageTemplate='{jakarta.validation.constraints.NotBlank.message}'}
violation.message=공백일 수 없습니다
violation=ConstraintViolationImpl{interpolatedMessage='9999 이하여야 합니다', propertyPath=quantity, rootBeanClass=class com.minod.itemservice.domain.ItemValidation, messageTemplate='{jakarta.validation.constraints.Max.message}'}
violation.message=9999 이하여야 합니다
violation=ConstraintViolationImpl{interpolatedMessage='1000에서 1000000 사이여야 합니다', propertyPath=price, rootBeanClass=class com.minod.itemservice.domain.ItemValidation, messageTemplate='{org.hibernate.validator.constraints.Range.message}'}
violation.message=1000에서 1000000 사이여야 합니다
         */

    }

}