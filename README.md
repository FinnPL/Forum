# Forum

## Setup

Update & upgrade

```
sudo apt-get update
sudo apt-get upgrade
```



Clone projekt
```
git clone https://github.com/FinnPL/Forum
```

run install script
```
chmod +x install-and-set-env-variables.sh
./install-and-set-env-variables.sh <DB_PASSWORD> <DB_NAME> <GF_SECURITY_ADMIN_PASSWORD>
```


```
chmod +x mvnw
```

Build Docker Image

```
sudo docker build -t forum .
```

Run Docker Image

```
 sudo docker run -p 8080:8080 --network="host" --restart always forum
```

## Docker Compose

Set the DB password, Name and Grafana password as an environment variable
```
export DB_PASSWORD=example
export DB_NAME=example
export GF_SECURITY_ADMIN_PASSWORD=example
```

Start the containers
```
sudo docker-compose up -d
```

