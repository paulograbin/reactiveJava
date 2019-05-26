<<<<<<< HEAD
package com.greglturnquist.learningspringboot.learningspringboot.QueryByExample;
=======
package com.greglturnquist.learningspringboot.learningspringboot.Example;
>>>>>>> 752a64f510a4c4182f346aefc94620d5396b1766

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document
public class Employee {

<<<<<<< HEAD

    /**
     *
     * Lombok's @Data annotation provides getters, setters, equals, hashCode, and toString
     * methods
     * Spring Data MongoDB's @Document annotation indicates this POJO is a target for
     * storage in MongoDB
     * Spring Data Commons' @Id annotation indicates that the id field is the identifier
     * The rest of the fields are simple strings
     *
     */

    @Id
    private String id;
=======
    @Id
    private String idl;
>>>>>>> 752a64f510a4c4182f346aefc94620d5396b1766
    private String lastName;
    private String firstName;
    private String role;

}
