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
        stage('integration tests') {
            steps {
                execute('gradlew --no-daemon integrationTest')
            }
        }
    }
}
