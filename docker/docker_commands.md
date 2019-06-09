## Useful Docker commands

```docker ps --no-trunc```

```docker exec -it <image-name> /bin/bash```

```docker run -it <image-name> /bin/bash```

```docker tag <existing-image> <hub-user>/<repo-name>[:<tag>]```

```docker build -t <image-name>:latest .```

```docker-compose -f <docker-compose-file.yml> build --no-cache```

```docker-compose -f <docker-compose-file.yml> up```

```docker run --name <container-name> -d -p <external-port>:<container-port> <image-name>:latest```

```docker pull <image-name>:latest```

```docker ps -a```

```docker kill $(docker ps -a -q)```

```docker rm -f $(docker ps -a -q)```

```docker image rm -f $(docker images -q)```

```docker system prune -a```

```docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}'```

```docker port <container>```

```docker network ls```

```docker network inspect <network-name>```

```winpty docker exec -it verb_conjugate_fr_web //bin//sh```
