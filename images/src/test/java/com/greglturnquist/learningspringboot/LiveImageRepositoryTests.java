package com.greglturnquist.learningspringboot;

import com.greglturnquist.learningspringboot.images.ImageRepository;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class LiveImageRepositoryTests {

    @Resource
    ImageRepository imageRepository;

    @Resource
    MongoOperations operations;
//
//    @Before
//    public void setUp() {
//        operations.dropCollection(Image.class);
//
//        operations.insert(new Image("1", "learning-spring-boot-cover.jpg"));
//        operations.insert(new Image("2", "learning-spring-boot-2nd-edition-cover.jpg"));
//        operations.insert(new Image("3", "bazinga.jpg"));
//        operations.insert(new Image("4", "bazinga has ended.jpg"));
//
//        operations.findAll(Image.class).forEach(image -> System.out.println(image.toString()));
//    }
//
//    /**
//     *
//     * @Test indicates this is a test method and the method name describes our overall
//     * goal.
//     * We use Reactor Test's StepVerifier to subscribe to the Flux from the repository
//     * and then assert against it.
//     * Because we want to assert against the whole collection, we need to pipe it
//     * through Reactor Test's recordWith method, which fetches the entire Flux and
//     * converts it into an ArrayList via a method handle.
//     * We verify that there were indeed three entries.
//     * We write a lambda to peek inside the recorded ArrayList. In it, we can use
//     * AssertJ to verify the size of ArrayList as well as extract each image's name with
//     * Image::getName and verify them.
//     * Finally, we can verify that Flux emitted a Reactive Streams complete signal,
//     * meaning that it finished correctly.
//     *
//     */
//    @Test
//    public void findAllImagesShouldWork() {
//        final Flux<Image> all = imageRepository.findAll();
//
//        StepVerifier.create(all)
//                .recordWith(ArrayList::new)
//                .expectNextCount(4)
//                .consumeRecordedWith(results -> {
//                    assertThat(results).hasSize(4);
//
//                    assertThat(results)
//                            .extracting(Image::getName)
//                            .contains("learning-spring-boot-cover.jpg",
//                                    "learning-spring-boot-2nd-edition-cover.jpg",
//                                    "bazinga.jpg",
//                                    "bazinga has ended.jpg"
//                            );
//                })
//                .expectComplete()
//                .verify();
//    }

//    /**
//     * repository.findByName() is used to fetch one record
//     * We again use StepVerifier to create a subscriber for our Mono and then expect the
//     * next signal to come through, indicating that it was fetched
//     * Inside the lambda, we perform a couple of AssertJ assertions to verify the state
//     * of this Image
//     */
//    @Test
//    public void findByNameShouldWork() {
//        final Mono<Image> image = imageRepository.findByName("bazinga.png");
//
//        StepVerifier.create(image)
//                .expectNextMatches(results -> {
//
//                    assertThat(results.getName()).isEqualTo("bazinga.png");
//                    assertThat(results.getId()).isEqualTo("3");
//
//                    return true;
//                });
//    }
}
