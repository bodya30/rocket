#!/usr/bin/env groovy

def execute(command) {
    if (Boolean.valueOf(env.UNIX)) {
        sh(script: "./${command}")
    }
    else {
        bat(script: "${command}")
    }
}

pipeline {

    agent any

    environment {
        UNIX = isUnix()
    }

    parameters {
        booleanParam(name: 'Unit_tests', defaultValue: true, description: 'Include Unit Tests into pipeline')
        booleanParam(name: 'Integration_tests', defaultValue: true, description: 'Include Integration Tests into pipeline')
        string(name: 'DB_server', defaultValue: 'localhost', description: 'Data base server name')
    }
    stages {
        stage('checkout master') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master-private']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [],
                userRemoteConfigs: [[credentialsId: '6ab817aa-4c27-4891-9f0b-93bafba95f64', url: 'https://github.com/bodya30/rocket.git']]])

            }
        }
        stage('cleanup') {
            steps {
                execute('gradlew --no-daemon clean')
            }
        }
        stage('build') {
            steps {
                execute('gradlew --no-daemon build -x test')
            }
        }
        stage('unit tests') {
            when { expression { return params.Unit_tests} }
            steps {
                execute('gradlew --no-daemon test')
            }
            post {
                always {
                    echo 'Publish JUnit Results...'
                    junit 'build/test-results/test/*.xml'
                }
            }
        }
        stage('start DB') {
            when {
                environment name: 'UNIX', value: 'true'
                expression { return params.Integration_tests}
            }
            steps {
                sh '''
                    if [ ! "$(docker ps -q -f name=${DB_server})" ]; then
                        if [ "$(docker ps -aq -f status=exited -f name=${DB_server})" ]; then
                            docker rm ${DB_server}
                        fi
                    docker run -d -p 3306:3306 --name ${DB_server} -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=rocket mysql:5.7
                    fi
                '''
            }
        }
        stage('integration tests') {
            when {
                expression { return params.Integration_tests}
            }
            steps {
                execute('gradlew --no-daemon integrationTest')
            }
            post {
                always {
                    script {
                        if (Boolean.valueOf(env.UNIX)) {
                            sh '''
                                if [ "$(docker ps -q -f name=${DB_server})" ]; then
                                    docker stop ${DB_server}
                                fi
                            '''
                        }
                    }
                }
            }
        }
        stage('update docker image') {
            when { environment name: 'UNIX', value: 'true' }
            steps {
                sh '''
                    docker login -u "${DOCKER_USER}" -p "${DOCKER_PASS}"
                    docker build --no-cache -t rocket .
                    docker tag rocket:latest boderto/rocket:latest
                    docker push boderto/rocket:latest
                    docker rmi rocket:latest
               '''
            }
        }
    }
}
