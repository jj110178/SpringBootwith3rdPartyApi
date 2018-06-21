# Code Problem  
Using Spring Boot 2, Spring Data JPA, Hibernate, MySQL5.7.21, Java 8

Simple REST API with CRUD operation, getting details attribute value from a third party api


## Usage
I've use `MySQL 5.7.21` here, with configured Hibernate properties in Spring Boot's application properties 

```
## Hibernate Properties
# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect

# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto = update

```

once you have MySQL installed locally and you can then create a database named `test` and 
you can run the application thru the command line

``` 
$ mvn spring-boot:run
```

###Testing the API

When a new movie is added to the library, the library service 
will check third-party APIs for additional details about
the movie.  We want to integrate with one third-party API:

https://ghibliapi.herokuapp.com/

Here we add a movie to our library:

`curl http://localhost:9000/movies -d '{ "name": "Casablanca" }' -Hcontent-type:application/json`

```
HTTP/1.1 201 Created
```
```
{
    "id"         : "1",
    "created_at" : "2018-05-22 18:58:07",
    "name"       : "Casablanca",
    "details"    : []
}
```

Casablanca doesn't exist in the Ghibli API so the details come back empty.

Now we add a movie that is present in the Ghibli API:

`curl http://localhost:9000/movies -d '{ "name": "My Neighbor Totoro" }' -Hcontent-type:application/json`
```
HTTP/1.1 201 Created
```
```
{
    "id"         : "2",
    "created_at" : "2018-05-22 18:59:22",
    "name"       : "My Neighbor Totoro",
    "details"    : [
        {
            "source" : "https://ghibliapi.herokuapp.com/films?title=My%20Neighbor%20Totoro",
            "body"   :   {
                "id"           : "58611129-2dbc-4a81-a72f-77ddfc1b1b49",
                "title"        : "My Neighbor Totoro",
                "description"  : "Two sisters move to the country with their father in order to be closer to their hospitalized mother, and discover the surrounding trees are inhabited by Totoros, magical spirits of the forest. When the youngest runs away from home, the older sister seeks help from the spirits to find her.",
                "director"     : "Hayao Miyazaki",
                "producer"     : "Hayao Miyazaki",
                "release_date" : "1988",
                "rt_score"     : "93",
                "people": [
                    "https://ghibliapi.herokuapp.com/people/986faac6-67e3-4fb8-a9ee-bad077c2e7fe",
                    "https://ghibliapi.herokuapp.com/people/d5df3c04-f355-4038-833c-83bd3502b6b9",
                    "https://ghibliapi.herokuapp.com/people/3031caa8-eb1a-41c6-ab93-dd091b541e11",
                    "https://ghibliapi.herokuapp.com/people/87b68b97-3774-495b-bf80-495a5f3e672d",
                    "https://ghibliapi.herokuapp.com/people/d39deecb-2bd0-4770-8b45-485f26e1381f",
                    "https://ghibliapi.herokuapp.com/people/591524bc-04fe-4e60-8d61-2425e42ffb2a",
                    "https://ghibliapi.herokuapp.com/people/c491755a-407d-4d6e-b58a-240ec78b5061",
                    "https://ghibliapi.herokuapp.com/people/f467e18e-3694-409f-bdb3-be891ade1106",
                    "https://ghibliapi.herokuapp.com/people/08ffbce4-7f94-476a-95bc-76d3c3969c19",
                    "https://ghibliapi.herokuapp.com/people/0f8ef701-b4c7-4f15-bd15-368c7fe38d0a"
                ],
                "species": [
                    "https://ghibliapi.herokuapp.com/species/af3910a6-429f-4c74-9ad5-dfe1c4aa04f2",
                    "https://ghibliapi.herokuapp.com/species/603428ba-8a86-4b0b-a9f1-65df6abef3d3",
                    "https://ghibliapi.herokuapp.com/species/74b7f547-1577-4430-806c-c358c8b6bcf5"
                ],
                "locations": [
                    "https://ghibliapi.herokuapp.com/locations/"
                ],
                "vehicles": [
                    "https://ghibliapi.herokuapp.com/vehicles/"
                ],
                "url": "https://ghibliapi.herokuapp.com/films/58611129-2dbc-4a81-a72f-77ddfc1b1b49"
            }
        }
    ]
}
```

