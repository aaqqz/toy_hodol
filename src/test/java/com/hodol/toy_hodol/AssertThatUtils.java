package com.hodol.toy_hodol;

import org.assertj.core.api.AssertProvider;
import org.assertj.core.api.Assertions;
import org.springframework.test.json.JsonPathValueAssert;

import java.util.function.Consumer;

public class AssertThatUtils {

    public static Consumer<AssertProvider<JsonPathValueAssert>> equalsTo(String expected) {
        return value ->
                Assertions.assertThat(value).isEqualTo(expected);
    }

    public static Consumer<AssertProvider<JsonPathValueAssert>> notEmpty() {
        return value -> Assertions.assertThat(value).isNotEmpty();
    }
}
