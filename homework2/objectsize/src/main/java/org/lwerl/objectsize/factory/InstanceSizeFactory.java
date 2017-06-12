package org.lwerl.objectsize.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by lWeRl on 12.06.2017.
 */
public class InstanceSizeFactory {
    final static Map<String, Supplier<InstanceSize>> map = new HashMap<>();

    static {
        map.put("OBJECT", ObjectSize::new);
        map.put("STRING_EMPTY", StringEmptySize::new);
        map.put("STRING_5_LETTERS", String5LettersSize::new);
        map.put("ARRAYLIST", ArrayListSize::new);
        map.put("ARRAY", ArraySize::new);
        map.put("ARRAY_ZERO_LENGTH", ArrayZeroLengthSize::new);
        map.put("MYCLASS", MyClassSize::new);
    }

    public InstanceSize getInstanceSize(String instanceSizeType) {
        Supplier<InstanceSize> instanceSize = map.get(instanceSizeType.toUpperCase());
        if (instanceSize != null) {
            return instanceSize.get();
        }
        throw new IllegalArgumentException("No such instance " + instanceSizeType.toUpperCase());
    }

}
