package com.minod.itemservice.validation;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

public class MessageCodeResolverTest {
    // 메시지 코드 리졸버  MessageCodesResolver  이런게 있다. 핵심. item field required 등등 여러가지 반환해줌
    // 구현체는 defaultmessagecodesresolver  주로 다음과 함께 사용 ObjectError , FieldError
    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        // 에러코드에 해당하는게 배열로 나온다고함.
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = "+messageCode);
        }
//        new ObjectError("item", new String[]{"required.item", "required"}); 이런식으로 만들어진 ObjectError 가 reject rejectValue 쓰는것과 같은결과임.
        /* 결과
        messageCode = required.item
        messageCode = required

         */
    }

    @Test
    void messageCodesResolverField() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        // 에러코드에 해당하는기 배열로 나온다고함.
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = "+messageCode);
        }
//        new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[] {9999}, null); 이런식으로 만들어진 ObjectError 가 reject rejectValue 쓰는것과 같은결과임.
        /* 결과   (자세하게 적힌 순으로 출력된다고 함.)
        messageCode = required.item.itemName
        messageCode = required.itemName
        messageCode = required.java.lang.String
        messageCode = required
         */
        // BindingResult.rejectValue()   사용하게 되면 내부에서 codesResolver를 호출해서 에러코드(messageCode)를 얻고난 뒤
        // new FieldErrror() 를 만든다고 함, 내부 인자는 불러온 에러코드와 추가시킨 인자를 넣는다고 함.
        // 즉 BindingResult.rejectValue() 는    BindingResult.addErrors(new FieldErrors(~~~)) 를 대체하기 위해 나온 메서드
        // 즉 BindingResult.reject() 는    BindingResult.addErrors(new ObjectErrors(~~~)) 를 대체하기 위해 나온 메서드
    }
}
