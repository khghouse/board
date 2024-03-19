package com.board.component;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class HashingTest {

    @Test
    @DisplayName("솔트 키를 생성한다.")
    void createSaltKey() {
        // when
        String result = Hashing.createSaltKey();

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("서로 같은 솔트 키로 문자열을 해싱하면 같은 다이제스트를 얻는다.")
    void hashWithSameSalt() {
        // given
        String text1 = "1234";
        String text2 = "1234";
        String salt = Hashing.createSaltKey();

        // when
        String result1 = Hashing.hash(text1, salt);
        String result2 = Hashing.hash(text2, salt);

        // then
        assertThat(result1).isEqualTo(result2);
    }

    @Test
    @DisplayName("서로 다른 솔트 키로 문자열을 해싱하면 다른 다이제스트를 얻는다.")
    void hashWithAnotherSalt() {
        // given
        String text1 = "1234";
        String text2 = "1234";

        // when
        String result1 = Hashing.hash(text1, Hashing.createSaltKey());
        String result2 = Hashing.hash(text2, Hashing.createSaltKey());

        // then
        assertThat(result1).isNotEqualTo(result2);
    }

}