pipeline {
    agent any

    parameters {
        base64File description: 'Uploaded file', name: 'FILE'
    }

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }


    stages {

        //Building and creating JAR
        stage('Build') {
            steps {
            dir('Backend') {
                    bat 'mvn -Dmaven.test.failure.ignore=true clean install'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Run Docker Compose with environment variables from the file
                    withFileParameter('FILE') {
                        // Pass environment variables from the file to Docker Compose
                        bat "type $FILE > .env && docker-compose --env-file .env build"
                        bat 'docker tag foodwastereductionplatform-backend spatil1609/foodwastereductionplatform-app:latest'
                    }
                }
            }
        }

        stage('Docker Hub Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'sanketpatil-dockerhub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
                    bat "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                bat 'docker push spatil1609/foodwastereductionplatform-app:latest'
            }
        }
    }

    post {
        always {
            bat 'docker logout'
        }
    }
}
