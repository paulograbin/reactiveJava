package com.greglturnquist.learningspringboot.images;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class ImageService {

    public static String UPLOAD_ROOT = "upload-dir";

    private final ResourceLoader resourceLoader;
    private final ImageRepository imageRepository;
    private final MeterRegistry meterRegistry;


    public ImageService(ResourceLoader resourceLoader, ImageRepository imageRepository, MeterRegistry meterRegistry) {
        this.resourceLoader = resourceLoader;
        this.imageRepository = imageRepository;
        this.meterRegistry = meterRegistry;
    }

    /**
     * It's good to remember that the Flux of images being returned is lazy. That means that
     only the number of images requested by the client is pulled from the database into
     memory and through the rest of the system at any given time. In essence, the client
     can ask for one or as many as possible, and the database, thanks to reactive drivers,
     will comply.
     */
    public Flux<Image> findAllImages() {
        final Flux<Image> findAll = imageRepository.findAll();//.log("findAll");

//        meterRegistry.summary("images.returned").record(findAll.count().block());

        return findAll;
    }

    public Mono<Resource> findOneImage(String filename) {
        return Mono.fromSupplier(() ->
                resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + filename));//.log("findOne");
    }

    /**
     * With a Flux of multipart files, flatMap each one into two independent actions:
     * saving the image and copying the file to the server.
     * Using imageRepository, put together a Mono that stores the image in MongoDB,
     * using UUID to create a unique key and the filename.
     * Using FilePart, WebFlux's reactive multipart API, build another Mono that copies
     * the file to the server.
     * To ensure both of these operations are completed, join them together using
     * Mono.when(). This means that each file won't be completed until the record is
     * written to MongoDB and the file is copied to the server.
     * The entire flow is terminated with then() so we can signal when all the files
     * have been processed.
     */
    public Mono<Void> createImage(Flux<FilePart> files) {
        return files
                .log("createImage-files")
                .flatMap(file -> {
                    Mono<Image> saveDatabaseImage = imageRepository.save(
                            new Image(UUID.randomUUID().toString(), file.filename()))
                            .log("createImage-save");

                    Mono<Void> copyFile = Mono.just(
                            Paths.get(UPLOAD_ROOT, file.filename()).toFile()).log("createImage-picktarget")
                            .map(destFile -> {
                                try {
                                    destFile.createNewFile();
                                    return destFile;
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .log("createImage-newFile")
                            .flatMap(file::transferTo)
                            .log("createImage-copy");

                    return Mono.when(saveDatabaseImage, copyFile)
                            .log("createImage-when");
                }).log("createImage-flatMap")
                .then()
                .log("createImage-done");
    }

    public Mono<Void> deleteImage(String filename) {
        Mono<Void> deleteDatabaseImage = imageRepository.findByName(filename).log("deleteImage-find")
                .flatMap(imageRepository::delete).log("deleteImage-record");

        Mono<Object> deleteFile = Mono.fromRunnable(() -> {
            try {
                Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }).log("deleteImage-file");

        return Mono.when(deleteDatabaseImage, deleteFile)
                .log("deleteImage-when")
                .then()
                .log("deleteImage-Done");
    }

    @Bean
    CommandLineRunner setUp() {
        return args -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));
            Files.createDirectory(Paths.get(UPLOAD_ROOT));

            FileCopyUtils.copy("Test file",
                    new FileWriter(UPLOAD_ROOT + "/learning-spring-boot-cover.jpg"));

            FileCopyUtils.copy("Test file2",
                    new FileWriter(UPLOAD_ROOT + "/learning-spring-boot-2nd-edition-cover.jpg"));

            FileCopyUtils.copy("Test file3",
                    new FileWriter(UPLOAD_ROOT + "/bazinga.png"));
        };
    }
}
