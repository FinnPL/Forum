# Forum

[![.github/workflows/installScript.yml](https://github.com/FinnPL/Forum/actions/workflows/installScript.yml/badge.svg)](https://github.com/FinnPL/Forum/actions/workflows/installScript.yml)
[![Java CI with Maven](https://github.com/FinnPL/Forum/actions/workflows/maven.yml/badge.svg)](https://github.com/FinnPL/Forum/actions/workflows/maven.yml)
[![.github/workflows/codeql-analysis.yml](https://github.com/FinnPL/Forum/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/FinnPL/Forum/actions/workflows/codeql-analysis.yml)
[![Frontend Build](https://github.com/FinnPL/Forum/actions/workflows/build-frontend.yml/badge.svg)](https://github.com/FinnPL/Forum/actions/workflows/build-frontend.yml)

## Setup

Clone Projekt

```
git clone https://github.com/FinnPL/Forum
```

Make the install script executable

```
cd Forum/
chmod +x install.sh
```

Run the script

```
sudo ./install.sh <DB_PASSWORD> <DB_NAME> <GF_SECURITY_ADMIN_PASSWORD>
```

## Conection

You can conect to the spring server: `localhost:8080`
and to grafana `localhost:3000`

if you are using an cloud provider you might need to disable the firewall for these ports

## [Documentation in Github Wiki](https://github.com/FinnPL/Forum/wiki)
