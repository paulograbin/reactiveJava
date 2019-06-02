package com.greglturnquist.learningspringboot.learningspringboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@WebFluxTest(controllers = HomeController.class)
@Import({ThymeleafAutoConfiguration.class})
public class HomeControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    ImageService imageService;

    /**
     * @Test marks this method as a JUnit test case.
     *
     * The method name, baseRouteShouldListAllImages, gives us a quick summary of
     * what this method should verify.
     *
     * The first three lines mock up the ImageService bean to return a Flux of two images
     * when findAllImages gets called.
     *
     * webClient is then used to perform a GET / using its fluent API.
     *
     * We verify the HTTP status to be a 200 OK, and extract the body of the result
     * into a string.
     *
     * We use Mockito's verify to prove that our ImageService bean's findAllImages was
     * indeed called.
     *
     * We use Mockito's verifyNoMoreInteractions to prove that no other calls are made
     * to our mock ImageService.
     *
     * Finally, we use AssertJ to inspect some key parts of the HTML page that was
     * rendered.
     */
    @Test
    public void baseRouteShouldListAllImages() {
        Image alphaImage = new Image("1", "alpha.png");
        Image bravoImage = new Image("2", "bravo.png");
        given(imageService.findAllImages()).willReturn(Flux.just(alphaImage, bravoImage));

        EntityExchangeResult<String> result = webTestClient
                .get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult();

        verify(imageService).findAllImages();
        verifyNoMoreInteractions(imageService);
        assertThat(result.getResponseBody())
                .contains("<title>Learning Spring Boot: Spring-a-Gram</title")
                .contains("<a href=\"/images/alpha.png/raw\">")
                .contains("<a href=\"/images/bravo.png/raw\">");
    }

    /**
     * @Test flags this method as a JUnit test case.
     * The method name, fetchingImageShouldWork, hints that this tests successful file
     * fetching.
     * The ImageService.findOneImage method returns a Mono<Resource>, so we need to
     * assemble a mock resource. That can be achieved using Spring's
     * ByteArrayResource, which takes a byte[]. Since all Java strings can be turned into
     * byte arrays, it's a piece of cake to plug it in.
     * webClient calls GET /images/alpha.png/raw.
     * After the exchange() method, we verify the HTTP status is OK.
     * We can even check the data content in the body of the HTTP response given
     * that the bytes can be curried back into a Java string.
     * Lastly, we use Mockito's verify to make sure our mock was called once and in
     * no other way.
     */
    @Test
    public void fetchingImageShouldWork() {
        given(imageService.findOneImage(any()))
                .willReturn(Mono.just(new ByteArrayResource("data".getBytes())));

        webTestClient
                .get().uri("/images/alpha.png/raw")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("data");

        verify(imageService).findOneImage("alpha.png");

        verifyNoMoreInteractions(imageService);
    }

    /**
     * @Test flags this method as a JUnit test case.
     * The method name, fetchingNullImageShouldFail, hints that this test is aimed at a
     * failure scenario.
     *
     * We need to mock out the file on the server, which is represented as a Spring
     * Resource. That way, we can force it to throw an IOException when getInputStream
     * is invoked.
     *
     * That mock is returned when ImageService.findOneImage is called. Notice how we
     * use Mockito's any() to simplify inputs?
     *
     * webClient is again used to make the call.
     *
     * After the exchange() method is made, we verify that the HTTP status is a 400 Bad
     * Request.
     *
     * We also check the response body and ensure it matches the expected body from
     * our controller's exception handler.
     *
     * Finally, we use Mockito to verify that our mock ImageService.findOneImage() was
     * called once (and only once!) and that no other calls were made to this mock
     * bean.
     * @throws IOException
     */
    @Test
    public void fetchingNullImageShouldFail() throws IOException {
        Resource resource = mock(Resource.class);
        given(resource.getInputStream())
                .willThrow(new IOException("Bad file"));
        given(imageService.findOneImage(any()))
                .willReturn(Mono.just(resource));

        webTestClient
                .get().uri("/images/alpha.png/raw")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Couldn't find alpha.png => Bad file");

        verifyNoMoreInteractions(imageService);
    }

    /**
     * The @Test flags this method as a JUnit test case.
     * We prep our ImageService mock bean to handle a deleteImage by returning
     * Mono.empty(). This is the way to construct a Mono<Void> object, which represents
     * the promise that our service hands us when deletion of the file and its
     * corresponding MongoDB record are both completed.
     * webClient performs a DELETE /images/alpha.png.
     * After the exchange() is complete, we verify the HTTP status is 303 See Other, the
     * outcome of a Spring WebFlux redirect:/ directive.
     * As part of the HTTP redirect, there should also be a Location header containing
     * the new URL, /.
     * Finally, we confirm that our ImageService mock bean's deleteImage method was
     * called and nothing else.
     */
    @Test
    public void deleteImageShouldWork() {
        Image alphaImage = new Image("1", "alpha.png");
        given(imageService.deleteImage(any())).willReturn(Mono.empty());

        webTestClient
                .delete().uri("/images/alpha.png")
                .exchange()
                .expectStatus().isSeeOther()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/");
    }
}