The details in the above API response contain an embedded view of 
the same response we would get if we ran

`curl 'https://ghibliapi.herokuapp.com/films?title=My%20Neighbor%20Totoro'`

In addition to supporting `POST /movies`, the web service should also support `GET /movies/:id` and `GET /movies?name=`:

`curl http://localhost:9000/movies/1`
```
HTTP/1.1 200 OK
```
```
{
    "id"         : "1",
    "created_at" : "2018-05-22 18:58:07",
    "name"       : "Casablanca",
    "details"    : []
}
```

`curl 'http://localhost:9000/movies?name=My%20Neighbor%20Totoro'`
```
HTTP/1.1 200 OK
```
```
{
    "id"         : "2",
    "created_at" : "2018-05-22 18:59:22",
    "name"       : "My Neighbor Totoro",
    "details"    : [
        {
            "source" : "https://ghibliapi.herokuapp.com/films?title=My%20Neighbor%20Totoro",
            "body"   :   {
                "id"           : "58611129-2dbc-4a81-a72f-77ddfc1b1b49",
                "title"        : "My Neighbor Totoro",
                "description"  : "Two sisters move to the country with their father in order to be closer to their hospitalized mother, and discover the surrounding trees are inhabited by Totoros, magical spirits of the forest. When the youngest runs away from home, the older sister seeks help from the spirits to find her.",
                "director"     : "Hayao Miyazaki",
                "producer"     : "Hayao Miyazaki",
                "release_date" : "1988",
                "rt_score"     : "93",
                "people": [
                    "https://ghibliapi.herokuapp.com/people/986faac6-67e3-4fb8-a9ee-bad077c2e7fe",
                    "https://ghibliapi.herokuapp.com/people/d5df3c04-f355-4038-833c-83bd3502b6b9",
                    "https://ghibliapi.herokuapp.com/people/3031caa8-eb1a-41c6-ab93-dd091b541e11",
                    "https://ghibliapi.herokuapp.com/people/87b68b97-3774-495b-bf80-495a5f3e672d",
                    "https://ghibliapi.herokuapp.com/people/d39deecb-2bd0-4770-8b45-485f26e1381f",
                    "https://ghibliapi.herokuapp.com/people/591524bc-04fe-4e60-8d61-2425e42ffb2a",
                    "https://ghibliapi.herokuapp.com/people/c491755a-407d-4d6e-b58a-240ec78b5061",
                    "https://ghibliapi.herokuapp.com/people/f467e18e-3694-409f-bdb3-be891ade1106",
                    "https://ghibliapi.herokuapp.com/people/08ffbce4-7f94-476a-95bc-76d3c3969c19",
                    "https://ghibliapi.herokuapp.com/people/0f8ef701-b4c7-4f15-bd15-368c7fe38d0a"
                ],
                "species": [
                    "https://ghibliapi.herokuapp.com/species/af3910a6-429f-4c74-9ad5-dfe1c4aa04f2",
                    "https://ghibliapi.herokuapp.com/species/603428ba-8a86-4b0b-a9f1-65df6abef3d3",
                    "https://ghibliapi.herokuapp.com/species/74b7f547-1577-4430-806c-c358c8b6bcf5"
                ],
                "locations": [
                    "https://ghibliapi.herokuapp.com/locations/"
                ],
                "vehicles": [
                    "https://ghibliapi.herokuapp.com/vehicles/"
                ],
                "url": "https://ghibliapi.herokuapp.com/films/58611129-2dbc-4a81-a72f-77ddfc1b1b49"
            }
        }
    ]
}
```


