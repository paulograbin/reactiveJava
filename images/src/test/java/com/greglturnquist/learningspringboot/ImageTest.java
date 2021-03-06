package com.greglturnquist.learningspringboot;

import com.greglturnquist.learningspringboot.images.Image;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ImageTest {

    @Test
    public void imagesManagedByLomokShouldWork() {
        Image image = new Image("id", "filename.jpg");
        assertThat(image.getId()).isEqualTo("id");
        assertThat(image.getName()).isEqualTo("filename.jpg");
    }
}
