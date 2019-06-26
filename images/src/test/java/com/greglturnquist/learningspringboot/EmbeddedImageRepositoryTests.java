package com.greglturnquist.learningspringboot;

import com.greglturnquist.learningspringboot.images.Image;
import com.greglturnquist.learningspringboot.images.ImageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.annotation.Resource;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataMongoTest
public class EmbeddedImageRepositoryTests {

    /**
     * @RunWith(SpringRunner.java) is needed to ensure that Spring Boot test annotations
     * run properly within JUnit
     * @DataMongoTest will disable the general Spring Boot autoconfiguration, and
     * instead, use Spring Boot's test-based autoconfigurations to create a
     * MongoTemplate, a MongoDB connection, MongoDB property settings, a
     * ReactiveMongoTemplate and an embedded MongoDB instance; it will also enable
     * the MongoDB repositories
     *
     * With the Spring Data MongoDB repositories enabled, Spring Boot will
     * automatically instantiate an ImageRepository, and inject it into our autowired
     * repository field
     */

    @Resource
    ImageRepository imageRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Before
    public void setUp() {
        mongoOperations.dropCollection(Image.class);

        mongoOperations.insert(new Image("1", "learning-spring-boot-cover.jpg"));
        mongoOperations.insert(new Image("2", "learning-spring-boot-2nd-edition-cover.jpg"));
        mongoOperations.insert(new Image("3", "bazinga.jpg"));
        mongoOperations.insert(new Image("4", "bazinga has ended.jpg"));

        mongoOperations.findAll(Image.class).forEach(image -> System.out.println(image.toString()));
    }

    /**
     *
     * @Test indicates this is a test method and the method name describes our overall
     * goal.
     * We use Reactor Test's StepVerifier to subscribe to the Flux from the repository
     * and then assert against it.
     * Because we want to assert against the whole collection, we need to pipe it
     * through Reactor Test's recordWith method, which fetches the entire Flux and
     * converts it into an ArrayList via a method handle.
     * We verify that there were indeed three entries.
     * We write a lambda to peek inside the recorded ArrayList. In it, we can use
     * AssertJ to verify the size of ArrayList as well as extract each image's name with
     * Image::getName and verify them.
     * Finally, we can verify that Flux emitted a Reactive Streams complete signal,
     * meaning that it finished correctly.
     *
     */
    @Test
    public void findAllImagesShouldWork() {
        final Flux<Image> all = imageRepository.findAll();

        StepVerifier.create(all)
                .recordWith(ArrayList::new)
                .expectNextCount(4)
                .consumeRecordedWith(results -> {
                    assertThat(results).hasSize(4);

                    assertThat(results)
                            .extracting(Image::getName)
                            .contains("learning-spring-boot-cover.jpg",
                                    "learning-spring-boot-2nd-edition-cover.jpg",
                                    "bazinga.jpg",
                                    "bazinga has ended.jpg"
                            );
                })
                .expectComplete()
                .verify();
    }

    /**
     * repository.findByName() is used to fetch one record
     * We again use StepVerifier to create a subscriber for our Mono and then expect the
     * next signal to come through, indicating that it was fetched
     * Inside the lambda, we perform a couple of AssertJ assertions to verify the state
     * of this Image
     */
    @Test
    public void findByNameShouldWork() {
        final Mono<Image> image = imageRepository.findByName("bazinga.png");

        StepVerifier.create(image)
                .expectNextMatches(results -> {

                    assertThat(results.getName()).isEqualTo("bazinga.png");
                    assertThat(results.getId()).isEqualTo("3");

                    return true;
                });
    }
}
