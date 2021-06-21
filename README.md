# MYERP

## Organisation du répertoire

*   `doc` : documentation
*   `docker` : répertoire relatifs aux conteneurs **docker** utiles pour le projet
    *   `dev` : environnement de développement
*   `racine` : code source de l'application

## Environnement de développement

Les composants nécessaires lors du développement sont disponibles via des conteneurs **docker**.
L'environnement de développement est assemblé grâce à **docker-compose**
(cf docker/dev/docker-compose.yml).

Il comporte une base de données **PostgreSQL** contenant un jeu de données de démo (`postgresql://127.0.0.1:9032/db_myerp`)
 
## Déploiement Docker

#### Lancement

    cd docker/dev
    docker-compose up

#### Arrêt

    cd docker/dev
    docker-compose stop

#### Remise à zero

    cd docker/dev
    docker-compose stop
    docker-compose rm -v
    docker-compose up
    
#### Réinitialisation de la base de données
Le script `restart.sh` présent dans le dossier `docker/dev` permet de supprimer le container 
de la base de données et de le recréer avec le jeu de données de base.

    Important : Cette manipulation est à effectuer à chaque lancement de build Jenkins.

## Intégration continue
Un système d'intégartion continue est mis en place avec **Jenkins**. L'instance est configurable pour lancer un build à chaque Push GitHub. 

### Configuration
Une fois Jenkins installé, plusieurs plugins sont à intégrer :
- Maven : Création de Job spécifique
- GitHub plugin : Connexion à GitHub
- GitHub Authentication : Authentification via GitHub (facultatif)
- Sonarqube Scanner : Connexion à SonarQube

Avant de créer un **Job**, il est important de configurer Jenkins avec un serveur **GitHub** et un serveur **SonarQube** (*Configuration globale* de Jenkins).

> La configuration de l'authentification via GitHub se fait dans la section **Configurer la sécurité globale** de l'administration Jenkins. Elle permet de s'authentifier sur l'instance de jenkins avec son compte GitHub. Plus d'infos sur [https://jenkins.io/solutions/github/]



Créez un **Job Maven** et le configurer en tant que projet GitHub (URL, Repository) avec l'option *GitHub Hook Trigger* activée. Pour permettre le *Build* au push GitHub, il est nécessaire de configurer un [Webhook](https://developer.github.com/webhooks/). 

> Si vous ne savez pas configurer un WebHook avec Jenkins, [suivez ce tutorial](https://blog.tentamen.eu/jenkins-and-github-integration-using-webhooks/)

### Agrégation des données de tests
Les données liées aux tests sont automatiquement intégrées, via la commande maven lancée depuis Jenkins, 
à **Sonarqube**.

Dans la configuration du **Job** Jenkins, il faut ajouter la commande donnée par SonarQube (exemple : `sonar:sonar -Dsonar.projectKey=MyERP -Dsonar.host.url=http://51.77.230.10:9000 -Dsonar.login=6122654de79d360c392fcbfe6e0e06432343798a`) àdans la partie *Goals et Options* dans la section *Build*.

## Correctifs et évolutions
*Se référer au fichier `CHANGELOG.md`* 