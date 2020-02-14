#!/usr/bin/env groovy

def execute(command)
{
    if (env.UNIX)
    {
        sh(script: "./${command}")
    }
    else
    {
        bat(script: "${command}")
    }
}

node
{
    stage('setup context')
    {
        env.UNIX = isUnix();
    }
    stage('checkout master')
    {
        checkout([$class: 'GitSCM', branches: [[name: '*/master-private']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [],
        userRemoteConfigs: [[credentialsId: '6ab817aa-4c27-4891-9f0b-93bafba95f64', url: 'https://github.com/bodya30/rocket.git']]])
    }
    stage('cleanup')
    {
        execute('gradlew --no-daemon clean')
    }
    stage('build')
    {
        execute('gradlew --no-daemon build -x test')
    }
    stage('unit tests')
    {
        execute('gradlew --no-daemon test')
    }
    stage('integration tests')
    {
        execute('gradlew --no-daemon integrationTest')
    }
}
