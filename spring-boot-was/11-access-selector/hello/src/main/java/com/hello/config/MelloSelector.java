package com.hello.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
// selector 만들기, import시킬 config 클래스를 동적으로 할당해줄 수 있다.
// 그러려면 ImportSelector를 구현해야 하고, selectImports메서드를 Override 해줘야 한다.
public class MelloSelector  implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"com.hello.config.MelloConfig"};
    }
}
