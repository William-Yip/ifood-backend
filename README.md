# [Ifood] backend challenge

### Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina a ferramenta de containers: docker
[Docker](https://www.docker.com/) e de um `clientId` e um `clientSecret` no [Spotify](https://developer.spotify.com/dashboard/).

### Rodando o Back End (servidor)

```bash
# Clone este repositório
$ git clone <https://github.com/William-Yip/ifood-backend>

# Acesse a pasta do projeto
$ cd ifood-backend

# Contrua a imagem com o Docker
$ docker build -t ifood-backend:teste .

# Rode o container passando o clientId e o clientSecret
$ docker run -e clientId=<CLIENT_ID> -e clientSecret=<CLIENT_SECRET> -p 8080:8080 ifood-backend:teste

# Agora está tudo certo para fazer as requisições !! 
```

## GET /ifood{?city_name}{?lon}{?lat}

+ **Request** `(application/json)`

    + **Parameters** should be provided at least a city name or latitude with longitude
     
      + `city_name` (string, optional) - A valid city name from all over the world
      + `lat` (double, optional) - latitude from the city searched
      + `lon` (double, optional) - longitude from the city searched
       

+ Response
  + **200** - returns a list of tracks based on challenge rules


    body (example):
          [
            "Goodness Gracious",
            "All My Friends (feat. Tinashe & Chance the Rapper)",
            "Bloom",
            "All Time Low",
            "Something New",
            "Time of Our Lives",
            "Kream (feat. Tyga)",
            "Spark The Fire",
            "Liability",
            "Earned It (Fifty Shades Of Grey)"
          ]

  + **404** -  city not found on weather API
   
   
    body:
   
          ["city not found"] 
  + 500 - Internal server error
   