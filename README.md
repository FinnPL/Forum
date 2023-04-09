# GHSE-Forum

[![.github/workflows/installScript.yml](https://github.com/FinnPL/Forum/actions/workflows/installScript.yml/badge.svg)](https://github.com/FinnPL/Forum/actions/workflows/installScript.yml)
[![Java CI with Maven](https://github.com/FinnPL/Forum/actions/workflows/maven.yml/badge.svg)](https://github.com/FinnPL/Forum/actions/workflows/maven.yml)
[![.github/workflows/codeql-analysis.yml](https://github.com/FinnPL/Forum/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/FinnPL/Forum/actions/workflows/codeql-analysis.yml)
[![Frontend Build](https://github.com/FinnPL/Forum/actions/workflows/build-frontend.yml/badge.svg)](https://github.com/FinnPL/Forum/actions/workflows/build-frontend.yml)

## A Full Stack Forum for the GHSE built with Spring Boot and SvelteKit

This GHSE Forum allows students to create posts and comments. It consists of a Spring Boot backend providing a REST API for the SvelteKit frontend. Data is stored in a MySQL database, and the Forum's performance can be monitored via a Grafana dashboard. Use the included shell script to easily install with Docker Compose.

## Installation

Clone Projekt

```bash
git clone https://github.com/FinnPL/Forum
```

Make the install script executable

```bash
cd Forum/
chmod +x install.sh
```

Run the script

```bash
sudo ./install.sh <DB_PASSWORD> <DB_NAME> <GF_SECURITY_ADMIN_PASSWORD>
```

### Conection

- The Frontend is located at `localhost`. (Port 80)
- Spring server's REST API at `localhost:8080`
- Grafana at `localhost:3000`. Use the default user admin and your chosen password from the install script.

> **_NOTE:_** If you are using an cloud provider you might need to disable the firewall for these ports and replace localhost with the ip of your server.

## Contributing

If you find any bugs or have any suggestions, please open an issue.

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
Feel free to take a look at the [**Documentation in Github Wiki**](https://github.com/FinnPL/Forum/wiki)

### Contributing Guide

- Fork the project
- Create your feature branch (git checkout -b feature/fooBar)
- Make your changes
- Test and Build
- Commit your changes (git commit -m 'Add some fooBar')
- Push to the branch (git push origin feature/fooBar)
- Create a new Pull Request

### Building

#### Using Docker Desktop:

https://user-images.githubusercontent.com/77006988/230790915-b05777d8-1a56-4c6c-ac6d-e52b293728f7.mp4


#### Whole project:

```bash
docker-compose up --build
```

#### Backend:

```bash
mvn clean install
```

Frontend:

```bash
cd board/
npm install
npm run build
```
