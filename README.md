# Aplicacion Web de integracion con API Marvel
Tecnologías: Java 17 + Spring-boot + JPA (Hibernate) + API REST

## Abstract
Aplicación WEB que integra la API de marvel y consulte el API de Marvel para encontrar todos los personajes por id.
1.Crear una cuenta en el portal de desarrollo de marvel [aquí](https://developer.marvel.com/).
2.Obtener una API Key. El API Key va tener dos componentes uno público y otro privado. Puede solicitar al facilitador unas API de prueba.
3.Revise la documentación en: [aquí](https://developer.marvel.com/documentation/generalinfo).
4.Implementa una interfaz con nombre descripcion comic y ID para interactuar con el servicio (El alcance de la implementación es solo consulta por ID).
La aplicación esta construida con SpringBoot, versión 2.5.5. Se hace uso de JPA , REST , HASH para accceder al webservice de marvel consumir el servicio y traer mediante json todos los personajes de marvel.

## Consideraciones adicionales
Para enviar las peticiones se requiere del envío de un hash. Revise el funcionamiento en [aquí](https://developer.marvel.com/documentation/authorization).
La lista de los endpoints se puede ver [aquí](https://developer.marvel.com/docs) (Solo requiere usar solo el de consulta de personaje por ID).
Algunos ID de personajes que puede usar son: 1009610: Spider-Man 1009726: X-Men 1009351: Hulk 1009368: Iron Man 1009165: Avengers 1009664: Thor 1009220: Captain America 1010338: Captain Marvel

## Pasos para la realización de la aplicación
### Creación del proyecto
Para crear el proyecto inicial hacemos uso de [Spring Initializr](http://start.spring.io/). Seleccionamos las dependencias de JPA, Web, DevTools, Lombok y Thymeleaf en un primer momento. Descargamos el proyecto resultante y lo importamos como maven project en Eclipse, que sera el IDE escogido para realizar el ejercicio.

### Implementación del modelo
Para la implementación del modelo se emplea una herramienta de generación de clases de java a partir de la api de marvel llamado JSON2CSharp que se puede encontrar [aquí](https://json2csharp.com/json-to-pojo).
Se consume el servicio de marvel que devuelve toda la informacion del personaje en JSON, se pega en la pagina de JSON2CSSharp , click en Convert y se puede pasar cada clase a mano en el proyecto o exportar todo en un zip(incluyendo cada clase del model, nos quedaremos con las clases que muestren Nombre, Descripcion, la lista de Comics y el ID de personaje).

En un primer momento utilizamos el servicio web de marvel remoto, por lo que añadimos las siguientes lineas al application.properties:
```sh
url.marvel=http://gateway.marvel.com
```

### Implementación del modelo
Para consumir el webservice de marvel utilizaremos una clase con el objeto data , extiende de una clase con los personajes de marvel en una lista, esa lista extiende de otra clase con los atributos del personaje en cuestión.
Basta con crear una interface y extender de JpaRepository. De esta manera tendremos los métodos basicos de accceso a la base de datos. Si quisieramos realizar otro tipo de consultas más complejas, basta con añadir la signatura del método indicando el campo de busqueda.Ej: buscar un personaje por id (atributo id) -> añadir en la interface el siguiente método:
```sh
encontrarPersonaje(
	@PathVariable(required = true) @NotNull @Size(min = 7, max = 7) Long id,
	@RequestParam(value = "ts") Long timeStamp, @RequestParam(value = "apikey") String publicKey,
	@RequestParam(value = "hash") String hashMD5);

```
Recordar que debemos añadir la anotación @Repository para que SpringBoot lo reconozca como una clase repositorio y este en el contexto.

### Implementación de los servicios
Para realizar los servicios usaremos una interface donde definiremos los métodos y su implementación, que la capa superior no tiene por que conocer. Debemos recordar que la clase implementada debe llevar la anotación @Service. Con ella decimos que esta es una clase de configuración a tener en cuenta, igual que pasaba con los repositorios.
Como los métodos necesitarán realizar llamadas al repositorio, es necesario una instancia del mismo en la clase. Para ello usaremos inyección. Gracias a haber anotado previamente el repositorio con @Repository, spring sabrá que debe inyectar. Para conseguir que funcione pondremos la anotación @Aurowired sobre el atributo del repositorio en la clase.
Los métodos a implementar son (listarPersonajes, encontrarPersonaje) en el servicio de MarvelService.
A su vez, los métodos de este servicio son transacionales, para ello usamos la anotacion @Transactional en los métodos (menos en el GET). Con esta anotación se intenta ejecutar el código del método, si surgiese algún error o excepción se ejecutaría un rollback. Podemos probar que funciona si forzamos a incluir una excepcion despues de un save y veremos que la bd no sufre ninguna alteración.

### Controladores Rest
Los controladores Rest serán nuestro punto de acceso desde el exterior. Es necesario anotarlos con @Controller. A partir de aquí cada método anotado tendrá su propia URL, parametros, etc. La siguiente tabla muestra de que manera se anotan los métodos en función de que queremos implementar (usaremos el controlador de inicio como ejemplo):

| HTTP Method | CRUD Method | Anotation     | URL |
| ----------- | ----------- | --------------| ------ |
| GET 		  | inicio 		| @GetMapping 	| / |
| GET 		  | buscarPersonaje 			| @GetMapping  | /marvel/characters/{id}|
| GET 		  | buscarPersonaje 			| @PostMapping | /marvel/characters/ 	|
| GET 		  | listarPersonajes 			| @GetMapping  | /marvel/characters 	|
| GET 		  | getPersonaje 				| @GetMapping  | /marvel/characters/{id}|


Además, estos métodos contienen parametros, ya sean por la URL o por json. Aquellos que vengan con la URL llevarán la anotacion
@PathParam o @PathVariable. A las entidades que pasemos en formato json se les puede añadir la anotacion @Valid, que comprobara anotaciones dentro de las entidades que verifiquen los atributos como @NotNull o @Size.

En caso de que los métodos se ejecuten de forma correcta devolveremos un código 200 con la entidad creada, modificada... En caso 
contrario se devolverá un códido de error, se capturará y se informará al usuario.

### Primeras pruebas
Ya estamos en disposición de hacer las primeras pruebas. Si todo ha salido según lo esperado deberiamos tener la aplicación en ejecución, con conexión a la base de datos y las URL de acceso disponibles. En nuestro caso vamos a añadir un path de acesso previo a los servicios web. Añadimos la siguiente línea en el fichero application.properties:
```sh
server.contextPath = /api # En SpringBoot 1.5.x, la versión 2.0.x modifica el nombre de esta propiedad
```
Si quisieramos modificar el puerto, que por defecto es el 8080, añadiriamos la siguiente:
```sh
server.port = 8090
```
Ejecutamos la primera sentencia, por ejemplo para obtener el personaje con identificador 5. Podemos usar el navegador o una herramienta. En este caso haremos uso de Postman:
- Indicamos la url: http://localhost:8080/marvel/characters/5
- Indicamos que es un GET
- Clicamos "Send"
Obtenemos la siguiente respuesta:
```sh
{
  "code": 200,
  "status": "Ok",
  "copyright": "© 2021 MARVEL",
  "attributionText": "Data provided by Marvel. © 2021 MARVEL",
  "attributionHTML": "<a href=\"http://marvel.com\">Data provided by Marvel. © 2021 MARVEL</a>",
  "etag": "f6b31899ab1e7b0686e19386fd59389016921723",
  "data": {
    "offset": 0,
    "limit": 20,
    "total": 1,
    "count": 1,
    "results": [
      {
        "id": 1009610,
        "name": "Spider-Man (Peter Parker)",
        "description": "Bitten by a radioactive spider, high school student Peter Parker gained the speed, strength and powers of a spider. Adopting the name Spider-Man, Peter hoped to start a career using his new abilities. Taught that with great power comes great responsibility, Spidey has vowed to use his powers to help people.",
        "modified": "2021-06-30T17:32:26-0400",
        "thumbnail": {
          "path": "http://i.annihil.us/u/prod/marvel/i/mg/3/50/526548a343e4b",
          "extension": "jpg"
        },
        "resourceURI": "http://gateway.marvel.com/v1/public/characters/1009610",
        "comics": {
          "available": 4048,
          "collectionURI": "http://gateway.marvel.com/v1/public/characters/1009610/comics",
          "items": [
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/62304",
              "name": "Spider-Man: 101 Ways to End the Clone Saga (1997) #1"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/78503",
              "name": "2099 Alpha (2019) #1"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/60151",
              "name": "A YEAR OF MARVELS TPB (Trade Paperback)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/57926",
              "name": "A Year of Marvels: April Infinite Comic (2016) #1"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/57924",
              "name": "A Year of Marvels: February Infinite Comic (2016) #1"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/43501",
              "name": "A+X (2012) #4"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/76012",
              "name": "Absolute Carnage (2019) #1"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/76014",
              "name": "Absolute Carnage (2019) #2"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/78960",
              "name": "Absolute Carnage (2019) #5"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/77071",
              "name": "Absolute Carnage: Symbiote Spider-Man (2019) #1"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/320",
              "name": "Actor Presents Spider-Man and the Incredible Hulk (2003) #1"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/76863",
              "name": "Adventures of Spider-Man (1996) #1"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/76864",
              "name": "Adventures of Spider-Man (1996) #2"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/76865",
              "name": "Adventures of Spider-Man (1996) #3"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/76866",
              "name": "Adventures of Spider-Man (1996) #4"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/76867",
              "name": "Adventures of Spider-Man (1996) #5"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/76868",
              "name": "Adventures of Spider-Man (1996) #6"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/76869",
              "name": "Adventures of Spider-Man (1996) #7"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/76870",
              "name": "Adventures of Spider-Man (1996) #8"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/comics/76871",
              "name": "Adventures of Spider-Man (1996) #9"
            }
          ],
          "returned": 20
        },
        "series": {
          "available": 1041,
          "collectionURI": "http://gateway.marvel.com/v1/public/characters/1009610/series",
          "items": [
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/26024",
              "name": " Superior Spider-Man Vol. 2: Otto-matic (2019)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/27980",
              "name": "2099 Alpha (2019)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/22102",
              "name": "A YEAR OF MARVELS TPB (2017)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/27342",
              "name": "A Year of Marvels: April Infinite Comic (2016)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/25677",
              "name": "A Year of Marvels: February Infinite Comic (2016)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/16450",
              "name": "A+X (2012 - 2014)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/27272",
              "name": "Absolute Carnage (2019)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/27637",
              "name": "Absolute Carnage: Symbiote Spider-Man (2019)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/458",
              "name": "Actor Presents Spider-Man and the Incredible Hulk (2003)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/27587",
              "name": "Adventures of Spider-Man (1996 - 1997)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/27025",
              "name": "Adventures Of Spider-Man: Sinister Intentions (2019)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/27671",
              "name": "Adventures Of Spider-man: Spectacular Foes (2019)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/9790",
              "name": "Age of Heroes (2010)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/10235",
              "name": "AGE OF HEROES TPB (2011)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/17318",
              "name": "Age of Ultron (2013)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/24134",
              "name": "Agents of Atlas: The Complete Collection Vol. 1 (2018)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/672",
              "name": "Alias (2001 - 2003)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/13383",
              "name": "Alias Omnibus (2006)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/2116",
              "name": "Alpha Flight (1983 - 1994)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/series/17650",
              "name": "Alpha: Big Time (2013)"
            }
          ],
          "returned": 20
        },
        "stories": {
          "available": 6039,
          "collectionURI": "http://gateway.marvel.com/v1/public/characters/1009610/stories",
          "items": [
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/483",
              "name": "Interior #483",
              "type": "interiorStory"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/486",
              "name": "Cover #486",
              "type": "cover"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/487",
              "name": "Interior #487",
              "type": "interiorStory"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/498",
              "name": "SENSATIONAL SPIDER-MAN (2006) #23",
              "type": "cover"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/499",
              "name": "Interior #499",
              "type": "interiorStory"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/598",
              "name": "Cover #598",
              "type": "cover"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/599",
              "name": "Interior #599",
              "type": "interiorStory"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/805",
              "name": "Interior #805",
              "type": "interiorStory"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/824",
              "name": "Cover #824",
              "type": "cover"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/838",
              "name": "3 of 3 - Family Business",
              "type": "cover"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/842",
              "name": "1 of 1 -  Secret of the Spider Shop",
              "type": "cover"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/866",
              "name": "Cover #866",
              "type": "cover"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/912",
              "name": "Fantastic Four (1998) #512",
              "type": "cover"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/913",
              "name": "Interior #913",
              "type": "interiorStory"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/916",
              "name": "Fantastic Four (1998) #513",
              "type": "cover"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/917",
              "name": "Interior #917",
              "type": "interiorStory"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/977",
              "name": "Cover #977",
              "type": "cover"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/979",
              "name": "Cover #979",
              "type": "cover"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/1018",
              "name": "Amazing Spider-Man (1999) #500",
              "type": "cover"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/1019",
              "name": "Interior #1019",
              "type": "interiorStory"
            }
          ],
          "returned": 20
        },
        "events": {
          "available": 38,
          "collectionURI": "http://gateway.marvel.com/v1/public/characters/1009610/events",
          "items": [
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/116",
              "name": "Acts of Vengeance!"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/314",
              "name": "Age of Ultron"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/233",
              "name": "Atlantis Attacks"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/234",
              "name": "Avengers Disassembled"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/310",
              "name": "Avengers VS X-Men"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/296",
              "name": "Chaos War"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/238",
              "name": "Civil War"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/318",
              "name": "Dark Reign"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/240",
              "name": "Days of Future Present"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/332",
              "name": "Dead No More: The Clone Conspiracy"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/245",
              "name": "Enemy of the State"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/246",
              "name": "Evolutionary War"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/302",
              "name": "Fear Itself"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/251",
              "name": "House of M"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/252",
              "name": "Inferno"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/315",
              "name": "Infinity"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/317",
              "name": "Inhumanity"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/255",
              "name": "Initiative"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/258",
              "name": "Kraven's Last Hunt"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/events/151",
              "name": "Maximum Carnage"
            }
          ],
          "returned": 20
        },
        "urls": [
          {
            "type": "detail",
            "url": "http://marvel.com/comics/characters/1009610/spider-man_peter_parker?utm_campaign=apiRef&utm_source=5c85ed4a749f1f858b4d9bf28c5d8c33"
          },
          {
            "type": "wiki",
            "url": "http://marvel.com/universe/Spider-Man_(Peter_Parker)?utm_campaign=apiRef&utm_source=5c85ed4a749f1f858b4d9bf28c5d8c33"
          },
          {
            "type": "comiclink",
            "url": "http://marvel.com/comics/characters/1009610/spider-man_peter_parker?utm_campaign=apiRef&utm_source=5c85ed4a749f1f858b4d9bf28c5d8c33"
          }
        ]
      }
    ]
  }
}
```
A partir de aquí podemos probar todas las URL para ver como funciona el servicio web implementado. Hay que recordar que debemos 
pasar datos válidos, de lo contrario el servicio dará un error (Omitido para el usuario, dado que los capturadores están vacios
en este ejemplo a falta de implementación, aunque deberían usar ApiError y un código de respuesta de error con información para 
el usuario que indique que sucedio).

### Documentando la aplicación con Swagger pendiente
Swagger es un framework que permite tanto documentar apis como crearlas. De igual forma, una API como es la nuestra, con las anotaciones de Swagger hace que el framework genere una UI accesible desde la web, capaz de explicar que hace cada método así como lanzar peticiones.

Para documentar usaremos varias anotaciones. Existen tanto para los controladores, como para las clases de modelo. Si observamos la UI, a la que podemos acceder mediante http://localhost:8080/marvel/swagger-ui.html, vemos que contiene anotaciones personalizadas tanto de lo que hace cada método como de las posibles respuestas. Además las clases de modelo que aparecen abajo, también tienen documentados sus atributos. Todo esto se logra gracias a las anotaciones @ApiOperation, @ApiResponse y @ApiModelProperty. Existen más, pero en este ejemplo son las que se han utilizado para manejar incluir Swagger.

## Construyendo la aplicación
La aplicación está lista para su despliegue y construcción. Debemos tener maven instalado en el ordenador. Si todo es correcto, podemos ejecutar el comando que aparece debajo. Con el se construye la aplicación y se crea el .jar listo para su despliegue.
```sh
mvn clean package
java -jar app-name.jar
```