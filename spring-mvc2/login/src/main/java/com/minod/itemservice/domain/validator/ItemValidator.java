package com.minod.itemservice.domain.validator;

import com.minod.itemservice.domain.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {
// 복잡한 검증 로직을 별도로 분리하자. 컨트롤러에서 검증하는걸 분리하자.
// 이런 경우 별도의 클래스로 역할을 분리하는 것이 좋다. 그리고 이렇게 분리한 검증 로직을 재사용 할 수도 있다.
    // 인터페이스 Validator 라는게 있다. 모르면 못쓴다. 알아야 쓰지

    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }
    // Item class인 경우만 검증하겠다는 소리

    @Override
    public void validate(Object target, Errors errors) { // BindingResult가 errors로 바뀌었다고 보면 된다.
        Item item = (Item) target;

        if (!StringUtils.hasText(item.getItemName())) { // FieldError, ObjectError 길다 번거롭다, 다른거쓰자
            // rejectValue
            errors.rejectValue("itemName", "required"); // 에러코드의 .(쉼표) 기준 첫글자만 errorCode영역에 넣는다.
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.rejectValue("price", "range", new Object[]{1000, 1000000},null); // 에러코드의 .(쉼표) 기준 첫글자만 errorCode영역에 넣는다.
        }
        if (item.getQuantity() == null || item.getQuantity()>= 9999) {
            errors.rejectValue("quantity", "max", new Object[]{9999, 1000000},null); // 에러코드의 .(쉼표) 기준 첫글자만 errorCode영역에 넣는다.
        }
        if (item.getPrice() != null && item.getQuantity() != null) {
            if (item.getPrice()*item.getQuantity() < 10000) { // 특정 필드가 아닌 복합 룰 검증
                int resultPrice = item.getPrice() * item.getQuantity();
                // rejectValue 가 아닌 reject 씀 -> new ObjectError 를 대신함.
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
    }
}
